package Processing;

// Client Side
import java.io.*;
import java.net.*;

public class StoreClientTCP {
    public void run() {
        try {
            //assume that the server is listening for connection request on port serverPort via TCP.
            int serverPort = 4020;
            InetAddress host = InetAddress.getByName("localhost");
            System.out.println("Connecting to server on port " + serverPort);

            UsableNetwork socket = new UsableNetwork(host,serverPort);
            //Socket socket = new Socket("127.0.0.1", serverPort);
            System.out.println("Just connected to a server!");

            /**When the client's request is accepted, the client creates an input stream to receive data from its socket and an output stream to send data to the socket at the server's end of the channel*/



            System.out.println("Client received: " + line + " from Server");
            toServer.close();
            fromServer.close();
            socket.close();
        }
        catch(UnknownHostException ex) {
            ex.printStackTrace();
        }
        //throws an IOException if there is no connection
        catch(IOException e){
            e.printStackTrace();
        }
    }

}