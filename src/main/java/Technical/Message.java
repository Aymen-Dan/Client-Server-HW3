package Technical;

import lombok.Data;
import java.nio.ByteBuffer;

@Data
public class Message {

    Integer bUserId;
    Integer cType;

    String message;

    public static final int nonMessage_bytes = 8;

    public Message() {}

    public Message( Integer bUserId, Integer cType, String message) {
        this.cType = cType;
        this.bUserId = bUserId;
        this.message = message;
    }

    public byte[] toPacketPart() {
        return ByteBuffer.allocate(getMessageBytesLength())
                .putInt(cType)
                .putInt(bUserId)
                .put(message.getBytes()).array();
    }

    public int getMessageBytesLength() {
        return nonMessage_bytes + getMessageBytes();
    }

    public Integer getMessageBytes() {
        return message.length();
    }

    public Integer getbUserId() {
        return bUserId;
    }

    public Integer getCType() {
        return cType;
    }


    public void encode() {
        message = Cypher.encode(message);
    }
    public void decode() {
        message = Cypher.decode(message);
    }

}