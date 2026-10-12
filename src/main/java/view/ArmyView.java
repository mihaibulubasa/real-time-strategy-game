package view;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class ArmyView implements Drawable {
    private Animation animation;
    private double worldX, worldY;
    private double speed = 6.0;
    private double directionX, directionY;
    private double screenX, screenY;
    private TileView location;
    private int width = 256;
    private int height = 256;
    private int currentWidth, currentHeight;
    private long id;
    private Rectangle hitBox;
    private boolean selected;
    private Queue<TileView> path;
    private TileView nextDestination;
    private double currentZoom = 1.0;
    private int currentCameraX = 0;
    private int currentCameraY = 0;

    public ArmyView(long id, TileView location) {
        this.location = location;
        this.id = id;
        this.worldX = location.getCol() * 128 - location.getRow() * 128;
        this.worldX = (location.getX() - currentCameraX) / currentZoom;
        this.worldY = (location.getY() - currentCameraY) / currentZoom;
        this.worldX = (location.getCol() - location.getRow()) * (location.getWidth() / 2.0);
        this.worldY = (location.getCol() + location.getRow() + 1) * (location.getHeight() / 2.0);
        this.width = 256;
        this.height = 256;
        hitBox = new Rectangle(0, 0, width, height);
        animation = new Animation(TextureLoader.getInstance().getAnimationFrames(TextureType.ARMY_WALK), 16, 25);
        path = new LinkedList<>();
        recalculateGeometry(1.0, 0, 0);
    }

    @Override
    public void recalculateGeometry(double zoomFactor, int cameraX, int cameraY) {
        this.currentZoom = zoomFactor;
        this.currentCameraX = cameraX;
        this.currentCameraY = cameraY;
        this.currentWidth = (int) (width * zoomFactor);
        this.currentHeight = (int) (height * zoomFactor);
        updateScreenPosition();
    }

    private void updateScreenPosition() {
        this.screenX = (worldX * currentZoom + currentCameraX) - currentWidth / 2.0;
        this.screenY = (worldY * currentZoom + currentCameraY) - currentHeight + (location.getHeight() * currentZoom / 2.0);
        this.hitBox = new Rectangle((int) screenX, (int) screenY, currentWidth, currentHeight);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void render(Graphics g) {
        animation.animate();
        if (nextDestination != null) {
            double targetWorldX = (nextDestination.getCol() - nextDestination.getRow()) * (nextDestination.getWidth() / 2.0);
            double targetWorldY = (nextDestination.getCol() + nextDestination.getRow() + 1) * (nextDestination.getHeight() / 2.0);
            double dx = targetWorldX - worldX;
            double dy = targetWorldY - worldY;
            double distance = Math.hypot(dx, dy);
            if (distance <= speed) {
                worldX = targetWorldX;
                worldY = targetWorldY;
                location = nextDestination;
                nextDestination = path.poll();
                if (nextDestination != null) {
                    moveTo(nextDestination);
                } else {
                    animation.stopAnimation();
                }
            } else {
                worldX += directionX;
                worldY += directionY;
            }
            updateScreenPosition();
        } else if (!path.isEmpty()) {
            nextDestination = path.poll();
            if (nextDestination != null) {
                moveTo(nextDestination);
            } else {
                animation.stopAnimation();
            }
        }
        g.drawImage(animation.getCurrentFrame(), (int) screenX, (int) screenY, currentWidth, currentHeight, null);
    }

    public void moveTo(TileView next) {
        double targetWorldX = (next.getCol() - next.getRow()) * (next.getWidth() / 2.0);
        double targetWorldY = (next.getCol() + next.getRow() + 1) * (next.getHeight() / 2.0);
        double dx = targetWorldX - worldX;
        double dy = targetWorldY - worldY;
        double distance = Math.hypot(dx, dy);
        if (distance > 0) {
            directionX = (dx / distance) * speed;
            directionY = (dy / distance) * speed;
            animation.startAnimation();
        } else {
            directionX = 0;
            directionY = 0;
            animation.stopAnimation();
        }
    }

    @Override
    public Line getDepth() {
        // Feet center position on screen
        Point feet = new Point((int) (screenX + currentWidth / 2.0), (int) (screenY + currentHeight));
        return new Line(feet, feet);
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            animation.setFrames(TextureLoader.getInstance().getAnimationFrames(TextureType.ARMY_SELECT_WALK));
        } else {
            animation.setFrames(TextureLoader.getInstance().getAnimationFrames(TextureType.ARMY_WALK));
        }
    }

    public void setPath(LinkedList<TileView> path) {
        this.path = path;
    }

    public TileView getLocation() {
        return location;
    }

    public long getId() {
        return id;
    }
}