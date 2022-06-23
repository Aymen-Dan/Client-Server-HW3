import Processing.Processor;
import Processing.UsableNetwork;
import Technical.Message;
import Technical.Packet;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TestTest {

    /**check encryption*/
@Test
    void test0() {
        Message message = new Message(1, 1, new String("checkencryption"));

        Packet packet = new Packet((byte) 1, (long) 4, message);
        byte[] encodedPacket = packet.toPacket();
        try {
            Packet dcddPacket = new Packet(encodedPacket);
            byte[] dPacket = dcddPacket.toPacket();
            assertEquals(packet.getBSource(), dcddPacket.getBSource());
            assertEquals(packet.getWCRC16_1(), dcddPacket.getWCRC16_1());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**check packet description*/
    @Test
    void test2() throws Exception {

        Message message = new Message(1, 1, "descriptioneeee");

        byte[] messageToBytes = message.toPacketPart();//msg into bytes
        Packet packet = new Packet((byte) 1, (long) 444, message);
//
        byte[] packetToBytes = packet.toPacket();//packet into bytes

        Packet packet_decoded = new Packet(packetToBytes);
        Message message_decoded = packet_decoded.getBMsq();

        byte[] decoded_messageToBytes = message_decoded.toPacketPart();//decoded msg into bytes
        assert (Arrays.equals(messageToBytes, decoded_messageToBytes));
    }

    @Test
    void finalTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 20; i++) {

            //execute
            executorService.submit(() -> {
                UsableNetwork tcp = new UsableNetwork();
                tcp.receiveMessage();
            });
        }
        try {
            executorService.shutdown();
            while (!executorService.awaitTermination(24L, TimeUnit.HOURS)) {
                System.out.println("Please wait.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Processor.shutdown();

    }
}