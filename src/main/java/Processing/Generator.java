package Processing;

import Technical.Message;

import java.util.Random;



/**the actual receiver*/
public class Generator {
    public static byte[] generateMessage() {

        Random random = new Random();

        int command = random.nextInt(cTypes.values().length);//random command

        String commandMessage = (cTypes.values()[command]).toString();
        Message testMessage = new Message(command,1, commandMessage);

        long bPaketID = random.nextLong();//generate a random Long

        Packet packet = new Packet((byte)1, bPaketID, testMessage);

        return packet.toPacket();
    }
}