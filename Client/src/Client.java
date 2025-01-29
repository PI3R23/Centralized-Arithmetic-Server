import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) {
        int udpPort = 12345; // Example port, use one that matches your server
        String serverIP = null;

        // 1. Sending a UDP broadcast packet in the local network
        try {
            serverIP = sendUDPBroadcast(udpPort);
            if (serverIP == null) {
                System.out.println("Failed to detect the server.");
                return;
            }
            System.out.println("Received server IP address: " + serverIP);

            // 2. Connecting to the server via TCP
            try (Socket socket = new Socket(serverIP, udpPort)) {
                System.out.println("Connected to server TCP: " + serverIP + " on port " + udpPort);

                // 3. Loop to send requests to the server at random intervals
                Random random = new Random();
                String[] operations = {"ADD", "SUB", "MUL", "DIV"};
                while (true) {
                    int arg1 = random.nextInt(100);  // random number
                    int arg2 = random.nextInt(100);  // random number
                    String operation = operations[random.nextInt(operations.length)];

                    String message = operation + " " + arg1 + " " + arg2;
                    System.out.println("Sending operation: " + message);
                    sendMessage(socket, message);

                    String response = receiveMessage(socket);
                    System.out.println("Server response: " + response);

                    long delay = random.nextInt(5) + 1; // Wait for a random time (1-5 seconds)
                    TimeUnit.SECONDS.sleep(delay);
                }

            } catch (IOException | InterruptedException e) {
                System.out.println("Error connecting to the server: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error while sending UDP packet: " + e.getMessage());
        }
    }

    // Function to send a UDP broadcast packet to detect the server in the local network
    private static String sendUDPBroadcast(int port) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        // Create a broadcast UDP packet
        String message = "CAS DISCOVER";
        byte[] buffer = message.getBytes();
        InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcastAddress, port);
        socket.send(packet);
        System.out.println("Sent UDP packet to: " + broadcastAddress + " on port: " + port);

        // Waiting for a response
        socket.setSoTimeout(5000); // Wait up to 5 seconds for a response
        DatagramPacket responsePacket = new DatagramPacket(new byte[256], 256);

        try {
            socket.receive(responsePacket);
            String response = new String(responsePacket.getData()).trim();
            System.out.println("Received response: " + response + " from " + responsePacket.getAddress());

            if (response.startsWith("CAS FOUND")) {
                return responsePacket.getAddress().getHostAddress(); // Return the IP address from the response
            } else {
                System.out.println("Server response is not valid: " + response);
                return null;
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout: No response from the server.");
            return null;
        } finally {
            socket.close();
        }
    }

    // Function to send a message to the TCP server
    private static void sendMessage(Socket socket, String message) {
        try {
            socket.getOutputStream().write(message.getBytes());
            socket.getOutputStream().flush();
        } catch (IOException e) {
            System.out.println("Error sending message to the server: " + e.getMessage());
        }
    }

    // Function to receive a message from the TCP server
    private static String receiveMessage(Socket socket) {
        byte[] buffer = new byte[256];
        try {
            int bytesRead = socket.getInputStream().read(buffer);
            return new String(buffer, 0, bytesRead).trim();
        } catch (IOException e) {
            System.out.println("Error receiving message from the server: " + e.getMessage());
            return null;
        }
    }
}
