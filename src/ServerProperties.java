import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerProperties {
    private final int port;
    private final String root;
    private final String defaultPage;
    private final int maxThreads;

    public ServerProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.ini"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load server.properties file");
        }
        this.port = Integer.parseInt(properties.getProperty("port"));
        this.root = properties.getProperty("root");
        this.defaultPage = properties.getProperty("defaultPage");
        this.maxThreads = Integer.parseInt(properties.getProperty("maxThreads"));
    }

    public int getPort() {
        return port;
    }

    public String getRoot() {
        return root;
    }

    public String getDefaultPage() {
        return defaultPage;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

}