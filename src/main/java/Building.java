import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Building implements Drawable{
    protected String name;
    protected int width, height;
    protected Cell location;
    protected BufferedImage image;
    protected int onScreenWidth;
    protected int onScreenHeight;
    protected int locationOnScreen;

    public Building(Cell location) {
        setLocation(location);
    }

    public void render(Graphics g, Camera camera) {
        int wt = (int)(onScreenWidth * camera.getZoomFactor());
        int ht = (int)(onScreenHeight * camera.getZoomFactor());
        int x = (int)(camera.getX() + location.x[0] * camera.getZoomFactor() - (double) wt / 2);
        int y = (int)(camera.getY() + location.y[2] * camera.getZoomFactor() - ht);
        if(x + wt >= 0 && x <= camera.getWidth() && y + ht >= 0 && y <= camera.getHeight()) {
            g.drawImage(image, x, y, wt, ht, null);
        }
    }

    public String getName() {
        return name;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Cell getLocation() {
        return location;
    }

    public void setLocation(Cell cell) {
        this.location = cell;
        if(location != null) {
            onScreenWidth = width * location.cellwidth;
            double k = (double) image.getWidth() / (double) onScreenWidth;
            onScreenHeight = (int)(image.getHeight() / k);
            image = ImageHandler.resizeImage(image, onScreenWidth, onScreenHeight);
            locationOnScreen = new Cell(location.row, location.col - width).y[1];
        }
    }

    @Override
    public int getLocationOnScreen() {
        return locationOnScreen;
    }

}
