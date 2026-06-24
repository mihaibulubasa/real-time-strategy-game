import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MapMenu extends JPanel implements ActionListener, ObjectClickedObserver {
    private JComboBox comboBox;
    private JComboBox changeGameMode;
    private Player player;
    public MapMenu(Player player) {
        setLayout(new FlowLayout());
        setBackground(Color.darkGray);
        changeGameMode = new JComboBox(GameMode.values());
        changeGameMode.setSelectedItem(player.getGameMode());
        this.player = player;
        for(int i = 0; i < player.getMap().getLength(); i++) {
            for(int j = 0; j < player.getMap().getLength(); j++) {
                player.getMap().getCell(i, j).addObserver(this);
            }
        }
        if(player.getGameMode() == GameMode.ATTACK_MODE) {
            comboBox = new JComboBox<>(getArmyArray(player));
            player.setPlacedArmy((Army) comboBox.getSelectedItem());
        } else if(player.getGameMode() == GameMode.EDIT_MODE) {
            comboBox = new JComboBox(getBuildingArray(player));
            player.setCurrentBuilding((Building)comboBox.getSelectedItem());
        }
        comboBox.addActionListener(this);
        add(comboBox);
        changeGameMode.addActionListener(this);
        add(changeGameMode);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == changeGameMode) {
            GameMode gameMode = (GameMode) changeGameMode.getSelectedItem();
            player.setGameMode(gameMode);
            setComboBox();
        }
        if(e.getSource() == comboBox) {
            if(player.getGameMode() == GameMode.EDIT_MODE) {
                Building button = (Building) comboBox.getSelectedItem();
                player.setCurrentBuilding(button);
            } else if(player.getGameMode() == GameMode.ATTACK_MODE){
                Army army = (Army) comboBox.getSelectedItem();
                player.setPlacedArmy(army);
            }
        }
    }

    private void setComboBox() {
        comboBox.removeActionListener(this);
        if(player.getGameMode() == GameMode.ATTACK_MODE) {
            Object[] armies = getArmyArray(player);
            comboBox.setModel(new DefaultComboBoxModel<>(armies));
            player.setPlacedArmy((Army) comboBox.getSelectedItem());
        } else if(player.getGameMode() == GameMode.EDIT_MODE) {
            Object[] buildings = getBuildingArray(player);
            comboBox.setModel(new DefaultComboBoxModel<>(buildings));
            player.setCurrentBuilding((Building)comboBox.getSelectedItem());
        }
        comboBox.addActionListener(this);
    }

    private Army[] getArmyArray(Player player) {
        Army[] armyArray = player.getArmies().toArray(new Army[0]);
        Army[] newArmyArray = new Army[armyArray.length + 1];
        System.arraycopy(armyArray, 0, newArmyArray, 1, armyArray.length);
        return newArmyArray;
    }

    private Building[] getBuildingArray(Player player) {
        Building[] buildingArray = player.getBuildings().toArray(new Building[0]);
        Building[] newBuildingArray = new Building[buildingArray.length + 1];
        System.arraycopy(buildingArray, 0, newBuildingArray, 1, buildingArray.length);
        return newBuildingArray;
    }

    @Override
    public void click(int x, int y) {
        setComboBox();
    }
}
