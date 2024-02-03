//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
       String requestHeader = "GET /index.html?param1=value1&param2=value2 HTTP/1.1\n" +
                               "Host: example.com\n" +
                               "Content-Length: 100\n" +
                               "Referer: http://referer.com\n" +
                               "User-Agent: Mozilla/5.0";
        
        HTTPRequest httpRequest = new HTTPRequest(requestHeader);
    }
}