package Processing;


import Technical.Message;

/** packs the already encoded answer to the client*/
public class Sender {
    public static byte[] packResponse(Packet packet) {
        String response = "OK";

        Message answer = new Message(packet.getBMsq().getCType(), packet.getBMsq().getBUserId(), response);
        Packet respPacket = new Packet((byte) 1, packet.getBPaketID(), answer);

        return respPacket.toPacket();
    }
}