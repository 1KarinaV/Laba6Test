package lib.io.net;

import lib.commands.CommandData;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Request {
    private final DatagramChannel channel;
    private final Selector selector;
    private final HashMap<SocketAddress, PeerRecord> peerRecords;

    private static final int TIMEOUT = 5000;

    public Request(DatagramChannel ch, HashMap<SocketAddress, PeerRecord> peers) throws IOException {
        channel = ch;
        peerRecords = peers;
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_WRITE);
    }

    public CommandData execute(CommandData cmdResult) throws JAXBException, IOException {
        if (cmdResult == null)
            return null;

        PeerRecord server = peerRecords.get(channel.getRemoteAddress());
        server.sendBuffer().set(cmdResult.marshall());
        Sender sender = new Sender(channel);
        Receiver receiver = new Receiver(channel);

        while (true) {
            if (selector.select(TIMEOUT) == 0)
                return null;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (key.isReadable()) {
                     PeerRecord peer = receiver.receive(peerRecords);
                     if (peer != null)
                         return CommandData.unmarshall(peer.receiveBuffer().get());
                } else if (key.isWritable()) {
                    if (sender.send(server))
                        key.interestOps(SelectionKey.OP_READ);
                }
            }
        }
    }
}
