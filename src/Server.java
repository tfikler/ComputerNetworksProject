import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private static final ServerProperties properties = new ServerProperties();
    private static final int PORT = ServerProperties.getPort();
    private static final int MAX_THREADS = ServerProperties.getMaxThreads();
    private static final ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_THREADS);
    private static final Set<Client> clients = new HashSet<>();

    public static void main(String[] args) throws IOException {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                if (threadPool.getActiveCount() <= MAX_THREADS) {
                    Client client = new Client(clientSocket);
                    clients.add(client);
                    threadPool.submit(new ClientHandler(client));
                } else {
                    System.out.println("Maximum number of threads reached. Connection rejected.");
                    clientSocket.close();
                }
            }
        }
    }

    private static class ClientHandler extends Thread {
        private final Client client;
        private HTTPRequest httpRequest;

        public ClientHandler(Client client) {
            this.client = client;
        }

        public void run() {
            try {
               BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                OutputStream out = client.getOutputStream();
                StringBuilder input= new StringBuilder();
                String requestLine = in.readLine();
                System.out.println("Request Line: " + requestLine);
                input.append(requestLine).append("\r\n");

                // Read request headers
                String headerLine;
                while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                    System.out.println("Header: " + headerLine);
                    input.append(headerLine).append("\r\n");
                }

                // Read request body if it exists
                StringBuilder requestBody = new StringBuilder();
                while (in.ready()) {
                    char[] buffer = new char[1024];
                    int bytesRead = in.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        requestBody.append(buffer, 0, bytesRead);
                    }
                }
                if (requestBody.length() > 0) {
                    System.out.println("Request Body:");
                    System.out.println(requestBody.toString());
                    input.append(requestBody);
                }

                System.out.println(input);
                httpRequest = new HTTPRequest(input.toString());
                httpRequest.handleRequest(out);

            } catch (IOException e) {
                throw new RuntimeException(e);

            } finally {
                // Close the client and remove it from the set
                clients.remove(client);
                try {
                    client.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}