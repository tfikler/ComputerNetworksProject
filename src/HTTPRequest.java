import java.util.HashMap;

public class HTTPRequest {
     String httpRequest;
     String requestType; 
     String requestedPage;
     boolean isImage;
     int contentLength;
     String referer;
     String userAgent; 
     HashMap<String,String> parameters; 

     public HTTPRequest(String Httprequest)
     {
       httpRequest=Httprequest;
       ParseTheRequest();
     }
     public void ParseTheRequest()
     {
       String[] lines = this.httpRequest.split("\n");
       String input=lines[0];
       //handeling the 1th line, getting the type and page
       requestType = input.substring(0, input.indexOf(' '));
       input=input.substring(input.indexOf(' ')+1,input.length());
       requestedPage = input.substring(0, input.indexOf(' '));
       isImage = requestedPage.matches(".+\\.(jpg|bmp|gif|jpeg|tiff|psd|svg|raw|ico|heic)$");
        //handeling the 3th line, getting the Length
        input=lines[2];
        int contentLength=Integer.parseInt(input.substring(input.indexOf(' ')+1, input.length()));
        //handeling the 4th line, getting the referer 
        input=lines[3];
        referer=input.substring(input.indexOf(' ')+1, input.length());
        //handeling the 5th line, getting the user agent
        input=lines[4];
        userAgent=input.substring(input.indexOf(' ')+1, input.length());
       
     }
    }
