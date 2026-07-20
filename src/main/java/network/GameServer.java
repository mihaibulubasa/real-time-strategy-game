package network;

import model.Army;
import model.Game;
import model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameServer {
    private final Game game;
    private final int port;

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
        Army army = new Army("Army");
        army.setLocation(game.getMapGrid().getTile(row, col));
        player.addArmy(army);
        game.getEntityHandler().addArmy(army);
        for(ClientHandler client : clients){
            client.sendMessage("ADD_ARMY:" + row + ':' + col);
        }

    }
}
