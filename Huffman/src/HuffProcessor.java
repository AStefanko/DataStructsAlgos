import java.util.PriorityQueue;


public class HuffProcessor implements Processor {
	
	//WHAT IS WRONG: IT SAYS THAT THE END BIT DOESN'T MATCH 
	int[] countArray= new int[257]; //0th index is # of zeros, 1st is 1s, 97th is # of a's
	String[] nodeVals = new String[257];
	HuffNode myRoot;
	
	public void compress(BitInputStream in, BitOutputStream out) {
		countArray=countChars(in);
		
		myRoot=HuffTree(in);
		TraverseExtract(myRoot, ""); //there is a problem for sure with traverse extract
		out.writeBits(BITS_PER_INT, HUFF_NUMBER);
		for(int i=0; i<nodeVals.length; i++) {
			if(nodeVals[i]!=null) {
				System.out.println("nodevals[i]: " + nodeVals[i]);
				System.out.println("i: " + i);
			}
		}
		writeHeader(myRoot, out);
		compWrite(in, out); //IS THE ORDER WRONG?
		//IS THIS PART OF IT WRONG 
		out.writeBits(nodeVals[PSEUDO_EOF].length(), Integer.parseInt(nodeVals[PSEUDO_EOF], 2)); //writing the pseudoEOF
		

	}
	
	public int[] countChars(BitInputStream in) {
		int[] temp = new int[256];
		int toRead;

		//SHOULD I READ THIS IN ONE BIT AT A TIME OR 8
		while((toRead=in.readBits(8))!=-1) {
			temp[toRead]++; //in.readBits returns an int
			//System.out.println("toRead: " + toRead);
		}//make sure you aren't calling in.ReadBits more than once 
		in.reset();
//		for(int i=0; i<temp.length; i++) {
//			System.out.println("temp[i] in countChars: " + temp[i]);
//		}
		return temp;
	}
	public HuffNode HuffTree(BitInputStream in) {
		//avoid all characters that are 0
		//every i is a character
		//for each char whose frequency isn't 0, create a huffnode 
		PriorityQueue<HuffNode> pq = new PriorityQueue<HuffNode>();
		for(int i=0; i<256; i++) {
			if(countArray[i]!=0) { //if it's zero, don't add that ish 
				HuffNode toAdd= new HuffNode(i, countArray[i]); //IS THIS THE RIGHT WEIGHT?
				System.out.println("countArray[i]: " + countArray[i]);
				pq.add(toAdd);
			} else {
				continue;
			}
		}
		HuffNode psEOF = new HuffNode(PSEUDO_EOF, 0);
		pq.add(psEOF); //DOES THE EOF THING LOOK OK? 
		
		//another loop that goes all the way down to a node
		while(pq.size()> 1) {
			HuffNode tiniest = pq.poll(); //remove smallest node
			HuffNode secondTiniest=pq.poll(); //remove secondsmallest Node
			HuffNode mergeNode = new HuffNode(-1, tiniest.weight() + secondTiniest.weight(), tiniest, secondTiniest);
			pq.add(mergeNode); //add new node to PQ
			//poll two smallest nodes 
			//combine them into a new huffnode 
			//this is where you use the second huffnode constructor 
			//set left and right subtrees euqal to the things you just polled
			//set the value to be -1, weight is sum of weights of 2 babies
			//add new huffnode into priosity queueueueu
		}
		
		HuffNode huffer=pq.poll();
		return huffer;
	}
	
	//i think there is something wrong with this but I'm not sure what it is. 
	
	public void TraverseExtract(HuffNode curr, String path) {
		//my thing is telling me the first thing is a leaf, but that isn't right 
		if(curr.left()==null && curr.right()==null) { //if thing is a leaf
			nodeVals[curr.value()]=path; 
			System.out.println("curr.value: " + curr.value());
			System.out.println("nodeVals[curr.value]: " + nodeVals[curr.value()]);
			//this is returning an empty string?
			return;
		}
			
		
		System.out.println("path: " + path);
		TraverseExtract(curr.left(), path+"0"); //IS IT OK TO HAVE NODEVALS IN THIS 
		TraverseExtract(curr.right(), path+"1");
		
	}
	//Level of confidence about this one: 8
	public void writeHeader(HuffNode curr, BitOutputStream out) {
		if(curr.left()==null && curr.right()==null) { //if it's a leaf 
			out.writeBits(1, 1); //single bit, value 1
			out.writeBits(9, curr.value());
			return;
		}
//		if(curr.value()==PSEUDO_EOF) { //IS THIS USEFUL?
//			return;
//		}
		out.writeBits(1, 0);
		writeHeader(curr.left(), out);
		writeHeader(curr.right(), out);
	}
	
	public void compWrite(BitInputStream in, BitOutputStream out) {
		int inp; //=in.readBits(8);
		while((inp=in.readBits(8))!=-1) {
			String code = nodeVals[inp];
			//System.out.println("code: "+ code);
			out.writeBits(code.length(), Integer.parseInt(code, 2));
			//inp=in.readBits(8);
		}
	}
	
	public void decompress(BitInputStream in, BitOutputStream out) {
		if(in.readBits(32)!=HUFF_NUMBER) {
			throw new HuffException("Huff Number isn't there");
		}
		myRoot = readHeader(in); //WAS THIS THE RIGHT THING TO DO? 
		HuffNode curr=myRoot; //AGAIN SOMETHING IS WRONG WITH MYROOT
		int chinga;
		while((chinga=in.readBits(1))!=-1) {
			if(chinga==1) {
				curr=curr.right();
			} else {
				curr=curr.left();
			}
			if(curr.left()==null && curr.right()==null) {
				if(curr.value()==PSEUDO_EOF) { //then you at EOF
					return;
				} else {
					out.writeBits(8, curr.value());
					//chinga=in.readBits(1);
					curr=myRoot; //WHY IS THIS A THING
				}
			}
		}
		throw new HuffException("Problem with Pseudo-EOF");
		
	}
	
	public HuffNode readHeader(BitInputStream in) {
		int f = in.readBits(1);
		if(f==0) { //AM I NOT UPDATING THIS CORRECTLY 
			HuffNode left = readHeader(in);
			HuffNode right = readHeader(in);
			HuffNode newN = new HuffNode(-1, 0, left, right);
			return newN;
		} else {
			HuffNode sec = new HuffNode(in.readBits(9), 0);
			return sec;
		}
	}
	

	
	//stopped at 1:12:02


}
