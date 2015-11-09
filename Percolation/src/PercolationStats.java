import java.util.Random;

import javax.swing.JOptionPane;

import princeton.*;

/**
 * Print statistics on Percolation: prompts the user for N and T, performs T
 * independent experiments on an N-by-N grid, prints out the 95% confidence
 * interval for the percolation threshold, and prints mean and std. deviation
 * of timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 */

public class PercolationStats {
	public static double average(double[] avs) {
		double len=avs.length;
		double sum=0.0;
		for(int k=0; k<avs.length; k++) {
			sum+=avs[k];
		}
		double fin= sum/len;
		//System.out.print("Average " + fin);
		return fin;
	}
	public static double StdDev(double mean, double[] avs){
		double[] devs = new double[avs.length];
		for(int m=0; m<avs.length; m++){
			devs[m]=Math.pow(avs[m]-mean, 2);
		}
		double sum=0.0;
		double len=devs.length;
		for(int k=0; k<devs.length; k++) {
			sum+=devs[k];
		}
		double chinga = sum/len;
		double fin = Math.pow(chinga, .5);
		//System.out.println("StdDEv " + fin);
		return fin;
	}
	public static void main(String[] args) {
		int N, T;
		if (args.length == 2) { // use command-line arguments for
								// testing/grading
			N = Integer.parseInt(args[0]);
			T = Integer.parseInt(args[1]);
		} else {
			String input = JOptionPane.showInputDialog("Enter N and T", "20 100");
			String[] nums = input.split(" ");
			N=Integer.parseInt(nums[0]);
			T=Integer.parseInt(nums[1]);
		}
		long start=System.currentTimeMillis();
		double[] avs = new double[T];
		for(int a=0; a<T; a++) {
			//IPercolate perc = new PercolationDFS(N);
			//PercolationUF perc = new PercolationUF(N, new QuickFind());
			PercolationUF perc = new PercolationUF(N, new QuickFind());
			int opened=0;
			while(!perc.percolates()) {
				Random r1= new Random();
				int i=r1.nextInt(N);
				//System.out.println("R1 " + i);
				Random r2= new Random();
				int j=r1.nextInt(N);
				//System.out.println("R2 " + j);
				
//				if(perc.isOpen(i, j)){
//					continue;
//				}
				if(!perc.isOpen(i, j)) {
					perc.open(i, j);
					opened++;
				} else {
					continue;
				}
				//System.out.println("opened " + opened);
			}
			double openedd = (double) opened;
			double size = (double) N*N;
			double toAdd = openedd/size;
			avs[a]=toAdd;
			//System.out.println("Avs[a] is " + avs[a]);
		}
		long end = System.currentTimeMillis();
		long totTime=end-start;
		System.out.println("Total time is: " + totTime);
		
		double mean= average(avs);
		double std= StdDev(mean, avs);
		double c1 = (mean-(1.96*std))/Math.sqrt(T);
		double c2 = (mean+(1.96*std))/Math.sqrt(T);
		
		System.out.println("mean is" + mean);
		System.out.println("Standard deviation is" + std);
		System.out.println("Confidence interval goes from" + c1+ "to " + c2);
		

		// TODO: Perform T experiments for N-by-N grid

		// TODO: print statistics and confidence interval

	}
}
