/**
 * Created by Roar on 29.11.14.
 */
public class Node {

    public Node left, right;
    private String c;
    private int freq;
    public boolean leaf;
    private String bitRep;

    Node(String c, int freq, Node left, Node right) {
        this.c = c;
        this.freq = freq;
        this.left = left;
        this.right = right;

    }

    public void print() {

        if (left != null) {
            left.print();
        }
        if (right != null) {
            right.print();
        }
        System.out.println("Char value: " + c);
    }

    public void incrFreq() {
        freq++;
    }

    public boolean isLeaf() {
        return leaf;
    }
    public int getFreq() {
        return freq;
    }

    public String getChar() {
        return c;
    }
    public void setBitRep(String bit) {
        bitRep = bit;
    }
    public String getBitRep() {
        return bitRep;
    }

    public String getCharFromBit(String code) {

        if (left != null) {
            left.getCharFromBit(code);
        }
        if (right != null) {
            right.getCharFromBit(code);
        }
        if (code.equals(bitRep)) {
            return c;
        } else {
            return "-1";
        }
    }

}
