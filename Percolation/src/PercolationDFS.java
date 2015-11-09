import java.util.Arrays;

import princeton.*;

/**
 * Simulate percolation thresholds for a grid-base system using depth-first-search,
 * aka 'flood-fill' techniques for determining if the top of a grid is connected
 * to the bottom of a grid.
 * <P>
 * Modified from the COS 226 Princeton code for use at Duke. The modifications
 * consist of supporting the <code>IPercolate</code> interface, renaming methods
 * and fields to be more consistent with Java/Duke standards and rewriting code
 * to reflect the DFS/flood-fill techniques used in discussion at Duke.
 * <P>
 * @author Kevin Wayne, wayne@cs.princeton.edu
 * @author Owen Astrachan, ola@cs.duke.edu
 * @author Jeff Forbes, forbes@cs.duke.edu
 */

//I bet this is mostly rightish. or at least on the right track. Save debugging for tmorrow


public class PercolationDFS implements IPercolate {
	// possible instance variable for storing grid state
	public int[][] myGrid;
	private int myRows;
	private int myCols;
	private int N;
	
	public static final int BLOCKED = 0;
	public static final int OPEN = 1;
	public static final int FULL = 2;

	/**
	 * Initialize a grid so that all cells are blocked.
	 * 
	 * @param n
	 *            is the size of the simulated (square) grid
	 */
	
		
	public PercolationDFS(int n) {
		// TODO complete constructor and add necessary instance variables
		myRows=n;
		myCols=n;
		int size = n*n;
		N = n;
		myGrid= new int[myRows][myCols];
		for(int i=0; i<myRows; i++) { //might not even need this, actually will probs neex
			for(int j=0; j<myCols; j++) {
				myGrid[i][j]=BLOCKED;
			}
		}
	}

	private boolean works(int row, int col) {
		if (row<0 || col<0 || row>=myGrid.length || col>=myGrid.length) {
			throw new IndexOutOfBoundsException();
		}
		return true;
	}
	
	public void flush() {
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				if(myGrid[i][j]==FULL) {
					myGrid[i][j]=OPEN;
				}
			}
		}
	}
	public void open(int i, int j) {
		// TODO complete open
		if (i<0 || j<0 || i>=myGrid.length || j>=myGrid.length) {
			throw new IndexOutOfBoundsException();
		}
		myGrid[i][j]=OPEN;

		for(int row=0; row<N; row++) {
			for(int col=0; col<N; col++) {
				if(myGrid[row][col]==FULL) {
					myGrid[row][col]=OPEN;
				}
			}
		}
		
		//System.out.println(n);
		for(int a=0; a<N; a++) {
			dfs(0, a);
		}
	
		
	}

	public boolean isOpen(int i, int j) {
		// TODO complete isOpen
		if (i<0 || j<0 || i>=myGrid.length || j>=myGrid.length) {
			throw new IndexOutOfBoundsException(); 
			//return false;
		}
		
		if (myGrid[i][j]==OPEN) {
			return true;
		}
		return false;
	}

	public boolean isFull(int i, int j) {
		// TODO complete isFull
		if (i<0 || j<0 || i>=myGrid.length || j>=myGrid.length) {
			throw new IndexOutOfBoundsException();
		}
		
		if (myGrid[i][j]==FULL) {
			return true;
		}
		return false;
	}

	public boolean percolates() {
		// TODO: run DFS to find all full sites
		
		//comment out 3 lines
		
		for(int i=0; i<N; i++) {
//			for(int j=0; j<n; j++) {
				//dfs(i, j);
				if(myGrid[N - 1][i]==FULL) {
					return true;
				}
			}
		return false;
	}

	/**
	 * Private helper method to mark all cells that are open and reachable from
	 * (row,col).
	 * 
	 * @param row
	 *            is the row coordinate of the cell being checked/marked
	 * @param col
	 *            is the col coordinate of the cell being checked/marked
	 */
	private void dfs(int row, int col) {
		// TODO: complete dfs
		//System.out.println("dfs calls");

		if (row<0 || col<0 || row>=myGrid.length || col>=myGrid.length) {
			//throw new IndexOutOfBoundsException();
			return;
		}
		
		if (myGrid[row][col]==BLOCKED) {
			return; //Just want it to give the fuck up and stop
		} //also how do I stop it? Don't you need a base case like, always?
		if(isFull(row, col)) {
			return; //walkthrough says to stop if the thing is full 
		}
		myGrid[row][col]=FULL;
		if(row < myGrid.length - 1 && isOpen(row+1, col)) {
			//System.out.println("1");
			dfs(row+1, col); //adjacents
		}
		if(row > 0 && isOpen(row-1, col)) {
			//System.out.println("2");
			dfs(row-1, col);
		}
		if(col < myGrid.length-1 && isOpen(row, col+1)) {
			//System.out.println("3");
			dfs(row, col+1);
		}
		if(col > 0 && isOpen(row, col-1)) {
			//System.out.println("4");
			dfs(row, col-1);
		}
		
	}

}
