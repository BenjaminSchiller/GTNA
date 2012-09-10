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
 * PFP1.java
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

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * Implements the so-called the Positive-Feedback Preference (PFP) model
 * described by Shi Zhou and Raul J. Mondragon in their publication
 * "Accurately modeling the internet topology" (2004).
 * 
 * Parameters are the initial network size, the two probabilities to choose
 * between three growth strategies and the parameter for the preference
 * 
 * @author truong
 * 
 */
public class PFP extends Network {

	// parameter
	private double p;
	private double q;
	private double delta;
	private int numOfStartNodes;

	// variables for algorithm
	private double[] nodePref;
	private HashMap<String, Point> edgesList;
	private int[] nodeDegree;
	private Random pRand = new Random();
	private Random prefRand = new Random();

	public PFP(int nodes, int numOfStartNodes, double probabilityP,
			double probabilityQ, double delta, Transformation[] transformations) {
		super("PFP", nodes, new Parameter[] {
				new DoubleParameter("PROBABILITY_P", probabilityP),
				new DoubleParameter("PROBABILITY_Q", probabilityQ),
				new DoubleParameter("DELTA", delta),
				new IntParameter("NUMBER_OF_START_NODES", numOfStartNodes) },
				transformations);
		this.p = probabilityP;
		this.q = probabilityQ;
		this.delta = delta;
		this.numOfStartNodes = numOfStartNodes;
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
		nodePref = new double[nodes.length];
		edgesList = new HashMap<String, Point>();
		nodeDegree = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			this.nodeDegree[i] = 0;
		}

		// In the paper, it was not shown what type of start graph they used. So
		// I take a BA graph to start with, because the IG network model was
		// developed
		// upon the BA network model
		Network ba = new BarabasiAlbert(this.numOfStartNodes, 3, null);
		Graph g = ba.generate();
		for (Edge e : g.getEdges().getEdges()) {
			int src = e.getSrc();
			int dst = e.getDst();
			this.addEdge(src, dst);
		}

		// graph growths
		for (int i = this.numOfStartNodes; i < nodes.length; i++) {
			double randVal = pRand.nextDouble();
			int maxIter = 100;
			int temp;
			if (randVal < this.p) {
				// 1. strategy with probability p, a new node is attached to one
				// host node, and at the same time one new internal link
				// appears between the host node and a peer node
				int hostIndex = this.chooseNode(i - 1);
				int peerIndex = hostIndex;
				temp = 0;
				while ((peerIndex == hostIndex || this.hasEdge(hostIndex,
						peerIndex)) && temp < maxIter) {
					peerIndex = this.chooseNode(i - 1);
					temp++;
				}
				this.addEdge(i, hostIndex);
				this.addEdge(hostIndex, peerIndex);
			} else if (randVal < this.p + this.q) {
				// 2. strategy with probability q, a new node is attached to one
				// host node, and at the same time two new internal links appear
				// between the host node and two peer nodes
				int hostIndex = this.chooseNode(i - 1);
				int peer1Index = hostIndex;
				temp = 0;
				while ((peer1Index == hostIndex || this.hasEdge(hostIndex,
						peer1Index)) && temp < maxIter) {
					peer1Index = this.chooseNode(i - 1);
					temp++;
				}
				int peer2Index = peer1Index;
				temp = 0;
				while ((peer2Index == peer1Index || peer2Index == hostIndex || this
						.hasEdge(hostIndex, peer2Index)) && temp < maxIter) {
					peer2Index = this.chooseNode(i - 1);
					temp++;
				}
				this.addEdge(i, hostIndex);
				this.addEdge(hostIndex, peer2Index);
				this.addEdge(hostIndex, peer1Index);
			} else {
				// 3. strategy with probability (1 - p - q), a new node is
				// attached to two host nodes, and dat the same time on new
				// internal link appears between one of the host nodes and one
				// peer node
				int host1Index = this.chooseNode(i - 1);
				int host2Index = host1Index;
				temp = 0;
				while (host2Index == host1Index && temp < maxIter) {
					host2Index = this.chooseNode(i - 1);
					temp++;
				}
				int peerIndex = host1Index;
				temp = 0;
				while ((peerIndex == host1Index || peerIndex == host2Index || this
						.hasEdge(peerIndex, host1Index)) && temp < maxIter) {
					peerIndex = this.chooseNode(i - 1);
					temp++;
				}
				this.addEdge(i, host1Index);
				this.addEdge(i, host2Index);
				this.addEdge(host1Index, peerIndex);
			}
		}

		// copy generated edges to graph
		Edges edges = new Edges(nodes, this.edgesList.size());
		for (Point p : this.edgesList.values()) {
			edges.add(p.x, p.y);
		}

		edges.fill();
		graph.setNodes(nodes);

		return graph;
	}

	/**
	 * Add an edge from src to dst to the graph
	 * 
	 * @param src
	 * @param dst
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
		this.updatePref(src);
		this.updatePref(dst);
	}

	/**
	 * used for hash table
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	private String edge(int src, int dst) {
		return "from " + src + " to " + dst;
	}

	/**
	 * check if an edge from src to dst already existed
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	private boolean hasEdge(int src, int dst) {
		if (this.edgesList.containsKey(this.edge(src, dst)))
			return true;
		if (this.edgesList.containsKey(this.edge(dst, src)))
			return true;
		return false;
	}

	/**
	 * update the preferences after a node added
	 * 
	 * @param index
	 */
	private void updatePref(int index) {
		int k = this.nodeDegree[index];
		this.nodePref[index] = Math.pow((double) k,
				1 + this.delta * Math.log((double) k));
	}

	/**
	 * choose a node from 1 to maxIndex using the defined preferences
	 * 
	 * @param maxIndex
	 * @return
	 */
	private int chooseNode(int maxIndex) {
		double prefSum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			prefSum += this.nodePref[i];
		}

		double r = prefRand.nextDouble();
		double randSum = r * prefSum;

		double sum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			sum += this.nodePref[i];
			if (sum >= randSum) {
				return i;
			}
		}

		System.out.println("Cannot choose node using preference");
		return Integer.MAX_VALUE;
	}

}