package view;

import java.awt.*;

public class ArmyView implements Drawable{
    private Animation animation;
    private int x, y;
    private double speed;
    private TileView location;
    private int width, height;
    private int currentWidth, currentHeight;
    private Rectangle hitBox;
    private boolean selected;

    public ArmyView(TileView location){
        this.location = location;
        x = location.getX() - width / 2;
        y = location.getY() - height / 2;
        speed = 2;
        width = 256;
        height = 256;
        hitBox = new Rectangle(x, y, width, height);
        animation = new Animation(TextureLoader.getInstance().getAnimationFrames(TextureType.ARMY_WALK), 16);
        animation.startAnimation();
    }

    @Override
    public void recalculateGeometry(double zoomFactor, int cameraX, int cameraY) {
        this.currentWidth = (int) (width * zoomFactor);
        this.currentHeight = (int) (height * zoomFactor);
        this.x = location.getX() - currentWidth / 2;
        this.y = location.getY() - currentHeight / 2;
        this.hitBox = new Rectangle(x, y, currentWidth, currentHeight);
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void render(Graphics g) {
        animation.animate();
        g.drawImage(animation.getCurrentFrame(), x, y, currentWidth, currentHeight, null);
    }

    @Override
    public int getDepth() {
        return 0;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if(selected) {
            animation.setFrames(TextureLoader.getInstance().getAnimationFrames(TextureType.ARMY_SELECT_WALK));
        } else {
            animation.setFrames(TextureLoader.getInstance().getAnimationFrames(TextureType.ARMY_WALK));
        }
    }
}
