package outil;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HttpResponse {
    private Map<String, String> headers; 
    private String body;
    
    public HttpResponse() {
        this.headers = new HashMap<>();
    }

    public String getRelativePath(String path, String rootDirectory) {
        String valiny = new String();
        if (path.strip().equals(rootDirectory)) {
            return valiny = "/";
        }
        valiny = path.split(rootDirectory)[1];
        valiny = valiny.replaceAll("\\\\", "/");
        return valiny;
    }

    public String assetsRelativePath(String ressource) {
        String valiny = new String();
        int nbSlash = 0;
        for (int i = 0; i < ressource.length(); i++) {
            if (ressource.charAt(i) == '/') {
                nbSlash++;
            }
        }
        nbSlash++;
        for (int i = 1; i < nbSlash; i++) {
            valiny += "../";
        }

        return valiny;
    }

    public File getIndex(File dir) throws Exception {
        for (File file : dir.listFiles()) {
            if (file.getName().equalsIgnoreCase("index.html") || file.getName().equalsIgnoreCase("index.php"))
                return new File(dir.getPath() + "/" + file.getName());
        }
        return null;
    }

    public String generateDirContent(File dir, String ressource, String relativePath) throws Exception { 
        String valiny = new String();
        File[] contenus = dir.listFiles(); 
        for (int i = 0; i < contenus.length; i++) {
            if (contenus[i].getName().compareToIgnoreCase("index.html") == 0 || contenus[i].getName().compareToIgnoreCase("index.php") == 0) {
                return null;
            }
        }
        valiny = "<h1>Index of " + relativePath + "</h1>";
        valiny += "<table>\r\n";
        valiny += "<tr>\r\n";
        valiny += "<th valign=\"top\"></th>\r\n";
        valiny += "<th>Name</th>\r\n";    
        valiny += "<th>Last modified</th>\r\n";    
        valiny += "<th>Size</th>\r\n";    
        valiny += "</tr>\r\n";
        valiny += "<tr>\r\n";
        valiny += "<th colspan=\"4\"><hr></th>\r\n";
        valiny += "</tr>\r\n";
        if (dir.getParent() != null) {
            String iconPath = this.assetsRelativePath(ressource) + "utilitaire/icons/";
            valiny += "<tr>";
            valiny += "<td></td>";
            valiny += "<td class=\"fileNameTd\"><a href=\"../\" class=\"parentDirLink\">";
            valiny += "<img src=\"" + iconPath + "retour.png\" alt=\"\" class=\"icon\">";
            valiny += "Parent Directory</a></td>"; 
            valiny += "</tr>";
        }
        for (int i = 0; i < contenus.length; i++) {
            valiny += "<tr>";
            valiny += "<td class=\"iconTd\">";
            String iconPath = this.assetsRelativePath(ressource) + "utilitaire/icons/";
            System.err.println("path icon"+iconPath);
            if (contenus[i].isFile()) {
                valiny += "<img src=\"" + iconPath + "fichier.png\" alt=\"\" class=\"icon\">";
            }
            else {
                valiny += "<img src=\"" + iconPath + "dossier.png\" alt=\"\" class=\"icon\">";
            }
            valiny += "</td>";
            valiny += "<td class=\"fileNameTd\"><a href=\"";
            valiny += contenus[i].getName() + "\">" + contenus[i].getName() + "</a></td>";
            LocalDateTime lastModified = LocalDateTime.ofInstant(Instant.ofEpochMilli(contenus[i].lastModified()), ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDateTime = lastModified.format(formatter);
            valiny += "<td>" + formattedDateTime + "</td>";
            valiny += "<td>" + contenus[i].length() + "</td>";
            valiny += "<tr>";
        };
        valiny += "<th colspan=\"4\"><hr></th>\r\n";
        valiny += "</table>\r\n";
        return valiny;
    }

    public String generateDirHtmlStructure(File dir, String ressource, String rootDirectory) throws Exception {
        String valiny = new String();
        String relativePath = this.getRelativePath(dir.getPath(), rootDirectory);
        String cssRelativePath = this.assetsRelativePath(ressource) + "utilitaire/css/style.css";
        valiny = "<!DOCTYPE html>\r\n";
        valiny += "<html lang=\"en\">\r\n";
        valiny += "<head>\r\n";
        valiny += "<meta charset=\"UTF-8\">\r\n";
        valiny += "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n";
        valiny += "<link rel=\"stylesheet\" href=\"" + cssRelativePath + "\">\r\n";
        valiny += "<title>Index of " + relativePath + "</title>\r\n";
        valiny += "</head>\r\n";
        valiny += "<body>\r\n";
        String dirContent = this.generateDirContent(dir, ressource, relativePath);
        if (dirContent == null) { 
            return null;
        }
        valiny += dirContent;
        valiny += "</body>\r\n";
        valiny += "</html>\r\n";
        return valiny;
    }

    public byte[] getFileContent(HttpRequest request, File file, String filePath,boolean phpEnabled) throws Exception{
        byte[] retour=new byte[1024];
        if (phpEnabled && filePath.endsWith(".php")) {
            retour= sendPHPFile(request, file);
        } else {
            retour= sendFile(file);
        }
        return retour;
    }

    public byte[] generateResponse(HttpRequest request, String rootDirectory,boolean phpEnabled) throws Exception {
        System.out.println("la resource: "+ request.getUrl());
        String filePath="";
        if(request.getUrl().startsWith("/utilitaire")){
            filePath=request.getUrl();
        }
        else if(request.getUrl().startsWith("/"+rootDirectory)){
            filePath=request.getUrl().replaceAll("/"+rootDirectory, rootDirectory);
        }
        else{
            filePath = rootDirectory + request.getUrl();
        }
        System.out.println("file path " + filePath);
        File file=null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (filePath.startsWith("/utilitaire")) {
            file = new File(filePath.substring(1));
        }
        else{
            file = new File(filePath);
        }
        if (file.exists() && !file.isDirectory()) {
            byteArrayOutputStream.write(getFileContent(request, file, filePath,phpEnabled));
        } 
        else if (file.isDirectory()) {
            String stringResponse = new String();
            if (!filePath.endsWith("/")) {
                filePath += "/";
                stringResponse = "HTTP/1.1 301 Moved Permanently\r\nLocation: " + filePath + "\r\n\r\n";
            }
            else {
                stringResponse = this.generateDirHtmlStructure(file, filePath, rootDirectory);
                if (stringResponse == null) {
                    file = this.getIndex(file);
                    System.out.println("index "+file.getName());
                    return this.getFileContent(request,file,file.getAbsolutePath(),phpEnabled);
                }
                stringResponse = "HTTP/1.1 200 OK\r\n" + 
                            "Content-Type: text/html\r\n\r\n" + stringResponse;
            }
            byte[] ret = stringResponse.getBytes(StandardCharsets.UTF_8);
            byteArrayOutputStream.write(ret);
        }
        else {
            byte[] errorContent = sendError(404, "File Not Found");
            byteArrayOutputStream.write(errorContent);
        }
        return byteArrayOutputStream.toByteArray();
    }



    private byte[] sendPHPFile(HttpRequest request, File file) throws Exception {
        ProcessBuilder processBuilder;
        Process process;
        String method = request.getMethod();
        String parameters = request.getParameters();
        String fileName = file.getAbsolutePath();
        List<String> commands = new ArrayList<>();
        if (parameters.isEmpty()) {
            if (method.equalsIgnoreCase("GET")) {
                commands.add("php");
                commands.add(fileName);
            }
        } else {
            if (method.equalsIgnoreCase("GET")) {
                commands.add("php");
                commands.add("-r");
                commands.add("parse_str('" + parameters + "', $_GET); include('" + fileName + "');");
            } else if (method.equalsIgnoreCase("POST")) {
                commands.add("php");
                commands.add("-r");
                commands.add("parse_str('" + parameters + "', $_POST); include('" + fileName + "');");
            }
        }
        processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder phpOutput = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            phpOutput.append(line).append("\r\n");
        }
        String output = phpOutput.toString();
        System.out.println("output: " + output);
        int bodyIndex = output.indexOf("\r\n\r\n");
        if (bodyIndex != -1) {
            return output.getBytes(StandardCharsets.UTF_8);
        } else {
            String headers = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + output.length() + "\r\n" +
                    "\r\n";
            return (headers + output).getBytes(StandardCharsets.UTF_8);
        }
    }

    private byte[] sendFile(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String mimeType = getMimeType(file);
        setHeader("Content-Type", mimeType);
        setHeader("Content-Length", String.valueOf(fileContent.length));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        sendResponseHeaders(200, "OK", byteArrayOutputStream);
        byteArrayOutputStream.write(fileContent);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] sendError(int statusCode, String message) throws IOException {
        setHeader("Content-Type", "text/html");
        String bodyContent = "<html><body><h1>Error " + statusCode + ": " + message + "</h1></body></html>";
        setBody(bodyContent);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        sendResponseHeaders(statusCode, message, byteArrayOutputStream);
        byteArrayOutputStream.write(body.getBytes());
        Logger.error("Error "+ statusCode , new Exception(message));
        return byteArrayOutputStream.toByteArray();
    }

    private void sendResponseHeaders(int statusCode, String statusMessage, ByteArrayOutputStream byteArrayOutputStream)
            throws IOException {
        String statusLine = "HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n";
        byteArrayOutputStream.write(statusLine.getBytes());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            byteArrayOutputStream.write((entry.getKey() + ": " + entry.getValue() + "\r\n").getBytes());
        }
        byteArrayOutputStream.write("\r\n".getBytes());
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String getMimeType(File file) {
        String mimeType = "application/octet-stream";
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".html") || fileName.endsWith(".php")) {
            mimeType = "text/html";
        } else if (fileName.endsWith(".css")) {
            mimeType = "text/css";
        } else if (fileName.endsWith(".js")) {
            mimeType = "application/javascript";
        } else if (fileName.endsWith(".png")) {
            mimeType = "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            mimeType = "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            mimeType = "image/gif";
        }else if (fileName.endsWith(".mp4")) {
            mimeType = "video/*";
        }

        return mimeType;
    }
}
