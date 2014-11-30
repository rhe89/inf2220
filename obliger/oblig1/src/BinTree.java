/***
 * @author Roar Hoksnes Eriksen
 */

public class BinTree {
	private Node root;
	public int nodeCountAtLevels[], nodeCount, totDepth, maxDepth;
	
	public void insert  (String word) {
		String lowerCaseWord = word.toLowerCase();
		if (root == null) {
			root = new Node(lowerCaseWord);
			nodeCount++;
		} else {
			nodeCount++;
			root.insert(lowerCaseWord);
		}
	}
	public void print() {
		if (root != null)
			root.print();
	}
	public boolean validateTree() {
		if (root == null) 
			return true;
		else 
			return root.validateTree();
	}
	
	public boolean contains(String word) {
		String lowerCaseWord = word.toLowerCase();
		if (root == null) 
			return false;
		else 
			return root.contains(lowerCaseWord);
	}
	public double getAvgDepth() {
		return totDepth/nodeCount;	
	}

	/*
	 * The case to solve here instead of in the Node class is if the root is
	 * the node to be removed in a non-empty tree. Then the smallest
	 * value in the roots right subtree has to be set as the new root,
	 * and that value has to be removed from the right subtree using recursion
	 */
	public boolean remove(String word) {
		String lowerCaseWord = word.toLowerCase();
		if (root == null) {
			throw new NullPointerException();
		} else if (root.word.equals(lowerCaseWord)) {
			root.word = root.right.getFirstWord();
			root.right.remove(root.word, null);
			return false;
		} else {
			return root.remove(lowerCaseWord, null);
		}
	}
	
	/*
	 * Traverses the tree, and sets the tree'oppgaver depth. Then declares an array at size of the depth of the entire tree (the longest
	 * path from root to null) 
	*/
	public void setDepthOfTree() {
		if (root == null)
			throw new NullPointerException();
		else 
			root.setDepthOfTree();
		
		if (nodeCountAtLevels == null)
			nodeCountAtLevels = new int[maxDepth+1];
		
		root.setNodeCountAtMyLevel();
	}

	public void setDepthOfEachNode() {
		if (root == null) 
			throw new NullPointerException();
		else 
			root.setDepthOfNode(0);
	}
	
	public void setHeightOfEachNode() {
		if (root == null) 
			throw new NullPointerException();
		else 
			root.getHeight();
		
	}
	public int getDepthOfTree() {
		return maxDepth;
	}
	public int getNodeCount() {
		return nodeCount;
	}
	public int [] getNodeCountAtAllLevels() {
		return nodeCountAtLevels;
	}

	public String getFirstWord() {
		if (root == null) 
			throw new NullPointerException();
		else 
			return root.getFirstWord();
	}
	
	public String getLastWord() {
		if (root == null) 
			throw new NullPointerException();
		else 
			return root.getLastWord();
	}
	
	
	private class Node {
		private String word;
		private Node right, left;
		private int height, depth;
		private boolean hasBeenHere = false;
	
