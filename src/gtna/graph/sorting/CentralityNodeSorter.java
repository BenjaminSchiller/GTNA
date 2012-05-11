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

import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * Implement the Betweenness Centrality described by Ulrik Brandes in
 * "A Faster Algorithm for Betweenness Centrality" (2001)
 * 
 * The Closeness Centrality and Effective Eccentricity are implicit calculated
 * 
 * @author truong
 * 
 */
public class CentralityNodeSorter extends NodeSorter {

	public enum CentralityMode {
		BETWEENNESS, CLOSENESS, ECCENTRICITY;

		@Override
		public String toString() {
			switch (this) {
			case BETWEENNESS:
				return "BETWEENNESS";
			case CLOSENESS:
				return "CLOSENESS";
			case ECCENTRICITY:
				return "ECCENTRICITY";
			}
			return null;
		}
	}

	private Graph g;

	/*
	 * This map saves the centrality points of the nodes
	 */
	private HashMap<Node, Double> map = new HashMap<Node, Double>();

	/*
	 * This map saves the effective eccentricity of the nodes
	 */
	private HashMap<Node, Double> eeMap = new HashMap<Node, Double>();

	/*
	 * This map saves the betweennes centrality of the nodes
	 */
	private HashMap<Node, Double> bcMap = new HashMap<Node, Double>();

	/*
	 * Parameter r of effective eccentricity. Default: r = 0.9
	 */
	private double r = 0.9;

	/*
	 * This map saves the closeness centrality of the nodes
	 */
	private HashMap<Node, Double> ccMap = new HashMap<Node, Double>();

	public CentralityNodeSorter(CentralityMode c, NodeSorterMode mode) {
		// super(key, mode);
		super(c.toString(), mode);
		switch (c) {
		case BETWEENNESS:
			map = bcMap;
			break;
		case CLOSENESS:
			map = ccMap;
			break;
		case ECCENTRICITY:
			map = eeMap;
			break;
		default:
			System.out.println("Invalid Centrality Mode!");
			break;
		}
	}

	public Node[] resort(String key, Random rand) {
		if (key.equalsIgnoreCase("BETWEENNESS"))
			map = bcMap;
		if (key.equalsIgnoreCase("ECCENTRICITY"))
			map = eeMap;
		if (key.equalsIgnoreCase("CLOSENESS"))
			map = ccMap;
		Node[] sorted = this.clone(g.getNodes());
		Arrays.sort(sorted, new DegreeAsc());
		this.randomize(sorted, rand);
		if (this.mode == NodeSorterMode.DESC) {
			sorted = this.reverse(sorted);
		}
		return sorted;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.graph.sorting.NodeSorter#sort(gtna.graph.Graph,
	 * java.util.Random)
	 */
	@Override
	public Node[] sort(Graph g, Random rand) {
		this.g = g;
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

		// initiate centrality points
		for (Node n : nodes) {
			this.bcMap.put(n, 0.0);
		}

		for (Node s : nodes) {
			// empty stack
			Stack<Node> S = new Stack<Node>();
			S.clear();

			HashMap<Node, ArrayList<Node>> P = new HashMap<Node, ArrayList<Node>>();
			for (Node n : nodes) {
				P.put(n, new ArrayList<Node>());
			}

			// sigma
			HashMap<Node, Integer> sigma = new HashMap<Node, Integer>();
			for (Node n : nodes) {
				sigma.put(n, 0);
			}
			sigma.put(s, 1);

			// d
			HashMap<Node, Integer> d = new HashMap<Node, Integer>();
			for (Node n : nodes) {
				d.put(n, -1);
			}
			d.put(s, 0);

			// empty queue
			LinkedList<Node> Q = new LinkedList<Node>();
			Q.clear();
			Q.add(s);

			while (!Q.isEmpty()) {
				Node v = Q.remove();
				S.add(v);

				// for each neighbor w of v
				for (int outIndex : v.getOutgoingEdges()) {
					Node w = nodes[outIndex];

					// w found for the first time?
					if (d.get(w).intValue() < 0) {
						Q.add(w);
						d.put(w, d.get(v).intValue() + 1);
					}
					// shortest path to w via v?
					if (d.get(w).intValue() == d.get(v).intValue() + 1) {
						sigma.put(w, sigma.get(w).intValue()
								+ sigma.get(v).intValue());
						P.get(w).add(v);
					}
				}
			}

			// delta
			HashMap<Node, Double> delta = new HashMap<Node, Double>();
			for (Node n : nodes) {
				delta.put(n, 0.0);
			}

			// S returns vertices in order of non-increasing distance from s

			// ----- calculate effective eccentricity of s
			int rTimesNodes = (int) Math.floor(r * g.getNodes().length);
			// distance from s to the node with index rTimesNodes
			int distance = Integer.MAX_VALUE;
			try {
				distance = d.get(S.get(rTimesNodes)).intValue();
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Cannot not get " + r + " nodes!");
			}
			// put the eccentricity point to the map
			this.eeMap.put(s, (double) distance);
			// -----

			// ----- calculate closeness centrality of s
			double sum = 0;
			for (Node n : S) {
				sum += d.get(n).doubleValue();
			}
			this.ccMap.put(s, 1 / sum);
			// -----

			// for betweenness
			while (!S.isEmpty()) {
				Node w = S.pop();
				for (Node v : P.get(w)) {
					delta.put(v, delta.get(v).doubleValue()
							+ sigma.get(v).doubleValue()
							/ sigma.get(w).doubleValue()
							* (1 + delta.get(w).doubleValue()));
				}
				if (w.getIndex() != s.getIndex()) {
					// put the centrality point to the map
					bcMap.put(w, bcMap.get(w).doubleValue()
							+ delta.get(w).doubleValue());
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

	public double getCentrality(Node n) {
		return map.get(n);
	}

}
