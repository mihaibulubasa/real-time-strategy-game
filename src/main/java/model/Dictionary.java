package model;

public class Dictionary {

    private static Dictionary instance;

    private class TreeNode{
        private boolean isEnding;
        private TreeNode[] next;
        private TreeNode() {
            next = new TreeNode[127];
        }
    }

    public static Dictionary getInstance(){
        if(instance == null) {
            synchronized (Dictionary.class) {
                if(instance == null) {
                    instance = new Dictionary();
                }
            }
        }
        return instance;
    }

    private TreeNode root;
    private String safeChars;

    private Dictionary(){
        root = new TreeNode();
        safeChars = "^[a-zA-Z0-9_-]{3,16}$";
    }

    public synchronized void addWord(String word) {
        if(word == null || !word.matches(safeChars)){
            return;
        }
        TreeNode current = root;
        for(char c : word.toCharArray()) {
            if(current.next[c] == null) {
                current.next[c] = new TreeNode();
            }
            current = current.next[c];
        }
        current.isEnding = true;
    }

    public synchronized boolean contains(String word) {
        if(word == null || !word.matches(safeChars)) {
            return false;
        }
        TreeNode current = root;
        for(char c : word.toCharArray()) {
            if(current.next[c] == null) {
                return false;
            }
            current = current.next[c];
        }
        return current.isEnding;
    }

    public synchronized void removeWord(String word) {
        if(word == null || !word.matches(safeChars)){
            return;
        }
        canRemoveNode(word, root, 0);
    }

    private boolean canRemoveNode(String word, TreeNode node, int index) {
        if(node == null) {
            return false;
        }
        if(index == word.length()) {
            if(!node.isEnding) {
                return false;
            }
            node.isEnding = false;
            return hasNoChildren(node);
        }
        char c = word.charAt(index);
        if(canRemoveNode(word, node.next[c], index + 1)) {
            node.next[c] = null;
            return !node.isEnding && hasNoChildren(node);
        }
        return false;
    }

    private boolean hasNoChildren(TreeNode node){
        for(int i = 0; i < 127; i++) {
            if(node.next[i] != null) {
                return false;
            }
        }
        return true;
    }

    public String getSafeChars(){
        return safeChars;
    }
}
