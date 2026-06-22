import java.awt.*;
import java.util.ArrayList;

public class Camera implements MouseDraggedObserver, MouseScrolled{
    private int x, y;
    private int width, height;
    private double zoomFactor;
    private ArrayList<CameraMovedObserver> observers;
    public Camera(int x, int y) {
        this.x = x;
        this.y = y;
        zoomFactor = 1.0;
        width = Toolkit.getDefaultToolkit().getScreenSize().width;
        height = Toolkit.getDefaultToolkit().getScreenSize().height;
        observers = new ArrayList<>();
    }
    public void addObserver(CameraMovedObserver observer) {
        observers.add(observer);
    }
    public void removeObserver(CameraMovedObserver observer) {
        observers.remove(observer);
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y ;
    }
    public void setY(int y) {
        this.y = y;
    }

    public double getZoomFactor() {
        return zoomFactor;
    }

    @Override
    public void onMouseDragged(int x, int y) {
        this.x += x;
        this.y += y;
        for(CameraMovedObserver observer : observers) {
            observer.notify(this);
        }
    }

    @Override
    public void onMouseScrolled(int x, int y, int amount) {
        double oldZoomFactor = zoomFactor;
        if(amount < 0) {
            zoomFactor += 0.1;
        } else if(zoomFactor > 1){
            zoomFactor -= 0.1;
        }
        double mapX = (x - this.x) / oldZoomFactor;
        double mapY = (y - this.y) / oldZoomFactor;
        this.x = (int)(x - (mapX * zoomFactor));
        this.y = (int)(y - (mapY * zoomFactor));
    }
}
