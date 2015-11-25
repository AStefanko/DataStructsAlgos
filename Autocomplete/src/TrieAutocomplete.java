import java.util.ArrayList;

import java.util.Collections;
//import java.util.HashMap;
import java.util.PriorityQueue;

//import packag.Node;
//import packag.each;

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
	//private HashMap<String, Double> intree;

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

		Node curr = myRoot;
		char[] cc = word.toCharArray();
		for(char ch : cc) {
			if(curr.mySubtreeMaxWeight<weight) {
				curr.mySubtreeMaxWeight=weight;
				//System.out.println(weight);
			}

			if(!curr.children.containsKey(ch)) {
				curr.children.put(ch, new Node(ch, curr, weight));
				//System.out.println("ch: " + ch);
			}
			//System.out.println("curr.tostring: "+curr.toString());
			curr=curr.children.get(ch);
			//System.out.println("Add");

		}
		

		curr.isWord=true;
		curr.setWeight(weight);
		curr.setWord(word);		
		curr.mySubtreeMaxWeight=weight;

		
		//System.out.println("curr.tostring: " + curr.toString());
		if(curr.mySubtreeMaxWeight<weight) {
			curr.mySubtreeMaxWeight=weight;
			//System.out.println("IFFFFF");
		}
		
		if(curr.mySubtreeMaxWeight>weight) {
			while(curr!=null) {
				curr=curr.parent;
				double maxS=curr.myWeight;
				//System.out.println("IN PARENT THING");
				for(Node babay: curr.children.values()) {
					if(babay.mySubtreeMaxWeight>maxS) {
						maxS=babay.mySubtreeMaxWeight;
					}
				}
				//System.out.println("last");
				
				curr.mySubtreeMaxWeight=maxS;
				
			}
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
		if(prefix==null) {
			throw new NullPointerException();
		}
		Node curr=myRoot;
		
		for(char ch: prefix.toCharArray()){
			if(!curr.children.containsKey(ch)) {
				return new String[0];
			}
			curr=curr.children.get(ch);
		}
		if(k==0) {
			return new String[0];
		}
		
		PriorityQueue<Node> pq = new PriorityQueue<Node>(new Node.ReverseSubtreeMaxWeightComparator()); //whatdo i do here? How do i initialize the node?
		ArrayList <Node> lst = new ArrayList<Node>();
		pq.add(curr);
		while(!pq.isEmpty() || (lst.size()>0 && curr.myWeight>lst.get(lst.size()-1).myWeight)) {
			//if(!lst.isEmpty() && curr.myWeight<Math.min(a, b))
			curr=pq.poll();
			if(curr.isWord) {
				lst.add(curr);
			}
			//System.out.println("While loop in topKMatches");
			//if(!lst.isEmpty() && curr.myWeight<Math.min(a, b))
			for(Node babay: curr.children.values()) {
				pq.add(babay);
			//loop through children
				//add them to queue
			}
		}
		Collections.sort(lst, Collections.reverseOrder()); 
		int inn=0;
		if(k>lst.size()) {
			inn=lst.size();
		} else{
			inn=k;
		}
		String[] fin= new String[inn];
		for(int i=0; i<inn; i++) {
			fin[i]=lst.get(i).getWord();
		}
		return fin;

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
		if(prefix==null) {
			throw new NullPointerException();
		}
		Node curr = myRoot;
		for(Character key: prefix.toCharArray()) {
			if(!curr.children.containsKey(key)){
				return "";
			}
			curr=curr.children.get(key);
		}
		
		while(curr.mySubtreeMaxWeight!=curr.myWeight) {

		//	System.out.println(curr.mySubtreeMaxWeight);
		//	System.out.println(curr.myWeight);
			
		//	System.out.println(curr.children.values().size());
			
			for(Node babay: curr.children.values()) {
			//	System.out.println(babay.mySubtreeMaxWeight);
				if(curr.mySubtreeMaxWeight==babay.mySubtreeMaxWeight){
					curr=babay;
					break;
				}
			}
		}
		
		//System.out.println("curr.getWord in topmatch: " + curr.getWord());

		return curr.getWord(); 
	}

}
