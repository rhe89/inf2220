import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Roar on 28.10.14.
 */
public class Oblig3 {
    private String haystack, needle;

    public static void main(String[] args) {


        if (args.length == 2) {
            new Oblig3(args[0], args[1]);
        } else {
            System.out.println("java Oblig3: none or invalid arguments");
            System.out.println("usage: java Oblig3 [haystack] [needle]");
            System.exit(1);
        }
    }

    Oblig3(String haystack, String needle) {
        this.haystack = getTextInFile(haystack);
        this.needle = getTextInFile(needle);
    }

    private String getTextInFile(String file) {
        try {
            return new Scanner(new File(file)).nextLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return "";
        }

    }
}
