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
 * PositiveFeedbackPreference.java
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
import java.util.Random;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.canonical.Complete;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author truong
 * 
 */
public class PFP extends Network {

	// parameter
	private double p;
	private double delta;
	private int numOfStartNodes;

	// variables for algorithm
	private double[] nodePref;
	private ArrayList<Point> edgesList;
	private double[] nodeDegree;

	/**
	 * @param key
	 * @param nodes
	 * @param parameters
	 * @param transformations
	 */
	public PFP(int nodes, int numOfStartNodes, double probability,
			double delta, Transformation[] transformations) {
		super("PFP", nodes, new Parameter[] {
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
		System.out.println("p = " + this.p);
		System.out.println("delta = " + this.delta);
		// Graph graph = new Graph(this.getDescription());
		Graph graph = new Graph("test");
		Node[] nodes = Node.init(this.getNodes(), graph);
		nodePref = new double[nodes.length];
		edgesList = new ArrayList<Point>();
		nodeDegree = new double[nodes.length];

		Random rand = new Random();

		// original random graph
		Complete ba = new Complete(this.numOfStartNodes, null);
		Graph g = ba.generate();
		for (Edge e : g.getEdges().getEdges()) {
			int src = e.getSrc();
			int dst = e.getDst();
			this.addEdge(src, dst);
		}

		for (int i = this.numOfStartNodes; i < nodes.length; i++) {
			// TODO: generate original random graph
			// ---Test---
			/*
			 * if (i < this.numOfStartNodes) { for (int j = 0; j < i; j++) {
			 * this.addEdge(i, j); } continue; }
			 */
			// ---
			if (rand.nextDouble() < p) {

				// a new node is attached to "host"
				int hostIndex = this.selectNodeUsingPref(i - 1);
				addEdge(i, hostIndex);

				// the host develops new links to two peers
				int peer1Index = hostIndex;
				while (peer1Index == hostIndex) {
					peer1Index = this.selectNodeUsingPref(i - 1);
				}
				int peer2Index = hostIndex;
				while (peer2Index == peer1Index || peer2Index == hostIndex) {
					peer2Index = this.selectNodeUsingPref(i - 1);
					// System.out.println("host = " + hostIndex);
					// System.out.println("peer1 = " + peer1Index);
					// System.out.println("perr2 = " + peer2Index);
				}
				addEdge(hostIndex, peer1Index);
				addEdge(hostIndex, peer2Index);

			} else {

				// a new node is attached to two hosts
				int host1Index = this.selectNodeUsingPref(i - 1);
				int host2Index = host1Index;
				while (host1Index == host2Index) {
					host2Index = this.selectNodeUsingPref(i - 1);
					// System.out.println("host1 = " + host1Index);
					// System.out.println("host2 = " + host2Index);
				}
				addEdge(i, host1Index);
				addEdge(i, host2Index);

				// one of the hosts is linked to a peer
				int peerIndex = host1Index;
				while (peerIndex == host1Index || peerIndex == host2Index) {
					peerIndex = this.selectNodeUsingPref(i - 1);
					// System.out.println("peer = " + peerIndex);
				}
				if ((new Random()).nextInt(2) == 0) {
					addEdge(host1Index, peerIndex);
				} else {
					addEdge(host2Index, peerIndex);
				}
			}
		}

		// copy edges to graph
		Edges edges = new Edges(nodes, 2 * edgesList.size());
		for (Point p : edgesList) {
			edges.add(p.x, p.y);
			edges.add(p.y, p.x);
		}

		// return graph
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	private void updatePreference(int nodeIndex) {
		double degree = (double) (nodeDegree[nodeIndex]);
		nodePref[nodeIndex] = Math.pow(degree, 1 + delta * Math.log(degree));
	}

	private Random refRand = new Random();

	private int selectNodeUsingPref(int maxIndex) {
		double prefSum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			prefSum += nodePref[i];
		}
		double r = refRand.nextDouble();
		double threshold = r * prefSum;
		double sum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			sum += nodePref[i];
			if (sum >= threshold) {
				return i;
			}
		}
		return (int) (r * maxIndex);
	}

	private void addEdge(int src, int dst) {
		for (Point p : this.edgesList) {
			if ((p.x == src && p.y == dst) || (p.x == dst && p.y == src))
				return;
		}
		edgesList.add(new Point(src, dst));
		nodeDegree[src]++;
		nodeDegree[dst]++;
		updatePreference(src);
		updatePreference(dst);
	}
}
