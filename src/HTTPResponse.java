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
    final int chunk=1024;
    public HTTPResponse(HTTPRequest httpRequest) throws IOException {
        try{
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
            else if (httpRequest.getRequestType().equals("OPTIONS"))
            {
                handleOptionsRequest(httpRequest);
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
        catch (Exception e)
        {
            ErrorHandler.handle400Error();
            content = ErrorHandler.getContent().getBytes();
            contentLength = ErrorHandler.getContentLength();
            contentType = ErrorHandler.getContentType();
            statusCode = ErrorHandler.getStatusCode();
        }
    }

    private void handleOptionsRequest(HTTPRequest httpRequest) throws IOException {
        try {
            if (httpRequest.getRequestedPage().equals("/") || httpRequest.getRequestedPage().equals("/" + ServerProperties.getDefaultPage())||httpRequest.getRequestedPage().contains("/params_info.html")||(httpRequest.isImage()&&Files.exists(Paths.get(ServerProperties.getRoot() + httpRequest.getRequestedPage())))) {
                contentType = "application/octet-stream";
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

    private void handlePostRequest(HTTPRequest httpRequest){
        contentType = "text/html";
        statusCode = "200 OK";
        content = generateParamsInfoPage(httpRequest.getParameters());
        contentLength = content.length;
    }

    private void handleGetAndHeadRequest(HTTPRequest httpRequest) throws IOException {
        try {
            if (httpRequest.getRequestedPage().equals("/") || httpRequest.getRequestedPage().equals("/" + ServerProperties.getDefaultPage())) {
                contentType = "text/html";
                statusCode = "200 OK";
                content = Files.readAllBytes(Paths.get(ServerProperties.getRoot() + ServerProperties.getDefaultPage()));
                contentLength = content.length;
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
                contentType = "text/html";
                statusCode = "200 OK";
                content = generateParamsInfoPage(httpRequest.getParameters());
                contentLength = content.length;
            }
            else if (Files.exists(Paths.get(ServerProperties.getRoot() + httpRequest.getRequestedPage()))) {
                contentType = "text/html";
                statusCode = "200 OK";
                content = Files.readAllBytes(Paths.get(ServerProperties.getRoot() + httpRequest.getRequestedPage()));
                contentLength = content.length;
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
        contentType = "application/octet-stream";
        statusCode = "200 OK";
        content = httpRequest.getHTTPRequest().getBytes();
        contentLength = content.length;
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
                html = html.replace("{{new_table}}", paramsHTML.toString());
                return html.getBytes();
            }
        } catch (IOException e) {
            ErrorHandler.handle500Error();
            content = ErrorHandler.getContent().getBytes();
            contentLength = ErrorHandler.getContentLength();
            contentType = ErrorHandler.getContentType();
            statusCode = ErrorHandler.getStatusCode();
            return content;
        }
        return null;
    }


    public void send(OutputStream out) throws IOException {
        out.write(("HTTP/1.1 " + statusCode + "\r\n").getBytes());
        System.out.print("HTTP/1.1 " + statusCode + "\r\n");
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        System.out.print("Content-Type: " + contentType + "\r\n");
        out.write(("Content-Length: " + contentLength + "\r\n\r\n").getBytes());
        System.out.print("Content-Length: " + contentLength + "\r\n\r\n");
        out.write(content);
        out.flush();
    }

    public void sendOptions(OutputStream out) {
        try {
            out.write(("HTTP/1.1 " + statusCode + "\r\n").getBytes());
            System.out.print("HTTP/1.1 " + statusCode + "\r\n");
            out.write(("Content-Type: " + contentType + "\r\n").getBytes());
            System.out.print("Content-Type: " + contentType + "\r\n");
            out.write(("Allow: OPTIONS, GET, HEAD, POST"+ "\r\n\r\n").getBytes());
            out.flush();
        } catch (IOException e) {
            ErrorHandler.handle500Error();
            content = ErrorHandler.getContent().getBytes();
            contentLength = ErrorHandler.getContentLength();
            contentType = ErrorHandler.getContentType();
            statusCode = ErrorHandler.getStatusCode();;
        }
    }

    public void sendHead(OutputStream out) {
        try {
            System.out.print("HTTP/1.1 " + statusCode + "\r\n");
            out.write(("HTTP/1.1 " + statusCode + "\r\n").getBytes());
            System.out.print("Content-Type: " + contentType + "\r\n");
            out.write(("Content-Type: " + contentType + "\r\n").getBytes());
            out.write(("Content-Length: " + contentLength + "\r\n\r\n").getBytes());
            System.out.print("Content-Length: " + contentLength + "\r\n");
            out.flush();
        } catch (IOException e) {
            ErrorHandler.handle500Error();
            content = ErrorHandler.getContent().getBytes();
            contentLength = ErrorHandler.getContentLength();
            contentType = ErrorHandler.getContentType();
            statusCode = ErrorHandler.getStatusCode();;
        }
    }
    public void sendChunked(OutputStream out) throws IOException {
        try {
            out.write(("HTTP/1.1 " + statusCode + "\r\n").getBytes());
            System.out.print("HTTP/1.1 " + statusCode + "\r\n");
            out.write(("Content-Type: " + contentType + "\r\n").getBytes());
            System.out.print("Content-Type: " + contentType + "\r\n");
            out.flush();
            for (int i = 0; i < content.length; i += chunk) {
                int size = Math.min(content.length - i,chunk);
                String hexSize = Integer.toHexString(size) + "\r\n";
                out.write(hexSize.getBytes());
                out.write(content, i, size);
                out.write("\r\n".getBytes());
                out.flush();
            }
            out.write("0\r\n\r\n".getBytes());
            out.flush();
        } catch (IOException e) {
            ErrorHandler.handle500Error();
            content = ErrorHandler.getContent().getBytes();
            contentLength = ErrorHandler.getContentLength();
            contentType = ErrorHandler.getContentType();
            statusCode = ErrorHandler.getStatusCode();;
        }
    }
}