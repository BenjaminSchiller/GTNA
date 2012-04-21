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
 * CentralityNodeSorter.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

/**
 * @author truong
 * 
 */
public class CentralityNodeSorter extends NodeSorter {

	private HashMap<String, Integer> shortestPaths = new HashMap<String, Integer>();
	private HashMap<Node, Double> closenessPoints = new HashMap<Node, Double>();
	private HashMap<Node, Double> betweennessPoints = new HashMap<Node, Double>();
	private HashMap<Node, Double> eccentricityPoints = new HashMap<Node, Double>();
	private HashMap<Node, Double> usedCentrality = new HashMap<Node, Double>();

	private double eccentricityRate = 0.9;

	/**
	 * @param key
	 * @param mode
	 */
	public CentralityNodeSorter(NodeSorterMode mode) {
		super("CENTRALITY", mode);

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
	 * @param g
	 */
	private void calculate(Graph g) {
		Node[] nodes = g.getNodes();

		// initiate betweenness centrality points
		for (Node n : nodes) {
			this.betweennessPoints.put(n, 0.0);
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

			/*
			 * s = root S = list of nodes (increasing distance from s) P = TODO:
			 * sigma = TODO: d = distances from root
			 */
			// collect informations for the others centralities
			// collect distances
			for (Node n : d.keySet()) {
				shortestPaths.put(Edge.toString(s.getIndex(), n.getIndex()), d
						.get(n).intValue());
			}
			// collect eccentricity
			int lastIndex = (int) (Math.floor(nodes.length
					* this.eccentricityRate - 1));
			int pathLengthToLastIndex = d.get(S.get(lastIndex));
			this.eccentricityPoints.put(s, (double) pathLengthToLastIndex + 1);

			// delta
			HashMap<Node, Double> delta = new HashMap<Node, Double>();
			for (Node n : nodes) {
				delta.put(n, 0.0);
			}

			// S returns vertices in order of non-increasing distance from s
			while (!S.isEmpty()) {
				Node w = S.pop();
				for (Node v : P.get(w)) {
					delta.put(v, delta.get(v).doubleValue()
							+ sigma.get(v).doubleValue()
							/ sigma.get(w).doubleValue()
							* (1 + delta.get(w).doubleValue()));
				}
				if (w.getIndex() != s.getIndex()) {
					betweennessPoints.put(w, betweennessPoints.get(w)
							.doubleValue() + delta.get(w).doubleValue());
				}
			}
		}

		// calculate closeness
		this.calculatCloseness(g);

	}

	/**
	 * 
	 */
	private void calculatCloseness(Graph g) {
		Node[] nodes = g.getNodes();
		for (int i = 0; i < nodes.length; i++) {
			double sum = 0.0;
			for (int j = 0; j < nodes.length; j++) {
				sum += shortestPaths.get(Edge.toString(i, j));
			}
			// TODO:
			this.closenessPoints.put(nodes[i], 1 / sum);
		}
	}

	private class DegreeAsc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			double point1 = usedCentrality.get(n1).doubleValue();
			double point2 = usedCentrality.get(n2).doubleValue();
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

	public void setCentrality(String centrality) {
		if (centrality.equalsIgnoreCase("BETWEENNESS")) {
			usedCentrality = betweennessPoints;
			System.out.println("Betweenness is chosen");
			return;
		}
		if (centrality.equalsIgnoreCase("CLOSENESS")) {
			usedCentrality = closenessPoints;
			System.out.println("Closeness is chosen");
			return;
		}
		if (centrality.equalsIgnoreCase("ECCENTRICITY")) {
			usedCentrality = eccentricityPoints;
			System.out.println("Eccentricity is chosen");
			return;
		}
		usedCentrality = betweennessPoints;
		System.out
				.println("Cannot find the centrality algorithm, default (betweenness) is used!");
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
		return usedCentrality.get(n1).doubleValue() == usedCentrality.get(n2)
				.doubleValue();
	}

	public double getCentrality(Node n) {
		return usedCentrality.get(n);
	}
}
