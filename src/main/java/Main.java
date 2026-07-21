import controller.GameController;

public class Main {
    public static void main(String[] args) {
        Thread server = new Thread(ServerMain::startServer);
        server.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException _) {

        }
        ClientMain.startClient();
    }
}
