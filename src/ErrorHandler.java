public class ErrorHandler {
    private static String statusCode;
    private static int contentLength;
    private static String content;

    public static void handle400Error() {
        statusCode = "400 Bad Request";
        content = "<!DOCTYPE html><html><head><title>400 Bad Request</title></head><body><h1>400 Bad Request</h1><p>The server could not understand the request due to invalid syntax or other client-side error.</p></body></html>";
        contentLength = content.length();
    }

    public static void handle404Error() {
        statusCode = "404 Not Found";
        content = "<!DOCTYPE html><html><head><title>404 Not Found</title></head><body><h1>Not Found</h1><p>The requested URL was not found on this server.</p></body></html>";
        contentLength = content.length();
    }

    public static void handle500Error() {
        statusCode = "500 Internal Server Error";
        content = "<!DOCTYPE html><html><head><title>500 Internal Server Error</title></head><body><h1>Internal Server Error</h1><p>The server encountered an unexpected condition that prevented it from fulfilling the request.</p></body></html>";
        contentLength = content.length();
    }


    public static void handle501Error() {
        statusCode = "501 Not Implemented";
        content = "<!DOCTYPE html><html><head><title>501 Not Implemented</title></head><body><h1>501 Not Implemented</h1><p>The server does not support the functionality required to fulfill the request.</p></body></html>";
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
