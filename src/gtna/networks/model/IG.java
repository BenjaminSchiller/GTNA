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
 * IG.java
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
public class IG extends Network {

	// parameters
	private double p;
	private int numOfStartNodes;

	// variables
	private HashMap<String, Point> edgesList;
	private int[] nodeDegree;

	public IG(int nodes, int numOfStartNodes, double probability,
			Transformation[] transformations) {
		super("IG", nodes, new Parameter[] {
				new IntParameter("NUMBER_OF_START_NODES", numOfStartNodes),
				new DoubleParameter("PROBABILITY", probability) }, null);
		this.p = probability;
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
		this.edgesList = new HashMap<String, Point>();
		this.nodeDegree = new int[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			this.nodeDegree[i] = 0;
		}

		// original random graph
		/*
		Network nw = new BarabasiAlbert(this.numOfStartNodes, 3, null);
		Graph startGraph = nw.generate();
		for (Edge e : startGraph.getEdges().getEdges()) {
			int src = e.getSrc();
			int dst = e.getDst();
			this.addEdge(src, dst);
		}*/
		
		for (int i = 1; i < this.numOfStartNodes; i++) {
			int temp = (new Random()).nextInt(i);
			this.addEdge(i, temp);
		}
		for (int i = 0; i < this.numOfStartNodes; i++) {
			if (this.nodeDegree[i] < 1) {
				System.out.println("Node " + i
						+ " is not connected in start graph");
			}
		}
		System.out.println("Start Graph generated!");

		Random rand = new Random();
		int temp;
		int maxIter = 100;
		// graph growths
		for (int i = this.numOfStartNodes; i < nodes.length; i++) {
			if (rand.nextDouble() < this.p) {
				// with probability p
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
				// with probability (1 - p)
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
	 * @param i
	 * @return
	 */
	private Random chooseNodeRand = new Random();

	private int chooseNode(int maxIndex) {
		int degreeSum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			degreeSum += this.nodeDegree[i];
		}
		double r = this.chooseNodeRand.nextDouble();
		double threshold = r * degreeSum;

		int sum = 0;
		for (int i = 0; i <= maxIndex; i++) {
			sum += this.nodeDegree[i];
			if (sum >= threshold) {
				return i;
			}
		}

		System.out.println("Cannot choose node using degree!");
		return Integer.MAX_VALUE;
	}

	/**
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
	}

	/**
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
	 * @param src
	 * @param dst
	 * @return
	 */
	private String edge(int src, int dst) {
		return "from " + src + " to " + dst;
	}

}
