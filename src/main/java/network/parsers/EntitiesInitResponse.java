package network.parsers;

import controller.GameController;

import javax.swing.*;

public class EntitiesInitResponse {
    public static void parseResponse(GameController controller, String response) {
        String[] tokens = response.split(",");
        SwingUtilities.invokeLater(() -> {
            for(int i = 0; i < tokens.length; i+=3) {
                long id = Long.parseLong(tokens[i]);
                int row = Integer.parseInt(tokens[i+1]);
                int col  = Integer.parseInt(tokens[i+2]);
                controller.getFrame().getMainPanel().addArmy(id, row, col);
            }
            controller.getFrame().getMainPanel().repaint();
        });
    }
}
