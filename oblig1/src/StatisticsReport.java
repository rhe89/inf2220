/***
 * @author Roar Hoksnes Eriksen
 */

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/*
 * Prints the required statistics from the assignemnt to file
 */
public class StatisticsReport {
	private BinTree container;
	private Dictionary dictionary;
	private BufferedWriter writer = null;
	
	StatisticsReport(BinTree container, Dictionary dictionary) {
		this.container = container;
		this.dictionary = dictionary;
		printReportFile();
	}
	
	/*
	 * The following three methods is used to print the required searches and statistics from the assignment.
	 */
	private void printReportFile() {
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("utfil.txt"), "utf-8"));
			
			printTreeStatisticsToFile();
			printWordSearchStatsToFile("tterfølger");
			printWordSearchStatsToFile("Etterfølgern");
			printWordSearchStatsToFile("Etterfolger");
			printWordSearchStatsToFile("Eterfølger");
			printWordSearchStatsToFile("Etterfølger");
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void printWordSearchStatsToFile(String word) throws IOException {
		
		long startSearch = System.currentTimeMillis();
		writer.write("\nWord searched for: ");
		writer.write(word);
		
		if (container.contains(word)) {
			long endSearch = System.currentTimeMillis();
			long runTime = endSearch-startSearch;
			writer.write("\nThe word was found in the dictionary! Runtime for search (not included writing to file): " + runTime + "\n");
		} else {
			ArrayList<CaseInfo> similarWords = dictionary.getSimilarWords(word);
			long endSearch = System.currentTimeMillis();
			long runTime = endSearch-startSearch;
				
			writer.write("\nThe word wasn't found in the dictionary. Printing similar words ("+similarWords.size()+" positive lookups)\n");
			for (CaseInfo s : similarWords) {
				writer.write(" " + s +  ". Case: " + s.getCase() +"\n");
			}
			writer.write("Runtime for search and generating similar words (not included writing to file): " + runTime + "\n");
		
		}
	}
	private void printTreeStatisticsToFile() throws IOException {
		writer.write("Depth of the tree after insertion of " + container.getNodeCount() + " words: ");
		writer.write(container.getDepthOfTree()+"\n");
		writer.write("Optimal depth (log "+container.getNodeCount()+"): " + Math.log(container.getNodeCount()));
		
		writer.write("\nAverage depth of all nodes: ");
		writer.write(container.getAvgDepth()+"\n");
		
		writer.write("\nNumber of nodes per depth of the tree: \n");
		int [] levels = container.getNodeCountAtAllLevels();
		for (int i = 0; i < levels.length; i++) {
			writer.write("Depth: " + i + ", number of nodes: " + levels[i]+"\n");			
		}

		writer.write("\nFirst word in dictionary: ");
		writer.write(container.getFirstWord()+"\n");
		
		writer.write("Last word in dictionary: ");
		writer.write(container.getLastWord()+"\n");
		
	}
}
