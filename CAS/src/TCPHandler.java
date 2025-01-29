import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCPHandler {
    private Socket socket;

    public TCPHandler(Socket socket) {
        this.socket = socket;
    }

    public String getMessage() {
        byte[] buffer = new byte[127];
        try {
            int bytesRead = socket.getInputStream().read(buffer);
            return new String(buffer, 0, bytesRead).trim();
        } catch (IOException e) {
            System.err.println("Error reading message: " + e.getMessage());
        }
        return "";
    }


    public void sendMessage(String message) {
        byte[] buffer = message.getBytes();
        try {
            OutputStream stream = socket.getOutputStream();
            stream.write(buffer);
            stream.flush();
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}