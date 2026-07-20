package model;

import model.buildings.Building;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private Map<Long, Army> armies;
    private Map<Long, Building> buildings;

    public Player(String name) {
        this.name = name;
        armies = new HashMap<>();
        buildings = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Army getArmy(long id) {
        return armies.get(id);
    }

    public Building getBuilding(long id) {
        return buildings.get(id);
    }

    public void addArmy(Army army){
        armies.put(army.getId(), army);
    }

    public void addBuilding(Building building){
        buildings.put(building.getId(), building);
    }

    public void removeArmy(long id){
        armies.remove(id);
    }

    public void removeBuilding(long id){
        buildings.remove(id);
    }

    public Collection<Army> getArmies(){
        return armies.values();
    }

    public Collection<Building> getBuildings(){
        return buildings.values();
    }

    @Override
    public String toString(){
        return name;
    }
}
