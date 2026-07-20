package model;

import model.buildings.Building;

public class Tile {
    private boolean empty;
    private Building building;
    private int row, col;

    public Tile(int row, int col){
        empty = true;
        this.row = row;
        this.col = col;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void overlapBuilding(Building building) {
        this.building = building;
        empty = false;
    }

    public void removeBuilding(){
        building = null;
        empty = true;
    }

    public Building getBuilding() {
        return building;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public String getTexture(){
        if(!empty) {
            return "NO_TEXTURE";
        }
        return "TILE_IMAGE";
    }

}
