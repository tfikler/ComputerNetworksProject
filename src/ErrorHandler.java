import java.io.IOException;
import java.io.OutputStream;

public class ErrorHandler {
    private static String statusCode;
    private static int contentLength;
    private static String content;
    private static String contentType = "text/html";

    public static void handle400Error() throws IOException {
        statusCode = "400 Bad Request";
        content = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>400 Bad Request</title>
                </head>
                <body>
                <h1>Bad Request</h1>
                <p>Your browser sent a request that this server could not understand.</p>
                <p>To return to the main page please click here: <a href="http://localhost:8080/">Main page</a></p>
                </body>
                </html>
                """;
        contentLength = content.length();
    }
    public static void handle404Error() throws IOException {
        statusCode = "404 Not Found";
        content = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>404 Page not found</title>
                </head>
                <body>
                <h1>Page not found</h1>
                <p>The server encountered an unexpected condition that prevented it from fulfilling the request.</p>
                <p>To return to the main page please click here: <a href="http://localhost:8080/">Main page</a></p>
                </body>
                </html>
                """;
        contentLength = content.length();
    }

    public static void handle500Error() {
        statusCode = "500 Internal Server Error";
        content = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>500 Internal Server Error</title>
                </head>
                <body>
                <h1>Internal Server Error</h1>
                <p>The server encountered an unexpected condition that prevented it from fulfilling the request.</p>
                <p>To return to the main page please click here: <a href="http://localhost:8080/">Main page</a></p>
                </body>
                </html>
                """;
        contentLength = content.length();
    }


    public static void handle501Error() {
        statusCode = "501 Not Implemented";
        content = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>501 Not Implemented</title>
                </head>
                <body>
                <h1></h1>
                <p>The server encountered an unexpected condition that prevented it from fulfilling the request.</p>
                <p>To return to the main page please click here: <a href="http://localhost:8080/">Main page</a></p>
                </body>
                </html>
                """;
        contentLength = content.length();
    }


    public static String getStatusCode() {
        return statusCode;
    }

    public static int getContentLength() {
        return contentLength;
    }

    public static String getContent() {
        return content;
    }

    public static String getContentType() {
        return contentType;
    }
}
