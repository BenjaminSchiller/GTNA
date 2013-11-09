/* ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 *
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 *
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 *
 * GTNA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GTNA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * ---------------------------------------
 * CCSorterItertively.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.graph.sorting;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class CCSorterItertively extends NodeSorter {

	private boolean bidirectional;
	private int[][] degs;
	private double c1;
	private double c2;
	private double c3;
	private double f;
	private Node[] sorted;
	private double[] c;
	private double epm;
	private double ep;
	private Computation computation;
	
	public enum Computation{
		NODEBASED, DEGREEBASED
	}

	/**
	 * @param key
	 * @param mode
	 */
	public CCSorterItertively(boolean bidirectional, Computation computation) {
		super("CC-"+computation.toString(), NodeSorterMode.ASC);
		this.bidirectional = bidirectional;
		this.computation = computation;
	}

	

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#sort(gtna.graph.Graph, java.util.Random)
	 */
	@Override
	public Node[] sort(Graph g, Random rand) {
		Node[] nodes = g.getNodes();
		nodes = (new RandomNodeSorter()).sort(g, rand);
		this.sorted = this.clone(g.getNodes());
		if (this.bidirectional){
		this.degs = new int[sorted.length][1];
		} else {
			this.degs = new int[sorted.length][2];
		}
		f = 1/(double)this.degs.length;
		for (int i = 0; i < degs.length; i++){
			degs[i][0] = nodes[i].getInDegree();
			if (!this.bidirectional){
				degs[i][1] = nodes[i].getOutDegree();
			}
			if (this.bidirectional){
			   this.epm = this.epm +degs[i][0]*degs[i][0];
			} else {
				this.epm = this.epm +degs[i][0]*degs[i][1];
			}
			this.ep = this.ep + degs[i][0];
		}
		this.epm = this.epm*f;
		this.ep = this.ep*f;
		if (this.bidirectional){
			if (this.computation == Computation.DEGREEBASED){
				this.sorted = (new DegreeNodeSorter(NodeSorter.NodeSorterMode.DESC)).sort(g, rand);
			} else {
				int max = 0;
				for (int i = 0; i < degs.length; i++){
					if (degs[i][0] > max){
						max = degs[i][0];
					}
				}
				double[] degDist = new double[max+1];
				for (int i = 0; i < degs.length; i++){
						degDist[degs[i][0]]++;
				}
				for (int k = 0; k < degDist.length; k++){
					degDist[k] = degDist[k]*f;
				}
				double[] pk = new double[degDist.length];
				
				boolean[] removed = new boolean[nodes.length];
				for (int i = 0; i < nodes.length; i++){
					int minindex = -1;
					double min = Double.MAX_VALUE;
					for (int j = 0; j < nodes.length; j++){
						if (!removed[j]){
							degDist[degs[j][0]] = degDist[degs[j][0]]-f;
							int[] neighs = nodes[j].getIncomingEdges();
							for (int s = 0; s < neighs.length; s++){
								pk[nodes[neighs[s]].getInDegree()] = pk[nodes[neighs[s]].getInDegree()]+f;
							}
							double d = 0;
							for (int k = 0; k < degDist.length; k++){
								d = d + (k*(k-1)*(1-pk[k])*(1-pk[k])-(1-pk[k])*k)*degDist[k];
								
							}
							if (d < min){
								min = d;
								minindex = j;
							}
							degDist[degs[j][0]] = degDist[degs[j][0]]+f;
							for (int s = 0; s < neighs.length; s++){
								pk[nodes[neighs[s]].getInDegree()] = pk[nodes[neighs[s]].getInDegree()]-f;
							}
						}
					}
					sorted[i] = nodes[minindex];
					removed[minindex] = true;
					degDist[degs[minindex][0]] = degDist[degs[minindex][0]]-f;
					int[] neighs = nodes[minindex].getIncomingEdges();
					for (int s = 0; s < neighs.length; s++){
						pk[nodes[neighs[s]].getInDegree()] = pk[nodes[neighs[s]].getInDegree()]+f;
					}
				}
			}
		}else {
		c1 = 1;
		c2 = 1;
		c3 = epm-ep;
		boolean[] removed = new boolean[nodes.length];
		if (this.computation == Computation.DEGREEBASED){
		for (int i = 0; i < nodes.length; i++){
			double min = c1*c2*c3;
			int minindex = -1;
			for (int j = 0; j < nodes.length; j++){
				if (!removed[j]){
					double c1dash = c1-degs[j][0]/ep*f;
					double c2dash = c2-degs[j][1]/ep*f;
					double x = c1dash*c2dash*(c3-degs[j][0]*degs[j][1]*f);
					if (minindex == -1 || x <= min){
						min = x;
						minindex = j;
					}
					} 
				}
			
			removed[minindex]=true;
			sorted[i] = nodes[minindex];
			c1 = c1-degs[minindex][0]/ep*f;
			c2 = c2-degs[minindex][1]/ep*f;
			c3 = c3-degs[minindex][0]*degs[minindex][1]*f;
			
		}
		}
	 else {
			int maxOut = 0;
			int maxIn = 0;
			for (int i = 0; i < degs.length; i++){
				if (degs[i][0] >maxIn){
					maxIn = degs[i][0];
				} 
				if (degs[i][1] >maxOut){
					maxOut = degs[i][1];
				}
			}
			double[][] degDist = new double[maxIn+1][maxOut+1];
			for (int i = 0; i < degs.length; i++){
				degDist[degs[i][0]][degs[i][1]]++;
			}
			for (int i = 0; i < degDist.length; i++){
				for (int j = 0; j < degDist[i].length; j++){
					degDist[i][j] = degDist[i][j]*f;
				}
			}
			double[][] pkjIn = new double[degDist.length][degDist[0].length];
			double[][] pkjOut = new double[degDist.length][degDist[0].length];
			for (int i = 0; i < nodes.length; i++){
				int minindex = -1;
				double min = Double.MAX_VALUE;
				for (int j = 0; j < nodes.length; j++){
					if (!removed[j]){
						int[] neighs = nodes[j].getIncomingEdges();
						for (int s = 0; s < neighs.length; s++){
							pkjIn[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()] = 
									pkjIn[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()]+f;
						}
						neighs = nodes[j].getOutgoingEdges();
						for (int s = 0; s < neighs.length; s++){
							pkjOut[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()] = 
									pkjOut[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()]+f;
						}
						degDist[nodes[j].getInDegree()][nodes[j].getOutDegree()]= 
								degDist[nodes[j].getInDegree()][nodes[j].getOutDegree()]-f;
						double d = 0;
						for (int k = 0; k < degDist.length; k++){
							for (int l = 0; l < degDist[0].length; l++){
								    d = d + (2*k*l*(1-pkjIn[k][l])*(1-pkjOut[k][l])-k*(1-pkjOut[k][l])-l*(1-pkjIn[k][l]));
								}
						}
						if (d < min){
							min = d;
							minindex = j;
						}
						neighs = nodes[j].getIncomingEdges();
						for (int s = 0; s < neighs.length; s++){
							pkjIn[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()] = 
									pkjIn[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()]-f;
						}
						neighs = nodes[j].getOutgoingEdges();
						for (int s = 0; s < neighs.length; s++){
							pkjOut[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()] = 
									pkjOut[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()]-f;
						}
						degDist[nodes[j].getInDegree()][nodes[j].getOutDegree()]= 
								degDist[nodes[j].getInDegree()][nodes[j].getOutDegree()]+f;
					}
				}

				removed[minindex] = true;
				sorted[i] = nodes[minindex];
				int[] neighs = nodes[minindex].getIncomingEdges();
				for (int s = 0; s < neighs.length; s++){
					pkjIn[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()] = 
							pkjIn[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()]+f;
				}
				neighs = nodes[minindex].getOutgoingEdges();
				for (int s = 0; s < neighs.length; s++){
					pkjOut[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()] = 
							pkjOut[nodes[neighs[s]].getInDegree()][nodes[neighs[s]].getOutDegree()]+f;
				}
				degDist[nodes[minindex].getInDegree()][nodes[minindex].getOutDegree()]= 
						degDist[nodes[minindex].getInDegree()][nodes[minindex].getOutDegree()]-f;
			}
		}	
		}
		return sorted;
	}
	
	/**
	   * Computes the binomial coefficient "n over k".
	   * 
	   * @param n
	   * @param k
	   * @return the binomial coefficient
	   */
	  public static long binom(int n, final int k) {
	    final int min = (k < n - k ? k : n - k);
	    long bin = 1;
	    for (int i = 1; i <= min; i++) {
	      bin *= n;
	      // geht immer genau, da n * (n-1) * ... immer durch das
	      // entsprechende i teilbar ist
	      bin /= i;
	      n--;
	    }
	    return bin;
	  }

	
	private class CCAsc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			double d = c[n1.getIndex()] - c[n2.getIndex()];
			if (d < 0){
			return -1;
			} else {
				if (d == 0){
					return 0;
				}else {
					return 1;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#isPropertyEqual(gtna.graph.Node, gtna.graph.Node)
	 */
	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		return this.c[n1.getIndex()] == this.c[n2.getIndex()];
	}

}
