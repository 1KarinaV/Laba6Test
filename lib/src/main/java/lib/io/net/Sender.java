package lib.io.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;

public class Sender {
    // Для того чтобы пакет был принят любым хостом,
    // размер данных в UDP не должен превышать:
    // (минимальная длина IP пакета) — (Max IP Header Size) — (UDP Header Size)
    // = 576 — 60 — 8 = 508 байт.
    // При этом, избегаем фрагментации UDP пакетов (и возможной их потери).
    private static final int MAX_LEN = 508;
    private final DatagramChannel channel;
    private final ByteBuffer buffer;

    public Sender(DatagramChannel ch) {
        channel = ch;
        buffer = ByteBuffer.allocate(MAX_LEN);
        buffer.order(ByteOrder.BIG_ENDIAN);
    }

    public boolean send(PeerRecord peer) throws IOException {
        SendBuffer sourceData = peer.sendBuffer();

        int toSend = sourceData.available();
        if (toSend == 0)
            return true;

        int index = sourceData.nextDatagramIndex();
        buffer.clear();
        buffer.putInt(index);
        if (index == 0)
            buffer.putInt(toSend);
        if (toSend > buffer.remaining())
            toSend = buffer.remaining();

        buffer.put(sourceData.nextDatagramBytes(toSend));
        buffer.flip();

        if (channel.send(buffer, peer.address()) > 0) {
            sourceData.advance(toSend);
            if (sourceData.available() == 0) {
                sourceData.reset();
                return true;
            }
        }
        return false;
    }
}
