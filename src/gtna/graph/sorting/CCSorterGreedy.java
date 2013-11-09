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
 * CCSorterGreedy.java
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * @author stefanie
 *
 */
public class CCSorterGreedy extends NodeSorter {
	boolean bidirectional;
	private Computation computation;
	private double[] coc;
	
	public enum Computation{
		NODEBASED, DEGREEBASED
	}
	
		public CCSorterGreedy(boolean bidirectional, Computation computation) {
			super("CC-GREEDY-"+computation.toString(), NodeSorterMode.ASC);
			this.bidirectional = bidirectional;
			this.computation = computation;
		}

		@Override
		public boolean applicable(Graph g) {
			return true;
		}

		@Override
		public Node[] sort(Graph g, Random rand) {
			if (this.bidirectional && computation == Computation.DEGREEBASED){
				return (new DegreeNodeSorter(NodeSorterMode.DESC)).sort(g, rand);
			}
			this.calculate(g);
			Node[] sorted = this.clone(g.getNodes());
			Arrays.sort(sorted, new ConnectivityAsc());
			this.randomize(sorted, rand);
			return sorted;
		}
		
		private void calculate(Graph g){
			Node[] nodes = g.getNodes();
			this.coc = new double[nodes.length];
			if (this.bidirectional){
				if (this.computation == Computation.NODEBASED){
					int max = 0;
					for (int i = 0; i < nodes.length; i++){
						if (nodes[i].getInDegree() > max){
							max = nodes[i].getInDegree();
						}
					}
					double[] degDist = new double[max+1];
					for (int i = 0; i < nodes.length; i++){
						degDist[nodes[i].getInDegree()]++;
					}
					double f = 1/(double)nodes.length;
					for (int j = 0; j < degDist.length; j++){
						degDist[j] = degDist[j]*f;
					}
					double d = 0;
					for (int j = 0; j < degDist.length; j++){
						d = d + (j-2)*j*degDist[j];
					}
					for (int i = 0; i < nodes.length; i++){
						double ddash = d - nodes[i].getInDegree()*(nodes[i].getInDegree()-2)*f;
						int[] neighs = nodes[i].getIncomingEdges();
						for (int s = 0; s < neighs.length; s++){
							double deg = nodes[neighs[s]].getInDegree();
							ddash = ddash - (deg*(deg-2)-(deg-1)*(deg-3))*f;
						}
						this.coc[i]=ddash;
					}
				}
			} else {
				if (this.computation == Computation.DEGREEBASED){
					int maxIn = 0;
					int maxOut = 0;
					for (int i = 0; i < nodes.length; i++){
						if (nodes[i].getInDegree() > maxIn){
							maxIn = nodes[i].getInDegree();
						}
						if (nodes[i].getOutDegree() > maxOut){
							maxOut = nodes[i].getOutDegree();
						}
					}
					double[][] degDist = new double[maxIn+1][maxOut+1];
					for (int i = 0; i < nodes.length; i++){
						degDist[nodes[i].getInDegree()][nodes[i].getOutDegree()]++;
					}
					double f = 1/(double)nodes.length;
					for (int j = 0; j < degDist.length; j++){
						for (int k = 0; k < degDist[j].length; k++){
						degDist[j][k] = degDist[j][k]*f;
						}
					}
					double d = 0;
					double e = 0;
					for (int j = 0; j < degDist.length; j++){
						for (int k = 0; k < degDist[j].length; k++){
						d = d + (2*k*j-j-k)*degDist[j][k];
						e = e + k*degDist[j][k];
						}
					}
					for (int i = 0; i < nodes.length; i++){
						double degIn = nodes[i].getInDegree();
						double degOut = nodes[i].getOutDegree();
						double ddash = d - (2*degIn*degOut)*f;
						double pin = degIn/e*f;
						double pout = degOut/e*f;
						this.coc[i]=(1-pin)*(1-pout)*ddash;
					}
				}else {
					int maxIn = 0;
					int maxOut = 0;
					for (int i = 0; i < nodes.length; i++){
						if (nodes[i].getInDegree() > maxIn){
							maxIn = nodes[i].getInDegree();
						}
						if (nodes[i].getOutDegree() > maxOut){
							maxOut = nodes[i].getOutDegree();
						}
					}
					double[][] degDist = new double[maxIn+1][maxOut+1];
					for (int i = 0; i < nodes.length; i++){
						degDist[nodes[i].getInDegree()][nodes[i].getOutDegree()]++;
					}
					double f = 1/(double)nodes.length;
					for (int j = 0; j < degDist.length; j++){
						for (int k = 0; k < degDist[j].length; k++){
						degDist[j][k] = degDist[j][k]*f;
						}
					}
					double d = 0;
					for (int j = 0; j < degDist.length; j++){
						for (int k = 0; k < degDist[j].length; k++){
						d = d + (2*k*j-j-k)*degDist[j][k];
						}
					}
					for (int i = 0; i < nodes.length; i++){
						double degIn = nodes[i].getInDegree();
						double degOut = nodes[i].getOutDegree();
						double ddash = d - (2*degIn*degOut-degIn-degOut)*f;
						int[] neighs = nodes[i].getIncomingEdges();
						for (int s = 0; s < neighs.length; s++){
							degIn = nodes[neighs[s]].getInDegree();
							degOut = nodes[neighs[s]].getOutDegree();
							ddash = ddash - (degIn-1)*f;
						}
						neighs = nodes[i].getOutgoingEdges();
						for (int s = 0; s < neighs.length; s++){
							degIn = nodes[neighs[s]].getInDegree();
							degOut = nodes[neighs[s]].getOutDegree();
							if (nodes[i].hasIn(neighs[s])){
								degOut--; 
							}
							ddash = ddash - (degOut-1)*f;
						}
						this.coc[i]=ddash;
					}
				}
			}
		}

		private class ConnectivityAsc implements Comparator<Node> {
			public int compare(Node n1, Node n2) {
				double c = coc[n1.getIndex()] - coc[n2.getIndex()];
				if (c == 0){
				  return 0;
				} else {
				  if (c > 0){
					  return 1;
				}	else {
					return -1;
				}
				}
			}
		}

		@Override
		protected boolean isPropertyEqual(Node n1, Node n2) {
			return coc[n1.getIndex()] == coc[n2.getIndex()];
		}

}
