package controller;

import network.GameClient;
import view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class GameController implements ActionListener {
    private Frame frame;
    private MouseHandler mouse;
    private LoginController loginController;
    private KeyHandler keyHandler;
    private Timer timer;

    private GameClient gameClient;

    public GameController(String ip, int port) {
        frame = new Frame();
        mouse = new MouseHandler(this);
        timer = new Timer(0, this);
        keyHandler = new KeyHandler(this);
        try {
            gameClient = new GameClient(ip, port, this);
            Thread thread = new Thread(gameClient);
            thread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        loginController = new LoginController(frame.getLoginPanel(), this);
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public void hoverTile(int x, int y){
        TileView[][] grid = frame.getMainPanel().getGrid();
        for(int i = 0; i < frame.getMainPanel().getGridHeight(); i++) {
            for(int j = 0; j < frame.getMainPanel().getGridWidth(); j++) {
                grid[i][j].setHover(grid[i][j].isClicked(x, y));
            }
        }
    }

    public void hoverMultipleTile(int x, int y){
        TileView[][] grid = frame.getMainPanel().getGrid();
        TileView selected = null;
        for(int i = 0; i < frame.getMainPanel().getGridHeight(); i++) {
            for(int j = 0; j < frame.getMainPanel().getGridWidth(); j++) {
                if(grid[i][j].isClicked(x, y)) {
                    selected = grid[i][j];
                }
                grid[i][j].setHover(false);
            }
        }
        if(selected != null) {
            frame.getMainPanel().getSelectionView().hoverSelected(selected, frame.getMainPanel());
        }
    }

    public void placeArmy(int x, int y){
        TileView[][] grid = frame.getMainPanel().getGrid();
        for(int i = 0; i < frame.getMainPanel().getGridHeight(); i++) {
            for(int j = 0; j < frame.getMainPanel().getGridWidth(); j++) {
                if(grid[i][j].isClicked(x, y)) {
                    gameClient.sendMessage("PLACE_ARMY:"+i+":"+j);
                    break;
                }
            }
        }
    }

    public void placeBuilding(int x, int y){
        TileView[][] grid = frame.getMainPanel().getGrid();
        for(int i = 0; i < frame.getMainPanel().getGridHeight(); i++) {
            for (int j = 0; j < frame.getMainPanel().getGridWidth(); j++) {
                if(grid[i][j].isClicked(x, y)) {
                    gameClient.sendMessage("PLACE_BUILDING:" + i + ":" + j);
                }
            }
        }
    }

    public void multiSelectArmy() {
        MainPanel mainPanel = frame.getMainPanel();
        for(Drawable armyView : mainPanel.getDrawables()) {
            if(armyView instanceof ArmyView) {
                ArmyView aView = (ArmyView) armyView;
                if(mainPanel.getSelect().intersects(aView.getHitBox())) {
                    mainPanel.getSelectionView().addSelected(aView);
                    aView.setSelected(true);
                } else {
                    mainPanel.getSelectionView().removeSelected(aView);
                    aView.setSelected(false);
                }
            }
        }
    }

    public void selectArmy(int x, int y) {
        MainPanel mainPanel = frame.getMainPanel();
        ArmyView selected = null;
        for(Drawable armyView : mainPanel.getDrawables()) {
            if(armyView instanceof ArmyView) {
                if(selected == null) {
                    if(armyView.getHitBox().contains(x, y)) {
                        selected = (ArmyView) armyView;
                        if(selected.getSelected()) {
                            mainPanel.getSelectionView().removeSelected(selected);
                            selected.setSelected(false);
                        } else {
                            mainPanel.getSelectionView().addSelected(selected);
                            selected.setSelected(true);
                        }
                    } else {
                        mainPanel.getSelectionView().removeSelected((ArmyView) armyView);
                        ((ArmyView) armyView).setSelected(false);
                    }
                } else {
                    mainPanel.getSelectionView().removeSelected((ArmyView) armyView);
                    ((ArmyView) armyView).setSelected(false);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.getMainPanel().repaint();
    }

    public Timer getTimer() {
        return timer;
    }

    public MouseHandler getMouse() {
        return mouse;
    }

    public Frame getFrame() {
        return frame;
    }

    public GameClient getGameClient() {
        return gameClient;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }
}
