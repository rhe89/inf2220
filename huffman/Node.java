public class Node {
    int freq;
    String nodes = "";

    boolean leaf, merged = false;

    void incrFreq() {
        freq++;
    }

    boolean isLeaf() {
        return leaf;
    }

    public int getFreq() {
        return freq;
    }

    public void addNodes(String newNode) {
        nodes += newNode;
    }
    public String toString() {
        return nodes;
    }


}