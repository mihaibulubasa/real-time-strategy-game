import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Army implements Drawable, MouseRightClicked{
    private int amount;
    private String type;
    private Cell location;
    private double x, y;
    private boolean moving;
    private double speed;
    private double directionX, directionY;
    private int targetX, targetY;
    private int width, height;
    private ArrayList<Cell> path;
    private Animation animation;
    private boolean selected;
    private List<ArmySelectedObserver> observers;

    public Army(int amount, String type, Cell location) {
        this.amount = amount;
        observers = new ArrayList<>();
        path = null;
        this.type = type;
        if(location != null) {
            this.location = location;
            x = location.x[0];
            y = location.y[1];
        }
        speed = 2.0;
        moving = false;
        directionX = 0;
        directionY = 0;
        width = 64;
        height = 64;
        try {
            animation = new Animation(ImageIO.read(new File("src/resources/army.png")), 16, 256, 256, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void render(Graphics g, Camera camera) {
        int wt = (int) (width * camera.getZoomFactor());
        int ht = (int) (height * camera.getZoomFactor());
        int xt = (int) (x * camera.getZoomFactor()) - (wt / 2) + camera.getX();
        int yt = (int) (y * camera.getZoomFactor()) - (wt / 2) + camera.getY();
        if(xt + (wt) >= 0 && xt - (wt / 2) <= camera.getWidth() && yt + ht >= 0 && yt <= camera.getHeight()) {
            g.drawImage(animation.getCurrentFrame(), xt, yt, wt, ht,null);
        }
        if(selected) {
            g.setColor(Color.red);
            g.drawRect(xt, yt, wt, ht);
        }
    }

    public void addObserver(ArmySelectedObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ArmySelectedObserver observer) {
        observers.remove(observer);
    }

    public void tick() {
        animation.animate();
        if (moving) {
            x += directionX;
            y += directionY;
            if (Math.abs(x - targetX) <= speed && Math.abs(y - targetY) <= speed) {
                x = targetX;
                y = targetY;
                moving = false;
                if (path != null && !path.isEmpty()) {
                    moveTo(path.remove(0));
                } else {
                    animation.stopAnimate();
                }
            }
        }
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public Cell getLocation() {
        return location;
    }

    @Override
    public int getLocationOnScreen() {
        return (int)y;
    }

    public void moveTo(Cell destination) {
        location = destination;
        targetX = destination.x[0];
        targetY = destination.y[1];
        double dx = targetX - x;
        double dy = targetY - y;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length != 0) {
            directionX = speed * dx / length;
            directionY = speed * dy / length;
        } else {
            directionX = 0;
            directionY = 0;
        }
        moving = true;
    }

    public void followPath(ArrayList<Cell> path) {
        if (path == null || path.isEmpty()) {
            animation.stopAnimate();
            return;
        }
        this.path = new ArrayList<>(path);
        animation.startAnimate();
        moveTo(this.path.remove(0));
    }

    public void setLocation(Cell location) {
        this.location = location;
        x = location.x[0];
        y = location.y[1];
    }

    @Override
    public String toString() {
        return type + " " + amount;
    }

    @Override
    public void rightClick(int x, int y) {
        Rectangle rect = new Rectangle((int)this.x - (width / 2), (int)this.y - (height / 2), width, height);
        Army army;
        if (rect.contains(x, y)) {
            if(!selected) {
                army = this;
                for(ArmySelectedObserver observer : observers) {
                    observer.armySelected(army);
                }
            }
            selected = !selected;
        } else {
            selected = false;
        }
    }
}
