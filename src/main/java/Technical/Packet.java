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
            throw new Exception("First byte is incorrect!");

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

        Integer BgngLength = bMagic.BYTES + bSource.BYTES + Long.BYTES + wLength.BYTES;

        byte[] Bgng = ByteBuffer.allocate(BgngLength)
                .put(bMagic)
                .put(bSource)
                .putLong(bPaketID)
                .putInt(wLength)
                .array();

        wCRC16_1 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, Bgng);

        Integer ScndLength = message.getMessageBytesLength();

        byte[] Scnd = ByteBuffer.allocate(ScndLength)
                .put(message.toPacketPart())
                .array();

        wCRC16_2 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, Scnd);

        Integer packetLength = BgngLength + wCRC16_1.BYTES + ScndLength + wCRC16_2.BYTES;

        return ByteBuffer.allocate(packetLength).put(Bgng).putShort(wCRC16_1).put(Scnd).putShort(wCRC16_2).array();
    }

    public Long getbPaketID() {
        return bPaketID;
    }

}