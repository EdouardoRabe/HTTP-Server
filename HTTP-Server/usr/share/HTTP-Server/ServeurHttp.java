package outil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.ServerError;


public class ServeurHttp extends ServerSocket implements Runnable {
    private static final ServerConfig serverConfig = new ServerConfig("config/server.config");
    Thread serverThread;

    public ServeurHttp() throws Exception {
        super(serverConfig.getInt("port"));
        this.startServer();
    }

    @Override
    public void run() {
        try {
            while (!this.isClosed()) {
                Socket client = this.accept();
                System.out.println("Client socket " + client.getInetAddress() + " est connecté");

                client.setSoTimeout(serverConfig.getInt("request_timeout"));

                new Thread(new ClientHandler(client, serverConfig.getString("root"),serverConfig.getBoolean("php_enabled"))).start();
            }
        } catch (SocketException se) {
            System.out.println("Le serveur HTTP a été fermé");
        } catch (IOException e) {
            Logger.error("Erreur lors de la fermeture du serveur", e);
            e.printStackTrace();
        } finally {
            if (!this.isClosed()) {
                try {
                    this.close();
                } catch (IOException e) {
                    Logger.error("Erreur lors de la fermeture du serveur", e);
                    e.printStackTrace();
                }
            }
        }
    }

    public void startServer() {
        try {
            this.serverThread = new Thread(this);
            this.serverThread.start();
        } catch (Exception e) {
            Logger.error("Erreur lors de l'ouverture du serveur", e);
            e.printStackTrace();
        }
    }

}
