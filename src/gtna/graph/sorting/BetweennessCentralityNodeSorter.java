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
 * BetweennessCentralityNodeSorter.java
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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * @author truong
 * 
 */
public class BetweennessCentralityNodeSorter extends NodeSorter {

	private HashMap<Node, Double> map = new HashMap<Node, Double>();

	public BetweennessCentralityNodeSorter(NodeSorterMode mode) {
		super("BETWEENNESS", mode);
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
		Node[] nodes = g.getNodes();
		// -----
		for (int i = 0; i < nodes.length; i++) {
			map.put(nodes[i], 0.0);
		}
		// -----
		for (int i = 0; i < nodes.length; i++) {
			Node s = nodes[i];
			Stack<Node> S = new Stack<Node>();
			HashMap<Node, ArrayList<Node>> P = new HashMap<Node, ArrayList<Node>>();
			for (Node n : nodes) {
				P.put(n, new ArrayList<Node>());
			}

			HashMap<Node, Integer> sigma = new HashMap<Node, Integer>();
			for (Node n : nodes) {
				sigma.put(n, 0);
			}
			sigma.put(s, 1);

			HashMap<Node, Integer> d = new HashMap<Node, Integer>();
			for (Node n : nodes) {
				d.put(n, -1);
			}
			d.put(s, 0);

			LinkedList<Node> Q = new LinkedList<Node>();
			Q.add(s);

			while (!Q.isEmpty()) {
				Node v = Q.remove();
				S.add(v);
				for (int outIndex : v.getOutgoingEdges()) {
					Node w = nodes[outIndex];
					// w found for the first time?
					if (d.get(w) < 0) {
						Q.add(w);
						d.put(w, d.get(v) + 1);
					}
					// shortest path to w via v?
					if (d.get(w).intValue() == d.get(v).intValue() + 1) {
						sigma.put(w, sigma.get(w) + sigma.get(v));
						P.get(w).add(v);
					}
				}
			}

			HashMap<Node, Double> delta = new HashMap<Node, Double>();
			for (Node n : nodes) {
				delta.put(n, 0.0);
			}
			// S returns vertices in order of non-increasing distance from s
			while (!S.isEmpty()) {
				Node w = S.pop();
				for (Node v : P.get(w)) {
					delta.put(
							v,
							delta.get(v) + ((double) sigma.get(v))
									/ sigma.get(w) * (1 + sigma.get(w)));
				}
				if (w != s) {
					map.put(w, map.get(w) + delta.get(w));
				}
			}
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

}
