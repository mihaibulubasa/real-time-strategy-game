import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Castle extends Building{
    public Castle(Cell location) {
        super(location);
        width = 5;
        height = 5;
        try {
            image = ImageIO.read(new File("src/resources/castle.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Castle";
    }
}
