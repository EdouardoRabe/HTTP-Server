package outil;

import java.io.*;

public class HttpRequest {
    private String method;  
    private String url;     
    private String urlComplet;  
    private String  parameters; 
    private String headers; 

    public HttpRequest(String request, String postParameters) throws IOException {
        this.parameters = "";
        String[] lines = request.split("\r\n");

        if (lines.length > 0) {
            String[] requestLine = lines[0].split(" ");
            if (requestLine.length >= 2) {
                this.method = requestLine[0];
                System.out.println("url :"+requestLine[1]);
                this.urlComplet = requestLine[1];
                this.url= requestLine[1];
                int queryStartIndex = urlComplet.indexOf('?');
                if (queryStartIndex != -1) {
                    this.url = urlComplet.substring(0, queryStartIndex);
                }
            } else {
                throw new IllegalArgumentException("La requête ne contient pas de méthode ou d'URL");
            }
        } else {
            throw new IllegalArgumentException("La requête est vide");
        }
        
        StringBuilder headersBuilder = new StringBuilder();
        int i = 1;
        while (i < lines.length && !lines[i].isEmpty()) {
            headersBuilder.append(lines[i]).append("\r\n");
            i++;
        }
        this.headers = headersBuilder.toString();
        if (this.method.equals("GET")) {
            extractGETParameters();
        }
        if (this.method.equals("POST")) {
            System.out.println("post teste. "+method);
            extractPOSTParameters(postParameters);
        }
    }

    private void extractGETParameters() {
        int queryStartIndex = urlComplet.indexOf('?');
        if (queryStartIndex != -1) {
            this.parameters = urlComplet.substring(queryStartIndex + 1);
        }
    }

    private void extractPOSTParameters(String request) {
        this.parameters=request;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getParameters() {
        return this.parameters;
    }

    public String getHeaders() {
        return headers;
    }
}
