import java.io.IOException;
import java.net.SocketException;

public class CAS {
    public static void main(String[] args) {
        int port = 0;
        if(args.length!=1){
            System.err.println("Usage : java -jar CAS.jar <port>");
            System.exit(1);
        }
        try{
            port = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e){
            System.err.println("Error: argument must be an integer");
            System.exit(1);
        }

        try {
            int finalPort = port;
            new Thread(() -> {
                try {
                    new FindingService(finalPort);
                } catch (SocketException e) {
                    System.err.println("Error starting UDP service: " + e.getMessage());
                }
            }).start();

            RaportService.getTmpStatistics();
            RaportService.getStatistics();

            new ClientCommunication(port);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}