package lib.io.net;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SendBuffer {
    private ByteBuffer buffer;
    private int nextIndex = 0;

    public int available() {
        return buffer.remaining();
    }

    public ByteBuffer nextDatagramBytes(int size) {
        if (size > buffer.remaining())
            size = buffer.remaining();
        ByteBuffer result = buffer.slice();
        result.limit(size);
        return result;
    }

    public int nextDatagramIndex() {
        return nextIndex;
    }

    public void advance(int offset) {
        int newPosition = buffer.position() + offset;
        if (newPosition > buffer.limit())
            newPosition = buffer.limit();
        buffer.position(newPosition);
        ++nextIndex;
    }

    public void reset() {
        buffer = null;
        nextIndex = 0;
    }

    public void set(String data) {
        buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));
        nextIndex = 0;
    }
}
