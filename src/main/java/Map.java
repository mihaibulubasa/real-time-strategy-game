import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Map {
    private int length;
    private Cell[][] map;
    private MouseManager mouseManager;
    private List<Building> buildings;
    private List<Army> armies;
    private List<Drawable> entities;
    public Army test;

    public Map(int length, MouseManager mouseManager) {
        this.mouseManager = mouseManager;
        this.length = length;
        buildings = new ArrayList<>();
        armies = new ArrayList<>();
        entities = new ArrayList<>();
        map = new Cell[length][length];
        for(int y = 0; y < length; y++) {
            for(int x = 0; x < length; x++) {
                map[y][x] = new Cell(y, x);
            }
        }
        test = new Army(12, "Enemy", map[12][12]);
        test.setAuto(true);
        armies.add(test);
        entities.add(test);
    }

    public void tick() {
        for(Army army : armies) {
            army.tick(this);
        }
        entities.sort(Comparator.comparingInt(Drawable::getLocationOnScreen));
    }

    public void render(Graphics g, Camera camera) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                map[i][j].draw(g, camera);
            }
        }
        for(Drawable drawable : entities) {
            drawable.render(g, camera);
        }
        for(int i = 0; i < length; i++) {
            for(int j = 0; j < length; j++) {
                map[i][j].drawHover(g, camera);
            }
        }

    }

    public void connectObservers() {
        for(int i = 0; i < length; i++) {
            for(int j = 0; j < length; j++) {
                mouseManager.addMouseMovedObserver(map[i][j]);
                mouseManager.addMouseClickedObserver(map[i][j]);
            }
        }

    }


    public boolean canPlace(Building building) {
        for(int i = building.getLocation().row - building.getHeight() + 1; i <= building.getLocation().row; i++) {
            for(int j = building.getLocation().col - building.getWidth() + 1; j <= building.getLocation().col; j++) {
                if(i < 0 || i >= length || j < 0 || j >= length) {
                    return false;
                }
                if(!map[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void placeBuilding(Building building) {
        if(canPlace(building)) {
            buildings.add(building);
            for (int i = building.getLocation().row - building.getHeight() + 1; i <= building.getLocation().row; i++) {
                for (int j = building.getLocation().col - building.getWidth() + 1; j <= building.getLocation().col; j++) {
                    map[i][j].setEmpty(false);
                    map[i][j].setBuilding(building);
                }
            }
            entities.add(building);
        }
    }

    public void destroyBuilding(Building building) {
        for(int i = building.getLocation().row - building.getHeight() + 1; i <= building.getLocation().row; i++) {
            for(int j = building.getLocation().col - building.getWidth() + 1; j <= building.getLocation().col; j++) {
                map[i][j].setEmpty(true);
                map[i][j].setBuilding(null);
            }
        }
        entities.remove(building);
        buildings.remove(building);
    }

    public int getLength() {
        return length;
    }

    public Cell getCell(int i, int j) {
        return map[i][j];
    }

    public void placeArmy(Army army) {
        armies.add(army);
        entities.add(army);
    }

    public Army getArmy(int i) {
        return armies.get(i);
    }

    public List<Army> getArmies() {
        return armies;
    }

}
