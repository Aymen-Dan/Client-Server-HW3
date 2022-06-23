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

    public Long getbPaketID() {
        return bPaketID;
    }

    /**constructing packet*/
    public Packet(Byte bSource, Long bPaketID, Message bMsq) {
        this.bSource = bSource;
        this.bPaketID = bPaketID;
        this.bMsq = bMsq;
        wLength = bMsq.getMessage().length();
    }

    public Packet(byte[] bytes) throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        Byte expectedBMagic = byteBuffer.get();
        System.out.println(bMagic);
        if (!expectedBMagic.equals(bMagic))
            throw new Exception("First byte is wrong!");

        bSource = byteBuffer.get();
        bPaketID = byteBuffer.getLong();
        wLength = byteBuffer.getInt();
        wCRC16_1 = byteBuffer.getShort();

        //bmsq
        bMsq = new Message();
        bMsq.setCType(byteBuffer.getInt());
        bMsq.setBUserId(byteBuffer.getInt());

        //msg body
        byte[] messageBody = new byte[wLength];
        byteBuffer.get(messageBody);

        bMsq.setMessage(new String(messageBody));
        bMsq.decode();//decode

        wCRC16_2 = byteBuffer.getShort();
    }


    public byte[] toPacket() {

        Message message = getBMsq();

        message.encode();

        Integer BgngLength = bMagic.BYTES + bSource.BYTES + Long.BYTES + wLength.BYTES;//first length

        byte[] Bgng = ByteBuffer.allocate(BgngLength)
                .put(bMagic)
                .put(bSource)
                .putLong(bPaketID)
                .putInt(wLength)
                .array();

        wCRC16_1 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, Bgng);//16-1

        Integer ScndLength = message.getMessageBytesLength();//second length

        byte[] Scnd = ByteBuffer.allocate(ScndLength)
                .put(message.toPacketPart())
                .array();

        wCRC16_2 = (short) CRC.calculateCRC(CRC.Parameters.CRC16, Scnd);//16-2

        Integer packetLength = BgngLength + wCRC16_1.BYTES + ScndLength + wCRC16_2.BYTES;
//finally, returning
        return ByteBuffer.allocate(packetLength).put(Bgng).putShort(wCRC16_1).put(Scnd).putShort(wCRC16_2).array();
    }



}