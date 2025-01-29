import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientCommunication {
    public ClientCommunication(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("TCP service started on port: " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            RaportService.incrementNewClient();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            TCPHandler handler = new TCPHandler(clientSocket);
            while (true) {
                RaportService.incrementOperation();
                String message = handler.getMessage().trim();
                if (message.isEmpty()) {
                    break;
                }
                System.out.println("Received message: " + message);

                String[] args = message.split(" ");
                if (args.length != 3) {
                    handler.sendMessage("ERROR\n");
                    RaportService.incrementWrongOperation();
                    continue;
                }

                String oper = args[0];
                int arg1, arg2;
                try {
                    arg1 = Integer.parseInt(args[1]);
                    arg2 = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    handler.sendMessage("ERROR\n");
                    RaportService.incrementWrongOperation();
                    continue;
                }

                int result;
                switch (oper) {
                    case "ADD":
                        result = arg1 + arg2;
                        handler.sendMessage(result + "\n");
                        System.out.println("Result of ADD: " + result);
                        RaportService.addSumResult(result);
                        RaportService.incrementCalculatedOperation();
                        break;
                    case "SUB":
                        result = arg1 - arg2;
                        handler.sendMessage(result + "\n");
                        System.out.println("Result of SUB: " + result);
                        RaportService.addSumResult(result);
                        RaportService.incrementCalculatedOperation();
                        break;
                    case "MUL":
                        result = arg1 * arg2;
                        handler.sendMessage(result + "\n");
                        System.out.println("Result of MUL: " + result);
                        RaportService.addSumResult(result);
                        RaportService.incrementCalculatedOperation();
                        break;
                    case "DIV":
                        if (arg2 == 0) {
                            handler.sendMessage("ERROR\n");
                            System.out.println("Error: divide by zero.");
                            RaportService.incrementWrongOperation();
                        } else {
                            result = arg1 / arg2;
                            handler.sendMessage(result + "\n");
                            System.out.println("Result of DIV: " + result);
                            RaportService.addSumResult(result);
                            RaportService.incrementCalculatedOperation();
                        }
                        break;
                    default:
                        handler.sendMessage("ERROR\n");
                        System.out.println("Error: Unknown operation " + oper);
                        RaportService.incrementWrongOperation();
                }
            }
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                    System.out.println("Connection has been closed.");
                }
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}
