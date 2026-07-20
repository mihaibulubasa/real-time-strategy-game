package network.parsers;

import controller.GameController;
import view.MainPanel;

import javax.swing.*;

public class InitResponse {

    public static void parseResponse(GameController controller, String[] tokens) {
        int width = Integer.parseInt(tokens[1]);
        int height = Integer.parseInt(tokens[2]);
        SwingUtilities.invokeLater(() -> {
            controller.getFrame().setMainPanel(width, height);
            MainPanel panel = controller.getFrame().getMainPanel();
            controller.getMouse().setMainPanel(panel);
            panel.addMouseWheelListener(controller.getMouse());
            panel.addMouseListener(controller.getMouse());
            panel.addMouseMotionListener(controller.getMouse());
            controller.getTimer().start();
            controller.getFrame().getMainPanel().repaint();
        });
    }
}
