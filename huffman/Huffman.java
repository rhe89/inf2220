import java.io.File;
import java.util.Scanner;

public class Huffman {
    Node root;
    String bitFile = "", text, convertedText;
    int counter = 0;
    int[] freq = new int[256];
    BitCode[] bitTable;
    Heap binHeap;

    public static void main(String[] args) throws Exception{
        new Huffman(args[0], args[1]);
    }

    Huffman(String file, String size) throws Exception{

        this.text = new Scanner(new File(file)).nextLine();
        genFreqTable(text);
        binHeap = new Heap(Integer.parseInt(size));
    }
    /* Use an int-array and char-indexes to increase each char's frequences
     */
    private void genFreqTable(String text) {
        char[] arr = text.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            freq[arr[i]]++;
        }
        buildHeap();
    }

    //Insert every char with a frequency > 0 in the Heap
    private void buildHeap() {
        int freqCounter = 0;

        for (int i = 0; i < freq.length; i++) {
            if (freq[i] > 0) {
                Node n = new Node(""+(char)i, freq[i], null, null);
                n.leaf = true;
                binHeap.insert(n);
                freqCounter++;
            }
        }

        bitTable = new BitCode[freqCounter];
        buildFreqTree();
    }

    /* While the heap contains more than one item, do two
    deleteMins and use the two nodes return to construct
    a parent with the two nodes as children (left and right)
    and their combined frequences as the parents frequency */
    private void buildFreqTree() {
        while(binHeap.getSize() > 1) {
            Node right = binHeap.deleteMin();
            System.out.println(right.getChar() + ": " + right.getFreq());
            Node left = binHeap.deleteMin();
            System.out.println(left.getChar() + ": " + left.getFreq() + "\n");

            Node parent = new Node(left.getChar()+" "+right.getChar(), left.getFreq()+right.getFreq(), left, right);
            parent.leaf = false;
            binHeap.insert(parent);
        }

        root = binHeap.deleteMin();
        traverseTreeAndSetBitCodes(root, "");
    }

    /* Do an ordinary binary tree traversal and set bit codes for left (0) and right (1)
    until you reach a leaf-node (which is a char) */
    private void traverseTreeAndSetBitCodes(Node node, String bit) {

        if (node != null && !node.isLeaf()) {
            traverseTreeAndSetBitCodes(node.left, bit+"0");
            traverseTreeAndSetBitCodes(node.right, bit+"1");
        } else {
            node.setBitRep(bit);
            bitTable[counter] = new BitCode(node.getChar(), node.getBitRep());
            System.out.println("Char: " + bitTable[counter].c + " Code: " + bitTable[counter++].code);
        }
        generateBitFile();
    }

    //Generate the bitFile by getting the code representing each char in the string
    private void generateBitFile() {

        for (int i = 0; i < text.length(); i++) {
            bitFile += getBitCode(""+text.charAt(i));
        }
    }

    //Generate the text from the bitcode by getting the char represented by each code
    private void generateTextFromBitCode() {
        int currIndex = 0;
        for (int i = 0; i <= bitFile.length(); i++) {

            String letter = bitFile.substring(currIndex,i);

            String c = getBitValue(letter);

            if (!c.equals("9")) {
                convertedText += ""+c;
                currIndex = i;
            }
        }
    }

    private String getBitCode(String c) {
        for (int i = 0; i < bitTable.length; i++) {
            if (bitTable[i].c.equals(c)) {
                return bitTable[i].code;
            }
        }
        return "-1";
    }

    private String getBitValue(String code) {
        for (int i = 0; i < bitTable.length; i++) {
            if (bitTable[i] != null && bitTable[i].code.equals(code)) {
                return bitTable[i].c;
            }
        }
        return "9";
    }
}
