package controller;

import view.TileView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private GameController gameController;
    private boolean select;
    private boolean command;
    private boolean placeArmy;
    private boolean placeBuilding;

    public KeyHandler(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        if(e.getKeyCode() == KeyEvent.VK_S) {
            select = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_A) {
            command = true;
            gameController.hoverMultipleTile(gameController.getMouse().getMousePosition().x, gameController.getMouse().getMousePosition().y);
            TileView.changeHover(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_D) {
            placeArmy = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_F) {
            placeBuilding = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        if(e.getKeyCode() == KeyEvent.VK_A) {
            command = false;
            gameController.hoverTile(gameController.getMouse().getMousePosition().x, gameController.getMouse().getMousePosition().y);
            TileView.changeHover(false);
        }
        if(e.getKeyCode() == KeyEvent.VK_D) {
            placeArmy = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_F) {
            placeBuilding = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_S) {
            if(!gameController.getMouse().isLeftPressed()) {
                select = false;
            }
        }
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isCommand() {
        return command;
    }

    public boolean isPlaceArmy() {
        return placeArmy;
    }

    public boolean isPlaceBuilding() {
        return placeBuilding;
    }
}
