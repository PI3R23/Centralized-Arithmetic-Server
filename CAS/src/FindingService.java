import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class FindingService {
    byte[] buf;
    DatagramPacket datagramPacket;
    public FindingService(int port) throws SocketException {
        DatagramSocket socket=new DatagramSocket(port);

        new Thread(() -> {
            try {
                while (true) {
                    String s = getData(socket);

                    if (s.startsWith("CAS DISCOVER")) {
                        InetAddress clientAddress = datagramPacket.getAddress();
                        int clientPort = datagramPacket.getPort();

                        sendData(clientAddress, clientPort, socket);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error: cannot receive data");
                System.exit(1);
            }
        }).start();

    }

    private void sendData(InetAddress clientAddress, int clientPort,DatagramSocket socket) throws IOException {
        byte[] response = "CAS FOUND".getBytes();
        datagramPacket = new DatagramPacket(response, response.length, clientAddress, clientPort);
        socket.send(datagramPacket);
    }

    private String getData(DatagramSocket socket) throws IOException {
        buf = new byte[127];
        datagramPacket = new DatagramPacket(buf, buf.length);
        socket.receive(datagramPacket);
        return new String(buf).trim();
    }
}
