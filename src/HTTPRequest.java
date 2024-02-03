import java.util.HashMap;

public class HTTPRequest {
     private String httpRequest;
     private String requestType; 
     private String requestedPage;
     private boolean isImage;
     private int contentLength;
     private String referer;
     private String userAgent; 
     private HashMap<String,String> parameters; 

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
       ParseTheParams(requestedPage);
       for (String line : lines) {
        //handeling the 3th line, getting the Length
        if (line.startsWith("Content-Length:")){
        int contentLength=Integer.parseInt(line.substring(line.indexOf(' ')+1));
        }
        //getting the referer 
        else if(line.startsWith("Referer:")){
        referer=line.substring(line.indexOf(' ')+1);
        }
        //getting the user agent
        else if(line.startsWith("User-Agent:")){
        userAgent=line.substring(line.indexOf(' ')+1);
        }
       }
     }

     public void ParseTheParams(String requestedPage)
     {
      if (requestedPage.indexOf("?") != -1) {
            parameters = new HashMap<>();
            String queryString = requestedPage.substring(requestedPage.indexOf("?") + 1);
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                parameters.put(keyValue[0], keyValue[1]);
               }}}
     }

     public String getRequestType()
     {
       return requestType;
     }
     public String getRequestedPage()
     {
       return requestType;
     }
     public boolean isImage()
     {
       return isImage;
     }
     public int getContentLength()
     {
       return contentLength;
     }
     public String getReferer()
     {
       return referer;
     }
     public String getUserAgent()
     {
       return userAgent;
     }
     public HashMap<String,String> getParameters()
     {
       return parameters;
     }




}
