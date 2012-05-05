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
 * EigenvectorCentralityNodeSorter.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: truong;
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
import java.util.HashMap;
import java.util.Random;

/**
 * @author truong
 * 
 */
public class EigenvectorCentralityNodeSorter extends NodeSorter {

	private HashMap<Node, Double> map = new HashMap<Node, Double>();
	private int numRuns = 1000;

	public EigenvectorCentralityNodeSorter(NodeSorterMode mode) {
		super("EIGENVECTOR", mode);
	}

	public void setNumRuns(int numRuns) {
		this.numRuns = numRuns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.graph.sorting.NodeSorter#sort(gtna.graph.Graph,
	 * java.util.Random)
	 */
	@Override
	public Node[] sort(Graph g, Random rand) {
		this.calculate(g);
		Node[] sorted = this.clone(g.getNodes());
		Arrays.sort(sorted, new DegreeAsc());
		this.randomize(sorted, rand);
		if (this.mode == NodeSorterMode.DESC) {
			sorted = this.reverse(sorted);
		}
		return sorted;
	}

	private class DegreeAsc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			double point1 = EigenvectorCentralityNodeSorter.this.map.get(n1)
					.doubleValue();
			double point2 = EigenvectorCentralityNodeSorter.this.map.get(n2)
					.doubleValue();
			int result;
			if (point1 == point2) {
				result = 0;
			} else if (point1 > point2) {
				result = 1;
			} else {
				result = -1;
			}
			return result;
		}
	}

	/**
	 * @param g2
	 */
	private void calculate(Graph g) {
		int N = g.getNodes().length;
		double[] tmp = new double[N];
		double[] centralities = new double[N];

		int count = 0;

		HashMap<Integer, Node> indicies = new HashMap<Integer, Node>();
		HashMap<Node, Integer> invIndices = new HashMap<Node, Integer>();
		for (Node u : g.getNodes()) {
			indicies.put(count, u);
			invIndices.put(u, count);
			centralities[count] = 1;
			count++;
		}

		for (int s = 0; s < numRuns; s++) {
			double max = 0;
			for (int i = 0; i < N; i++) {
				Node u = indicies.get(i);
				for (int id : u.getOutgoingEdges()) {
					tmp[i] += centralities[id];
				}
				max = Math.max(max, tmp[i]);
			}

			for (int k = 0; k < N; k++) {
				if (max != 0) {
					centralities[k] = tmp[k] / max;
				}
			}
		}

		for (int i = 0; i < N; i++) {
			map.put(g.getNode(i), centralities[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.graph.sorting.NodeSorter#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.graph.sorting.NodeSorter#isPropertyEqual(gtna.graph.Node,
	 * gtna.graph.Node)
	 */
	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		return map.get(n1).doubleValue() == map.get(n2).doubleValue();
	}

	public double getCentrality(Node n) {
		return map.get(n);
	}

}
