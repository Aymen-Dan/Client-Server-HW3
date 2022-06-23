package Processing;


import Technical.Message;
import Technical.Packet;


public class usableNetwork implements Network{

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
                System.out.println("\n______________\n#" + Thread.currentThread().getId() + " Send respond to user: "
                        +  packet.getBMsq().getMessage() + "\nCommand: " + cTypes.values()[message1.getCType()]  + "\n______________");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

