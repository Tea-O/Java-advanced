package info.kgeorgiy.ja.mironov.hello;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class HelloUDPNonblockingClient extends AbstractHelloUDPClient {
    //

    private SocketAddress socketAddress;
    private DatagramChannel channel;
    int requestId;


    /**
     * InetSocketAddress is created with the specified host and server port
     *
     * @param host     server host
     * @param port     server port
     * @param prefix   request prefix
     * @param threads  number of request threads
     * @param requests number of requests per thread.
     */
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        try (Selector selector = Selector.open()) {
            socketAddress = new InetSocketAddress(host, port);
            for (int i = 1; i <= threads; i++) {
                try (DatagramChannel datagramChannel = DatagramChannel.open()) {
                    requestId = 1;
                    datagramChannel.configureBlocking(false);
                    datagramChannel.connect(socketAddress);
                    ByteBuffer buffer = ByteBuffer.allocate(datagramChannel.socket().getReceiveBufferSize());
                    datagramChannel.register(selector, SelectionKey.OP_WRITE, buffer);
                    while (!selector.keys().isEmpty() && !Thread.interrupted()) {
                        try {
                            selector.select(SOKET_TIMEOUT);
                        } catch (IOException e) {
                            System.err.println("I/O error: " + e.getMessage());
                        }
                        if (selector.selectedKeys().isEmpty()) {
                            for (final SelectionKey key : selector.keys()) {
                                key.interestOps(SelectionKey.OP_WRITE);
                            }
                        }
                        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()) {
                            final SelectionKey selectionKey = iterator.next();
                            try {
                                channel = (DatagramChannel) selectionKey.channel();
                                buffer = (ByteBuffer) selectionKey.attachment();
                                String request = String.format("%s%d_%d", prefix, i, requestId);
                                if (selectionKey.isWritable()) {
                                    send(channel, selectionKey, request);
                                }
                                if (selectionKey.isReadable()) {
                                    receive(buffer, request, selectionKey, requests);
                                }
                            } finally {
                                iterator.remove();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("I/O error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    private void receive(ByteBuffer buffer, String request, SelectionKey selectionKey, int requests) {
        buffer.clear();
        try {
            channel.receive(buffer);
        } catch (IOException e) {
            System.err.println("I/O error with receive: " + e.getMessage());
        }
        buffer.flip();
        String response = StandardCharsets.UTF_8.decode(buffer).toString();
        if (response.contains(request)) {
            requestId++;
            System.out.println("Received: " + response);
        }
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        if (requestId > requests) {
            try {
                channel.close();
            } catch (IOException e) {
                System.err.println("I/O error with close: " + e.getMessage());
            }
        }
    }

    private void send(DatagramChannel channel, SelectionKey selectionKey, String request) {
        try {
            channel.send(ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8)), socketAddress);
        } catch (IOException e) {
            System.err.println("I/O error with send: " + e.getMessage());
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
        System.out.println("Send: " + request);
    }
}


