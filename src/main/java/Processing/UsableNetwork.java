package Processing;


import Technical.Message;
import Technical.Packet;


public class UsableNetwork implements Network{

        @Override
        public void receiveMessage() {
            try {
                Processor.process(Generator.generateMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void sendMessage(byte[] message) {
            try {
                Packet packet = new Packet(message);
                Message message1 = packet.getBMsq();
                System.out.println("\nCurrent ID: " + Thread.currentThread().getId() + ";\nResponse sent to user: "
                        +  packet.getBMsq().getMessage() + "; \nThe command: " + cTypes.values()[message1.getCType()]+"\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

