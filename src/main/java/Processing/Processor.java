package Processing;

import Technical.Packet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Processor implements Runnable{
    private static final ExecutorService service = Executors.newFixedThreadPool(5);
    private final Packet packet;
    private Sender PackResponse;

    public Processor(Packet packet){
        this.packet = packet;
    }

    public static void process(byte [] encodedPacket) throws Exception {
        service.submit(new Processor(new Packet(encodedPacket)));
    }

    public static void shutdown(){
            service.shutdown();
    }

    @Override
    public void run() {

        try {
            Thread.sleep(3000);
            new UsableNetwork().sendMessage(Sender.packResponse(packet));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}