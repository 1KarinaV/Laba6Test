package lib.io.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Response {
    private final DatagramChannel channel;
    private final Selector selector;
    private final HashMap<SocketAddress, PeerRecord> peerRecords;

    private static final int TIMEOUT = 100;

    public Response(DatagramChannel ch, Selector s, HashMap<SocketAddress, PeerRecord> peers) {
        channel = ch;
        peerRecords = peers;
        selector = s;
    }

    public PeerRecord pollConnection() throws IOException {
        if (selector.select(TIMEOUT) > 0) {
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();
                if (key.isReadable()) {
                    Receiver receiver = new Receiver(channel);
                    PeerRecord peer = receiver.receive(peerRecords);
                    if (peer != null) {
                        channel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                        return peer;
                    }
                }
            }
        }
        return null;
    }

    public void trySend() throws IOException {
        List<PeerRecord> readyToSend = peerRecords.values().stream()
                .filter(x -> x.receiveBuffer().isFull())
                .collect(Collectors.toList());
        if (readyToSend.isEmpty() || selector.select(TIMEOUT) == 0)
            return;

        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> it = selectedKeys.iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();

            if (key.isWritable()) {
                Sender sender = new Sender(channel);
                final int total = readyToSend.size();
                int sent = 0;

                for (PeerRecord peer : readyToSend) {
                    if (sender.send(peer)) {
                        peerRecords.remove(peer.address());
                        ++sent;
                    }
                }

                if (sent == total)
                    channel.keyFor(selector).interestOps(SelectionKey.OP_READ);
            }
        }
    }

    public void pendSend(SocketAddress address, String data) {
        PeerRecord peer = peerRecords.get(address);
        if (peer == null) {
            peer = new PeerRecord(address);
            peerRecords.put(address, peer);
        }
        peer.sendBuffer().set(data);
    }
}
