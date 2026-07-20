package view;

import java.awt.*;

public class Camera {
    private int x, y;
    private double zoomFactor;
    private Rectangle bounds;
    private MainPanel panel;

    public Camera(int x, int y, MainPanel panel){
        this.x = x;
        this.y = y;
        this.panel = panel;
        zoomFactor = 0.5;
        bounds = new Rectangle(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        updateGridGeometry();
    }

    public void setPos(int x, int y) {
        this.x += x;
        this.y += y;
        updateGridGeometry();
    }

    public void setZoom(int x, int y, int amount) {
        double oldZoomFactor = zoomFactor;
        double zoomAmount = 0.2;
        if(amount < 0) {
            zoomFactor += zoomAmount;
        } else if(zoomFactor > 0.5){
            zoomFactor -= zoomAmount;
        }
        if(zoomFactor < 0.5) {
            zoomFactor = 0.5;
        }
        double mapX = (x - this.x) / oldZoomFactor;
        double mapY = (y - this.y) / oldZoomFactor;
        this.x = (int)(x - (mapX * zoomFactor));
        this.y = (int)(y - (mapY * zoomFactor));
        updateGridGeometry();
    }

    public void updateGridGeometry(){
        TileView[][] grid = panel.getGrid();
        for (int i = 0; i < panel.getGridHeight(); i++) {
            for (int j = 0; j < panel.getGridWidth(); j++) {
                grid[i][j].recalculateGeometry(zoomFactor, x, y);
            }
        }
        for(Drawable d : panel.getDrawables()) {
            d.recalculateGeometry(zoomFactor, x, y);
        }
    }

    public void render(Graphics g){
        for(int i = 0; i < panel.getGridHeight(); i++) {
            for(int j = 0; j < panel.getGridWidth(); j++) {
                if(bounds.intersects(panel.getGrid()[i][j].getDrawingBounds())) {
                    panel.getGrid()[i][j].render(g);
                }
            }
        }
        for(Drawable d : panel.getDrawables()) {
            if(bounds.intersects(d.getHitBox())) {
                d.render(g);
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY(){
        return y;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }
}
