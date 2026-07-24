package view;

import java.awt.*;

public interface Drawable {
    class Line {
        Point left, right;
        public Line(Point left, Point right) {
            this.left = left;
            this.right = right;
        }
    }

    void render(Graphics g);
    Line getDepth();
    void recalculateGeometry(double zoomFactor, int cameraX, int cameraY);
    Rectangle getHitBox();
    private static double getSide(Line line, Point point) {
        return (double)(line.right.x - line.left.x) * (point.y - line.left.y) - (double)(line.right.y - line.left.y) * (point.x - line.left.x);
    }
    static int compare(Drawable d1, Drawable d2) {
        if (d1 == d2) {
            return 0;
        }
        Line l1 = d1.getDepth();
        Line l2 = d2.getDepth();
        double side1_left = getSide(l2, l1.left);
        double side1_right = getSide(l2, l1.right);
        if (side1_left < 0 && side1_right < 0) {
            return -1;
        }
        if (side1_left > 0 && side1_right > 0) {
            return 1;
        }
        double side2_left = getSide(l1, l2.left);
        double side2_right = getSide(l1, l2.right);
        if (side2_left > 0 && side2_right > 0) {
            return -1;
        }
        if (side2_left < 0 && side2_right < 0) {
            return 1;
        }
        int maxY1 = Math.max(l1.left.y, l1.right.y);
        int maxY2 = Math.max(l2.left.y, l2.right.y);
        return Integer.compare(maxY1, maxY2);
    }
}
