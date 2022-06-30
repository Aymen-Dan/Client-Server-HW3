package Base;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.ByteBuffer;

public class Packet {

    public final static Byte B_MAGIC = 0x13;
    public final static Integer HEADER_LENGTH = 16;
    public final static Integer CRC16_LENGTH = 2;


    Long bPktId;
    Byte bSrc;
    Integer wLen;
    Short wCrc16_1;
    Message bMsq;
    Short wCrc16_2;


    public Message getBMsq() {
        return bMsq;
    }

    public Byte getbSrc() {
        return bSrc;
    }

    public void setbPktId(Long bPktId) {
        this.bPktId = bPktId;
    }

    public Long getbPktId() {
        return bPktId;
    }

    public final static Integer packetPartFirstLengthWithoutwLen = B_MAGIC.BYTES + Byte.BYTES + Long.BYTES;
    public final static Integer packetPartFirstLength = packetPartFirstLengthWithoutwLen + Integer.BYTES;
    public final static Integer packetPartFirstLengthWithCRC16 = packetPartFirstLength + Short.BYTES;


    public Packet(Byte bSrc, Long bPktId, Message bMsq) {
        this.bSrc = bSrc;
        this.bPktId = bPktId;
        this.bMsq = bMsq;
        wLen = bMsq.fullMessageBytesLength();
    }


    public Packet(byte[] encodedPacket) throws BadPaddingException, IllegalBlockSizeException {

        ByteBuffer buffer = ByteBuffer.wrap(encodedPacket);

        Byte expectedBMagic = buffer.get();


        bSrc = buffer.get();
        bPktId = buffer.getLong();
        wLen = buffer.getInt();

        wCrc16_1 = buffer.getShort();

        final short crc1Evaluated = Crc_16.evaluateCrc(encodedPacket, 0, 14);


        bMsq = new Message();
        bMsq.setCType(buffer.getInt());
        bMsq.setBUserId(buffer.getInt());

        byte[] messageBody = new byte[wLen - 8];
        buffer.get(messageBody);


        wCrc16_2 = buffer.getShort();

        bMsq.setEncryptedMessageInBytes(messageBody);
        bMsq.decode();

        byte[] messageToEvaluate = new byte[wLen];
        System.arraycopy(encodedPacket, 16, messageToEvaluate, 0, wLen);

        final short crc2Evaluated = Crc_16.evaluateCrc(messageToEvaluate, 0, wLen);

    }


    public byte[] toPacket() {

        Message message = getBMsq();


        byte[] packetPartFirst = ByteBuffer.allocate(packetPartFirstLength)
                .put(B_MAGIC)
                .put(bSrc)
                .putLong(bPktId.longValue())
                .putInt(wLen)
                .array();


        wCrc16_1 = Crc_16.evaluateCrc(packetPartFirst, 0, 14);


        Integer packetPartSecondLength = message.fullMessageBytesLength();

        byte[] packetPartSecond = ByteBuffer.allocate(packetPartSecondLength)
                .put(message.toPacketPart())
                .array();


        wCrc16_2 = Crc_16.evaluateCrc(packetPartSecond, 0, packetPartSecond.length);


        Integer packetLength = packetPartFirstLength + wCrc16_1.BYTES + packetPartSecondLength + wCrc16_2.BYTES;

        return ByteBuffer.allocate(packetLength).put(packetPartFirst).putShort(wCrc16_1).put(packetPartSecond).putShort(wCrc16_2).array();
    }
}