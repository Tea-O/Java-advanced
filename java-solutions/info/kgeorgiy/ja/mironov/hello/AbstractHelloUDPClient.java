package info.kgeorgiy.ja.mironov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

public abstract class AbstractHelloUDPClient implements HelloClient {
    //

    protected final int SOKET_TIMEOUT = 200;

    public static void main(String[] args) {
        if (args == null || args.length != 5) {
            System.out.println("Expected: <host> <prefix> <port> <number of threads> <number of requests>");
            return;
        }
        try {
            String host = args[0];
            int port = Integer.parseInt(args[1]);
            String prefix = args[2];
            int numOfThreads = Integer.parseInt(args[3]);
            int numOfRequests = Integer.parseInt(args[4]);

            new HelloUDPClient().run(host, port, prefix, numOfThreads, numOfRequests);
        } catch (NumberFormatException e) {
            System.out.println("Expected: <host> <prefix> <port> <number of threads> <number of requests> " + e.getMessage());
        }
    }
}
