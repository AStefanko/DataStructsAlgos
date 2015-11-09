import java.util.*;

/*
 * This class encapsulates N words/strings so that the
 * group of N words can be treated as a key in a map or an
 * element in a set, or an item to be searched for in an array.
 * <P>
 * @author YOU,COMPSCI 201 STUDENT
 */

public class WordNgram implements Comparable<WordNgram> {

	private String[] myWords;
	private String str2;

	/*
	 * Store the n words that begin at index start of array list as the N words
	 * of this N-gram.
	 * 
	 * @param list contains at least n words beginning at index start
	 * 
	 * @start is the first of the N worsd to be stored in this N-gram
	 * 
	 * @n is the number of words to be stored (the n in this N-gram)
	 */
	public WordNgram(String[] list, int start, int n) {
		myWords = new String[n];
		System.arraycopy(list, start, myWords, 0, n);
	}
	
	public WordNgram(String[] myWords) {
		for (int i=0; i<myWords.length; i++) {
			this.myWords[i]=myWords[i];
		}

	}

	/**
	 * Return value that meets criteria of compareTo conventions.
	 * 
	 * @param wg
	 *            is the WordNgram to which this is compared
	 * @return appropriate value less than zero, zero, or greater than zero
	 */
	public int compareTo(WordNgram wg) {
		// TODO implement compareTo
		//For compareTo to work then these need to be strings 
		for(int i=0; i<myWords.length; i++) {
			String oneGram = this.myWords[i];
			String twoGram = wg.myWords[i];
			int com = oneGram.compareTo(twoGram);
			if (com != 0) {
				return com;
			}
		}
		return 0;
	}

	/**
	 * Return true if this N-gram is the same as the parameter: all words the
	 * same.
	 * 
	 * @param o
	 *            is the WordNgram to which this one is compared
	 * @return true if o is equal to this N-gram
	 */
	//Took this straight from the markov quiz 
	public boolean equals(Object o) {
		if (o==this) {
			return true;
		}
		if (o==null || o.getClass() != this.getClass()) {
			return false;
		}
		WordNgram other = (WordNgram) o;
		for (int k=0; k<myWords.length; k++) {
			if (!myWords[k].equals(other.myWords[k])) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Returns a good value for this N-gram to be used in hashing.
	 * 
	 * @return value constructed from all N words in this N-gram
	 */
	
	//This came from the quiz 
	public int hashCode() {
		int sum=0;
		for(int k=0; k<myWords.length; k++) {
			sum = 100*sum+myWords[k].hashCode();
		}
		return sum; 
	}
	
	//this worked! use instead of 
	public String wordAt() {
		int end = this.myWords.length-1;
		return this.myWords[end];
	}
	
	public String toString() {
		int start = 0;
		int k=0;
		WordNgram a = new WordNgram(myWords, start, k);
		StringBuilder build = new StringBuilder();
		for(k=0; k<a.myWords.length; k++ ) {
			build.append(a.myWords[k]);
		}
		return build.toString();
		
	}
}
