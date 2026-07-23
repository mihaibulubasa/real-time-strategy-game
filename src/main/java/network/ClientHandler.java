package network;

import model.Army;
import model.Dictionary;
import model.Game;
import model.Player;
import model.buildings.Building;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable{

    private Socket clientSocket;
    private GameServer server;
    private Game game;

    private PrintWriter out;
    private BufferedReader in;
    private Player player;

    public ClientHandler(Socket socket, Game game, GameServer server) {
        this.game = game;
        this.server = server;
        clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String input;
            while ((input = in.readLine()) != null) {
                processPacket(input);
            }
        }catch (SocketException e) {
            cleanUp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            cleanUp();
        }
    }

    private void processPacket(String packet) {
        String[] tokens = packet.split(":");
        String command = tokens[0];
        switch (command) {
            case "LOGIN":
                handleLogin(tokens[1]);
                break;
            case "PLACE_ARMY" :
                server.addArmy(player, Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                break;
            case "PLACE_BUILDING" :
                server.addBuilding(player, Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                break;
            default:
                break;
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public Player getPlayer() {
        return player;
    }

    private void handleLogin(String username){
        if(Dictionary.getInstance().contains(username)) {
            player = new Player(username);
            game.addPlayer(player);
            out.println("LOGIN_SUCCESS:"+username);
            sendMap();
            sendEntities();
            server.addClient(this);
        } else {
            out.println("LOGIN_FAIL");
        }
    }

    private void sendMap(){
        int width = game.getMapGrid().getWidth();
        int height = game.getMapGrid().getHeight();
        StringBuilder tiles = new StringBuilder();
        for(int i = 0 ; i < height ; i++) {
            for(int j = 0 ; j < width ; j++) {
                tiles.append(game.getMapGrid().getTile(i, j).getTexture()).append(",");
            }
        }
        StringBuilder buildings = new StringBuilder();
        for(Building b : game.getMapGrid().getBuildings()){
            buildings
                    .append(b.getLocation().getRow()).append(",")
                    .append(b.getLocation().getCol()).append(",")
                    .append(b.getTexture()).append(",");
        }
        out.println("TILE_GRID_INIT:"+ width + ":" + height + ":"+ tiles);
        out.println("BUILDINGS_INIT:"+ buildings);
    }

    private void sendEntities(){
        StringBuilder entities = new StringBuilder();
        for(Army army : game.getEntityHandler().getArmies()) {
            entities
                    .append(army.getLocation().getRow()).append(",")
                    .append(army.getLocation().getCol()).append(",");
        }
        out.println("ENTITIES_INIT:"+ entities);
    }

    private void cleanUp() {
        try {
            if(player != null) {
                game.deletePlayer(player);
            }
            server.removeClient(this);
            if(in != null) in.close();
            if(out != null) out.close();
            if(clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
