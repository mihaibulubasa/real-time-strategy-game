import controller.GameController;

import javax.swing.*;

public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
                GameController gc = new GameController("localhost", 8080);
                gc.start();
        });
    }
}
