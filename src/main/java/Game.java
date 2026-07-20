import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private Timer timer;
    private Map map;
    private MouseManager mouseManager;
    private Player player;
    private MapMenu menu;
    private long lastFPSTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    private EntityManager entityManager;

    public Game() {
        setLayout(null);
        timer = new Timer(0, this);
        entityManager = new EntityManager();
        map = new Map(64);
        player = new Player(map);
        mouseManager = new MouseManager(player.getGameCamera());
        player.connectObservers(mouseManager, entityManager);
        map.connectObservers(mouseManager);
        addMouseListener(mouseManager);
        addMouseMotionListener(mouseManager);
        addMouseWheelListener(mouseManager);
        timer.start();
    }

    public void setMenu(int height, int width) {
        menu = new MapMenu(player);
        menu.setBounds(0, height - 200, width, 200);
        add(menu);
        revalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        map.render(g, player.getGameCamera());
        entityManager.render(g, player.getGameCamera());
        map.renderHover(g, player.getGameCamera());
        frames++;
        long now = System.nanoTime();
        if(now - lastFPSTime > 1000000000L) {
            fps = frames;
            frames = 0;
            lastFPSTime = now;
        }
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + fps, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        entityManager.tick(map);
        repaint();
    }
}
