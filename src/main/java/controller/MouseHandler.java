package controller;

import view.MainPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseHandler extends MouseAdapter {

    private GameController gameController;
    private MainPanel mainPanel;
    private int button;
    private Point lastDraggedPoint;
    private Point mousePosition;
    private Point startClick;
    private boolean leftPressed;

    public MouseHandler(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftPressed = true;
        }
        startClick = e.getPoint();
        button = e.getButton();
        lastDraggedPoint = e.getPoint();
        if(gameController.getKeyHandler().isSelect()) {
            gameController.getFrame().getMainPanel().initSelect(e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        if(e.getButton() == MouseEvent.BUTTON1) {
            leftPressed = false;
        }
        if(startClick.distance(e.getPoint()) < 5) {
            onClick(e);
        } else {
            if(gameController.getKeyHandler().isSelect()) {
                gameController.multiSelectArmy();
            }
        }
        gameController.getKeyHandler().setSelect(false);
        gameController.getFrame().getMainPanel().resetSelect();
    }

    private void onClick(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(gameController.getKeyHandler().isSelect()) {
                gameController.selectArmy(e.getX(), e.getY());
            } else if(gameController.getKeyHandler().isPlaceArmy()){
                gameController.placeArmy(e.getX(), e.getY());
            } else if(gameController.getKeyHandler().isPlaceBuilding()) {
                gameController.placeBuilding(e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
        if(gameController.getKeyHandler().isCommand()) {
            gameController.hoverMultipleTile(e.getX(), e.getY());
        } else {
            gameController.hoverTile(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePosition = e.getPoint();
        super.mouseDragged(e);
        if(button == MouseEvent.BUTTON3) {
            mainPanel.getCamera().setPos(e.getX() - lastDraggedPoint.x, e.getY() - lastDraggedPoint.y);
        }
        if(button == MouseEvent.BUTTON1 && gameController.getKeyHandler().isSelect()) {
            mainPanel.updateSelect(e.getX(), e.getY());
        }
        lastDraggedPoint = e.getPoint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e);
        mainPanel.getCamera().setZoom(e.getX(), e.getY(), e.getWheelRotation());
    }

    public void setMainPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }
}
