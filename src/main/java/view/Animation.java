package view;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private int numberOfFrames;
    private boolean animating;
    private long speed, timer, lastTime;

    public Animation(BufferedImage image, int numberOfFrames, int width, int height) {
        this.numberOfFrames = numberOfFrames;
        frames = new BufferedImage[numberOfFrames];
        for(int i = 0; i < numberOfFrames; i++) {
            frames[i] = image.getSubimage(i * width, 0, width, height);
        }
        currentFrame = 0;
        animating = false;
        speed = 100;
    }

    public void animate(){
        if(animating) {
            long currentTime = System.currentTimeMillis();
            timer +=  currentTime- lastTime;
            lastTime = currentTime;
            if(timer >= speed) {
                while(timer >= speed) {
                    currentFrame++;
                    timer -= speed;
                }
                if(currentFrame >= numberOfFrames) {
                    currentFrame = currentFrame % numberOfFrames;
                }
            }
        }
    }

    public void startAnimation() {
        animating = true;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }

    public BufferedImage getCurrentFrame(){
        return frames[currentFrame];
    }

    public void stopAnimation() {
        animating = false;
        currentFrame = 0;
    }
}
