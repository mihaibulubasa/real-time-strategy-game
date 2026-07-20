import model.Dictionary;
import model.EntityHandler;
import model.Game;
import model.MapGrid;
import network.GameServer;

public class ServerMain {
    public static void main(String[] args) {
        int mapWidth = 64;
        int mapHeight = 64;
        MapGrid mapGrid = new MapGrid(mapWidth, mapHeight);
        EntityHandler entityHandler = new EntityHandler();
        Game game = new Game(mapGrid, entityHandler);
        Dictionary.getInstance().addWord("admin");
        Dictionary.getInstance().addWord("admin2");
        int port = 8080;
        GameServer server = new GameServer(game, port);
        System.out.println("Server Started");
        server.listen();
    }

}
