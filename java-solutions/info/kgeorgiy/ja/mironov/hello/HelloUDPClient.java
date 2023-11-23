package info.kgeorgiy.ja.mironov.hello;



import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class HelloUDPClient extends AbstractHelloUDPClient {
    //

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
        try {
            InetSocketAddress socket = new InetSocketAddress(host, port);
            ExecutorService workers = Executors.newFixedThreadPool(threads);

            IntStream.rangeClosed(1, threads).forEach(idOfThread ->
                    workers.submit(() -> sendRequests(idOfThread, requests, prefix, socket)));

            workers.shutdown();
            if (!workers.awaitTermination(5, TimeUnit.MINUTES)) {
                workers.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Threads error: " + e.getMessage());
        }
    }

    /**
     * sends a specified number of requests to the specified socket
     *
     * @param idOfThread Stream identifier
     * @param requests   number of requests per thread.
     * @param prefix     request prefix
     * @param socket     socket for sending and receiving packets.
     */
    private void sendRequests(int idOfThread, int requests, String prefix, InetSocketAddress socket) {
        try (DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.setSoTimeout(SOKET_TIMEOUT);

            IntStream.rangeClosed(1, requests).forEach(j -> {
                String request = String.format("%s%d_%d", prefix, idOfThread, j);
                DatagramPacket packet = createPacket(request, socket);

                while (true) {
                    try {
                        datagramSocket.send(packet);
                        String result = receiveResponse(datagramSocket);
                        if (result.contains(request)) {
                            System.out.println(result);
                            break;
                        }
                    } catch (SocketTimeoutException e) {
                        System.err.println("Timeout error: " + e.getMessage());
                    } catch (IOException e) {
                        System.err.println("I/O error: " + e.getMessage());
                    }
                }
            });
        } catch (SocketException e) {
            System.err.println("Error with socket: " + e.getMessage());
        }
    }

    /**
     * create packet based on the passed request and the socket
     *
     * @param request Request to be added to the package
     * @param socket  socket for sending and receiving packets.
     * @return new {@link DatagramPacket}
     */
    private DatagramPacket createPacket(String request, InetSocketAddress socket) {
        byte[] requestData = request.getBytes(StandardCharsets.UTF_8);
        return new DatagramPacket(requestData, requestData.length, socket);
    }

    /**
     * receives the response from the specified socket
     *
     * @param datagramSocket Socket for receiving and answering packets
     * @return socket data converted to string
     * @throws IOException if there is an error when receiving packets
     */
    private String receiveResponse(DatagramSocket datagramSocket) throws IOException {
        byte[] responseData = new byte[datagramSocket.getReceiveBufferSize()];
        DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length);
        datagramSocket.receive(responsePacket);

        return new String(responsePacket.getData(), responsePacket.getOffset(), responsePacket.getLength(), StandardCharsets.UTF_8);
    }
}
