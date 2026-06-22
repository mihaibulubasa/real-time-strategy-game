import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private int frameCount;
    private long speed, timer, lastTime;
    private boolean animating;

    public Animation(BufferedImage image, int frameCount, int width, int height, int targetWidth, int targetHeight) {
        speed = 100;
        timer = 0;
        lastTime = System.currentTimeMillis();
        this.frameCount = frameCount;
        this.frames = new BufferedImage[frameCount];
        for(int i = 0; i < frameCount; i++) {
            frames[i] = ImageHandler.resizeImage(image.getSubimage((width * i), 0, width, height), targetWidth, targetHeight);
        }
        currentFrame = 0;
    }

    public void animate() {
        if(animating) {
            timer += System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
            if(timer > speed) {
                currentFrame++;
                if (currentFrame >= frameCount) {
                    currentFrame = 0;
                }
            }
        }
    }

    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }

    public void startAnimate() {
        animating = true;
    }

    public void stopAnimate() {
        currentFrame = 0;
        animating = false;
    }

    public void resize(int width, int height) {
        for(int i = 0; i < frameCount; i++) {
            frames[i] = ImageHandler.resizeImage(frames[currentFrame], width, height);
        }
    }
}
