package info.kgeorgiy.ja.mironov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

public abstract class AbstractHelloUDPServer implements HelloServer {
    //
    protected final int SOKET_TIMEOUT = 200;

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Expected: <port> <threads>");
            return;
        }
        try {
            int port = Integer.parseInt(args[0]);
            int workers = Integer.parseInt(args[1]);
            try (HelloUDPNonblockingServer server = new HelloUDPNonblockingServer()) {
                server.start(port, workers);
            }
        } catch (NumberFormatException e) {
            System.err.println("Expected: <port> <threads> " + e.getMessage());
        }
    }
}
