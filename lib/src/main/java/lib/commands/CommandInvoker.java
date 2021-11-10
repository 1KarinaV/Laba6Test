package lib.commands;

import lib.io.CheckedReader;

import javax.xml.bind.JAXBException;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;

public abstract class CommandInvoker implements Closeable {
    protected final CommandList commands;
    protected final CheckedReader reader;
    protected DatagramChannel channel;

    protected static final String CMD_HINT = "Введите 'help' для справки о доступных командах\n";
    protected final int PORT = 56789;

    protected CommandInvoker(InputStream in) {
        commands = new CommandList();
        reader = new CheckedReader(in);
    }

    private boolean initNetworking() throws IOException {
        if (channel != null)
            return false;
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        return true;
    }

    public boolean makeConnected(String hostname) throws IOException {
        if (!initNetworking() || channel.isConnected())
            return false;
        channel.connect(new InetSocketAddress(hostname, PORT));
        return true;
    }

    public boolean makeBound() throws IOException {
        if (!initNetworking() || channel.socket().isBound())
            return false;
        channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        channel.bind(new InetSocketAddress(PORT));
        return true;
    }

    // Читает команду из потока ввода и возвращает массив
    // строк, первым элементом которого будет название команды,
    // остальные - аргументы команды.
    protected String[] readCommand() throws IOException {
        String line = reader.readLine();
        if (line == null)
            return null;
        return Arrays.stream(line.split("\\s"))
                .filter(s -> s.length() > 0)
                .toArray(String[]::new);
    }

    public abstract void run() throws IOException, JAXBException;

    /**
     * Регистрация новой команды.
     * @param cmdList команда
     */
    public void register(CommandList cmdList) {
        cmdList.forEach((k, v) -> commands.add(v));
    }

    /**
     * Закрытие связанного с данным объектом потока ввода.
     * @throws IOException при ошибках закрытия потока
     */
    @Override
    public void close() throws IOException {
        reader.close();
        channel.close();
    }
}
