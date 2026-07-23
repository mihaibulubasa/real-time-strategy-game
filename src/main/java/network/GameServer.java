package network;

import model.Army;
import model.Game;
import model.Player;
import model.buildings.Building;
import model.buildings.Castle;
import model.buildings.House;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameServer {
    private final Game game;
    private final int port;
    private int caca;

    private List<ClientHandler> clients;

    public GameServer(Game game, int port) {
        this.game = game;
        this.port = port;
        clients = new CopyOnWriteArrayList<>();
    }

    public void listen(){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            while (true) {
                Socket client = serverSocket.accept();
                ClientHandler handler = new ClientHandler(client, game, this);
                Thread thread = new Thread(handler);
                thread.start();
            }
        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void broadcast(String message){
        for(ClientHandler client : clients){
            client.sendMessage(message);
        }
    }

    public void addClient(ClientHandler client){
        clients.add(client);
        broadcastPlayers();
    }

    public void removeClient(ClientHandler client){
        clients.remove(client);
        broadcastPlayers();
    }

    public void broadcastPlayers() {
        StringBuilder players = new StringBuilder("PLAYER_LIST:");
        for(ClientHandler client : clients){
            players.append(client.getPlayer()).append(",");
        }
        broadcast(players.toString());
    }

    public void addArmy(Player player,int row, int col){
        boolean deployed = game.deployArmy(player, new Army("Army"), row, col);
        if(deployed){
            for(ClientHandler client : clients){
                client.sendMessage("ADD_ARMY:" + row + ':' + col);
            }
        }
    }

    public void addBuilding(Player player, int row, int col){
        Building building;
        if(caca % 2 == 0) {
            building = new House("The TownHall");
        } else {
            building = new Castle("The Black Castle");
        }
        boolean placed = game.placeBuilding(player, building, row, col);
        if(placed) {
            caca++;
            for(ClientHandler client : clients){
                client.sendMessage("ADD_BUILDING:" + building.getTexture() + ":"+ row + ':' + col + ":" + building.getWidth() + ":" + building.getHeight());
            }
        }
    }
}
