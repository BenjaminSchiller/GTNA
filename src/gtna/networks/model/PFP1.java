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
package gtna.networks.model;

import java.awt.Point;
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
public class PFP1 extends Network {

	// parameter
	private double p;
	private double delta;
	private int numOfStartNodes;

	// variables for algorithm
	private double[] nodePref;
	private HashMap<String, Point> edgesList;
	private int[] nodeDegree;
	private Random pRand = new Random();
	private Random prefRand = new Random();

	public PFP1(int nodes, int numOfStartNodes, double probability,
			double delta, Transformation[] transformations) {
		super("PFP1", nodes, new Parameter[] {
				new DoubleParameter("PROBABILITY", probability),
				new DoubleParameter("DELTA", delta),
				new IntParameter("NUMBER_OF_START_NODES", numOfStartNodes) },
				transformations);
		this.p = probability;
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

		// original random graph
		Network ba = new ErdosRenyi(this.numOfStartNodes, 3, true, null);
		Graph g = ba.generate();
		for (Edge e : g.getEdges().getEdges()) {
			int src = e.getSrc();
			int dst = e.getDst();
			this.addEdge(src, dst);
		}
		System.out.println("Start graph generated");

		// graph growths
		for (int i = this.numOfStartNodes; i < nodes.length; i++) {
			if (pRand.nextDouble() < this.p) {
				// 1. strategy with probability p
				int hostIndex = this.chooseNode(i - 1);
				int peer1Index = hostIndex;
				while (peer1Index == hostIndex
						|| this.hasEdge(hostIndex, peer1Index)) {
					peer1Index = this.chooseNode(i - 1);
				}
				int peer2Index = hostIndex;
				while (peer2Index == hostIndex || peer2Index == peer1Index
						|| this.hasEdge(hostIndex, peer2Index)) {
					peer2Index = this.chooseNode(i - 1);
				}
				this.addEdge(i, hostIndex);
				this.addEdge(hostIndex, peer1Index);
				this.addEdge(hostIndex, peer2Index);
			} else {
				// 2. strategy with probability (1 - p)
				int host1Index = this.chooseNode(i - 1);
				int host2Index = host1Index;
				while (host2Index == host1Index) {
					host2Index = this.chooseNode(i - 1);
				}
				int peerIndex = host1Index;
				while (peerIndex == host1Index
						|| this.hasEdge(host1Index, peerIndex)) {
					peerIndex = this.chooseNode(i - 1);
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

	private void updatePref(int index) {
		int k = this.nodeDegree[index];
		this.nodePref[index] = Math.pow((double) k,
				1 + this.delta * Math.log((double) k));
	}

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
