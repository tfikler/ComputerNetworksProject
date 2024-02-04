public class ErrorHandler {
    private static String statusCode;
    private static int contentLength;
    private static String content;

    public static void handle404Error() {
        statusCode = "404";
        contentLength = 172;
        content = "<!DOCTYPE html><html><head><title>404 Not Found</title></head><body><h1>Not Found</h1><p>The requested URL was not found on this server.</p></body></html>";
    }

    public static void handle500Error() {
        statusCode = "500";
        contentLength = 224;
        content = "<!DOCTYPE html><html><head><title>500 Internal Server Error</title></head><body><h1>Internal Server Error</h1><p>The server encountered an unexpected condition that prevented it from fulfilling the request.</p></body></html>";
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

    public void setStatus_code(String statusCode) {
        ErrorHandler.statusCode = statusCode;
    }

    public void setContent_length(int contentLength) {
        ErrorHandler.contentLength = contentLength;
    }

    public void setContent(String content) {
        ErrorHandler.content = content;
    }
}
