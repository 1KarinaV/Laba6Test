package client.commands;

import lib.commands.*;
import lib.io.CheckedReader;
import lib.io.net.PeerRecord;
import lib.io.net.Request;

import javax.xml.bind.JAXBException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Класс регистрации и выполнения команд над базой данных.
 * Команды регистрируются с помощью {@link #register(CommandList) registre}.
 * Цикл обработки и выполнения команд запускается методом {@link #run() run}.
 * Команды считываются из входного потока с помощью
 * {@link CheckedReader}.
 */
public class ClientCommandInvoker extends CommandInvoker {
    private final LinkedList<String> history;
    private HashMap<SocketAddress, PeerRecord> server;

    private static ClientCommandInvoker global;
    private static final Stack<Path> scriptTrace = new Stack<>();

    private static final int HISTORY_ENTRIES = 14;

    // Команда history существует только в контексте CommandInvoker
    private class HistoryCommand extends AbstractCommand {
        HistoryCommand() {
            super("history", "history - вывести последние 14 команд (без их аргументов)");
        }

        @Override
        public CommandData execute(CheckedReader reader, String... args) {
            history.forEach(System.out::println);
            return null;
        }
    }

    // Создание нового CommandInvoker, читающего и исполняющего команды
    // из указанного потока./
    private ClientCommandInvoker(InputStream in) {
        super(in);
        history = new LinkedList<>();
        commands.add(new ExitCommand());
        commands.add(new HelpCommand(commands));
        commands.add(new HistoryCommand());
    }

    @Override
    public boolean makeConnected(String hostname) throws IOException {
        if (super.makeConnected(hostname)) {
            server = new HashMap<>();
            server.put(channel.getRemoteAddress(), new PeerRecord(channel.getRemoteAddress()));
            return true;
        }
        return false;
    }

    /**
     * Глобальный объект CommandInvoker.
     * Работает с потоком {@code System.in}
     * @return глобальный CommandInvoker
     */
    public static ClientCommandInvoker getGlobal() {
        if (global == null)
            global = new ClientCommandInvoker(System.in);
        return global;
    }

    /**
     * Создание нового CommandInvoker из текущего.
     * Созданный объект имеет такой же набор зарегистрированных команд.
     * Входной поток получения команд связывается с указанным файлом.
     * @param path путь к файлу
     * @return новый CommandInvoker
     * @throws FileNotFoundException если файл по указанному пути недоступен.
     * @throws CommandException при ошибках исполнения команд
     */
    public ClientCommandInvoker createFromThis(Path path) throws IOException {
        path = path.normalize().toAbsolutePath();
        if (scriptTrace.search(path) < 0) {
            scriptTrace.push(path);
            ClientCommandInvoker result = new ClientCommandInvoker(new FileInputStream(path.toFile()));
            result.register(commands);
            result.channel = this.channel;
            result.server = this.server;
            return result;
        }
        throw new CommandException("Ошибка: рекурсивное выполнение скриптов не поддерживается: " + path);
    }

    /**
     * Запуск цикла ввода и обработки команд.
     * @throws IOException при ошибках ввода/вывода
     */
    @Override
    public void run() throws IOException, JAXBException {
        reader.printPrompt(CMD_HINT);

        while (true) {
            reader.printPrompt("> ");

            String[] args = readCommand();
            if (args == null)
                break; // EOF
            if (args.length == 0) {
                reader.printPrompt(CMD_HINT);
                continue;
            }

            Command cmd = commands.get(args[0]);
            if (cmd == null) {
                System.out.println("Ошибка: Неизвестная команда: " + args[0]);
                reader.printPrompt(CMD_HINT);
                continue;
            }

            if (history.size() == HISTORY_ENTRIES)
                history.removeFirst();
            history.addLast(cmd.name());

            try {
                CommandData result = cmd.execute(reader, Arrays.copyOfRange(args, 1, args.length));
                if (result != null) {
                        Request request = new Request(channel, server);
                        result = request.execute(result);
                        if (result != null)
                            System.out.println(result.getCommand() + ":\n" + result.getContent());
                        else
                            System.out.println("Превышен интервал ожидания ответа.");
                }
            } catch (PortUnreachableException e) {
                System.out.println("Сервер недоступен");
            } catch (CommandException e) {
                System.out.println("Ошибка: " + e.getMessage());
                reader.printPrompt(CMD_HINT);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (!scriptTrace.empty()) {
            reader.close();
            scriptTrace.pop();
        } else {
            channel.close();
        }
    }
}
