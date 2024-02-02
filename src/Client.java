import java.io.*;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String getPort() {
        return Integer.toString(socket.getPort());
    }

    public String getIpAddress() {
        return socket.getInetAddress().getHostAddress();
    }


    public void close() throws IOException {
        socket.close();
        reader.close();
        writer.close();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }
}
