package network.parsers;

import controller.GameController;

import javax.swing.*;

public class BuildingsInitResponse {
    public static void parseResponse(GameController controller, String response) {
        String[] tokens = response.split(",");
        SwingUtilities.invokeLater(()->{
            for (int i = 0; i < tokens.length; i+=6) {
                long id = Long.parseLong(tokens[i]);
                int row = Integer.parseInt(tokens[i+1]);
                int col = Integer.parseInt(tokens[i+2]);
                int width = Integer.parseInt(tokens[i+3]);
                int height = Integer.parseInt(tokens[i+4]);
                String texture = tokens[i+5];
                controller.getFrame().getMainPanel().addBuilding(id, texture, row, col, width, height);
            }
        });
    }
}
