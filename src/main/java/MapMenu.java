import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            comboBox = new JComboBox<>(player.getArmies().toArray());
            player.setPlacedArmy((Army) comboBox.getSelectedItem());
            comboBox.addActionListener(this);
            add(comboBox);
        } else if(player.getGameMode() == GameMode.EDIT_MODE) {
            comboBox = new JComboBox(player.getBuildings().toArray());
            player.setCurrentBuilding((Building)comboBox.getSelectedItem());
            comboBox.addActionListener(this);
            add(comboBox);
        }
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
        if(comboBox != null) {
            remove(comboBox);
        }
        if(player.getGameMode() == GameMode.ATTACK_MODE) {
            comboBox = new JComboBox<>(player.getArmies().toArray());
            player.setPlacedArmy((Army) comboBox.getSelectedItem());
            comboBox.addActionListener(this);
            add(comboBox);
        } else if(player.getGameMode() == GameMode.EDIT_MODE) {
            comboBox = new JComboBox(player.getBuildings().toArray());
            player.setCurrentBuilding((Building)comboBox.getSelectedItem());
            comboBox.addActionListener(this);
            add(comboBox);
        }
        revalidate();
        repaint();
    }

    @Override
    public void click(int x, int y) {
        setComboBox();
    }
}
