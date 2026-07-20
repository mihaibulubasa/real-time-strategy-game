import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EntityManager implements ArmyPlacedObserver, BuildingObserver, MouseRightClicked{
    private List<Drawable> entities;
    private List<ArmySelectedObserver> armySelectedObservers;

    public EntityManager() {
        entities = new ArrayList<>();
        armySelectedObservers = new ArrayList<>();
    }

    public void tick(Map map) {
        for(Drawable entity : entities) {
            if(entity instanceof Army) {
                ((Army)entity).tick(map, this);
            }
        }
        entities.sort(Comparator.comparingInt(Drawable::getLocationOnScreen));
    }

    public void render(Graphics g, Camera camera) {
        for(Drawable entity : entities) {
            entity.render(g, camera);
        }
    }

    @Override
    public void onArmyPlaced(Army army) {
        entities.add(army);
    }

    public void addArmySelectedObserver(ArmySelectedObserver observer) {
        armySelectedObservers.add(observer);
    }

    public void removeArmySelectedObservers(ArmySelectedObserver observer) {
        armySelectedObservers.remove(observer);
    }

    @Override
    public void rightClick(int x, int y) {
        Army selectedArmy = null;
        for(Drawable entity : entities) {
            if(entity instanceof Army army) {
                if (army.wasClicked(x, y)) {
                    if (selectedArmy == null) {
                        if (!army.isSelected()) {
                            selectedArmy = army;
                        }
                        army.setSelected(!army.isSelected());
                    }
                } else {
                    army.setSelected(false);
                }
            }
        }
        for(ArmySelectedObserver armySelectedObserver : armySelectedObservers) {
            armySelectedObserver.armySelected(selectedArmy);
        }
    }

    public List<Army> getArmies(){
        return entities.stream().filter(Army.class::isInstance).map(Army.class::cast).toList();
    }

    public List<Building> getBuildings(){
        return entities.stream().filter(Building.class::isInstance).map(Building.class::cast).toList();
    }

    @Override
    public void onBuildingPlaced(Building building) {
        entities.add(building);
    }

    @Override
    public void onBuildingDestroyed(Building building) {
        entities.remove(building);
    }
}
