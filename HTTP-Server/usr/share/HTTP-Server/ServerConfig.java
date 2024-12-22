package outil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {
    private Properties properties;

    public ServerConfig(String configFilePath) {
        properties = new Properties();
        try (FileInputStream conf = new FileInputStream(configFilePath)) {
            
            properties.load(conf);

        } catch (IOException e) {
            Logger.error("Erreur lors du chargement du fichier de configuration : " + configFilePath, e);
            e.printStackTrace();
        }
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }
}