		Node(String word) {
			this.word = word;
		}
		/*
		 * Checks if each child is maintaining the conditions for a binary search tree
		 */
		private boolean validateTree() {
			if (left != null)
				left.validateTree();
			if (right != null)
				right.validateTree();
			
			if (left != null && word.compareTo(left.word) < 0) 
				return false;
			else if (right != null && word.compareTo(right.word) > 0)
				return false;
			else 
				return true;
			
		}
		/*
		 * Traverses the tree until left == null is reached, meaning
		 * that the smallest (or first) word in the dictionary has been 
		 * found
		*/
		private String getFirstWord() {
			if (left != null) 
				return left.getFirstWord();
			
			return word;
		}
		/*
		 * Traverses the tree until right == null is reach, meaning
		 * that the largest (or last) word in the dictionary has been 
		 * found
		*/
		private String getLastWord() {
			if (right != null) 
				return right.getLastWord();
			
			return word;
		}
		/*
		 * Checks the size of the incoming word. If it is smaller/larger
		 * than the current node'oppgaver word, it either creates a new
		 * left/right-node for the parent if it doesn't already exists, 
		 * or calls on the left/right node'oppgaver insert()-method until a
		 * null is reached in either the right or left node, meaning that
		 * this is the right place for the incoming word. (In this tree,
		 * duplicates/equal values is skipped)
		*/
		private void insert(String newWord) { 
			if (newWord.compareTo(word) < 0) {
				if (left == null) 
					left = new Node(newWord);
				else 
					left.insert(newWord);
				
			} else if (newWord.compareTo(word) > 0) {
				if (right == null) 
					right = new Node(newWord);
				else 
					right.insert(newWord);
				
			}
		}
		/*
		 * Checks if root is null, meaning that the search has come to
		 * an end without getting the right node. If the word searched for is 
		 * smaller than the current node'oppgaver word, it checks the left node, and visa
		 * versa if the word is larger. If the word searched for equals the current 
		 * node'oppgaver word, true is returned indicating that a match has been found.
		*/
		private boolean contains(String search) {
			if (word.compareTo(search) > 0) {
				if (left != null)
					return left.contains(search);
				else
					return false;
				
			} else if (word.compareTo(search) < 0) {
				if (right != null)
					return right.contains(search);
				else
					return false;
				
			} else 
				return true;
		}
		/*
		 * Traverses the tree recursively. When the bottom of each nodes subtree is reached
		 * (left and right is null), the method will start returning and
		 * increasing (by the +1 after the method call) each nodes height at the same time.
		 */
		private int getHeight() {
			if (left != null) {
				int newHeight = left.getHeight()+1;
				
				if (newHeight > height)
					height = newHeight;	
			}
			
			if (right != null) {
				int newHeight = right.getHeight()+1;
				
				if (newHeight > height)
					height = newHeight;
			}
			
			return height;
		}
		/*
		 * Somewhat equal to getHeight, except that the value is passed as an argument for each
		 * pass through a node, so that each node has an updated depth from its parent as the 
		 * traversing continues downwards the tree. Also increases the sum of all depths in the tree.
		 */
		private void setDepthOfNode(int newDepth) {
			depth = newDepth;
			
			if (left != null) 
				left.setDepthOfNode(newDepth+1);
			
			if (right != null)
				right.setDepthOfNode(newDepth+1);
		}
		/*
		 * Increases the number of nodes at the level/depth this node is on.
		 */
		private void setNodeCountAtMyLevel() {
			if (!hasBeenHere)
				nodeCountAtLevels[depth]++; hasBeenHere = true;
			
			if (left != null)
				left.setNodeCountAtMyLevel();
			
			if (right != null) 
				right.setNodeCountAtMyLevel();
		}
		/*
		 * Sets the max depth of the tree. Used to declare the array containing all
		 * the levels in the tree. Uses the boolean in case an update of the tree'oppgaver depth
		 * has to be made (to avoid setting each nodes depth twice in the levelcount-array)
		 */
		private void setDepthOfTree() {
			if (depth > maxDepth)
				maxDepth = depth;
			
			if (!hasBeenHere)
				totDepth += this.depth; 
			
			if (left != null) 
				left.setDepthOfTree(); 
			
			if (right != null)
				right.setDepthOfTree();
		}
		
		private boolean remove(String search, Node daddy) {
			//Search is smaller than t'oppgaver word. Check the left subtree, or no match
			if (search.compareTo(word) < 0) {
				if (left != null)
					return left.remove(search, this);
				return false;
			//Search is bigger than t'oppgaver word. Check the right subtree, or no match
			} else if (search.compareTo(word) > 0) {
				if (right != null)
					return right.remove(search, this);
				return false;
			//Match
			} else {
				/* If left or right is null, aka one or both have no children, 
				 * check if the current object
				 * equals the parents right or left child. If that'oppgaver the
				 * case, considering that we have a match, just 
				 * "remove me from the tree, and set my parents left/right child to be my child".
				 * If both left and right have children, get the smallest value
				 * in the right subtree, and then remove that value (incl. the node)
				 * from the right subtree.
				 */
				if (left == null || right == null) {
					if (daddy.left == this) {
						if (left != null) 
							daddy.left = left;
						else 
							daddy.left = right;
					} else if (daddy.right == this) {
						if (right != null)
							daddy.right = right;
						else 
							daddy.right = left;
					}
				} else {
					word = right.getFirstWord();
					right.remove(word, this);
				}
				return true;
			}
		}
		private void print() {
			if (left != null)
				left.print();
			
			System.out.println(word);
			
			if (right != null)
				right.print();
		}
	}
	
}