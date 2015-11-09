import java.awt.Color;
import java.util.Random;

import javax.swing.JOptionPane;

import princeton.*;

/**
 * Animates the results of opening sites in a percolation system
 * 
 * From Princeton COS 226, Kevin Wayne 
 * Modified by Owen Astrachan, January 2008
 * Modified by Jeff Forbes, October 2008
 */

public class PercolationVisualizer {
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	

	/**
	 * Draws a square of color c at (row,col) on a N*N grid
	 */
	public static void draw(int row, int col, int N, Color c) {
		StdDraw.setPenColor(c);
		StdDraw.filledSquare(col + .5, N - row - .5, .45);
	}

	public static void main(String[] args) {
		// Animate 20 times a second if possible
		final int DEFAULT_DELAY = 1000 / 20; // in milliseconds
		String input = "20"; // default
		if (args.length == 1) // use command-line arguments for testing/grading
			input = args[0];
		else
			input = JOptionPane.showInputDialog("Enter N", "20");
		int N = Integer.parseInt(input); // N-by-N lattice

		// set x- and y-scale
		StdDraw.setXscale(0, N);
		StdDraw.setYscale(0, N);
		// draw a black box
		StdDraw.setPenColor(Color.BLACK);
		//StdDraw.filledSquare(N / 2.0, N / 2.0, N / 2.0);
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				draw(i,j, N, Color.BLACK);
			}
		}
		StdDraw.show(DEFAULT_DELAY);
		StdDraw.show(DEFAULT_DELAY);
		StdDraw.show(DEFAULT_DELAY);
		StdDraw.show(DEFAULT_DELAY);
		
		//What it does right now- makes each thing black, then turns everything white at once. 

		//IPercolate perc = new PercolationDFS(N);
		IPercolate perc = new PercolationUF(N, new QuickFind());
		// TODO repeatedly declare sites open, draw, & pause until the system
		// percolates
		// draw percolation system
		
		//Might need a while loop with this
		Random rand = new Random();
		while(!perc.percolates()) {
			int i = rand.nextInt(N);
			int j= rand.nextInt(N);
			perc.open(i,  j);
		
			//redraw entire thing (nested for loop and three cases)
			for(int a=0; a<N; a++) {
				for(int b=0; b<N; b++) {
//					if(a==0) {
//						draw(a, b, N, Color.CYAN);
//					}

					if(perc.isOpen(a, b)) {
						draw(a, b, N, Color.WHITE);
					}
					if(perc.isFull(a, b)) {
						draw(a, b, N, Color.CYAN);
					}

				}
			}
			StdDraw.show(N);
			StdDraw.show(DEFAULT_DELAY);
		}
		
		

		// wait DEFAULT_DELAY milliseconds and then display
		StdDraw.show(DEFAULT_DELAY);

	}
}
