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

    public MouseHandler(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        button = e.getButton();
        lastDraggedPoint = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gameController.hoverTile(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameController.clickTile(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
        if(button == MouseEvent.BUTTON3) {
            mainPanel.getCamera().setPos(e.getX() - lastDraggedPoint.x, e.getY() - lastDraggedPoint.y);
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

}
