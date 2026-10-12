package network;

import controller.GameController;
import network.parsers.BuildingsInitResponse;
import network.parsers.EntitiesInitResponse;
import network.parsers.InitResponse;
import view.ArmyView;
import view.TileView;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

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
            case "BUILDINGS_INIT" :
                if(tokens.length == 2) {
                    SwingUtilities.invokeLater(() -> {
                        BuildingsInitResponse.parseResponse(controller, tokens[1]);
                    });
                }
                break;
            case "ADD_ARMY" :
                long id = Long.parseLong(tokens[1]);
                int row = Integer.parseInt(tokens[2]);
                int col = Integer.parseInt(tokens[3]);
                SwingUtilities.invokeLater(() -> {
                    controller.getFrame().getMainPanel().addArmy(id, row, col);
                });
                break;
            case "ADD_BUILDING" :
                id = Long.parseLong(tokens[1]);
                String textureName = tokens[2];
                row = Integer.parseInt(tokens[3]);
                col = Integer.parseInt(tokens[4]);
                int width = Integer.parseInt(tokens[5]);
                int height = Integer.parseInt(tokens[6]);
                SwingUtilities.invokeLater(() -> {
                    controller.getFrame().getMainPanel().addBuilding(id, textureName, row, col, width, height);
                });
                break;
            case "PATH" :
                id = Long.parseLong(tokens[1]);
                ArmyView armyView = controller.getFrame().getMainPanel().getArmyView(id);
                LinkedList<TileView> path = new LinkedList<>();
                SwingUtilities.invokeLater(() -> {
                   for(int i = 2; i < tokens.length; i+=2) {
                       int row2 = Integer.parseInt(tokens[i]);
                       int col2 = Integer.parseInt(tokens[i+1]);
                       System.out.println(row2 + " " + col2);
                       path.add(controller.getFrame().getMainPanel().getTileView(row2, col2));
                   }
                   armyView.setPath(path);
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
