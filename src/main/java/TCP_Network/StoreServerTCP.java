package TCP_Network;

// Server Side

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class StoreServerTCP extends Thread {

    public ServerSocket server;

    private int port;
    private ThreadPoolExecutor connectionPool;
    private ThreadPoolExecutor processPool;
    private int clientTimeout;

    public StoreServerTCP(int port, int maxNumOfConnectionThreads, int maxNumOfProcessThreads, int maxNumOfClientTimeout)
            throws IOException {
        super("Server");

        //max timeout?
        if (maxNumOfClientTimeout < 0) throw new IllegalArgumentException("Timeout can't be negative");
        this.port = port;
         processPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxNumOfProcessThreads);
         connectionPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxNumOfConnectionThreads);
         //timeout
        this.clientTimeout = maxNumOfClientTimeout;
        server = new ServerSocket(port);
    }

    public void shutdown() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        try {
            System.out.println("Server running on port: " + port);

            while (true) {
                connectionPool.execute(new ClientHandler(server.accept(), processPool, clientTimeout));
            }

        } catch (SocketException e) {
            System.out.println("Closing server");

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            connectionPool.shutdown();
            processPool.shutdown();
            System.out.println("Server shutdown");
        }

    }



}
