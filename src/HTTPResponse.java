public class HTTPResponse {
    private String status_code;
    private String content_type;
    private int content_length;
    private String content;

        public HTTPRespond(HTTPRequest hTTPRequest) {
        x(HTTPRequest);
    }
           
        public HTTPResponse(int statusCode, String statusMessage, String contentType, String content) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.contentType = contentType;
        this.content = content;
    }
    }

       public String generateResponse() {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(status_code).append("\r\n");
        response.append("content-type: ").append(content_type).append("\r\n");
        response.append("content-length: ").append(content_length).append("\r\n");
        response.append("\r\n");
        response.append(content);
        return response.toString();
    }

    public void get(HTTPRequest hTTPRequest)
    {
      try {
            File file = new File(fileName);

            if (!file.exists()) {
                handle404Error();
            }
            else
            (
              readFile(file);

            )
        } 
        catch(FileNotFoundException e){
         handle404Error();
     }
     content_type= hTTPRequest.getRequestType; 
    } 

    private byte[] readFile(File file)
   {

     try
   {
    FileInputStream fis = new FileInputStream(file);
    byte[] bFile = new byte[(int)file.length()];

     // read until the end of the stream.
    while(fis.available() != 0)
    {
     fis.read(bFile, 0, bFile.length);
    }
    return bFile;


    }
    catch(FileNotFoundException e)
    {
         handle404Error();
     }
     catch(IOException e)
    {
       handle500Error();
    }
         
} 

    private static void handle404Error() {
        status_code="404";
         content_length=172;
         content="<!DOCTYPE html><html><head><title>404 Not Found</title></head><body><h1>Not Found</h1><p>The requested URL /example-resource was not found on this server.</p></body></html>";
    }

    private static void handle500Error() {
        status_code="500";
         content_length=224;
         content="<!DOCTYPE html><html><head><title>500 Internal Server Error</title></head><body><h1>Internal Server Error</h1><p>The server encountered an unexpected condition that prevented it from fulfilling the request.</p></body></html>";
    }



}