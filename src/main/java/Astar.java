import java.util.ArrayList;
import java.util.Collections;

public class Astar {

    private enum State{
        UNKNOWN,
        VISITING,
        CHECKED
    }

    private class Node {
        private Cell cell;
        private double g, f;
        public Node(Cell cell, double g, double f) {
            this.cell = cell;
            this.g = g;
            this.f = f;
        }
    }

    private double getDistance(Cell a, Cell b) {
        return Math.sqrt((a.col - b.col) * (a.col - b.col) + (a.row - b.row) * (a.row - b.row));
    }

    private boolean checkBounds(int x, int y, int len) {
        return x >= 0 && x < len && y >= 0 && y < len;
    }

    private void visit(Map map, Cell next, Cell current, Cell destination, Node[][] prev, State[][] visited, Heap heap) {
        if(checkBounds(next.col, next.row, map.getLength()) && next.isEmpty()) {
            double newG = prev[current.row][current.col].g + getDistance(next, current);
            if(newG < prev[next.row][next.col].g) {
                prev[next.row][next.col].g = newG;
                prev[next.row][next.col].f = newG + getDistance(next, destination);
                prev[next.row][next.col].cell = current;
                heap.insert(new Heap.Node(next, prev[next.row][next.col].f));
                visited[next.row][next.col] = State.VISITING;
            }
        }
    }

    private void search(Map map, Cell destination, Node[][] prev, State[][] visited, Heap heap) {
        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1,  0,  1,-1, 1,-1, 0, 1};
        while (!heap.isEmpty()) {
            Heap.Node node = heap.extractMin();
            while (!heap.isEmpty() && visited[node.getCell().row][node.getCell().col] == State.CHECKED) {
                node = heap.extractMin();
            }
            if (visited[node.getCell().row][node.getCell().col] == State.CHECKED) {
                break;
            }
            visited[node.getCell().row][node.getCell().col] = State.CHECKED;
            if (node.getCell().row == destination.row &&
                    node.getCell().col == destination.col) {
                return;
            }
            int r = node.getCell().row;
            int c = node.getCell().col;
            for (int i = 0; i < 8; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];
                if(checkBounds(nr, nc, map.getLength())) {
                    Cell next = map.getCell(nr, nc);
                    if(next != null) {
                        visit(map, next, node.getCell(), destination, prev, visited, heap);
                    }
                }

            }
        }
    }

    private ArrayList<Cell> recreatePath(Cell destination, Cell start, Node[][] prev) {
        ArrayList<Cell> path = new ArrayList<>();
        Cell cell = destination;
        while(cell != start) {
            path.add(cell);
            cell = prev[cell.row][cell.col].cell;
            if(cell == null) {
                return null;
            }
        }
        return path;
    }

    public ArrayList<Cell> findPath(Army army, Map map, Cell destination) {
        int len = map.getLength();
        State[][] visited = new State[map.getLength()][map.getLength()];
        Node[][] prev = new Node[map.getLength()][map.getLength()];
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                visited[i][j] = State.UNKNOWN;
                prev[i][j] = new Node(null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            }
        }
        Heap heap = new Heap(len * len);
        prev[army.getLocation().row][army.getLocation().col].g = 0;
        prev[army.getLocation().row][army.getLocation().col].f = getDistance(army.getLocation(), destination);
        visited[army.getLocation().row][army.getLocation().col] = State.VISITING;
        heap.insert(new Heap.Node(army.getLocation(), prev[army.getLocation().row][army.getLocation().col].f));
        search(map, destination, prev, visited, heap);
        if(visited[destination.row][destination.col] == State.CHECKED) {
            ArrayList<Cell> path = recreatePath(destination, army.getLocation(), prev);
            if(path != null) {
                Collections.reverse(path);
                return path;
            }
        }
        return null;
    }
}
