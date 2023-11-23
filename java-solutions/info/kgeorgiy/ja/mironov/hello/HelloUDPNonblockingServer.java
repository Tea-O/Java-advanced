package info.kgeorgiy.ja.mironov.hello;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.*;

public class HelloUDPNonblockingServer extends AbstractHelloUDPServer {
    //

    private static class Data {
        private ByteBuffer buffer;
        private SocketAddress socketAddress;

        public Data(final SocketAddress socketAddress, final int buffSize) {
            buffer = ByteBuffer.allocate(buffSize);
            this.socketAddress = socketAddress;
        }
    }

    private Selector selector;
    private DatagramChannel datagramChannel;
    private ConcurrentLinkedQueue<Data> send;
    private ExecutorService worker;


    /**
     * Starts new {@link HelloUDPNonblockingServer}
     *
     * @param port    server port.
     * @param threads number of working threads.
     */
    @Override
    public void start(final int port, final int threads) {
        try {
            send = new ConcurrentLinkedQueue<>();
            selector = Selector.open();
            datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            datagramChannel.bind(new InetSocketAddress(port));
            datagramChannel.register(selector, SelectionKey.OP_READ);
            worker = Executors.newSingleThreadExecutor();
            worker.submit(this::processPackets);
        } catch (final IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Processes incoming packets.
     */
    public void processPackets() {
        while (!datagramChannel.socket().isClosed() && !Thread.interrupted()) {
            try {
                selector.select(SOKET_TIMEOUT);  // :NOTE: константа
                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    final SelectionKey selectionKey = iterator.next();
                    try {
                        if (selectionKey.isReadable()) {
                            receive(selectionKey);
                        }
                        if (selectionKey.isWritable()) {
                            send(selectionKey);
                        }
                    } finally {
                        iterator.remove();
                    }
                }

            } catch (final IOException e) {
                System.err.println("I/O error: " + e.getMessage());
            }
        }
    }

    private void send(final SelectionKey key) {

        Data attach = send.remove();
        attach.buffer.flip();
        final String request = "Hello, " + StandardCharsets.UTF_8.decode(attach.buffer);
        attach.buffer.clear();
        attach.buffer = ByteBuffer.wrap(request.getBytes(StandardCharsets.UTF_8));
        try {
            datagramChannel.send(attach.buffer, attach.socketAddress);
        } catch (final IOException e) {
            System.err.println("I/O error with send: " + e.getMessage());
        }
        key.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }


    private void receive(final SelectionKey key) {
        try {
            Data attachment = new Data(null, datagramChannel.socket().getReceiveBufferSize());
            attachment.socketAddress = datagramChannel.receive(attachment.buffer);

            send.add(attachment);
            key.interestOps(SelectionKey.OP_WRITE);
            selector.wakeup();


        } catch (final IOException e) {
            System.err.println("I/O error with receive: " + e.getMessage());
        }
    }

    /**
     * Close DatagramChannel, selector and terminate threads
     */
    @Override
    public void close() {
        try {
            if (datagramChannel != null && datagramChannel.isOpen()) {
                datagramChannel.close();
            }
            if (selector != null && selector.isOpen()) {
                selector.close();
            }
        } catch (IOException e) {
            System.err.println("I/O error with close: " + e.getMessage());
        }

        threadTermination(worker);
    }


    private void threadTermination(ExecutorService thread) {
        try {
            thread.shutdown();
            if (!thread.awaitTermination(4, TimeUnit.SECONDS)) {
                thread.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Threads error: " + e.getMessage());
        }
    }
}
