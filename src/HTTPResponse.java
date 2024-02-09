import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class HTTPResponse {
    byte[] content;
    int contentLength;
    String contentType;
    String statusCode;
    public HTTPResponse(HTTPRequest httpRequest) throws IOException {
        if ((httpRequest.getRequestType().equals("GET")) || (httpRequest.getRequestType().equals("HEAD")))
        {
            handleGetAndHeadRequest(httpRequest);
        }
        else if (httpRequest.getRequestType().equals("POST"))
        {
            handlePostRequest(httpRequest);
        }
        else if (httpRequest.getRequestType().equals("TRACE"))
        {
            handleTraceRequest(httpRequest);
        }
        else
        {
            ErrorHandler.handle501Error();
            content = ErrorHandler.getContent().getBytes();
            contentLength = ErrorHandler.getContentLength();
            contentType = ErrorHandler.getContentType();
            statusCode = ErrorHandler.getStatusCode();
        }
    }

    private void handlePostRequest(HTTPRequest httpRequest){
        content = generateParamsInfoPage(httpRequest.getParameters());
        contentLength = content.length;
        contentType = "text/html";
        statusCode = "200 OK";
    }

    private void handleGetAndHeadRequest(HTTPRequest httpRequest) throws IOException {
        try {
            if (httpRequest.getRequestedPage().equals("/") || httpRequest.getRequestedPage().equals("/" + ServerProperties.getDefaultPage())) {
                content = Files.readAllBytes(Paths.get(ServerProperties.getRoot() + ServerProperties.getDefaultPage()));
                contentLength = content.length;
                contentType = "text/html";
                statusCode = "200 OK";
            } else if (httpRequest.isImage()) {
                content = Files.readAllBytes(Paths.get(ServerProperties.getRoot() + httpRequest.getRequestedPage()));
                contentLength = content.length;
                if (httpRequest.getRequestedPage().endsWith(".ico")) {
                    contentType = "icon";
                } else {
                    contentType = "image";
                }
                statusCode = "200 OK";
            } else if (httpRequest.getRequestedPage().contains("/params_info.html")) {
                content = generateParamsInfoPage(httpRequest.getParameters());
                contentLength = content.length;
                contentType = "text/html";
                statusCode = "200 OK";
            }
            else {
                ErrorHandler.handle400Error();
                content = ErrorHandler.getContent().getBytes();
                contentLength = ErrorHandler.getContentLength();
                contentType = ErrorHandler.getContentType();
                statusCode = ErrorHandler.getStatusCode();

            }
        } catch (IOException e) {
            ErrorHandler.handle404Error();
            content = ErrorHandler.getContent().getBytes();
            contentLength = ErrorHandler.getContentLength();
            contentType = ErrorHandler.getContentType();
            statusCode = ErrorHandler.getStatusCode();
        }
    }

    private void handleTraceRequest(HTTPRequest httpRequest){
        content = httpRequest.getHTTPRequest().getBytes();;
        contentLength = content.length;
        contentType = "application/octet-stream";
        statusCode = "200 OK";
    }

    private byte[] generateParamsInfoPage(HashMap<String, String> params) {
        File htmlContent = new File(ServerProperties.getRoot() + "params_info.html");
        StringBuilder paramsHTML = new StringBuilder();
        try {
            if (htmlContent.exists()) {
                byte[] htmlByteContent = Files.readAllBytes(htmlContent.toPath());
                String html = new String(htmlByteContent);
                for (Object key : params.keySet()) {
                    paramsHTML.append("<tr>")
                            .append("<td>")
                            .append(key)
                            .append("</td>")
                            .append("<td>")
                            .append(params.get(key).toString())
                            .append("</td>")
                            .append("</tr>");
                }
                System.out.println(paramsHTML.toString());
                html = html.replace("{{new_table}}", paramsHTML.toString());

                return html.getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlContent.toString().getBytes();
    }

    public void send(OutputStream out) throws IOException {
        out.write(("HTTP/1.1 " + statusCode + "\r\n").getBytes());
        System.out.println("HTTP/1.1 " + statusCode + "\r\n");
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        System.out.println("Content-Type: " + contentType + "\r\n");
        out.write(("Content-Length: " + contentLength + "\r\n\r\n").getBytes());
        System.out.println("Content-Length: " + contentLength + "\r\n\r\n");
        out.write(content);
        out.flush();
    }

    public void sendHead(OutputStream out) {
        try {
            out.write(("HTTP/1.1 " + statusCode + "\r\n").getBytes());
            out.write(("Content-Type: " + contentType + "\r\n").getBytes());
            out.write(("Content-Length: " + contentLength + "\r\n\r\n").getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}