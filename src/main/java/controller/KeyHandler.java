package controller;

import view.TileView;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private GameController gameController;
    private boolean select;
    private boolean command;

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
            TileView.changeHover(true);
        }
    }

    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        if(e.getKeyCode() == KeyEvent.VK_A) {
            command = false;
            TileView.changeHover(false);
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
}
