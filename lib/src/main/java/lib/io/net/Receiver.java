package lib.io.net;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

public class Receiver {
    // Для того чтобы  пакет был принят любым хостом,
    // размер данных в UDP не должен превышать:
    // (минимальная длина IP пакета) — (Max IP Header Size) — (UDP Header Size)
    // = 576 — 60 — 8 = 508 байт.
    // При этом, избегаем фрагментации UDP пакетов (и возможной их потери).
    private static final int MAX_LEN = 508;
    private final DatagramChannel channel;
    private final ByteBuffer buffer;

    public Receiver(DatagramChannel ch) {
        channel = ch;
        buffer = ByteBuffer.allocate(MAX_LEN);
        buffer.order(ByteOrder.BIG_ENDIAN);
    }

    public PeerRecord receive(HashMap<SocketAddress, PeerRecord> peers) throws IOException {
        buffer.clear();
        SocketAddress address = channel.receive(buffer);
        if (address == null)
            return null;
        buffer.flip();

        PeerRecord peer = peers.get(address);
        if (peer == null) {
            peer = new PeerRecord(address);
            peers.put(address, peer);
        }

        ReceiveBuffer destinationData = peer.receiveBuffer();
        int index = buffer.getInt();
        if (index == 0) {
            int total = buffer.getInt();
            destinationData.setLimit(total);
        }
        destinationData.addDatagram(index, buffer);
        return destinationData.isFull() ? peer : null;
    }
}
