package  outil;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String rootDirectory;
    private boolean phpEnabled;

    public ClientHandler(Socket socket, String rootDirectory,boolean phpEnabled) {
        this.clientSocket = socket;
        this.rootDirectory = rootDirectory;
        this.phpEnabled = phpEnabled;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder request = new StringBuilder();
            String line;
            while ((line = input.readLine()) != null && !line.isEmpty()) {
                request.append(line).append("\r\n");
            }

            String[] headers = request.toString().split("\r\n");

            int contentLength = 0;
            for (String header : headers) {
                if (header.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(header.split(":")[1].trim());
                    break;
                }
            }

            String postParameters = "";
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                input.read(buffer, 0, contentLength);
                postParameters = new String(buffer);
            }

            HttpRequest httpRequest = new HttpRequest(request.toString(), postParameters);

            HttpResponse httpResponse = new HttpResponse();
    
            byte[] reponse = httpResponse.generateResponse(httpRequest, rootDirectory, phpEnabled);

            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(reponse);
            outputStream.flush();
            input.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   

}
