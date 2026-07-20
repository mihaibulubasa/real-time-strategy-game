package network.parsers;

import controller.GameController;

import javax.swing.*;

public class EntitiesInitResponse {
    public static void parseResponse(GameController controller, String response) {
        String[] tokens = response.split(",");
        SwingUtilities.invokeLater(() -> {
            for(int i = 0; i < tokens.length; i+=2) {
                int row = Integer.parseInt(tokens[i]);
                int col  = Integer.parseInt(tokens[i+1]);
                controller.getFrame().getMainPanel().addArmy(row, col);
            }
            controller.getFrame().getMainPanel().repaint();
        });
    }
}
