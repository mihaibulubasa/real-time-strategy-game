package view;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BuildingView implements Drawable{
    private int x, y;
    private TileView location;
    private int width, height;
    private int baseHeight, baseY;
    private int gridWidth, gridHeight;
    public int currentWidth, currentHeight;
    public int currentBaseHeight;
    private BufferedImage image;
    private Rectangle hitBox;

    public BuildingView(String imageName, TileView location, int gridWidth, int gridHeight) {
        this.location = location;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        width = (gridWidth + gridHeight) * (location.getWidth() / 2);
        baseHeight = (gridWidth + gridHeight) * (location.getHeight() / 2);
        image = TextureLoader.getInstance().getTexture(TextureType.valueOf(imageName));
        double ratio = (double) width / image.getWidth();
        height = (int)(image.getHeight() * ratio);
        x = location.getX() - (gridWidth * (location.getWidth() / 2));
        y = location.getY() + location.getHeight() - height;
        y = location.getY() + location.getHeight() - baseHeight;
        hitBox = new Rectangle(x, y, width, height);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, currentWidth, currentHeight, null);
//        g.setColor(Color.red);
//        g.drawRect(x, baseY, currentWidth, currentBaseHeight);
    }

    @Override
    public double getDepth() {
        double leftDist = location.getCol() - (gridWidth - 1) + location.getRow();
        double rightDist = location.getCol() + location.getRow() - (gridHeight - 1);
        return (leftDist + rightDist) / 2;
    }

    @Override
    public void recalculateGeometry(double zoomFactor, int cameraX, int cameraY) {
        currentWidth = (int)(width * zoomFactor);
        currentHeight = (int)(height * zoomFactor);
        currentBaseHeight = (int)(baseHeight * zoomFactor);
        x = location.getX() - (gridWidth * (location.getCurrentWidth() / 2));
        y = location.getY() + location.getCurrentHeight() - currentHeight;
        baseY = location.getY() + location.getCurrentHeight() - currentBaseHeight;
        hitBox = new Rectangle(x, y, currentWidth, currentHeight);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }
}
