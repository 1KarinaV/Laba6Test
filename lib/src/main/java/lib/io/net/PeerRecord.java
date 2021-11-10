package lib.io.net;

import java.net.SocketAddress;

//Хранит адрес удаленного узла, буфер отсылаемых ему данных и буфер принятых от него данных.
//Используется как клиентом, так и сервером, но больше нужен серверу,
// так как сервер принимает запросы от множества узлов.

public class PeerRecord {
    private final SocketAddress peerAddress;
    private final ReceiveBuffer receiveBuffer;
    private final SendBuffer sendBuffer;

    public PeerRecord(SocketAddress address) {
        peerAddress = address;
        receiveBuffer = new ReceiveBuffer();
        sendBuffer = new SendBuffer();
    }

    public SocketAddress address() {
        return peerAddress;
    }

    public ReceiveBuffer receiveBuffer() {
        return receiveBuffer;
    }

    public SendBuffer sendBuffer() {
        return sendBuffer;
    }
}
