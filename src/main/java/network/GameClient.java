package network;

import controller.GameController;
import network.parsers.EntitiesInitResponse;
import network.parsers.InitResponse;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient implements Runnable{

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private GameController controller;


    public GameClient(String ip, int port, GameController controller) throws IOException {
        socket = new Socket(ip, port);
        this.controller = controller;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            String response;
            while((response = in.readLine()) != null) {
                processResponse(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    private void processResponse(String response) {
        String[] tokens = response.split(":");
        String command = tokens[0];
        switch (command) {
            case "LOGIN_FAIL" :
                controller.getLoginController().getLoginPanel().onLogInFail();
                break;
            case "TILE_GRID_INIT" :
                InitResponse.parseResponse(controller, tokens);
                break;
            case "PLAYER_LIST" :
                SwingUtilities.invokeLater(() -> {
                    controller.getFrame().getMainPanel().updatePlayerList(tokens[1]);
                });
                break;
            case "ENTITIES_INIT" :
                if(tokens.length == 2) {
                    SwingUtilities.invokeLater(() -> {
                        EntitiesInitResponse.parseResponse(controller, tokens[1]);
                    });
                }
                break;
            case "ADD_ARMY" :
                int row = Integer.parseInt(tokens[1]);
                int col = Integer.parseInt(tokens[2]);
                SwingUtilities.invokeLater(() -> {
                    controller.getFrame().getMainPanel().addArmy(row, col);
                });
                break;
            default:
                break;
        }
    }

    private void closeConnection(){
        try {
            if(in != null) in.close();
            if(out != null) out.close();
            if(socket != null) socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
