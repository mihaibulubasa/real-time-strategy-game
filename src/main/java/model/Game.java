package model;

import model.buildings.Building;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private MapGrid mapGrid;
    private EntityHandler entityHandler;
    private Map<String, Player> players;

    public Game(MapGrid mapGrid, EntityHandler entityHandler) {
        this.mapGrid = mapGrid;
        this.entityHandler = entityHandler;
        players = new HashMap<>();
    }

    public void tick(){
        entityHandler.tick();
    }

    public boolean deployArmy(Player player, Army army, int row, int col) {
        Tile location = mapGrid.getTile(row, col);
        if(!location.isEmpty()) {
            return false;
        }
        army.setLocation(location);
        entityHandler.addArmy(army);
        player.addArmy(army);
        return true;
    }

    public void commandArmy(Army army, ArrayList<Tile> path){
        army.followPath(path);
    }

    public boolean placeBuilding(Player player, Building building, int row, int col) {
        Tile location = mapGrid.getTile(row, col);
        if(!location.isEmpty()) {
            return false;
        }
        mapGrid.placeBuilding(building, location);
        player.addBuilding(building);
        return true;
    }

    public void removeBuilding(Player player, Building building){
        mapGrid.removeBuilding(building);
        player.removeBuilding(building.getId());
    }

    public MapGrid getMapGrid(){
        return mapGrid;
    }

    public EntityHandler getEntityHandler(){
        return entityHandler;
    }

    public void addPlayer(Player player){
        players.put(player.getName(), player);
    }

    public void deletePlayer(Player player) {
        players.remove(player.getName());
    }

    public Collection<Player> getPlayers(){
        return players.values();
    }
}
