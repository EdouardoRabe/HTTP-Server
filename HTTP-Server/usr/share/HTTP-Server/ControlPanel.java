package fenetre;

import ecoute.ListenerControlPanel;
import outil.ServeurHttp;
import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JFrame {
    ServeurHttp serveurHttp;
    JButton[] buttons;

    public ControlPanel() {
        this.initComponents();
    }

    public void initComponents() {
        JPanel panelTitle = new JPanel();
        JPanel panelSubtitle = new JPanel();
        JPanel panelHttp = new JPanel();
    
        JLabel labelTitle = new JLabel();
        JLabel labelSubtitle = new JLabel();
        JLabel labelHttp = new JLabel();
    
        JButton startBtn1 = new JButton();
        JButton stopBtn1 = new JButton();
        JButton configBtn = new JButton("Config");
    
        this.setButtons(startBtn1, stopBtn1, configBtn);
    
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    
        panelTitle.setPreferredSize(new Dimension(400, 45));
    
        labelTitle.setFont(new Font("Arial Black", 1, 28));
        labelTitle.setText("Control Panel");
        labelTitle.setHorizontalTextPosition(SwingConstants.CENTER);
        panelTitle.add(labelTitle);
    
        panelSubtitle.setPreferredSize(new Dimension(40, 35));
        panelSubtitle.setLayout(new FlowLayout(FlowLayout.LEFT, 35, 5));
    
        labelSubtitle.setFont(new Font("Arial", 0, 20));
        labelSubtitle.setText("Available Servers: ");
        labelSubtitle.setHorizontalTextPosition(SwingConstants.CENTER);
        panelSubtitle.add(labelSubtitle);
    
        panelHttp.setPreferredSize(new Dimension(40, 70));
    
        labelHttp.setFont(new Font("Arial", 0, 16));
        labelHttp.setText("Http");
    
        startBtn1.setFont(new Font("Arial", 1, 18));
        startBtn1.setText("Start");
        startBtn1.setPreferredSize(new Dimension(100, 35));
    
        stopBtn1.setFont(new Font("Arial", 1, 18));
        stopBtn1.setText("Stop");
        stopBtn1.setPreferredSize(new Dimension(100, 35));
    
        configBtn.setFont(new Font("Arial", 1, 18));
        configBtn.setPreferredSize(new Dimension(150, 35));
    
        GroupLayout panelHttpLayout = new GroupLayout(panelHttp);
        panelHttp.setLayout(panelHttpLayout);
        panelHttpLayout.setHorizontalGroup(
                panelHttpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelHttpLayout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(labelHttp)
                                .addGap(30, 30, 30)
                                .addComponent(startBtn1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(stopBtn1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(configBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)));
        panelHttpLayout.setVerticalGroup(
                panelHttpLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelHttpLayout.createSequentialGroup()
                                .addContainerGap(9, Short.MAX_VALUE)
                                .addGroup(panelHttpLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelHttp)
                                        .addComponent(startBtn1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(stopBtn1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(configBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(9, Short.MAX_VALUE)));
    
        // Correct placement for GroupLayout initialization
        GroupLayout layout = new GroupLayout(getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panelSubtitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                        .addComponent(panelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE)
                        .addComponent(panelHttp, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(panelTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addComponent(panelSubtitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addComponent(panelHttp, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)));
    
        this.pack();
        this.setLocationRelativeTo(null);
    
        for (int i = 0; i < this.geButtons().length; i++) {
            JButton btn = this.geButtons()[i];
            if (btn != null) {
                btn.addActionListener(new ListenerControlPanel(this, btn));
                if (i % 2 == 1 && i != 2) {
                    btn.setEnabled(false);
                }
            }
        }
    }
    

    public ServeurHttp getServeurHttp() {
        return this.serveurHttp;
    }

    public JButton[] geButtons() {
        return this.buttons;
    }

    public void setServeurHttp(ServeurHttp serveurHttp) {
        this.serveurHttp = serveurHttp;
    }

    public void setButtons(JButton startBtn1, JButton stopBtn1, JButton configBtn) {
        this.buttons = new JButton[3];
        buttons[0] = startBtn1;
        buttons[1] = stopBtn1;
        buttons[2] = configBtn;
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ControlPanel().setVisible(true);
            }
        });
    }
}
