package Processing;


import Technical.Message;
import Technical.Packet;

/** packs the already encoded answer to the client*/
public class Sender {
    public static byte[] packResponse(Packet packet) {
        String response = "OK";

        Message answer = new Message(packet.getBMsq().getCType(), packet.getBMsq().getbUserId(), response);
        Packet respPacket = new Packet((byte) 1, packet.getbPaketID(), answer);

        byte[] encodedPacket = respPacket.toPacket();
        return encodedPacket;
    }
}