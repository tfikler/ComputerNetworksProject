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
        if (httpRequest.getRequestType().equals("GET")) {
            handleGetRequest(httpRequest);
        } else if (httpRequest.getRequestType().equals("HEAD")) {
//            handleHeadRequest(httpRequest);
        } else if (httpRequest.getRequestType().equals("POST")) {
            handlePostRequest(httpRequest);
        } else if (httpRequest.getRequestType().equals("TRACE")) {
//            handleTraceRequest(httpRequest);
        } else {
            ErrorHandler.handle501Error();
//            content = ErrorHandler.getContent();
            contentLength = ErrorHandler.getContentLength();
            statusCode = ErrorHandler.getStatusCode();
        }
    }

    private void handlePostRequest(HTTPRequest httpRequest) throws IOException {
        byte[] newParamsFile = generateSecondPage(httpRequest.getParameters());
//        content = Files.readAllBytes(Paths.get("src/params_info.html"));
        content = newParamsFile;
        contentLength = content.length;
        contentType = "text/html";
        statusCode = "200 OK";
    }

    private void handleGetRequest(HTTPRequest httpRequest) throws IOException {
        if (httpRequest.getRequestedPage().equals("/") || httpRequest.getRequestedPage().equals("/index.html")) {
            try {
                content = Files.readAllBytes(Paths.get("src/index.html"));
                contentLength = content.length;
                contentType = "text/html";
                statusCode = "200 OK";
            } catch (IOException e) {
                ErrorHandler.handle404Error();
//                content = ErrorHandler.getContent();
                contentType = httpRequest.getRequestType();
                contentLength = ErrorHandler.getContentLength();
                statusCode = ErrorHandler.getStatusCode();
            }
        } else if (httpRequest.isImage()) {
            content = Files.readAllBytes(Paths.get("src" + httpRequest.getRequestedPage()));
            contentLength = content.length;
            if (httpRequest.getRequestedPage().endsWith(".ico")) {
                contentType = "icon";
            } else {
                contentType = "image";
            }
            statusCode = "200 OK";
        } else {
            ErrorHandler.handle404Error();
//            content = ErrorHandler.getContent();
            contentType = httpRequest.getRequestType();
            contentLength = ErrorHandler.getContentLength();
            statusCode = ErrorHandler.getStatusCode();
        }
    }


//                case "/" -> {
//                    try{
//                    content = new String(Files.readAllBytes(Paths.get("src/index.html")));
//                    contentLength = content.length();
//                    contentType = httpRequest.getRequestType();
//                    statusCode = "200 OK";
//                    }
//                    catch (IOException e)
//                    {
//                      ErrorHandler.handle404Error();
//                      content = ErrorHandler.getContent();
//                      contentType = httpRequest.getRequestType();
//                      contentLength = ErrorHandler.getContentLength();
//                      statusCode = ErrorHandler.getStatusCode();
//                    }
//                }
//                case "/example-resource" -> {
//                    content = "<!DOCTYPE html><html><head><title>Example Resource</title></head><body><h1>This is an example resource</h1></body></html>";
//                    contentLength = content.length();
//                    contentType = httpRequest.getRequestType();
//                    statusCode = "200 OK";
//                }
//                case "/image.jpg" -> {
//                    content = "image.jpg";
//                    contentLength = content.length();
//                    contentType =httpRequest.getRequestType();
//                    statusCode = "200 OK";
//                }
//                default -> {
//                    ErrorHandler.handle404Error();
//                    content = ErrorHandler.getContent();
//                    contentType = httpRequest.getRequestType();
//                    contentLength = ErrorHandler.getContentLength();
//                    statusCode = ErrorHandler.getStatusCode();
//                }
//            }
//            if (httpRequest.getRequestType().equals("HEAD")&&statusCode.equals("200 OK"))
//            {
//              content="";
//            }
//            if (httpRequest.getRequestType().equals("POST")&&statusCode.equals("200 OK"))
//            {
//
//            }
//
//        }
//        else if(httpRequest.getRequestType().equals("TRACE"))
//        {
//          content=httpRequest.getHTTPRequest();
//          contentLength = content.length();
//          contentType =httpRequest.getRequestType();
//          statusCode = "200 OK";
//        }
//        else {
//            ErrorHandler.handle501Error();
//            content = ErrorHandler.getContent();
//            contentLength = ErrorHandler.getContentLength();
//            statusCode = ErrorHandler.getStatusCode();
//        }
//    }

    private byte[] generateSecondPage(HashMap<String, String> params) {
        // Load the base HTML from your existing file
        File htmlContent = new File("src/params_info.html");
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

    private String generateDynamicContent(HashMap<String, String> params) {
        // Generate the dynamic content based on the parsed parameters
        StringBuilder dynamicContentBuilder = new StringBuilder();

        // Populate table rows with parsed parameters
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            dynamicContentBuilder.append("<tr>");
            dynamicContentBuilder.append("<td>").append(entry.getKey()).append("</td>");
            dynamicContentBuilder.append("<td>").append(entry.getValue()).append("</td>");
            dynamicContentBuilder.append("</tr>");
        }

        return dynamicContentBuilder.toString();
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
}