package Technical;

import com.github.snksoft.crc.CRC;
import lombok.Data;
import java.nio.ByteBuffer;

@Data
public class Packet {
    public final static Byte bMagic = 0x13;

    Byte bSource;
    Long bPaketID;
    Integer wLength;
    Message bMsq;
    Short wCRC16_1;
    Short wCRC16_2;

    public Packet(Byte bSrc, Long bPktId, Message bMsq) {
        this.bSource = bSrc;
        this.bPaketID = bPktId;

        this.bMsq = bMsq;
        wLength = bMsq.getMessage().length();
    }

    public Packet(byte[] bytes) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        Byte expectedBMagic = byteBuffer.get();
        System.out.println(bMagic);
        if (!expectedBMagic.equals(bMagic))
            throw new Exception("Incorrect first byte");

        bSource = byteBuffer.get();
        bPaketID = byteBuffer.getLong();
        wLength = byteBuffer.getInt();

        wCRC16_1 = byteBuffer.getShort();

        bMsq = new Message();
        bMsq.setCType(byteBuffer.getInt());
        bMsq.setBUserId(byteBuffer.getInt());
        byte[] messageBody = new byte[wLength];
        byteBuffer.get(messageBody);
        bMsq.setMessage(new String(messageBody));
        bMsq.decode();

        wCRC16_2 = byteBuffer.getShort();
    }

    public byte[] toPacket() {
        Message message = getBMsq();

        message.encode();

        Integer packetPartFirstLength = bMagic.BYTES + bSource.BYTES + Long.BYTES + wLength.BYTES;
        byte[] packetPartFirst = ByteBuffer.allocate(packetPartFirstLength)
                .put(bMagic)
                .put(bSource)
                .putLong(bPaketID)
                .putInt(wLength)
                .array();

        wCRC16_1 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, packetPartFirst);

        Integer packetPartSecondLength = message.getMessageBytesLength();
        byte[] packetPartSecond = ByteBuffer.allocate(packetPartSecondLength)
                .put(message.toPacketPart())
                .array();

        wCRC16_2 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, packetPartSecond);

        Integer packetLength = packetPartFirstLength + wCRC16_1.BYTES + packetPartSecondLength + wCRC16_2.BYTES;

        return ByteBuffer.allocate(packetLength).put(packetPartFirst).putShort(wCRC16_1).put(packetPartSecond).putShort(wCRC16_2).array();
    }

    public Long getbPaketID() {
        return bPaketID;
    }

}