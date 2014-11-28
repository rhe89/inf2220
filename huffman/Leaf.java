/**
 * Created by Roar on 27.10.14.
 */
public class Leaf extends Node {
    private char c;
    private String bitValue = "";

    Leaf (char c, int freq) {
        this.c = c;
        this.freq = freq;
        leaf = true;
        nodes = ""+c;
    }
    public char getVal() {
        return c;
    }

    public void addBit(String bit) {
        bitValue += bit;
    }
    public String getBitValue() {
        return bitValue;
    }
}
