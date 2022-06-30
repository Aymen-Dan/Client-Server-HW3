package TCP_Network;

import Base.Message;
import Base.Packet;
import Base.Processor;
import TCP_Network.Network;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler implements Runnable {

    private Network network;

    private ThreadPoolExecutor executor;

    private AtomicInteger processingAmount = new AtomicInteger(0);

    public ClientHandler(Socket clientSocket, ThreadPoolExecutor executor, int maxNumOfTimeout) throws IOException {
        network = new Network(clientSocket, maxNumOfTimeout);
        this.executor = executor;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Client Handler: " + Thread.currentThread().getName());
        try {
            Packet helloPacket = null;
            try {
                helloPacket = new Packet((byte) 0, 0L,
                        new Message(Message.cTypes.RESPONSE, 0, "Connection established"));
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
            network.send(helloPacket.toPacket());

            while (true) {
                byte[] packetBytes = network.receive();
                if (packetBytes == null) {
                    System.out.println("Client timeout!");
                    break;
                }
                handlePacketBytes(Arrays.copyOf(packetBytes, packetBytes.length));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }


    public void shutdown() {
        while (processingAmount.get() > 0) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        network.shutdown();
    }

    private void handlePacketBytes(byte[] packetBytes) {
        processingAmount.incrementAndGet();

        CompletableFuture.supplyAsync(() -> {
                    Packet packet = null;
                    try {
                        packet = new Packet(packetBytes);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return packet;
                }, executor)

                .thenAcceptAsync((inputPacket -> {
                    Packet answerPacket = null;
                    try {
                        answerPacket = Processor.process(inputPacket);
                    } catch (BadPaddingException e) {
                        e.printStackTrace();

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }

                    try {
                        network.send(answerPacket.toPacket());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    processingAmount.decrementAndGet();

                }), executor)

                .exceptionally(ex -> {
                    ex.printStackTrace();
                    processingAmount.decrementAndGet();
                    return null;
                });
    }




}