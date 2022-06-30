package org.example;

import TCP_Network.StoreClientTCP;
import TCP_Network.StoreServerTCP;
import Base.Message;
import Base.Packet;
import UDP_Network.StoreClientUDP;
import UDP_Network.StoreServerUDP;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Starting\n");

        int portTCP = 12345;

        StoreServerTCP server = null;
        try {
            server = new StoreServerTCP(portTCP, 40, 10, 5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Packet packet1 = null;

        Packet packet2 = null;

        Packet packet3 = null;

        Packet packet4 = null;
        try {
            packet1 =
                    new Packet((byte) 1, 1L, new Message(Message.cTypes.ADD_PRODUCT_GROUP, 1, "client1"));
            packet2 = new Packet((byte) 1, 1L, new Message(Message.cTypes.ADD_PRODUCT, 1, "client2"));
            packet3 = new Packet((byte) 1, 1L, new Message(Message.cTypes.GET_PRODUCT_AMOUNT, 1, "client3"));
            packet4 = new Packet((byte) 1, 1L, new Message(Message.cTypes.ADD_PRODUCT_TO_GROUP, 1, "client4"));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        StoreClientTCP client1 = new StoreClientTCP(portTCP, packet1);

        StoreClientTCP client2 = new StoreClientTCP(portTCP, packet2);

        StoreClientTCP client3 = new StoreClientTCP(portTCP, packet3);

        StoreClientTCP client4 = new StoreClientTCP(portTCP, packet4);

        client1.start();
        client2.start();
        client3.start();
        client4.start();


        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        server.shutdown();


        System.out.println("\nEND OF  TCP PROTOCOL\n");


                System.out.println("Staring\n");

                int portUDP = 12345;

                Packet pac0 = null;
                Packet pac1 = null;
                Packet pac2 = null;
                Packet pac3 = null;
                Packet pac4 = null;

                try {
                    pac0 = new Packet((byte) 1, UnsignedLong.ONE,
                            new Message(Message.cTypes.ADD_PRODUCT_GROUP, 0, "client 0"));
                    pac1 = new Packet((byte) 1, UnsignedLong.ONE,
                            new Message(Message.cTypes.ADD_PRODUCT, 1, "client 1"));
                    pac2 = new Packet((byte) 1, UnsignedLong.ONE,
                            new Message(Message.cTypes.GET_PRODUCT_AMOUNT, 2, "client 2"));
                    pac3 = new Packet((byte) 1, UnsignedLong.ONE,
                            new Message(Message.cTypes.ADD_PRODUCT_TITLE_TO_GROUP, 3, "client 3"));
                    pac4 = new Packet((byte) 1, UnsignedLong.ONE,
                            new Message(Message.cTypes.SET_PRODUCT_PRICE, 4, "client 4"));
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }

                StoreServerUDP ss = new StoreServerUDP(portUDP);

                StoreClientUDP sc0 = new StoreClientUDP(portUDP, pac0);
                StoreClientUDP sc1 = new StoreClientUDP(portUDP, pac1);
                StoreClientUDP sc2 = new StoreClientUDP(portUDP, pac2);
                StoreClientUDP sc3 = new StoreClientUDP(portUDP, pac3);
                StoreClientUDP sc4 = new StoreClientUDP(portUDP, pac4);



                ss.join();


                System.out.println("\nEnd");
            }

        }