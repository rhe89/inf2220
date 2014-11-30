/***
 * @author Roar Hoksnes Eriksen
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Oblig1 {
	private String fileName = "ordbok1_utf.txt";
	private BinTree container;
	private Dictionary dictionary;
	private Scanner reader;
	
	public static void main(String[] args) {
		new Oblig1();
	}
	
	Oblig1() {
		container = new BinTree();
		dictionary = new Dictionary(container);
        System.out.println(new File("").getAbsolutePath());
		try {
			initReader();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		new StatisticsReport(container, dictionary);
		initGUI();
	}
	private void initGUI() {
		reader = new Scanner(System.in);
		String find = "";
		while (true) {
			System.out.println("\nPlease enter the word you want to find: (type 'q' and press 'Enter' to exit");					
			find = reader.next().toLowerCase();
			if (!find.equals("q"))
				findWord(find);
			else {
				System.out.println("Exiting"); System.exit(0);
			}
		}
	}
	/*
	 * Reads the file using Scanner, uses the skip variable to skip spaces.
	 * Inserts each word to the tree once read.
	 */
	private void initReader() throws FileNotFoundException{
		File file = new File(fileName);
		reader = new Scanner(file);		
		while (reader.hasNextLine()) {
			String word = reader.next();
			container.insert(word);
		}

		container.setHeightOfEachNode();
		container.setDepthOfEachNode();
		container.setDepthOfTree();
		
		container.remove("familie");
		container.insert("familie");

		container.setHeightOfEachNode();
		container.setDepthOfEachNode();
		container.setDepthOfTree();
	}
	private void findWord(String word) {
			try {
				Integer.parseInt(word);
				System.out.println("Please type something consisting of letters. This is a dictionary!");
				return;
			} catch (NumberFormatException e) {}
			
			boolean contains = container.contains(word);
			if (contains) {
				System.out.println(word + " was found!\n");
			} else {
				System.out.println(word + " was not found!\nGetting similar words\n");
				getSimilarWords(word);
			}

	}
	/*
	 * Gets the similar words from the container, and prints them to the terminal.
	 */
	private void getSimilarWords(String word) {
		long startSearch = System.currentTimeMillis();
		ArrayList<CaseInfo> similarWords = dictionary.getSimilarWords(word);
		
		System.out.println("Printing similar words ("+similarWords.size()+" positive lookups)");
		for (CaseInfo s : similarWords) {
			System.out.println(" " + s + ". Case: " + s.getCase());
		}
		long endSearch = System.currentTimeMillis();
		long runTime = endSearch-startSearch;
		System.out.println("Runtime for the search (in millis): " + runTime);
	}
	/*
	 * Used for testing purposes.
	 */
	@SuppressWarnings("unused")
	private void testInsert() {
		container.insert("I");
		container.insert("E");
		container.insert("M");
		container.insert("C");
		container.insert("G");
		container.insert("K");
		container.insert("O");
		container.insert("B");
		container.insert("D");
		container.insert("F");
		container.insert("H");
		container.insert("J");
		container.insert("L");
		container.insert("N");
		container.insert("P");
		container.insert("A");
		
		container.setHeightOfEachNode();
		container.setDepthOfEachNode();
		container.setDepthOfTree();
		
		container.remove("O");
		container.print();
		System.out.println("-----------");
		
		container.insert("O");
		container.print();
		//container.insert("Q");
	}
	
		
}