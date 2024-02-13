import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerProperties {
    private static int port;
    private static String root;
    private static String defaultPage;
    private static int maxThreads;

    public ServerProperties() {
        Properties properties = new Properties();
        try {
            System.out.println("current dir = " + System.getProperty("user.dir"));
            properties.load(new FileInputStream("../config.ini"));
        } catch (IOException e) {
            throw new RuntimeException("We couldn't find the configuration file. We are shutting down the server.");
        }
        port = Integer.parseInt(properties.getProperty("port"));
        root = properties.getProperty("root");
        defaultPage = properties.getProperty("defaultPage");
        maxThreads = Integer.parseInt(properties.getProperty("maxThreads"));
    }

    public static int getPort() {
        return port;
    }

    public static String getRoot() {
        return root;
    }

    public static String getDefaultPage() {
        return defaultPage;
    }

    public static int getMaxThreads() {
        return maxThreads;
    }

}