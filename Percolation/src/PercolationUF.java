import java.util.Arrays;


//import packag.IUnionFind;
//import packag.QuickUWPC;

/**
 * Simulate a system to see its Percolation Threshold, but use a UnionFind
 * implementation to determine whether simulation occurs. The main idea is that
 * initially all cells of a simulated grid are each part of their own set so
 * that there will be n^2 sets in an nxn simulated grid. Finding an open cell
 * will connect the cell being marked to its neighbors --- this means that the
 * set in which the open cell is 'found' will be unioned with the sets of each
 * neighboring cell. The union/find implementation supports the 'find' and
 * 'union' typical of UF algorithms.
 * <P>
 * 
 * @author Owen Astrachan
 * @author Jeff Forbes
 *
 */





public class PercolationUF implements IPercolate {
	public int[][] myGrid;
	private int N;
	
	private int myRows;
	private int myCols;
	
	public static final int BLOCKED = 0;
	public static final int OPEN = 1;
	public static final int FULL = 2;
	
	private final int OUT_BOUNDS = -1;
	
	private final int SOURCE;
	private final int SINK;
	IUnionFind myUnionThing;
	

	

	/**
	 * Constructs a Percolation object for a nxn grid that uses unionThing to
	 * store sets representing the cells and the top/source and bottom/sink
	 * virtual cells
	 */
	public PercolationUF(int n, IUnionFind unionThing) {
		// TODO complete PercolationUF constructor
		N=n;
		myUnionThing = new QuickFind(n*n+2);
		SOURCE=n*n;
		SINK=n*n+1;
		myRows=n;
		myCols=n;
		
		myGrid= new int[myRows][myCols];
		
		for(int i=0; i<myRows; i++) { //might not even need this, actually will probs neex
			for(int j=0; j<myCols; j++) {
				myGrid[i][j]=BLOCKED;
			}
		}
		
	}

	/**
	 * Return an index that uniquely identifies (row,col), typically an index
	 * based on row-major ordering of cells in a two-dimensional grid. However,
	 * if (row,col) is out-of-bounds, return OUT_BOUNDS.
	 */
	public int getIndex(int row, int col) {
		// TODO complete getIndex
		if (row<0 || col<0 || row>=myGrid.length || col>=myGrid.length) {
			return OUT_BOUNDS;
		}
		return row*N+col;
		
	}
	
	public void open(int i, int j) {
		// TODO complete open

		
//		System.out.println("I is " + i);
//		System.out.println("J is " + j);
//		System.out.println("NNNN is " + N);
		
		if (i<0 || j<0 || i>=myGrid.length || j>=myGrid.length) {
			throw new IndexOutOfBoundsException();
		}
		int idx = getIndex(i, j);
		//System.out.println("idx is  " + idx);
		if(idx==OUT_BOUNDS) {
			return;
		}
		
		//System.out.println("second " + idx);
		myGrid[i][j] = OPEN; 
		
		
		if(i==0) {
			//Union with the source
			myUnionThing.union(SOURCE, idx);
		} 
		if(i==N-1) {
			myUnionThing.union(SINK, idx);
		}
		
		if(i > 0 && isOpen(i-1, j)) {
			myUnionThing.union(getIndex(i-1, j), idx);
		}
		if(i< myGrid.length - 1 && isOpen(i+1, j)) {
			myUnionThing.union(getIndex(i+1, j), idx);
		}
		if(j>0 && isOpen(i, j-1)) {
			myUnionThing.union(getIndex(i, j-1), idx);
		}
		if(j<myGrid.length-1 && isOpen(i, j+1)) {
			myUnionThing.union(idx, getIndex(i, j+1));
		}
	}

	public boolean isOpen(int i, int j) {
		// TODO complete isOpen
		if (i<0 || j<0 || i>=myGrid.length || j>=myGrid.length) {
			throw new IndexOutOfBoundsException();
		}
		if (myGrid[i][j]==OPEN || myGrid[i][j]==FULL) {
			return true;
		}
		return false;
	}

	public boolean isFull(int i, int j) {
		// TODO complete isFull
		if (i<0 || j<0 || i>=myGrid.length || j>=myGrid.length) {
			throw new IndexOutOfBoundsException();
		}
		if(myUnionThing.connected(SOURCE, getIndex(i, j))) {
			return true;
		}
		return false;
	}

	public boolean percolates() {
		// TODO complete percolates
		return myUnionThing.connected(SOURCE, SINK);
	}

	/**
	 * Connect new site (row, col) to all adjacent open sites
	 */
	private void connect(int row, int col) {
		// TODO complete connect
		int idx = getIndex(row, col);
		if (row<0 || col<0 || row>=myGrid.length || col>=myGrid.length) {
			throw new IndexOutOfBoundsException();
		}
		if(row > 0 && isOpen(row-1, col)) {
			myUnionThing.union(getIndex(row-1, col), idx);
		}
		if(row< myGrid.length - 1 && isOpen(row+1, col)) {
			myUnionThing.union(getIndex(row+1, col), idx);
		}
		if(col>0 && isOpen(row, col-1)) {
			myUnionThing.union(getIndex(row, col-1), idx);
		}
		if(col<myGrid.length-1 && isOpen(row, col+1)) {
			myUnionThing.union(getIndex(row, col+1), idx);
		}
		
	}

}
