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
    private final Map<TextureType, Animation> animations;

    private TextureLoader() {
        images = new HashMap<>();
        animations = new HashMap<>();
        BufferedImage TILE_IMAGE = loadTexture("src/resources/grass.png", 256, 128);
        images.put(TextureType.TILE_IMAGE, TILE_IMAGE);
        Animation ARMY_WALK = new Animation(loadTexture("src/resources/army.png", 4096, 256), 16, 256, 256);
        animations.put(TextureType.ARMY_WALK, ARMY_WALK);
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

    public Animation getAnimation(TextureType name) {
        return animations.get(name);
    }
}
