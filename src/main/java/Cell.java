import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cell implements MouseMovedObserver, MouseClickedObserver {
    protected int row;
    protected int col;
    protected boolean empty;
    protected int cellwidth;
    protected int cellheight;
    protected boolean hover;
    private List<ObjectClickedObserver> clickedObservers;
    private BufferedImage image;
    private Building building;
    protected int[] x, y;

    public Cell(int row, int col) {
        this.row = row;
        clickedObservers = new ArrayList<>();
        this.col = col;
        cellwidth = 64;
        cellheight = cellwidth / 2;
        empty = true;
        x = new int[4];
        x[0] = (col - row) * (cellwidth / 2);               //top
        x[1] = (col - row + 1) * (cellwidth / 2);              //left
        x[2] = (col - row) * (cellwidth / 2);                  //down
        x[3] = (col - row - 1) * (cellwidth / 2);             //right
        y = new int[4];
        y[0] = (row + col) * (cellheight / 2);
        y[1] = (row + col + 1) * (cellheight / 2);
        y[2] = (row + col + 2) * (cellheight / 2);
        y[3] = (row + col + 1) * (cellheight / 2);
        try {
            image = ImageHandler.resizeImage(ImageIO.read(new File("src/resources/grass.png")), cellwidth, cellheight);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(Graphics g, Camera camera) {
        int xt = (int)((x[0] * camera.getZoomFactor()) + camera.getX());
        int yt = (int)((y[0] * camera.getZoomFactor()) + camera.getY());
        int cwt = (int)(cellwidth * camera.getZoomFactor());
        int cht = (int)(cellheight * camera.getZoomFactor());
        int offset = xt - (cwt / 2);
        if(empty && offset + cwt >= 0 && offset <= camera.getWidth() && yt + cht >= 0 && yt <= camera.getHeight()) {
            g.drawImage(image, offset, yt, cwt, cht, null);
        }
    }

    public void drawHover(Graphics g, Camera camera) {
        if(hover) {
            int[] xt = new int[4], yt = new int[4];
            for(int i = 0; i < 4; i++) {
                xt[i] = (int)((x[i] * camera.getZoomFactor()) + camera.getX());
                yt[i] = (int)((y[i] * camera.getZoomFactor()) + camera.getY());
            }
            if(empty) {
                g.setColor(new Color(20, 69, 33, 130));
            } else {
                g.setColor(new Color(243, 115, 98, 130));
            }
            g.fillPolygon(xt, yt, 4);

        }
    }

    public void addObserver(ObjectClickedObserver observer) {
        if(!clickedObservers.contains(observer)) {
            clickedObservers.add(observer);
        }
    }

    public void removeObserver(ObjectClickedObserver observer) {
        clickedObservers.remove(observer);
    }

    protected boolean isHovered(int x, int y) {
        Polygon p = new Polygon(this.x, this.y, 4);
        return p.contains(x, y);
    }

    @Override
    public void mouseMoved(int x, int y) {
        hover = isHovered(x, y);
    }

    @Override
    public void objectClicked(int x, int y) {
        if(isHovered(x, y)) {
            List<ObjectClickedObserver> copy = new ArrayList<>(clickedObservers);
            for(ObjectClickedObserver observer : copy) {
                observer.click(col, row);
            }
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public Building getBuilding() {
        return building;
    }

}