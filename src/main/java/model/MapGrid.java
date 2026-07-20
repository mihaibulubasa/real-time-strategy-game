package model;

import model.buildings.Building;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapGrid {
    private Tile[][] grid;
    private int width, height;
    private Map<Long, Building> buildings;

    public MapGrid(int width, int height) {
        buildings = new HashMap<>();
        this.height = height;
        this.width = width;
        grid = new Tile[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                grid[i][j] = new Tile(i, j);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Building getBuilding(long id) {
        return buildings.get(id);
    }

    private boolean canPlaceBuilding(Building building, Tile location) {
        if(location.getCol() < building.getWidth() - 1 || location.getRow()< building.getHeight() - 1) {
            return false;
        }
        for(int i = location.getRow(); i >= location.getRow() - building.getHeight() + 1; i--) {
            for (int j = location.getCol(); j >= location.getCol() - building.getWidth() + 1; j--) {
                if(!grid[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean placeBuilding(Building building, Tile location) {
        if(!canPlaceBuilding(building, location)){
            return false;
        }
        for(int i = location.getRow(); i >= location.getRow() - building.getHeight() + 1; i--) {
            for (int j = location.getCol(); j >= location.getCol() - building.getWidth() + 1; j--) {
                grid[i][j].overlapBuilding(building);
            }
        }
        building.setLocation(location);
        buildings.put(building.getId(), building);
        return true;
    }

    public Collection<Building> getBuildings() {
        return buildings.values();
    }

    public void destroyBuilding(Building building) {
        removeBuilding(building);
        IdGenerator.getInstance().destroyId(building.getId());
    }

    public void removeBuilding(Building building) {
        for(int i = building.getLocation().getRow(); i >= building.getLocation().getRow() - building.getHeight() + 1; i--) {
            for (int j = building.getLocation().getCol(); j >= building.getLocation().getCol() - building.getWidth() + 1; j--) {
                grid[i][j].removeBuilding();
            }
        }
        building.setLocation(null);
        buildings.remove(building.getId());
    }

    public Tile getTile(int i, int j) {
        if(i < 0 || i >= height || j < 0 || j >= width) {
            return null;
        }
        return grid[i][j];
    }
}
