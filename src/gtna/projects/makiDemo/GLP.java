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
package gtna.projects.makiDemo;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * Implements the so-called Barabasi Albert network growth model described by
 * Tian Bu and Don Towsley in their publication
 * "On Distinguishing between Internet Power Law Topology Generators" (2002).
 * 
 * Parameters are the initial network size, the number of edges per added, the
 * probability to choose between two strategies and the parameter for the
 * nonlinear preference.
 * 
 * @author truong
 * 
 */
public class GLP extends Network {

	// parameters
	private int numOfStartNodes;
	private double p;
	private double numOfAddedEdges;
	private double beta;

	// variables for algorithm
	private int[] nodeDegree;
	private HashMap<String, Point> edgesList;
	private double mThreshold;
	private int low;
	private int high;

	public GLP(int nodes, int numOfStartNode, double numOfAddedEdges, double p,
			double beta, Transformation[] t) {
		super("GLP", nodes, new Parameter[] {
				new IntParameter("NUMBER_OF_START_NODES", numOfStartNode),
				new DoubleParameter("NUMBER_OF_ADDED_EDGES", numOfAddedEdges),
				new DoubleParameter("PROBABILITY", p),
				new DoubleParameter("BETA", beta) }, t);
		this.beta = beta;
		this.numOfAddedEdges = numOfAddedEdges;
		this.numOfStartNodes = numOfStartNode;
		this.p = p;
		this.calculateMThreshold();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		// init
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		edgesList = new HashMap<String, Point>();
		nodeDegree = new int[nodes.length];
		int maxIter = 100; // after this amount of iteration we will break the
							// loop if we can not choose a "valid" node to new
							// edges
		int temp;

		Random randForP = new Random();
		Random randForM = new Random();
		Random randForNode = new Random();

		// test for the random variable m
		int sumOfM = 0;
		int usedM = 0;

		// create a start graph from numOfStartNodes nodes and (numOfStartNodes
		// - 1) edges (as in paper)
		for (int i = 1; i < this.numOfStartNodes; i++) {
			int dst = (new Random()).nextInt(i);
			this.addEdge(i, dst);
		}

		// graph growths
		int i = this.numOfStartNodes;
		while (i < nodes.length) {
			// we want the value of m to be a float but a single value must be
			// integer. So we take two nearby values and for each iteration one
			// will be selected using a random variable
			if (randForM.nextDouble() < this.mThreshold) {
				this.numOfAddedEdges = this.low;
			} else {
				this.numOfAddedEdges = this.high;
			}
			// for test purpose
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

		// for test purpose
		System.out.println("m = " + (((double) sumOfM) / usedM));

		// copy edges to graph
		Edges edges = new Edges(nodes, edgesList.size());
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
	 * we have low < m < height. we must calculate the threshold, 0 <= threshold
	 * < 1, such that with: probability threshold: value = low, with probability
	 * (1 - threshold): value = height, then average value = m
	 */
	private void calculateMThreshold() {
		this.low = (int) Math.ceil(this.numOfAddedEdges);
		this.high = (int) Math.floor(this.numOfAddedEdges);
		this.mThreshold = ((double) (this.high - this.numOfAddedEdges))
				/ (this.high - this.low);
	}

	/**
	 * Add edge from src to dst to the graph
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

	/**
	 * Use for edges hash table
	 */
	private String edge(int src, int dst) {
		return "from " + src + " to " + dst;
	}

	/**
	 * Check if the edge from src to dst already existed
	 */
	private boolean hasEdge(int src, int dst) {
		if (this.edgesList.containsKey(this.edge(src, dst)))
			return true;
		if (this.edgesList.containsKey(this.edge(dst, src)))
			return true;
		return false;
	}

	/**
	 * Choose a node using the defined reference
	 * 
	 * @param maxIndex
	 *            range of node will be chosen: 0 -> maxIndex
	 * @param rand
	 *            random variable
	 * @return the index of the node
	 */
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