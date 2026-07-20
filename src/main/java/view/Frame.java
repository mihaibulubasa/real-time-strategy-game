package view;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private LoginPanel loginPanel;
    private MainPanel mainPanel;
    private CardLayout layout;

    public Frame(){
        setTitle("Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        layout = new CardLayout();
        setLayout(layout);
        loginPanel = new LoginPanel();
        add(loginPanel, "login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        layout.show(getContentPane(), "login");
        setVisible(true);
    }

    public LoginPanel getLoginPanel(){
        return loginPanel;
    }

    public MainPanel getMainPanel(){
        return mainPanel;
    }

    public void setMainPanel(int width, int height){
        mainPanel = new MainPanel(width, height);
        add(mainPanel, "game");
        layout.show(getContentPane(), "game");
        revalidate();
        repaint();
    }
}
