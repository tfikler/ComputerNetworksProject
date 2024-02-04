import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HTTPResponse {
    String content;
    int contentLength;
    String contentType;
    String statusCode;
    public HTTPResponse(HTTPRequest httpRequest) throws IOException {
        if (httpRequest.getRequestType().equals("GET")) {
            switch (httpRequest.getRequestedPage()) {
                case "/" -> {
                    content = new String(Files.readAllBytes(Paths.get("src/index.html")));
                    contentLength = content.length();
                    contentType = "text/html";
                    statusCode = "200";
                }
                case "/example-resource" -> {
                    content = "<!DOCTYPE html><html><head><title>Example Resource</title></head><body><h1>This is an example resource</h1></body></html>";
                    contentLength = content.length();
                    contentType = "text/html";
                    statusCode = "200";
                }
                case "/image.jpg" -> {
                    content = "image.jpg";
                    contentLength = content.length();
                    contentType = "image/jpeg";
                    statusCode = "200";
                }
                default -> {
                    ErrorHandler.handle404Error();
                    content = ErrorHandler.getContent();
                    contentType = "text/html";
                    contentLength = ErrorHandler.getContentLength();
                    statusCode = ErrorHandler.getStatusCode();
                }
            }
        } else {
            ErrorHandler.handle500Error();
            content = ErrorHandler.getContent();
            contentLength = ErrorHandler.getContentLength();
            statusCode = ErrorHandler.getStatusCode();
        }
    }

    public void send(PrintWriter out) {
        out.write("HTTP/1.1 " + statusCode + " OK\r\n");
        System.out.println("HTTP/1.1 " + statusCode + " OK\r\n");
        out.write("Content-Type: " + contentType + "\r\n");
        System.out.println("Content-Type: " + contentType + "\r\n");
        out.write("Content-Length: " + contentLength + "\r\n\r\n");
        System.out.println("Content-Length: " + contentLength + "\r\n\r\n");
        out.write(content);
        out.flush();
    }
}