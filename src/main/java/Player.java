import java.util.ArrayList;
import java.util.List;

public class Player implements ObjectClickedObserver, ArmySelectedObserver{
    private Map map;
    private List<Building> buildings;
    private List<Army> armies;
    private Army currentArmy;
    private Building currentBuilding;
    private Army placedArmy;
    private GameMode gameMode;
    private Camera gameCamera;
    private List<ArmyPlacedObserver> armyPlacedObservers;
    private List<BuildingObserver> buildingObservers;

    public Player(Map map) {
        gameCamera = new Camera(1000, -500);
        gameMode = GameMode.ATTACK_MODE;
        setMap(map);
        buildings = new ArrayList<>();
        armies = new ArrayList<>();
        buildings.add(new Castle(null));
        buildings.add(new Castle(null));
        buildings.add(new Castle(null));
        armies.add(new Army(100, "Front Line soldiers", null));
        armies.add(new Army(70, "Back Line archers", null));
        for(Army army : armies) {
            army.setAuto(false);
            army.setSpeed(3);
        }
        armyPlacedObservers = new ArrayList<>();
        buildingObservers = new ArrayList<>();
    }

    public void setMap(Map map) {
        this.map = map;
        for(int i = 0; i < map.getLength(); i++) {
            for(int j = 0; j < map.getLength(); j++) {
                map.getCell(i, j).addObserver(this);
            }
        }
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
                for(BuildingObserver observer : buildingObservers) {
                    observer.onBuildingDestroyed(map.getCell(y, x).getBuilding());
                }
                map.destroyBuilding(map.getCell(y,x).getBuilding());

            } else {
                if(currentBuilding != null) {
                    currentBuilding.setLocation(map.getCell(y, x));
                    placeBuilding(currentBuilding);
                    for(BuildingObserver observer : buildingObservers) {
                        observer.onBuildingPlaced(map.getCell(y, x).getBuilding());
                    }
                }
            }
        } else if(gameMode == GameMode.ATTACK_MODE){
            if(placedArmy != null) {
                placedArmy.setLocation(map.getCell(y, x));
                armies.remove(placedArmy);
                if(currentArmy != null) {
                    currentArmy.setSelected(false);
                    currentArmy = null;
                }
                for(ArmyPlacedObserver armyPlacedObserver : armyPlacedObservers) {
                    armyPlacedObserver.onArmyPlaced(placedArmy);
                }
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

    public void setCurrentBuilding(Building building) {
        this.currentBuilding = building;
    }

    public void setPlacedArmy(Army placedArmy) {
        this.placedArmy = placedArmy;
    }

    public void connectObservers(MouseManager mouseManager, EntityManager entityManager) {
        mouseManager.addMouseDraggedObserver(gameCamera);
        mouseManager.addMouseScrolledObserver(gameCamera);
        armyPlacedObservers.add(entityManager);
        mouseManager.addRightClickObserver(entityManager);
        entityManager.addArmySelectedObserver(this);
        buildingObservers.add(entityManager);
    }

    public Camera getGameCamera() {
        return gameCamera;
    }

    @Override
    public void armySelected(Army army) {
        currentArmy = army;
    }
}
