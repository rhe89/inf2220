import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Roar on 27.10.14.
 */
public class Huffman {

    String file, text, bitFile = "", convertedText = "";
    Node [] heap;
    BitCode [] bitTable;
    int size, currSize = 0;

    public static void main(String[] args) {
        new Huffman(args[0], args[1]);
    }

    Huffman(String file, String size) {
        this.file = file;
        this.size = Integer.parseInt(size);
        heap = new Node[this.size];
        bitTable = new BitCode[this.size];
        readFile();
    }

    private void readFile() {

        try {
            text = new Scanner(new File(file)).nextLine();
        } catch (IOException e) {
            System.out.println(new File("").getAbsolutePath());
            e.printStackTrace();
            System.exit(1);
        }

        generateFrequencies();
        System.out.println("Merging:");
        generateHeap();
        generateBitRepresentations(currSize-1, "");
        System.out.println("\nBit-table");
        generateBitTable();
        System.out.println("\nFile with bit values");
        generateBitFile();
        System.out.println(bitFile);
        System.out.println("\nConverted text");
        generateTextFromBitCode();
        System.out.println(convertedText);
    }

    private void generateFrequencies() {

        char[] arr = text.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            if (getChar(arr[i]) == null) {
                heap[currSize++] = new Leaf(arr[i], 1);
            } else {
                getChar(arr[i]).incrFreq();
            }

        }

    }

    Node getChar(char c) {
        for (int i = 0; i < currSize; i++) {
            Leaf tmp = (Leaf) heap[i];
            if (tmp.getVal() == c)
                return heap[i];
        }
        return null;
    }

    private void generateHeap() {
        int min1 = getMin();
        if (min1 != -1) heap[min1].merged = true;
        int min2 = getMin();
        if (min2 != -1) heap[min2].merged = true;
        boolean finished = false;


        if (min1 != -1 && min2 != -1) {
            int freq = heap[min1].getFreq() + heap[min2].getFreq();
            heap[currSize] = new Parent(freq, min2, min1);
            
            heap[currSize].addNodes(heap[min1].toString());
            heap[currSize].addNodes(heap[min2].toString());
            System.out.println("Merged: " + heap[min1].toString() + "["+min1+"] and " + heap[min2].toString()+"[" +min2 +"] - Freq: " + heap[currSize].getFreq()+"["+currSize+++"]");

        } else {
            finished = true;
        }
        if (!finished)
            generateHeap();
        else {

        }

    }

    private int getMin() {

        int min = Integer.MAX_VALUE;
        int tmp = -1;

        for (int i = 0; i < currSize; i++) {

            if (!heap[i].merged) {

                if (heap[i].getFreq() <= min) {
                    min = heap[i].getFreq();
                    tmp = i;

                }

            }

        }

        return tmp;

    }

    private void generateBitRepresentations(int index, String bit) {

        if (heap[index].isLeaf()) {

            Leaf leaf = (Leaf) heap[index];
            leaf.addBit(bit);

        } else {

            Parent par = (Parent) heap[index];
            generateBitRepresentations(par.left, bit+"0");
            generateBitRepresentations(par.right, bit+"1");

        }
    }

    private void generateBitTable() {
        int counter = 0;
        for (int i = 0; i < currSize; i++) {
            if (heap[i].isLeaf()) {
                Leaf leaf = (Leaf) heap[i];
                bitTable[counter] = new BitCode(leaf.getVal(), leaf.getBitValue());
                System.out.println("Char: " + bitTable[counter].c + " Code: " + bitTable[counter++].code);
            }
        }
    }

    private void generateBitFile() {

        for (int i = 0; i < text.length(); i++) {
            bitFile += getBitCode(text.charAt(i));
        }
    }

    private void generateTextFromBitCode() {
        int currIndex = 0;
        for (int i = 0; i <= bitFile.length(); i++) {

            String letter = bitFile.substring(currIndex,i);
            char c = getBitValue(letter);
            if (c != '9') {
                convertedText += ""+c;
                currIndex = i;
            }

        }
    }

    private String getBitCode(char c) {
        for (int i = 0; i < bitTable.length; i++) {
            if (bitTable[i].c == c) {
                return bitTable[i].code;
            }
        }
        return "-1";
    }

    private char getBitValue(String code) {
        for (int i = 0; i < bitTable.length; i++) {
            if (bitTable[i] != null && bitTable[i].code.equals(code)) {
                return bitTable[i].c;
            }
        }
        return '9';
    }


}

