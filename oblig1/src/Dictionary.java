/***
 * @author Roar Hoksnes Eriksen
 */

import java.util.ArrayList;

/*
 * Handles the different lookup cases
 */
public class Dictionary {
	BinTree container;
	
	Dictionary(BinTree container) {
		this.container = container;
	}
	/*
	 * Checks the word against all four cases, and returns 
	 * a combined list of matches from each case
	*/
	public ArrayList<CaseInfo> getSimilarWords(String word) {
		ArrayList<CaseInfo> similarWords = new ArrayList<CaseInfo>();
		
		String lowerCaseWord = word.toLowerCase();
		String[] caseOne = similarCaseOne(lowerCaseWord);
		String[] caseTwo = similarCaseTwo(lowerCaseWord);
		String[] caseThree = similarCaseThree(lowerCaseWord);
		String[] caseFour = similarCaseFour(lowerCaseWord);
		
		/*

		System.out.println("CHECKING " + word + " TO SEE IF A LETTER HAS BEEN REPLACED");
		for (String oppgaver : caseTwo) {
			System.out.println(oppgaver);
		}
		
		System.out.println("CHECKING " + word + " TO SEE IF A LETTER HAS BEEN REMOVED");
		for (String oppgaver : caseThree) {
			System.out.println(oppgaver);
		}
		
		System.out.println("CHECKING " + word + " TO SEE IF A LETTER HAS BEEN ADDED");
		for (String oppgaver : caseFour) {
			System.out.println(oppgaver);
		}
		*/
		
		addSimilarWordsToList(caseOne, "Two letters next to each other have been switched", similarWords);
		addSimilarWordsToList(caseTwo, "One letter has been replaced with another", similarWords);
		addSimilarWordsToList(caseThree, "One letter has been removed", similarWords);
		addSimilarWordsToList(caseFour, "One letter has been added in front, or at the end, or somewhere in between", similarWords);
	
		return similarWords;
	}
	
	/*
	 * Gets an array of potential candidates of similar words, and checks if these candidates
	 * is in the dictionary. If so, they are added to the list to be returned.
	*/
	public void addSimilarWordsToList(String[] candidates, String lookupCase, ArrayList<CaseInfo> list) {
		if (candidates != null)
		for (String s : candidates) {
			if (container.contains(s) && !list.contains(s)) {
				list.add(new CaseInfo(s, lookupCase));
			}
		}
	}
	
	/*
	 * A word identical to X, except that two letters next to each other have been switched. 
	 * (Example search term: laek; generated words: alek, leak, lake; where leak and lake are 
	 * examples of English words that are generated)
	 * 
	 * This is the method provided from the assignment
	*/
	public String[] similarCaseOne(String word) {
		char[] word_array = word.toCharArray();
	    char[] tmp;
	    String[] words = new String[word_array.length-1];
	    for(int i = 0; i < word_array.length - 1; i++){
	        tmp = word_array.clone();
	        words[i] = swap(i, i+1, tmp);
	    }
	    return words;
	}
	
	/*
	 * This is the method provided from the assignment
	*/
	public String swap(int a, int b, char[] word){
	    char tmp = word[a];
	    word[a] = word[b];
	    word[b] = tmp;
	    return new String(word);
	}
	
	/*
	 * From the assignment: A word identical to X, except one letter has been replaced with another. 
	 * (Example search term: laak; generated words: aaak, baak, ..., laaz; where leak 
	 * is an example of an English word that is generated)
	 * 
	 * Uses the alphabet to replace each char in the word with a letter from the alphabet at every index of the word.
	 * Returns a complete list of all possible combinations of this.
	*/
	public String[] similarCaseTwo(String word) {
		char[] alphabet = "abcdefghijklmnopqrstuvwxyzæøå".toCharArray();
		char[] word_array = word.toCharArray();
		char[] tmp;
		ArrayList<String> candidates = new ArrayList<String>();
		
		for (int i = 0; i < word_array.length; i++) {
			for (int j = 0; j < alphabet.length; j++) {
				tmp = word_array.clone();
				candidates.add(replace(alphabet[j], tmp, i));
			}
		}
		String[] toRet = new String[candidates.size()]; 
		return candidates.toArray(toRet);
	}
	
	/*
	 * Replaces the char at the word array'oppgaver index with a new char
	*/
	public String replace(char newcomer, char[] word, int index) {
		word[index] = newcomer;
		return new String(word);
	}
	
	/*
	 * From the assignment: A word identical to X, except one letter has been removed. (Example search term: lek; 
	 * generated words: alek, laek, leak, ..., lekz; where leak and leek are examples of English 
	 * words that are generated)
	 * 
	 * Uses the alphabet to insert or append a letter from the alphabet to the word at every index of the word.
	 * Have to go "outside" the loop at the last iteration (length+1) to be able to append at the end of the word
	 * Returns a complete list of all possible combinations of this.
	*/
	public String[] similarCaseThree(String word) {
		char[] alphabet = "abcdefghijklmnopqrstuvwxyzæøå".toCharArray();
		char[] word_array = word.toCharArray();
		
		ArrayList<String> candidates = new ArrayList<String>();
		
		for (int i = 0; i < word_array.length+1; i++) {
			for (int j = 0; j < alphabet.length; j++) {
				char[] tmp = word_array.clone();
				candidates.add(insertOrAppend(alphabet[j], tmp, i));
			}
		}
		String[] toRet = new String[candidates.size()-1]; 
		return candidates.toArray(toRet);
	}
	
	/*
	 * Uses StringBuilder to manipulate the word. Either appends the replacer-char at the end 
	 * of the word or inserts it at index[index] of the word.
	*/
	private String insertOrAppend(char replacer, char[] word, int index) {
		StringBuilder newWord = new StringBuilder(new String(word));
		
		if (index < word.length) {
			newWord.insert(index, replacer);
		} else if (index == word.length) {			
			newWord.append(replacer);
		}
		
		return new String(newWord);
	}
	
	/*
	 * From the assignment: A word identical to X, except one letter has been added in front, or at the end, or 
	 * somewhere in between. (Example search term: leaak; generated words: eaak, laak, leak, leaa; 
	 * where leak is an English word that is generated)
	 * 
	 * Uses the alphabet to remove each char in the word at every index.
	 * Returns a complete list of all possible combinations of this.
	*/
	public String[] similarCaseFour(String word) {
		char[] word_array = word.toCharArray();
		char[] tmp;
		ArrayList<String> candidates = new ArrayList<String>();
		
		for (int i = 0; i < word_array.length; i++) {
			tmp = word_array.clone();
			candidates.add(remove(tmp, i));
			
		}
		String[] toRet = new String[candidates.size()]; 
		return candidates.toArray(toRet);
	}
	
	/*
	 * Uses StringBuilder to remove the char at index [index] in the char array, and returns a new String with
	 * that char removed
	*/
	public String remove(char[] word, int index) {
		StringBuilder newWord = new StringBuilder(new String(word));
		
		newWord.deleteCharAt(index);
		
		return new String(newWord);
	}
}
