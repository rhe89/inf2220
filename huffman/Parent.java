/**
 * Created by Roar on 27.10.14.
 */
public class Parent extends Node {
    int left, right;

    Parent (int freq, int left, int right) {
        this.freq = freq;
        this.left = left;
        this.right = right;
        leaf = false;
    }
}
