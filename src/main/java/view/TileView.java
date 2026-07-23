package view;

import java.awt.*;

public class TileView {
    private int x, y;
    private int width, height;
    private int col, row;
    private Polygon hitBox;
    private int currentWidth, currentHeight;
    private Image image;
    private boolean hover;
    private Rectangle drawingBounds;
    private BuildingView buildingView;
    private static Color hoverColor = new Color(20, 69, 33, 130);

    public TileView(int row, int col){
        this.col = col;
        this.row = row;
        width = 256;
        height = 128;
        image = TextureLoader.getInstance().getTexture(TextureType.TILE_IMAGE);
        recalculateGeometry(1.0, 0, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCurrentWidth() {
        return currentWidth;
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public Polygon getHitBox(){
        return hitBox;
    }

    public boolean isClicked(int x, int y){
        return hitBox.contains(x, y);
    }

    public void render(Graphics g){
        if(image != null){
            g.drawImage(image, x - currentWidth / 2, y, currentWidth, currentHeight, null);
        }
        if(hover) {
            g.setColor(hoverColor);
            g.fillPolygon(hitBox);
        }
    }

    public void recalculateGeometry(double zoomFactor, int cameraX, int cameraY) {
        this.currentWidth = (int) (width * zoomFactor);
        this.currentHeight = (int) (height * zoomFactor);
        int[] xP = new int[4];
        int[] yP = new int[4];
        xP[0] = (int) (((col - row) * (width / 2.0)) * zoomFactor) + cameraX;
        xP[1] = (int) (((col - row + 1) * (width / 2.0)) * zoomFactor) + cameraX;
        xP[2] = (int) (((col - row) * (width / 2.0)) * zoomFactor) + cameraX;
        xP[3] = (int) (((col - row - 1) * (width / 2.0)) * zoomFactor) + cameraX;
        yP[0] = (int) (((row + col) * (height / 2.0)) * zoomFactor) + cameraY;
        yP[1] = (int) (((row + col + 1) * (height / 2.0)) * zoomFactor) + cameraY;
        yP[2] = (int) (((row + col + 2) * (height / 2.0)) * zoomFactor) + cameraY;
        yP[3] = (int) (((row + col + 1) * (height / 2.0)) * zoomFactor) + cameraY;
        this.hitBox = new Polygon(xP, yP, 4);
        this.x = xP[0];
        this.y = yP[0];
        this.drawingBounds = new Rectangle(x - currentWidth / 2, y, currentWidth, currentHeight);
    }

    public static void changeHover(boolean command){
        if(command) {
            hoverColor =  new Color(28, 77, 108, 130);
        } else {
            hoverColor = new Color(20, 69, 33, 130);
        }
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public void setBuildingView(BuildingView buildingView) {
        this.buildingView = buildingView;
    }

    public BuildingView getBuildingView() {
        return buildingView;
    }

    public Rectangle getDrawingBounds(){
        return drawingBounds;
    }
}