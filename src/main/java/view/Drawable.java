package view;

import java.awt.*;

public interface Drawable {
    void render(Graphics g);
    double getDepth();
    void recalculateGeometry(double zoomFactor, int cameraX, int cameraY);
    Rectangle getHitBox();
}
