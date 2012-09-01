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
	private ArrayList<Point> edgesList;

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
		System.out.println("m0 = " + numOfStartNodes);
		System.out.println("p = " + p);
		System.out.println("m = " + numOfAddedEdges);
		System.out.println("beta = " + beta);

		// Graph graph = new Graph(this.getDescription());
		Graph graph = new Graph("test");
		Node[] nodes = Node.init(this.getNodes(), graph);
		// nodePref = new double[nodes.length];
		edgesList = new ArrayList<Point>();
		nodeDegree = new int[nodes.length];

		Random randForP = new Random();
		Random randForM = new Random();
		int i = 1;

		// test
		int sumOfM = 0;
		int usedM = 0;

		while (i < nodes.length) {
			// we start with m0 nodes connected through (m0 - 1) edges
			if (i < numOfStartNodes) {
				// select a node randomly from 0 to (i - 1)
				int nodeIndex = (new Random()).nextInt(i);
				addEdge(i, nodeIndex);
				i++;
				continue;
			}

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

			Random randForNode = new Random();

			// with probability p we add m <= m0 new links.
			if (randForP.nextDouble() < p) {
				for (int j = 0; j < numOfAddedEdges; j++) {
					int src = this.selectNodeUsingPref(i - 1, randForNode);
					int dst = src;
					while (dst == src) {
						dst = this.selectNodeUsingPref(i - 1, randForNode);
					}
					addEdge(src, dst);
				}
			}
			// with probability (1 - p) we add a new node. The new nodes has m
			// new links
			else {
				for (int j = 0; j < numOfAddedEdges; j++) {
					int dst = this.selectNodeUsingPref(i - 1, randForNode);
					// TODO: is it already used???
					addEdge(i, dst);
				}
				i++;
			}
		}

		// test
		System.out.println("m = " + (((double) sumOfM) / usedM));

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

	/**
	 * @param i
	 * @param nodeIndex
	 */
	private void addEdge(int src, int dst) {
		edgesList.add(new Point(src, dst));
		nodeDegree[src]++;
		nodeDegree[dst]++;
		// updatePreference(src);
		// updatePreference(dst);
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
			sum += nodeDegree[i];
			if (sum >= threshold) {
				return i;
			}
		}
		return (int) (r * maxIndex);
	}

}
