package info.kgeorgiy.ja.mironov.hello;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class HelloUDPServer extends AbstractHelloUDPServer {

    //
    private DatagramSocket socket;
    private ExecutorService workers;



    /**
     * Starts new {@link HelloUDPServer}
     *
     * @param port    server port.
     * @param threads number of working threads.
     */
    @Override
    public void start(final int port, final int threads) {
        try {
            socket = new DatagramSocket(port);
            workers = Executors.newFixedThreadPool(threads);

            IntStream.range(0, threads).forEach(ignored -> workers.submit(this::processPackets));
        } catch (final SocketException e) {
            System.err.println("Error with socket: " + e.getMessage());
        }
    }

    /**
     * Processes incoming packets.
     */
    private void processPackets() {
        try {
            final int bufferSize = socket.getReceiveBufferSize();
            final DatagramPacket packet = new DatagramPacket(new byte[bufferSize], bufferSize);

            while (!socket.isClosed() && !Thread.interrupted()) {
                try {
                    socket.receive(packet);
                    String responseText = "Hello, " + new String(
                            packet.getData(),
                            packet.getOffset(),
                            packet.getLength(),
                            StandardCharsets.UTF_8
                    );
                    byte[] responseData = responseText.getBytes(StandardCharsets.UTF_8);
                    packet.setData(responseData);
                    socket.send(packet);
                } catch (final IOException e) {
                    System.err.println("I/O error: " + e.getMessage());
                }
            }
        } catch (final SocketException e) {
            System.err.println("Error with socket: " + e.getMessage());
        }
    }

    /**
     * Close server and terminate threads
     */
    @Override
    public void close() {
        socket.close();
        workers.shutdown();
        try {
            if (!workers.awaitTermination(4, TimeUnit.SECONDS)) {
                workers.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Threads error: " + e.getMessage());
        }
    }
}
