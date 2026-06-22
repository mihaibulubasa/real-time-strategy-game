import java.util.ArrayList;
import java.util.List;

public class Player implements ObjectClickedObserver, ArmySelectedObserver {
    private Map map;
    private List<Building> buildings;
    private List<Army> armies;
    private Army currentArmy;
    private Building currentBuilding;
    private Army placedArmy;
    private GameMode gameMode;

    public Player(Map map) {
        gameMode = GameMode.ATTACK_MODE;
        setMap(map);
        buildings = new ArrayList<>();
        armies = new ArrayList<>();
        buildings.add(new Castle(null));
        buildings.add(new Castle(null));
        buildings.add(new Castle(null));
        armies.add(new Army(100, "Front Line soldiers", null));
        armies.add(new Army(70, "Back Line archers", null));
    }

    public void setMap(Map map) {
        this.map = map;
        for(int i = 0; i < map.getLength(); i++) {
            for(int j = 0; j < map.getLength(); j++) {
                map.getCell(i, j).addObserver(this);
            }
        }
    }

    public void placeArmy(Army army) {
        map.placeArmy(army);
        armies.remove(army);
    }

    public void placeBuilding(Building building) {
        if(map.canPlace(building)) {
            map.placeBuilding(building);
            buildings.remove(building);
        }
    }

    @Override
    public void click(int x, int y) {
        if(gameMode == GameMode.EDIT_MODE) {
            if(!map.getCell(y, x).isEmpty()) {
                buildings.add(map.getCell(y,x).getBuilding());
                map.destroyBuilding(map.getCell(y,x).getBuilding());
            } else {
                if(currentBuilding != null) {
                    currentBuilding.setLocation(map.getCell(y, x));
                    placeBuilding(currentBuilding);
                }
            }
        } else if(gameMode == GameMode.ATTACK_MODE){
            if(placedArmy != null) {
                placedArmy.setLocation(map.getCell(y, x));
                placeArmy(placedArmy);
            }
            if(currentArmy != null) {
                Astar astar = new Astar();
                currentArmy.followPath(astar.findPath(currentArmy, map, map.getCell(y, x)));
            }
        }
    }

    public Map getMap() {
        return map;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public List<Army> getArmies() {
        return armies;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setCurrentArmy(Army currentArmy) {
        this.currentArmy = currentArmy;
    }

    public void setCurrentBuilding(Building building) {
        this.currentBuilding = building;
    }

    public void setPlacedArmy(Army placedArmy) {
        this.placedArmy = placedArmy;
    }

    public void connectObservers(MouseManager mouseManager) {
        for(Army army : armies) {
            army.addObserver(this);
            mouseManager.addRightClickObserver(army);
        }
    }

    @Override
    public void armySelected(Army army) {
        currentArmy = army;
    }
}
