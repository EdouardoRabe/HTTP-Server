package outil;

import java.io.FileWriter; 
import java.io.IOException;

public class Logger {
    private static String LOG_FILE ;
    private static final ServerConfig serverConfig = new ServerConfig("config/server.config");

    public static synchronized void log(String level, String message) {
        LOG_FILE=serverConfig.getString("log");
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write("[" + level + "] " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void error(String message, Throwable throwable) {
        LOG_FILE=serverConfig.getString("log");
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write("[ERROR] " + message + "\n");
            if (throwable != null) {
                fw.write("[ERROR] Détails : " + throwable.getMessage() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
}
