package lib.io.net;

import java.nio.ByteBuffer;
import java.util.TreeMap;
import java.util.stream.Collectors;


//Для получения буффера
public class ReceiveBuffer {
    private final TreeMap<Integer, ByteBuffer> datagrams = new TreeMap<>();
    private int bytesReceived = 0;
    private int bytesLimit = 0;

    public void reset() {
        datagrams.clear();
        bytesReceived = 0;
        bytesLimit = 0;
    }

    public void setLimit(int limit) {
        bytesLimit = limit;
    }

    public void addDatagram(int index, ByteBuffer buffer) {
        bytesReceived += buffer.remaining();
        datagrams.putIfAbsent(index, makeBufferCopy(buffer));
    }

    public boolean isFull() {
        return bytesReceived == bytesLimit;
    }

    public String get() {
        String result = datagrams.values().stream()
                .map(x -> new String(x.array()))
                .collect(Collectors.joining());
        reset();
        return result;
    }

    private static ByteBuffer makeBufferCopy(ByteBuffer buf) {
        ByteBuffer result = ByteBuffer.allocate(buf.remaining());
        result.put(buf);
        result.flip();
        return result;
    }
}
