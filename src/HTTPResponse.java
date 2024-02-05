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
        if (httpRequest.getRequestType().equals("GET")||httpRequest.getRequestType().equals("HEAD")||httpRequest.getRequestType().equals("POST")) {
            switch (httpRequest.getRequestedPage()) {
                case "/" -> {
                    try{
                    content = new String(Files.readAllBytes(Paths.get("src/index.html")));
                    contentLength = content.length();
                    contentType = httpRequest.getRequestType();
                    statusCode = "200 OK";
                    
                     } catch (FileNotFoundException e) {
                    ErrorHandler.handle404Error();
                    content = ErrorHandler.getContent();
                    contentType =httpRequest.getRequestType();
                    contentLength = ErrorHandler.getContentLength();
                    statusCode = ErrorHandler.getStatusCode();
                     } catch (IOException e) {
                    ErrorHandler.handle500Error();
                    content = ErrorHandler.getContent();
                    contentType = httpRequest.getRequestType();;
                    contentLength = ErrorHandler.getContentLength();
                    statusCode = ErrorHandler.getStatusCode();
                     }
                }
                case "/example-resource" -> {
                    content = "<!DOCTYPE html><html><head><title>Example Resource</title></head><body><h1>This is an example resource</h1></body></html>";
                    contentLength = content.length();
                    contentType = httpRequest.getRequestType();
                    statusCode = "200 OK";
                }
                case "/image.jpg" -> {
                    content = "image.jpg";
                    contentLength = content.length();
                    contentType =httpRequest.getRequestType();
                    statusCode = "200 OK";
                }
                default -> {
                    ErrorHandler.handle404Error();
                    content = ErrorHandler.getContent();
                    contentType = httpRequest.getRequestType();
                    contentLength = ErrorHandler.getContentLength();
                    statusCode = ErrorHandler.getStatusCode();
                }
            }
            if (httpRequest.getRequestType().equals("HEAD")&&statusCode.equals("200 OK"))
            {
              content="";
            }
            if (httpRequest.getRequestType().equals("POST")&&statusCode.equals("200 OK"))
            {
              
            }

        } 
        else if(httpRequest.getRequestType().equals("TRACE"))
        {
          content=httpRequest.getHTTPRequest();
          contentLength = content.length();
          contentType =httpRequest.getRequestType();
          statusCode = "200 OK";
        }
        else {
            ErrorHandler.handle501Error();
            content = ErrorHandler.getContent();
            contentLength = ErrorHandler.getContentLength();
            statusCode = ErrorHandler.getStatusCode();
        }
    }

    public void send(PrintWriter out) {
        out.write("HTTP/1.1 " + statusCode + "\r\n");
        System.out.println("HTTP/1.1 " + statusCode + "\r\n");
        out.write("Content-Type: " + contentType + "\r\n");
        System.out.println("Content-Type: " + contentType + "\r\n");
        out.write("Content-Length: " + contentLength + "\r\n\r\n");
        System.out.println("Content-Length: " + contentLength + "\r\n\r\n");
        out.write(content);
        out.flush();
    }
}