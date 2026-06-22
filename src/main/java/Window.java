import javax.swing.*;

public class Window extends JFrame {
    public Window(Game game) {
        setTitle("Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        add(game);
        setVisible(true);
        game.setMenu(getHeight(), getWidth());
    }
}
