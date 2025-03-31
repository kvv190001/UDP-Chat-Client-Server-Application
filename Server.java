import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Server {
    private DatagramSocket datagramSocket;
    private byte[] receiveData = new byte[1024]; // Buffer for receiving data from clients
    private byte[] sendData  = new byte[1024]; // Buffer for sending data to clients

    // Constructor
    public Server(DatagramSocket datagramSocket){
        this.datagramSocket = datagramSocket;
    }

    public void receiveThenSend(){
        int expectedCount = 0; // Keeps track of the expected sequence number
        String messageToClient = "ACK";
        while(true){
            try{
                // Receive data from client
                DatagramPacket datagramPacket = new DatagramPacket(receiveData, receiveData.length);
                datagramSocket.receive(datagramPacket);

                InetAddress inetAddress = datagramPacket.getAddress();// Get client's IP address
                int port = datagramPacket.getPort(); // Get client's port number

                // Convert received data to string
                String messageFromClient = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println("recv: " + messageFromClient);

                // Verify if the message is in correct format
                if (messageFromClient.contains("DATA")) {
                    if(messageFromClient.contains(Integer.toString(expectedCount))){
                        messageToClient = "ACK " + expectedCount + "\n";
                        expectedCount++;
                        System.out.println("Send out to screen: " + messageFromClient.charAt(messageFromClient.length() - 2));
                    }
                }
                System.out.println("send: " + messageToClient);

                // Convert acknowledgment message to bytes and prepare a datagram packet
                sendData = messageToClient.getBytes();
                datagramPacket = new DatagramPacket(sendData, sendData.length, inetAddress, port);
                datagramSocket.send(datagramPacket);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) throws SocketException {
        // Initialize the datagram socket on port 3456 to listen for client messages
        DatagramSocket datagramSocket = new DatagramSocket(3456);
        Server server = new Server(datagramSocket); // Create server instance
        server.receiveThenSend(); // Start receiving and responding to messages
    }
}
