/***
 * @author Roar Hoksnes Eriksen
 */

/*
 * Used to see in which of the four cases the word was found. Just for fun.
*/
class CaseInfo {
	private String word, lookupCase;
	
	CaseInfo(String word, String lookupCase) {
		this.word = word;
		this.lookupCase = lookupCase;
	}
	public String toString() {
		return word;
	}
	public String getCase() {
		return lookupCase;
	}
}
