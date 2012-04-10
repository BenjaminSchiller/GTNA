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
 * ClosenessCentralityNodeSorter.java
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

import gtna.graph.Edge;
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
public class ClosenessCentralityNodeSorter extends NodeSorter {

	private HashMap<Node, Double> map = new HashMap<Node, Double>();
	private HashMap<String, Double> distances = new HashMap<String, Double>();

	public ClosenessCentralityNodeSorter(NodeSorterMode mode) {
		super("CLOSENESS", mode);
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

	/**
	 * 
	 */
	private void calculate(Graph g) {
		allShortestPaths(g);
		for (int i = 0; i < g.getNodes().length; i++) {
			double sum = 0;
			for (int j = 0; j < g.getNodes().length; j++) {
				if (j == i) {
					continue;
				}
				if (distances.containsKey(Edge.toString(i, j))) {
					sum += distances.get(Edge.toString(i, j));
				}
			}
			map.put(g.getNode(i), 1 / sum);
		}
	}

	private class DegreeAsc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			double point1 = map.get(n1).doubleValue();
			double point2 = map.get(n2).doubleValue();
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

	private final double INF = Double.MAX_VALUE / 2;

	private void allShortestPaths(Graph g) {
		for (int i = 0; i < g.getNodes().length; i++) {
			this.distances.put(new String(Edge.toString(i, i)), 0.0);
		}

		for (int i = 0; i < g.getEdges().size(); i++) {
			Edge e = g.getEdges().getEdges().get(i);
			this.distances.put(
					new String(Edge.toString(e.getSrc(), e.getDst())), 1.0);
		}

		for (int k = 0; k < g.getNodes().length; k++) {
			for (int i = 0; i < g.getNodes().length; i++) {
				for (int j = 0; j < g.getNodes().length; j++) {
					if (!distances.containsKey(Edge.toString(i, j))) {
						distances.put(Edge.toString(i, j), INF);
					}
					double newDistance = distances.get(Edge.toString(i, k))
							+ distances.get(Edge.toString(k, j));
					if (distances.get(Edge.toString(i, j)) < newDistance) {
						distances.put(Edge.toString(i, j), newDistance);
					}
				}
			}
		}
	}
}
