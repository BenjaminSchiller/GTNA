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
 * GLP.java
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
package gtna.networks.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author truong
 * 
 */
public class GLP extends Network {

	// parameters
	private int numOfStartNodes;
	private double p;
	private int numOfAddedEdges;
	private double beta;

	// variables for algorithm
	private int[] nodeDegree;
	private HashMap<String, Point> edgesList;

	public GLP(int nodes, int numOfStartNode, int numOfAddedEdges, double p,
			double beta, Transformation[] t) {
		super("GLP", nodes, new Parameter[] {
				new IntParameter("NUMBER_OF_START_NODES", numOfStartNode),
				new IntParameter("NUMBER_OF_ADDED_EDGES", numOfAddedEdges),
				new DoubleParameter("PROBABILITY", p),
				new DoubleParameter("BETA", beta) }, t);
		this.beta = beta;
		this.numOfAddedEdges = numOfAddedEdges;
		// TODO: use probability to generate m
		this.numOfStartNodes = numOfStartNode;
		this.p = p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		edgesList = new HashMap<String, Point>();
		nodeDegree = new int[nodes.length];
		int maxIter = 100;
		int temp;

		Random randForP = new Random();
		Random randForM = new Random();
		Random randForNode = new Random();

		// test
		int sumOfM = 0;
		int usedM = 0;

		/*
		 * Network ba = new BarabasiAlbert(this.numOfStartNodes, 3, null); Graph
		 * startGraph = ba.generate(); for (Edge e :
		 * startGraph.getEdges().getEdges()) { int src = e.getSrc(); int dst =
		 * e.getDst(); this.addEdge(src, dst); }
		 */
		/*
		 * for (int i = 1; i < this.numOfStartNodes; i++) { this.addEdge(i, i -
		 * 1); }
		 */
		for (int i = 1; i < this.numOfStartNodes; i++) {
			int dst = (new Random()).nextInt(i);
			this.addEdge(i, dst);
		}

		int i = this.numOfStartNodes;
		while (i < nodes.length) {
			// we start with m0 nodes connected through (m0 - 1) edges

			// m, the initial degree of new nodes in the GLP model, is a
			// constant integer. However, the initial degree can be a random
			// variable with some distribution.
			if (randForM.nextDouble() < 0.87) {
				this.numOfAddedEdges = 1;
			} else {
				this.numOfAddedEdges = 2;
			}

			// test
			sumOfM += this.numOfAddedEdges;
			usedM++;

			// with probability p we add m <= m0 new links.
			if (randForP.nextDouble() < p) {
				for (int j = 0; j < numOfAddedEdges; j++) {
					int src = this.selectNodeUsingPref(i - 1, randForNode);
					int dst = src;
					temp = 0;
					while ((dst == src || this.hasEdge(src, dst))
							&& temp < maxIter) {
						dst = this.selectNodeUsingPref(i - 1, randForNode);
						temp++;
					}
					addEdge(src, dst);
				}
			}
			// with probability (1 - p) we add a new node. The new nodes has m
			// new links
			else {
				for (int j = 0; j < numOfAddedEdges; j++) {
					int dst = i;
					temp = 0;
					while ((dst == i || this.hasEdge(i, dst)) && temp < maxIter) {
						dst = this.selectNodeUsingPref(i - 1, randForNode);
					}
					addEdge(i, dst);
				}
				i++;
			}
		}

		// test
		System.out.println("m = " + (((double) sumOfM) / usedM));

		// copy edges to graph
		Edges edges = new Edges(nodes, 2 * edgesList.size());
		for (Point p : edgesList.values()) {
			edges.add(p.x, p.y);
			edges.add(p.y, p.x);
		}

		// return graph
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	/**
	 * @param i
	 * @param nodeIndex
	 */
	private void addEdge(int src, int dst) {
		if (src == dst) {
			System.out.println("src = dst");
			return;
		}
		if (this.hasEdge(src, dst)) {
			// System.out.println("Edge is already existed");
			return;
		}
		this.edgesList.put(this.edge(src, dst), new Point(src, dst));
		this.edgesList.put(this.edge(dst, src), new Point(dst, src));
		this.nodeDegree[src]++;
		this.nodeDegree[dst]++;
	}

	private String edge(int src, int dst) {
		return "from " + src + " to " + dst;
	}

	private boolean hasEdge(int src, int dst) {
		if (this.edgesList.containsKey(this.edge(src, dst)))
			return true;
		if (this.edgesList.containsKey(this.edge(dst, src)))
			return true;
		return false;
	}

	private int selectNodeUsingPref(int maxIndex, Random rand) {
		double prefSum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			prefSum += nodeDegree[i] - beta;
		}
		double r = rand.nextDouble();
		double threshold = r * prefSum;
		double sum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			sum += nodeDegree[i] - this.beta;
			if (sum >= threshold) {
				return i;
			}
		}
		System.out.println("ERROR: While choosing node using preferences!");
		return (int) (r * maxIndex);
	}

}
