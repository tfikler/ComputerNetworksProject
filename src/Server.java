import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private static final ServerProperties properties = new ServerProperties();
    private static final int PORT = properties.getPort();
    private static final int MAX_THREADS = properties.getMaxThreads();
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

        public ClientHandler(Client client) {
            this.client = client;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            } catch (IOException e) {
                e.printStackTrace();
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