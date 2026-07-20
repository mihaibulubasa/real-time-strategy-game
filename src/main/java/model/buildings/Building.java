package model.buildings;

import model.IdGenerator;
import model.Tile;

public abstract class Building {
    protected Tile location;
    protected int width, height;
    protected String name;
    protected long id;

    public Building(String name) {
        id = IdGenerator.getInstance().getNewId();
        this.name = name;
    }

    public void setLocation(Tile location) {
        this.location = location;
    }

    public Tile getLocation() {
        return location;
    }

    public boolean isOnMapGrid(){
        return location != null;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth() {
        return width;
    }

    public long getId(){
        return id;
    }

    public abstract String getTexture();

    @Override
    public String toString(){
        return name;
    }
}
