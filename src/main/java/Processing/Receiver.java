package Processing;

import Technical.Message;
import Technical.Packet;
import java.util.Random;

enum cTypes {
    GET_PRODUCT_AMOUNT,
    SET_PRODUCT_AMOUNT,
    SET_PRODUCT_DESCRIPTION,
    GET_PRODUCT_DESCRIPTION,
    SET_PRODUCT_PRICE,
    GET_PRODUCT_PRICE,
    GET_PRODUCT,
    ADD_PRODUCT,
    ADD_PRODUCT_NAME,
    ADD_PRODUCT_TO_GROUP
}

/**the actual receiver*/
public class Receiver {
    public static byte[] receive() {
        Random random = new Random();

        int command = random.nextInt(cTypes.values().length);//random command

        String commandMessage = (cTypes.values()[command]).toString();
        Message testMessage = new Message(command,1, commandMessage);

        long bPaketID = random.nextLong();//generate a random Long

        Packet packet = new Packet((byte)1, bPaketID, testMessage);

        byte[] packetToBytes = packet.toPacket();
        return packetToBytes;
    }
}