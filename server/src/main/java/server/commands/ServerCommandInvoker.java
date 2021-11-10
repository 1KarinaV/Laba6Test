package server.commands;

import lib.commands.*;
import lib.db.Database;
import lib.io.CheckedReader;
import lib.io.net.PeerRecord;
import lib.io.net.Response;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.HashMap;

public class ServerCommandInvoker extends CommandInvoker {
    private HashMap<SocketAddress, PeerRecord> clients;
    private final CommandList netCommands;
    private Selector selector;

    private static ServerCommandInvoker global;

    private ServerCommandInvoker(Database db) {
        super(System.in);
        commands.add(new ExitCommand(db));
        commands.add(new HelpCommand(commands));
        netCommands = new CommandList();
    }

    public void registerRemote(CommandList cmdList) {
        cmdList.forEach((k, v) -> netCommands.add(v));
    }

    public static ServerCommandInvoker getGlobal(Database db) {
        if (global == null)
            global = new ServerCommandInvoker(db);
        return global;
    }

    @Override
    public boolean makeBound() throws IOException {
        if (super.makeBound()) {
            clients = new HashMap<>();
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            return true;
        }
        return false;
    }

    @Override
    public void run() throws IOException, JAXBException {
        reader.printPrompt(CMD_HINT);
        reader.printPrompt("> ");
        while (true) {
            if (reader.ready()) {
                String[] args = readCommand();
                if (args == null) // EOF
                    args = new String[]{"exit"};
                if (args.length == 0) {
                    reader.printPrompt(CMD_HINT);
                    continue;
                }

                Command cmd = commands.get(args[0]);
                if (cmd == null) {
                    System.out.println("Ошибка: Неизвестная команда: " + args[0]);
                    reader.printPrompt(CMD_HINT);
                } else {
                    cmd.execute(reader, Arrays.copyOfRange(args, 1, args.length));
                }
                reader.printPrompt("> ");
            }

            Response response = new Response(channel, selector, clients);
            response.trySend();

            PeerRecord newPeer = response.pollConnection();
            if (newPeer != null) {
                CommandData responseResult = new CommandData();
                try {
                    CommandData requestCommand = CommandData.unmarshall(newPeer.receiveBuffer().get());
                    InputStream stream = new ByteArrayInputStream(requestCommand.getContent().getBytes());
                    CheckedReader reader = new CheckedReader(stream);
                    Command cmd = netCommands.get(requestCommand.getCommand());
                    responseResult.setCommand(cmd.name());
                    responseResult = cmd.execute(reader, requestCommand.getArgument());
                } catch (CommandException e) {
                    responseResult.setContent(e.getMessage());
                } finally {
                    response.pendSend(newPeer.address(), responseResult.marshall());
                }
            }
        }
    }
}
