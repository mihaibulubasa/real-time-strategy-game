public class Heap {
    public static class Node{
        private Cell cell;
        private double f;

        public Node(Cell cell, double f){
            this.cell = cell;
            this.f = f;
        }

        public Cell getCell(){
            return cell;
        }

        public double getF(){
            return f;
        }
    }

    private Node[] nodes;
    private int size;

    public Heap(int maxSize){
        nodes = new Node[maxSize];
        size = 0;
    }

    private int parent(int i){
        return (i-1)/2;
    }

    private int left(int i){
        return 2*i+1;
    }

    private int right(int i){
        return 2*i+2;
    }

    private void heapifyUp(int i){
        if(i == 0) {
            return;
        }
        if(nodes[i].getF() < nodes[parent(i)].getF()){
            Node swap = nodes[i];
            nodes[i] = nodes[parent(i)];
            nodes[parent(i)] = swap;
            heapifyUp(parent(i));
        }
    }

    private void heapifyDown(int i){
        int min = i;
        int left = left(i);
        int right = right(i);
        if(left < size && nodes[left].getF() < nodes[min].getF()){
            min = left;
        }
        if(right < size && nodes[right].getF() < nodes[min].getF()){
            min = right;
        }
        if(min != i){
            Node swap = nodes[min];
            nodes[min] = nodes[i];
            nodes[i] = swap;
            heapifyDown(min);
        }
    }

    public void insert(Node n) {
        nodes[size] = n;
        heapifyUp(size);
        size++;
    }

    public Node extractMin(){
        Node min = nodes[0];
        nodes[0] = nodes[size-1];
        size--;
        heapifyDown(0);
        return min;
    }

    public boolean isEmpty(){
        return size == 0;
    }
}
