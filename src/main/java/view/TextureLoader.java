package view;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureLoader {

    private static TextureLoader instance;

    private final Map<TextureType, BufferedImage> images;
    private final Map<TextureType, BufferedImage[]> animationFrames;

    private TextureLoader() {
        images = new HashMap<>();
        animationFrames = new HashMap<>();
        BufferedImage TILE_IMAGE = loadTexture("src/resources/grass.png", 256, 128);
        images.put(TextureType.TILE_IMAGE, TILE_IMAGE);
        BufferedImage[] ARMY_WALK = splitTexture("src/resources/army.png", 4096, 256, 16);
        BufferedImage[] ARMY_SELECT_WALK = splitTexture("src/resources/army_select.png", 4096, 256, 16);
        animationFrames.put(TextureType.ARMY_WALK, ARMY_WALK);
        animationFrames.put(TextureType.ARMY_SELECT_WALK, ARMY_SELECT_WALK);
    }

    public static TextureLoader getInstance() {
        if (instance == null) {
            instance = new TextureLoader();
        }
        return instance;
    }

    public BufferedImage loadTexture(String name, int width, int height) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(name));
        } catch (IOException e) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        return resizeImage(image, width, height);
    }

    public BufferedImage[] splitTexture(String name, int width, int height, int frames) {
        BufferedImage rawImage = loadTexture(name, width, height);
        BufferedImage[] list = new BufferedImage[frames];
        int newWidth = width / frames;
        for(int i = 0; i < frames; i++) {
            list[i] = rawImage.getSubimage(i * newWidth, 0, newWidth, height);
        }
        return list;
    }

    public BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    public BufferedImage getTexture(TextureType name) {
        return images.get(name);
    }

    public BufferedImage[] getAnimationFrames(TextureType name) {
        return animationFrames.get(name);
    }
}
