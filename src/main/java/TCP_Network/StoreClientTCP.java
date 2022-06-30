package TCP_Network;

// Client Side
import Base.Packet;
import Technical.SrvrDownException;
import Technical.SrvrOverloadException;

import javax.crypto.BadPaddingException;
import java.io.*;
import java.net.*;


public class StoreClientTCP extends Thread {

    private int port;
    private Network network;
    private Packet packet;

    private int connectionTimeout = 500;


    public StoreClientTCP(int port, Packet packet) {
        this.port = port;
        this.packet = packet;
    }

    private void connect() throws IOException {
        int attempt = 0;

        while (true) {
            try {
                Socket socket = new Socket("localhost", port);
                network = new Network(socket, 3000);
                return;

            } catch (ConnectException e) {
                if (attempt > 3) {
                   throw new SrvrDownException("The server is down!");
                }

                try {
                    Thread.sleep(connectionTimeout + connectionTimeout * attempt);

                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                ++attempt;
            }
        }
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Client ID: " + Thread.currentThread().getId() + ";");

        System.out.println("\nStart thread "+Thread.currentThread().getName());

        try {
            try {
                int attempt = 0;
                while (true) {


                    if (attempt == 3) throw new SrvrOverloadException("The server is overloaded!");

                    connect();

                    byte[] firstPcktBytes = network.receive();


                    if (firstPcktBytes == null) {
                        ++attempt;
                        continue;
                    }

                    Packet helloPckt = new Packet(firstPcktBytes);

                    System.out.println("Received answer from server " +Thread.currentThread().getName() +": "+helloPckt.getBMsq().getMessage());

                    network.send(packet.toPacket());

                    byte[] dataPcktBytes = network.receive();
                    if (dataPcktBytes == null) {
                        System.out.println("Server timeout on thread "+Thread.currentThread().getName());


                        ++attempt;
                        continue;
                    }
                    Packet dataPacket = new Packet(dataPcktBytes);
                    System.out.println("Received answer from server " +Thread.currentThread().getName() +": " + dataPacket.getBMsq().getMessage());
                    break;
                }
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } finally {
            if (network != null) {
                network.shutdown();
            }
            System.out.println("END ON THREAD " + Thread.currentThread().getName());
        }
    }

    public void shutdown() {
        network.shutdown();
    }

}