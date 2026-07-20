import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Army implements Drawable{
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
    private Army followingArmy = null;
    private boolean following;
    private int attackRange;
    private int followRange;
    private boolean auto;
    private Cell nextLocation;

    public Army(int amount, String type, Cell location) {
        this.amount = amount;
        path = null;
        attackRange = 3;
        followRange = 12;
        this.type = type;
        if(location != null) {
            this.location = location;
            x = location.x[0];
            y = location.y[1];
        }
        speed = 1.1;
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
        nextLocation = null;
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

    private void findEnemies(EntityManager entityManager) {
        if(following) {
            return;
        }
        for(Army army : entityManager.getArmies()) {
            if(army == this) {
                continue;
            }
            if(army.getLocation() == null) {
                continue;
            }
            double distance = Astar.getDistance(location, army.getLocation());
            if(distance < followRange) {
                followingArmy = army;
                following = true;
                break;
            }
        }
    }

    public void tick(Map map, EntityManager entityManager) {
        animation.animate();
        if(auto) {
            findEnemies(entityManager);
        }
        if(following) {
            double distance = Astar.getDistance(location, followingArmy.getLocation());
            if(distance < followRange && distance > attackRange) {
                followArmy(map);
            } else {
                stopFollowing();
            }
        }
        if (moving) {
            x += directionX;
            y += directionY;
            if(!location.getSpace().contains(x, y)) {
                location = nextLocation;
            }
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

    private void followArmy(Map map){
        if(path == null || path.isEmpty() || !path.get(path.size() - 1).equals(followingArmy.getLocation())) {
            Astar astar = new Astar();
            ArrayList<Cell> newPath = astar.findPath(this, map, followingArmy.getLocation());
            followPath(newPath);
        }
    }

    public void stopFollowing() {
        if(following) {
            following = false;
            followingArmy = null;
        }
        if(path != null) {
            path.clear();
        }
    }

    public Cell getLocation() {
        return location;
    }

    @Override
    public int getLocationOnScreen() {
        return (int)y;
    }

    public void moveTo(Cell destination) {
        nextLocation = destination;
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
        if(!moving) {
            moveTo(this.path.remove(0));
        }
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


    public boolean wasClicked(int x, int y){
        Rectangle rect = new Rectangle((int)this.x - (width / 2), (int)this.y - (height / 2), width, height);
        return rect.contains(x, y);
    }

    public void setAuto(boolean auto){
        this.auto = auto;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
