package Processing;

import Technical.Packet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Processor implements Runnable{
    private static ExecutorService service = Executors.newFixedThreadPool(5);
    private Packet packet;
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
            new usableNetwork().sendMessage(PackResponse.packResponse(packet));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}