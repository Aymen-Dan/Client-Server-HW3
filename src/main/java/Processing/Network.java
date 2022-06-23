package Processing;

public interface Network {
    void receiveMessage();
    void sendMessage(byte[] mess);
}