package controller;

import network.GameClient;
import view.LoginPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController implements ActionListener {

    private LoginPanel loginPanel;
    private GameController gameController;

    public LoginController(LoginPanel loginPanel, GameController gameController){
        this.loginPanel = loginPanel;
        this.gameController = gameController;
        loginPanel.getConnectPlayer().addActionListener(this);
    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = loginPanel.getTextField().getText();
        if(username.isEmpty()) {
            return;
        }
        if(e.getSource() == loginPanel.getConnectPlayer()) {
            gameController.getGameClient().sendMessage("LOGIN:" + username);
        }
    }
}
