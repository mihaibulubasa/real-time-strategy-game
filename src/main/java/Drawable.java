import java.awt.*;

public interface Drawable {
    void render(Graphics g, Camera camera);
    Cell getLocation();
    int getLocationOnScreen();
}
