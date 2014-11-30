import java.io.File;
import java.io.IOException;
import java.util.Scanner;

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

        if (haystack.length == 0 || needle.length == 0) {
            System.out.println("Empty needle or haystack, or both!");
            System.exit(1);
        }
        WILDCARD_COUNT = findWildCards(needle);
        NEEDLE_SIZE = needle.length-1;
        LIMIT = haystack.length - needle.length;
        charset = new int[256];

        System.out.println("Wildcards: " + WILDCARD_COUNT);
        setCharValues(charset, needle);
        printHaystackAndNeedle(haystack, needle);
        findNeedle(haystack, needle);
    }

    /***
     * Reads the file rather primitive. Since the files in this assigmnent
     * only consists of one line of one or more words, it is necessary
     * to just read that line.
     * @param fileName The name of the file to be read
     * @return String The word in the file just read.
     */
    private String readFile(String fileName) {
        try {
            return new Scanner(new File(fileName)).nextLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return "";
        }
    }

    /***
     * Searches through the needle in order to check how many wildcards
     * that are present.
     *
     * @param needle The needle containing the (possible wildcards)
     * @return int The number of wildcards in the needle.
     */
    private int findWildCards(char[] needle) {
        int counter = 0;

        for (int i = 0; i < needle.length; i++) {
            if (needle[i] == '_') {
                counter++;
            }
        }
        return counter;
    }
    
    /***
     * Just prints the haystack and the needle.
     *
     * @param haystack The haystack
     * @param needle The needle
     */
    private void printHaystackAndNeedle(char[] haystack, char[] needle) {
        System.out.println("******** HAYSTACK *******");
        for (int i = 0; i < haystack.length; i++) {
            if (i < 10) System.out.print(" " + 0+i + "|");
            else System.out.print(" " + i + "|");
        }
        System.out.println();
        for (int i = 0; i < haystack.length; i++) {
            System.out.print(" " + haystack[i] + " |");
        }
        System.out.println("\n");

        System.out.println("********* NEEDLE ********");
        for (int i = 0; i < needle.length; i++) {
            if (i < 10) System.out.print(" " + 0+i + "|");
            else System.out.print(" " + i + "|");
        }
        System.out.println();
        for (int i = 0; i < needle.length; i++) {
            System.out.print(" " + needle[i] + " |");
        }
        System.out.println("\n");

    }

    /***
     * Searches through the haystack using bad character shift, in addition to
     * taking into account wildcards in the needle. Prints out indexes where
     * char matches, needle matches and mismatches occurs. Continues the search
     * even if a needle already has been found, in order to detect multiple
     * matches in the haystack.
     *
     * @param needle The word to be found in the haystack
     * @param haystack The word to be searched through
     */
    private void findNeedle(char[] haystack, char[] needle) {
        int counter = 0, needlePos = NEEDLE_SIZE;

        System.out.println("** BAD CHARACTER SHIFT ** ");
        while (counter <= LIMIT) {

            //Match at char in needle
            if (needle[needlePos] == haystack[counter+needlePos] || needle[needlePos] == '_') {

                /***
                 * If the position of the needle is zero, a match in the haystack is found. This we
                 * can know because for every char match in the needle, the needlePos is decrementet
                 * by one (from its length-1). Therefore, when the needlePos is zero, we know that
                 * we are at the start of the needle, since a match has also been found at needle[0].
                 */
                if (needlePos == 0) {
                    System.out.println("\nNeedle match at haystack[" + (needlePos+counter) + "]!");
                    printMatch(needlePos+counter, needle.length);
                    needlePos = NEEDLE_SIZE;
                    counter += charset[haystack[NEEDLE_SIZE + counter]];

                } else {
                    System.out.println("\nChar match at haystack[" + (needlePos+counter) + "]!");
                }

                needlePos--;
            //Mismatch
            } else {

                System.out.println("\nMismatch at index [" + (counter+needlePos) + "] Character: '" +
                        haystack[counter+needlePos] + "'");
                /***
                 * If a mismatch is encountered, the shift is determined by the value in the charset-array.
                 * The index of the array is set by getting the value of the position of the last char in the needle,
                 * plus the needle size. This value us used as the index in the haystack, and the char value
                 * obtained from that index is used as the index in the charset to find the correct shift
                 * of the needle.
                 *
                 * Here, it is also necessary to checkif the wildcard comes at the next shift. In that case, shift
                 * 1 step back, instead of what's in the shift table, to avoid skipping
                 * the wildcard, and thereby missing a potential match in the haystack.
                */

                if (needlePos > 0 && needle[needlePos-1] == '_')
                    counter += 1;
                else
                    counter += charset[haystack[NEEDLE_SIZE + counter]];

                //Reset the needlePos so that the new search starts at the back of the needle
                needlePos = NEEDLE_SIZE;

            }
        }
    }

    /***
     * Just prints the sequence in the haystack from start to end
     * @param start The starting point in the haystack
     * @param end The ending point in the haystack
     */
    private void printMatch(int start, int end) {
        System.out.println("In haystack: ");
        for (int i = start; i < start+end; i++) {
            System.out.print("["+i+"]");
        }
        System.out.println();
        for (int i = start; i < start+end; i++) {
            if (i > 9)
                System.out.print("[ "+haystack[i]+"]");
            else
                System.out.print("["+haystack[i]+"]");

        }
        System.out.println();
    }

    /***
     * Sets the shift lengths in the charset-array, according to
     * needle content.
     *
     * @param charset The int array to contain the shift lengths
     * @param needle The char array containing the needle
     */
    private void setCharValues(int[] charset, char[] needle) {
        boolean allWildcards = false;

        System.out.println("****** SHIFT TABLE *****");
        System.out.println();

        for (int i = 0; i < charset.length; i++) {

            charset[i] = needle.length-WILDCARD_COUNT;
            /***
            * If the needle only contains wildcard, a check is needed to make
            * sure that the needle shifts under execution. With a needle size of
            * 3, and 3 wildcards, needle.lenght-WILDCARD_COUNT would be 0, meaning
            * that the needle never shifts on a new match. In this case, it it also not necesarry
            * to check the needle for allocating the shift table, so the method will just
            * return here.
            */
            if (charset[i] == 0) {
                allWildcards = true;
                charset[i] = 1;
            }
        }

        if (allWildcards) {
            System.out.println("The needle only contains wildcards! No need to construct the shift table.");
            return;
        }

        /***
         * Iterates the needle. Sets each char's shift length according to the distance that can be
         * jumped without skipping this char again. A consideration has to bee taken if there is wildcards
         * in the needle, so that no wildcards is skipped. Therefore, in addition to the standard of
         * subtracting the position of the char in the needle from the needle.length-1, one also
         * has to substract the number of wildcards in the needle to determine the shift length.
         *
         * Starting at the end to only set a chars value once (the one farthest to the right in the needle)
         * if it occurs multiple times in the needle.
         */

        System.out.println("INDEX\tCHAR\tSHIFT");
        for (int i = NEEDLE_SIZE-1; i >= 0; i--) {
            if (charset[(int) needle[i]] == needle.length-WILDCARD_COUNT && needle[i] != '_') {
                charset[(int) needle[i]] = NEEDLE_SIZE - i - WILDCARD_COUNT;

                if (charset[(int) needle[i]] == 0) {
                    charset[(int) needle[i]] = 1;
                }


                System.out.println(i + "\t\t" + needle[i] + "\t\t" + charset[(int) needle[i]]);
            }


        }

        System.out.println("Other\tOther\t" +  (needle.length-WILDCARD_COUNT)+"\n");

    }
}
