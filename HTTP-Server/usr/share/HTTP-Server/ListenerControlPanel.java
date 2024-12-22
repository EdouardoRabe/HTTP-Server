package ecoute;

import fenetre.ControlPanel;
import outil.ServeurHttp;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Desktop;

public class ListenerControlPanel implements ActionListener {
    ControlPanel controlPanel;
    JButton currentBtn;

    public ListenerControlPanel() {
    }

    public ListenerControlPanel(ControlPanel controlPanel, JButton currentBtn) {
        this.setControlPanel(controlPanel);
        this.setCurrentBtn(currentBtn);
    }

    public ControlPanel getControlPanel() {
        return this.controlPanel;
    }

    public JButton getCurrentBtn() {
        return this.currentBtn;
    }

    public void setControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    public void setCurrentBtn(JButton currentBtn) {
        this.currentBtn = currentBtn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (this.getControlPanel().geButtons()[0].equals(this.getCurrentBtn())) {

                this.getControlPanel().setServeurHttp(new ServeurHttp());
                this.getControlPanel().geButtons()[0].setEnabled(false);
                this.getControlPanel().geButtons()[1].setEnabled(true);
                System.out.println("Serveur HTTP démarré...");

            } else if (this.getControlPanel().geButtons()[1].equals(this.getCurrentBtn())) {

                this.getControlPanel().getServeurHttp().close();
                this.getControlPanel().setServeurHttp(null);
                this.getControlPanel().geButtons()[1].setEnabled(false);
                this.getControlPanel().geButtons()[0].setEnabled(true);
                System.out.println("Serveur HTTP arrêté.");

            } else if (this.getControlPanel().geButtons()[2].equals(this.getCurrentBtn())) {

                System.out.println("Ouverture de la configuration...");
                String filePath = "config/server.config";
                File configFile = new File(filePath);

                if (configFile.exists()) {
                    try {
                        Desktop.getDesktop().open(configFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this.getControlPanel(),
                                "Impossible d'ouvrir le fichier server.conf.",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this.getControlPanel(),
                            "Le fichier server.conf est introuvable.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
