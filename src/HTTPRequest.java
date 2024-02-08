import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

public class HTTPRequest {
    private String httpRequest;
    private String requestType;
    private String requestedPage;
    private boolean isImage;
    private int contentLength;
    private String referer;
    private String userAgent;
    private HashMap<String, String> parameters=new HashMap<>();;

    public HTTPRequest(String httpRequest) {
        this.httpRequest = httpRequest;
        parseTheRequest();
    }

    public void parseTheRequest() {
        String[] lines = this.httpRequest.split("\n");
        String input = lines[0];
        // Handling the 1st line, getting the type and page
        requestType = input.substring(0, input.indexOf(' '));
        input = input.substring(input.indexOf(' ') + 1);
        requestedPage = input.substring(0, input.indexOf(' '));
        isImage = requestedPage.matches(".+\\.(jpg|bmp|gif|jpeg|tiff|psd|svg|raw|ico|heic|avif|png)$");
        if (requestedPage.contains("?")) {
            String queryString = requestedPage.substring(requestedPage.indexOf("?") + 1);
            parseTheParams(queryString);
            }

        for (String line : lines) {
            // Handling the 3rd line, getting the Length
            final String trim = line.substring(line.indexOf(' ') + 1).trim();
            if (line.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(trim);
            }
            // Getting the referer
            else if (line.startsWith("Referer:")) {
                referer = trim;
            }
            // Getting the user agent
            else if (line.startsWith("User-Agent:")) {
                userAgent = trim;
            }
            else if (getRequestType().equals("POST")&&line.contains("="))
            {
              String[] keyValue = line.split("=");
              if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
              }
            }
        }
    }

    public void parseTheParams(String queryString) {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                }
            }
        }

    public String getHTTPRequest() {
        return httpRequest;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getRequestedPage() {
        return requestedPage;
    }

    public boolean isImage() {
        return isImage;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    // New method to get the path
    public String getPath() {
        // Extract the path from the requestedPage
        int questionMarkIndex = requestedPage.indexOf("?");
        return (questionMarkIndex != -1) ? requestedPage.substring(0, questionMarkIndex) : requestedPage;
    }

    public void handleRequest(OutputStream out) throws IOException {
        HTTPResponse httpResponse = new HTTPResponse(this);
        httpResponse.send(out);
    }
}
