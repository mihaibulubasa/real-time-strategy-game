package view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainPanel extends JPanel {

    private TileView[][] grid;
    private Camera camera;
    private int width, height;
    private long lastFPSTime = System.nanoTime();
    private int frames = 0;
    private int fps = 0;
    private Map<Long, Drawable> drawables;
    private String[] players;
    private Rectangle select;
    private Point start;
    private List<TileView> selected;
    private SelectionView selectionView;

    public MainPanel(int width, int height){
        this.width = width;
        this.height = height;
        grid = new TileView[height][width];
        selectionView = new SelectionView();
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                grid[i][j] = new TileView(i, j);
            }
        }
        setLayout(null);
        setBackground(Color.black);
        drawables = new HashMap<>();
        select = new Rectangle();
        camera = new Camera(500, 0, this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        camera.render(g, sortDrawables());
        frames++;
        long now = System.nanoTime();
        if(now - lastFPSTime > 1000000000L) {
            fps = frames;
            frames = 0;
            lastFPSTime = now;
        }
        g.setColor(Color.WHITE);
        g.drawString("FPS: " + fps, 10, 20);
        if(players != null) {
            for (int i = 0; i < players.length; i++) {
                g.drawString(players[i], 1850, 20 + 20 * i);
            }
        }
        g.setColor(new Color(124, 128, 0, 145));
        g.fillRect(select.x, select.y, select.width, select.height);
    }

    public List<Drawable> sortDrawables() {
        List<Drawable> drawablesArr = new ArrayList<>(drawables.values());
        drawablesArr.sort(Drawable::compare);
        return drawablesArr;
    }

    public void initSelect(int x, int y){
        start = new Point(x, y);
        select = new Rectangle(x, y, 0, 0);
    }

    public void updateSelect(int x, int y) {
        if(start == null) {
            return;
        }
        int newX = Math.min(start.x, x);
        int newY = Math.min(start.y, y);
        int width = Math.abs(x - start.x);
        int height = Math.abs(y - start.y);
        select.setBounds(newX, newY, width, height);
    }

    public void resetSelect() {
        start = null;
        select = new Rectangle();
    }

    public ArmyView getArmyView(long id) {
        return (ArmyView)drawables.get(id);
    }

    public BuildingView getBuildingView(long id) {
        return (BuildingView)drawables.get(id);
    }

    public int getGridWidth(){
        return width;
    }

    public int getGridHeight(){
        return height;
    }

    public TileView[][] getGrid() {
        return grid;
    }

    public void setTileView(int i, int j){
        grid[i][j] = new TileView(i, j);
    }

    public Camera getCamera(){
        return camera;
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    public Collection<Drawable> getDrawables() {
        return drawables.values();
    }

    public void updatePlayerList(String token) {
        players = token.split(",");
    }

    public void addArmy(long id, int row, int col){
        ArmyView armyView = new ArmyView(id, grid[row][col]);
        drawables.put(id, armyView);
        armyView.recalculateGeometry(camera.getZoomFactor(), camera.getX(), camera.getY());
    }

    public void addBuilding(long id, String name, int row, int col, int width, int height) {
        BuildingView buildingView = new BuildingView(name, grid[row][col], width, height);
        drawables.put(id, buildingView);
        buildingView.recalculateGeometry(camera.getZoomFactor(), camera.getX(), camera.getY());
    }

    public Rectangle getSelect() {
        return select;
    }

    public TileView getTileView(int i, int j) {
        if(i < 0 || i >= height || j < 0 || j >= width) {
            return null;
        }
        return grid[i][j];
    }

    public SelectionView getSelectionView() {
        return selectionView;
    }
}
