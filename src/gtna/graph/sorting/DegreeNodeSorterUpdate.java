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
 * DegreeNodeSorterUpdate.java
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
public class DegreeNodeSorterUpdate extends NodeSorterUpdate{
     int[] degrees;
     Node[] sorted;
     Random rand;
     
	/**
	 * @param key
	 * @param mode
	 */
	public DegreeNodeSorterUpdate(NodeSorterMode mode) {
		super("DEGREE-UPDATE", mode);
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorterUpdate#update(int)
	 */
	@Override
	public Node[] update(boolean[] deleted, int index, Random rand) {
		int[] out = sorted[index].getOutgoingEdges();
		for (int i = 0; i < out.length; i++){
			if (!deleted[out[i]]){
				degrees[out[i]]--;
			}
		}
		int[] in = sorted[index].getIncomingEdges();
		for (int i = 0; i < in.length; i++){
			if (!deleted[in[i]]){
				degrees[in[i]]--;
			}
		}
//		Arrays.sort(sorted, new DegreeAsc());
//		this.randomize(sorted, rand);
		for (int i = index+1; i < sorted.length-1; i++){
			int j = i+1;
			while (j < sorted.length && degrees[sorted[j].getIndex()] > degrees[sorted[i].getIndex()]){
				j++;
			}
			if (j > i+1){
				Node temp = sorted[i];
				sorted[i] = sorted[j-1];
				sorted[j-1] = temp;
			}
		}
		return sorted;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#sort(gtna.graph.Graph, java.util.Random)
	 */
	@Override
	public Node[] sort(Graph g, Random rand) {
		this.sorted = this.clone(g.getNodes());
		this.degrees = new int[sorted.length];
		for (int i = 0; i < degrees.length; i++){
			degrees[i] = sorted[i].getDegree();
		}
		Arrays.sort(sorted, new DegreeAsc());
		this.randomize(sorted, rand);
		if (this.mode == NodeSorterMode.DESC) {
			sorted = this.reverse(sorted);
		}
		return sorted;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.sorting.NodeSorter#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	private class DegreeAsc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			return degrees[n1.getIndex()] - degrees[n2.getIndex()];
		}
	}

	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		return n1.getDegree() == n2.getDegree();
	}
	

}
