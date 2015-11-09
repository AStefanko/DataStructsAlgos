
public class QuickUWPC implements IUnionFind {
	private int[] myID;
	private int myComponents;
	private int[] size;
	
	public QuickUWPC() {
		myID=null;
		myComponents=0;
		size=null;
	}
	
	public QuickUWPC(int N) {
		initialize(N);
	}
	
	public void initialize(int n) {
		myComponents=n;
		size= new int[n];
		myID= new int[n];
		for(int i=0; i<n; i++) {
			myID[i]=i;
			size[i]=1;
		}
	}
	
	public int components() {
		return myComponents;
	}
	
	public int find(int p) {
		validate(p);
		while(p!=myID[p]) {
			p=myID[p];
		}
		return p;
	}
	
	public void validate(int p) {
		int l1=myID.length;
		if(p<0 || p>=l1) {
			throw new IndexOutOfBoundsException("index" + p + "is not between" +(l1-1));
		}
	}
	
	public boolean connected(int p, int q) {
		return find(p)==find(q);
	}
	
	public void union(int p, int q) {
		int rootP=find(p);
		int rootQ=find(q);
		if(rootP==rootQ) {
			return;
		}
		if(size[rootP]<size[rootQ]) {
			myID[rootP]=rootQ;
			size[rootQ]+=size[rootP];
		} else {
			myID[rootQ]=rootP;
			size[rootP]+=size[rootQ];
		}
		myComponents--;
	}
	
	
	

}
