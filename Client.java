import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer;

    // Constructor
    public Client(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }

    public void sendThenReceive() throws SocketException {
        Scanner scanner = new Scanner(System.in);
        datagramSocket.setSoTimeout(5000); // Set timeout to 5000 ms (1 second)
        int count = 0; // Sequence number for messages

        // Loop to continuously send messages
        while (true) {
            try {
                String input = scanner.nextLine(); // Read user input from console
                for (int i = 0; i < input.length(); i++) {
                    // Prepare message in "DATA <count> <character>" format
                    String messageToSend = "DATA " + count + " " + input.charAt(i) + "\n";
                    System.out.println("send: " + messageToSend);
                    buffer = messageToSend.getBytes(); // Convert message to bytes

                    DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 3456);

                    while (true) { // Keep sending until a response is received
                        try {
                            datagramSocket.send(datagramPacket);
                            datagramSocket.receive(datagramPacket);

                            // Get the server's response and check if it contains the expected ACK sequence
                            String messageFromServer = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                            if (messageFromServer.contains("ACK") && messageFromServer.contains(Integer.toString(count))) {
                                System.out.println("recv: " + messageFromServer);
                                break; // Exit the loop if response is received
                            }
                            System.out.println("wrong seq #");
                        } catch (SocketTimeoutException e) {
                            System.out.println("TO-resend: " + messageToSend);
                        }
                    }
                    count++;  // Increment the sequence number after successful acknowledgment
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        scanner.close();
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket(); // Create a socket for communication
        String hostName = null;
        try {
            hostName = args[0];
            if (!hostName.contains(".utdallas.edu"))
                hostName = hostName + ".utdallas.edu";
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need argument: remoteHost");
            System.exit(-1);
        }
        InetAddress inetAddress = InetAddress.getByName(hostName); // Resolve host name to IP address
        Client client = new Client(datagramSocket, inetAddress); // Initialize Client instance
        client.sendThenReceive(); // Start sending and receiving messages
    }
}
