package UDP_Network;


import Base.Packet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.*;

public class StoreClientUDP extends Thread {
    DatagramSocket dtgrmSckt = null;
    DatagramPacket dtgrmPckt = null;
    InetAddress IP = InetAddress.getLocalHost();

    private final int port;
    private final Packet packet;
    private Packet answerPacket;



    public StoreClientUDP(int port, Packet packet) throws UnknownHostException {
        this.port = port;
        this.packet = packet;

        try {
            dtgrmSckt = new DatagramSocket();
            dtgrmSckt.setSoTimeout(1500);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        this.start();
    }

    public Packet getAnswerPacket() throws InterruptedException {
        while (answerPacket == null) {
            sleep(1);
        }
        return answerPacket;
    }

    @Override
    public void run() {

        sendAndReceive(packet);

    }

    private void sendAndReceive(Packet packet) {

        if (dtgrmSckt.isClosed()) {
            try {
                dtgrmSckt = new DatagramSocket();
                dtgrmSckt.setSoTimeout(1500);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }


        byte[] packetBytes = packet.toPacket();
        dtgrmPckt = new DatagramPacket(packetBytes, packetBytes.length, IP, port);
        boolean received = false;

        try {
            dtgrmSckt.send(dtgrmPckt);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {

            byte[] buff = new byte[1024];
            DatagramPacket incomingDatagramPacket = new DatagramPacket(buff, buff.length);

            try {
                dtgrmSckt.receive(incomingDatagramPacket);
            } catch (IOException e) {
                if (received) {
                    System.out.println("Client Socket timed out!");
                    dtgrmSckt.close();
                    break;

                } else {
                    dtgrmSckt.close();
                    System.out.println("Resending packet. Packet ID: " + packet.getbPktId());
                    sendAndReceive(packet);
                    break;
                }
            }

            Packet answerPacket = null;
            try {
                answerPacket = new Packet(incomingDatagramPacket.getData());
                this.answerPacket = answerPacket;

            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }

            assert answerPacket != null;
            if (answerPacket.getbPktId().compareTo(packet.getbPktId()) == 0) received = true;
            System.out.println("Message from server : " + answerPacket.getBMsq().getMessage() + ";\nPacket ID : " + answerPacket.getbPktId());
        }
    }



}