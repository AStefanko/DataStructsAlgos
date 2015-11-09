import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

import packag.Node;
import packag.each;

/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 *
 */

//Questions:
//How do i iterate through the children of a thing?
//Is there a 'haschildren' thing?
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;
	private HashMap<String, Double> intree;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws a
	 *             NullPointerException if either argument is null
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 * 
	 */
	private void add(String word, double weight) {
		// TODO: Implement add
		if(word==null) {
			throw new java.lang.NullPointerException();
		}
		if(weight<0) {
			throw new java.lang.IllegalArgumentException();
		}
		//if weights is zero, then make the weight the weight 
		//and the subtree weight the weight 
		//if you're adding a bigger word, then follow and change all the pointers
		double max=0.0;
		Node curr = myRoot;
		for(int i=0; i<word.length(); i++) {
			Node babay=curr.children.get(word.charAt(i));
			if(curr.mySubtreeMaxWeight<weight) {
				curr.mySubtreeMaxWeight=weight;
			}
			if(babay==null || !curr.containsKey(word.charAt(i))); {
				curr.children.put(word.charAt(i), new Node(word.charAt(i), curr, weight));
			}
			if(babay.myWeight>max) {
				max=babay.myWeight;
			}
			
			curr=curr.children.get(word.charAt(i));
			
		}
		
		if(intree.containsKey(word)) {
			if(intree.get(word) > weight) {
				for(int i=0; i<word.length(); i++) {
					if(curr.mySubtreeMaxWeight>weight) {
						curr.mySubtreeMaxWeight=weight+max;
					}
					curr=curr.parent;
				}
			}
		}
		intree.put(word, weight);
		curr.isWord=true;
		curr.myWeight=weight;
		curr.myWord=word;
		if(curr.mySubtreeMaxWeight<weight) {
			curr.mySubtreeMaxWeight=weight;
		}
	}

	@Override
	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An array of the k words with the largest weights among all words
	 *         starting with prefix, in descending weight order. If less than k
	 *         such words exist, return an array containing all those words If
	 *         no such words exist, return an empty array
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public String[] topKMatches(String prefix, int k) {
		// TODO: Implement topKMatches
		PriorityQueue<Node> pq = new PriorityQueue<Node>();; //whatdo i do here? How do i initialize the node?
		ArrayList <String> lst = new ArrayList<String>();
		Node curr=myRoot;
		for(int i=0; i<prefix.length(); i++) {
			Node babay=curr.children.get(prefix.charAt(i));
			curr=babay;
		}
		pq.add(curr);
		while(pq!=null || lst.size()<k) {
			curr=pq.poll();
			if(curr.isWord) {
				lst.add(curr.myWord);
			}
			for(each cild of curr) {
				pq.push(curr);
			}
		}
		Collections.sort(lst);
		String[] finna = new String[k];
		for(int i=0; i<k; i++) {
			finna[i]=lst.get(i);
		}
		return finna;
		
	}

	@Override
	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from _terms with the largest weight starting with
	 *         prefix, or an empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 * 
	 */
	public String topMatch(String prefix) {
		// TODO: Implement topMatch
		Node curr = myRoot;
		while(curr.mySubtreeMaxWeight!=curr.myWeight) {
			for(int i=0; i<prefix.length(); i++) {
				if(curr.children.get(prefix.charAt(i)).mySubtreeMaxWeight==curr.mySubtreeMaxWeight) {
					curr=curr.children.get(prefix.charAt(i));
					break;
				}
			}
		}
		return curr.myWord;
	}
	}

}
