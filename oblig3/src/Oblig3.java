import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Roar on 28.10.14.
 */
public class Oblig3 {
    private char[] haystack, needle;
    final int NEEDLE_SIZE, LIMIT, WILDCARD_COUNT;
    private int charset[];

    public static void main(String[] args) {

        if (args.length == 2) {
            new Oblig3(args[0], args[1]);
        } else {
            System.out.println("java Oblig3: none or invalid arguments");
            System.out.println("usage: java Oblig3 [haystack] [needle]");
            System.exit(1);
        }
    }

    Oblig3(String word, String searchTerm) {
        haystack = readFile(word).toCharArray();
        needle = readFile(searchTerm).toCharArray();
        WILDCARD_COUNT = findWildCards(needle);
        NEEDLE_SIZE = needle.length-1;
        LIMIT = haystack.length-1;
        charset = new int[256];

        System.out.println("Wildcards: " + WILDCARD_COUNT);
        setCharValues();
        findNeedle();
    }

    private String readFile(String fileName) {
        try {
            return new Scanner(new File(fileName)).nextLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return "";
        }
    }

    private int findWildCards(char[] needle) {
        int counter = 0;

        for (int i = 0; i < needle.length; i++) {
            if (needle[i] == '_') {
                counter++;
            }
        }
        return counter;
    }
    private void findNeedle() {
        int counter = 0, needlePos = NEEDLE_SIZE;

        for (int i = 0; i < haystack.length; i++) {
            if (i < 10) System.out.print(" " + 0+i + "|");
            else System.out.print(" " + i + "|");
        }
        System.out.println();
        for (int i = 0; i < haystack.length; i++) {
            System.out.print(" " + haystack[i] + " |");
        }

        while ((counter+NEEDLE_SIZE) <= LIMIT) {

            printExecursion(counter, needlePos);

            if (needlePos == 0 && (needle[needlePos] == haystack[counter+needlePos] || needle[needlePos] == '_')) {
                //System.out.println("\nMatch found at haystack[" + (needlePos+counter) + "]!");

                //System.out.println("Increasing counter according to value at index ["+(counter+NEEDLE_SIZE)+"] " +
                //        "Character: '" + haystack[counter+NEEDLE_SIZE] + "' (val: " + charset[haystack[counter+NEEDLE_SIZE]] + ")");

                counter += charset[haystack[needlePos+counter]];
                needlePos = NEEDLE_SIZE;

                //System.out.println("New counter: " + counter);

            } else if (needle[needlePos] == haystack[counter+needlePos] || needle[needlePos] == '_') {

                //System.out.println("\nMatch at char: " + haystack[counter+needlePos]);
                needlePos--;

            } else {

                //System.out.println("\nMismatch at index [" + (counter+needlePos) + "] Character: '" +
                //      haystack[counter+needlePos] + "'");
                //System.out.println("Increasing counter according to value at index ["+(counter+NEEDLE_SIZE)+"] " +
                //        "Character: '" + haystack[counter+NEEDLE_SIZE] + "' (val: " + charset[haystack[counter+NEEDLE_SIZE]] + ")");
                counter += charset[haystack[NEEDLE_SIZE+counter]];
                needlePos = NEEDLE_SIZE;

            }
        }
    }

    private void printExecursion(int counter, int needlePos) {
        System.out.println();

        for (int i = 0; i < counter; i++) {
            System.out.print("   |");
        }

        for (int i = 0; i < needle.length; i++) {
            String n = ""+needle[i];

            if (i >= needlePos || needlePos == 0) n = n.toUpperCase();

            System.out.print(" " + n + " |");

        }

        for (int i = counter; i < haystack.length-1-WILDCARD_COUNT; i++) {
            System.out.print("   |");
        }
    }

    /***
     * Sets all the position in the charset-array to the length of the needle.
     * Then iterates through the needle, and sets the position with an index
     * matching the (int)char-value to the length of the needle-1, to indicate
     * how far one safely can jump before risking bumping into the same char
     * again.
     *
     */
    private void setCharValues() {
        for (int i = 0; i < charset.length; i++) {

            charset[i] = needle.length-WILDCARD_COUNT;

        }

        for (int i = needle.length-2; i >= 0; i--) {

                charset[(int)needle[i]] = needle.length-1-i-WILDCARD_COUNT;

                System.out.println("Jump length at char: " + needle[i] + ": " + charset[(int)needle[i]]);


        }

        System.out.println("Jump length at other chars: " + (needle.length-WILDCARD_COUNT));

    }
}
