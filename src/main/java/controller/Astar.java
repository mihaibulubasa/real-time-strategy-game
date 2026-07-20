package controller;

import java.util.ArrayList;
import java.util.Collections;

import model.MapGrid;
import model.Tile;

public class Astar {

    private enum State{
        UNKNOWN,
        VISITING,
        CHECKED
    }

    private class Node {
        private Tile tile;
        private double g, f;
        public Node(Tile tile, double g, double f) {
            this.tile = tile;
            this.g = g;
            this.f = f;
        }
    }

    public static double getDistance(Tile a, Tile b) {
        return Math.sqrt((a.getCol() - b.getCol()) * (a.getCol() - b.getCol()) + (a.getRow() - b.getRow()) * (a.getRow() - b.getRow()));
    }

    private boolean checkBounds(int x, int y, int width, int height) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private void visit(MapGrid map, Tile next, Tile current, Tile destination, Node[][] prev, State[][] visited, Heap heap) {
        if(checkBounds(next.getCol(), next.getRow(), map.getWidth(), map.getHeight()) && next.isEmpty()) {
            double newG = prev[current.getRow()][current.getCol()].g + getDistance(next, current);
            if(newG < prev[next.getRow()][next.getCol()].g) {
                prev[next.getRow()][next.getCol()].g = newG;
                prev[next.getRow()][next.getCol()].f = newG + getDistance(next, destination);
                prev[next.getRow()][next.getCol()].tile = current;
                heap.insert(new Heap.Node(next, prev[next.getRow()][next.getCol()].f));
                visited[next.getRow()][next.getCol()] = State.VISITING;
            }
        }
    }

    private void search(MapGrid map, Tile destination, Node[][] prev, State[][] visited, Heap heap) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1,  0,  1,-1, 1,-1, 0, 1};
        while (!heap.isEmpty()) {
            Heap.Node node = heap.extractMin();
            while (!heap.isEmpty() && visited[node.getTile().getRow()][node.getTile().getCol()] == State.CHECKED) {
                node = heap.extractMin();
            }
            if (visited[node.getTile().getRow()][node.getTile().getCol()] == State.CHECKED) {
                break;
            }
            visited[node.getTile().getRow()][node.getTile().getCol()] = State.CHECKED;
            if (node.getTile().getRow() == destination.getRow() &&
                    node.getTile().getCol() == destination.getCol()) {
                return;
            }
            int r = node.getTile().getRow();
            int c = node.getTile().getCol();
            for (int i = 0; i < 8; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if(checkBounds(nr, nc, map.getWidth(), map.getHeight())) {
                    Tile next = map.getTile(nr, nc);
                    if(next != null) {
                        visit(map, next, node.getTile(), destination, prev, visited, heap);
                    }
                }

            }
        }
    }

    private ArrayList<Tile> recreatePath(Tile destination, Tile start, Node[][] prev) {
        ArrayList<Tile> path = new ArrayList<>();
        Tile tile = destination;
        while(tile != start) {
            path.add(tile);
            tile = prev[tile.getRow()][tile.getCol()].tile;
            if(tile == null) {
                return new ArrayList<>();
            }
        }
        return path;
    }

    public ArrayList<Tile> findPath(Tile start, MapGrid map, Tile destination) {
        int width = map.getWidth();
        int height = map.getHeight();
        State[][] visited = new State[map.getHeight()][map.getWidth()];
        Node[][] prev = new Node[map.getHeight()][map.getWidth()];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                visited[i][j] = State.UNKNOWN;
                prev[i][j] = new Node(null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
        }
        Heap heap = new Heap(width * height);
        prev[start.getRow()][start.getCol()].g = 0;
        prev[start.getRow()][start.getCol()].f = getDistance(start, destination);
        visited[start.getRow()][start.getCol()] = State.VISITING;
        heap.insert(new Heap.Node(start, prev[start.getRow()][start.getCol()].f));
        search(map, destination, prev, visited, heap);
        if(visited[destination.getRow()][destination.getCol()] == State.CHECKED) {
            ArrayList<Tile> path = recreatePath(destination, start, prev);
            Collections.reverse(path);
            return path;
        }
        return new ArrayList<>();
    }
}
