import java.awt.*;

public class Map {
    private int length;
    private Cell[][] map;

    public Map(int length) {
        this.length = length;
        map = new Cell[length][length];
        for(int y = 0; y < length; y++) {
            for(int x = 0; x < length; x++) {
                map[y][x] = new Cell(y, x);
            }
        }
    }

    public void render(Graphics g, Camera camera) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                map[i][j].draw(g, camera);
            }
        }
    }

    public void renderHover(Graphics g, Camera camera) {
        for(int i = 0; i < length; i++) {
            for(int j = 0; j < length; j++) {
                map[i][j].drawHover(g, camera);
            }
        }
    }

    public void connectObservers(MouseManager mouseManager) {
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
        for (int i = building.getLocation().row - building.getHeight() + 1; i <= building.getLocation().row; i++) {
            for (int j = building.getLocation().col - building.getWidth() + 1; j <= building.getLocation().col; j++) {
                map[i][j].setEmpty(false);
                map[i][j].setBuilding(building);
            }
        }
    }

    public void destroyBuilding(Building building) {
        for(int i = building.getLocation().row - building.getHeight() + 1; i <= building.getLocation().row; i++) {
            for(int j = building.getLocation().col - building.getWidth() + 1; j <= building.getLocation().col; j++) {
                map[i][j].setEmpty(true);
                map[i][j].setBuilding(null);
            }
        }
    }

    public int getLength() {
        return length;
    }

    public Cell getCell(int i, int j) {
        return map[i][j];
    }

}
