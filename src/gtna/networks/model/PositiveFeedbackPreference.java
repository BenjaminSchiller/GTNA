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
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model;

import java.util.Random;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.Timer;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Tim
 * 
 */
public class PositiveFeedbackPreference extends Network {

	private double DELTA; //
	private double Q;
	private double P;
	private int INITIAL;
	private double pER = 0.4; // probability to create an edge in the initial
								// random network

	/**
	 * @param nodes
	 * @param initialsize
	 * @param p
	 * @param q
	 * @param delta
	 * @param t
	 */
	public PositiveFeedbackPreference(int nodes, int initialsize, double p,
			double q, double delta, Transformation[] t) {
		super("PFP", nodes, new Parameter[] {
				new IntParameter("INITIALSIZE", initialsize),
				new DoubleParameter("P", p), new DoubleParameter("Q", q),
				new DoubleParameter("DELTA", delta) }, t);

		this.P = p;
		this.Q = q;
		this.DELTA = delta;
		this.INITIAL = initialsize;
	}

	// TODO add getter for [] of PFP networks

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.networks.Network#generate()
	 */
	@Override
	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, INITIAL);

		int[] nodeDegree = new int[nodes.length];

		// initial random graph
		for (int src = 0; src < INITIAL; src++) {
			for (int dst = 0; dst < INITIAL; dst++) {
				if (src == dst) {
					continue;
				}
				if (rand.nextDouble() >= pER) {
					edges.add(src, dst);
					nodeDegree[src]++;
					nodeDegree[dst]++;
				}
			}
		}


		// grow network to add the missing nodes
		for (int newNodeIndex = INITIAL; newNodeIndex < nodes.length; newNodeIndex++) {


			double pi = rand.nextDouble();
			Timer t = new Timer();
			if (pi <= 1 - P - Q) {
				// new node: 2 links to host nodes
				// 1 of the host nodes: 1 link to a peer

				// add links with the new node
				int h1 = getNPPNode(nodes, nodeDegree, edges, newNodeIndex, -1,
						newNodeIndex, rand);
				int h2 = getNPPNode(nodes, nodeDegree, edges, newNodeIndex, h1,
						newNodeIndex, rand);
				edges.add(newNodeIndex, h1);
				edges.add(newNodeIndex, h2);
				nodeDegree[newNodeIndex]++;
				nodeDegree[h1]++;
				nodeDegree[h2]++;

				// add link of one of the host nodes
				int h = (rand.nextDouble() < 0.5) ? h1 : h2;
				int peer = getNPPNode(nodes, nodeDegree, edges, newNodeIndex,
						h, h, rand);
				edges.add(h, peer);
				nodeDegree[h]++;
				nodeDegree[peer]++;



			} else if (pi <= 1 - P) {
				// new node: 1 link to a host node
				// host node: 2 links to peers

				// add link with the new node
				int h = getNPPNode(nodes, nodeDegree, edges, newNodeIndex, -1,
						newNodeIndex, rand);
				edges.add(newNodeIndex, h);
				nodeDegree[newNodeIndex]++;
				nodeDegree[h]++;

				int peer1 = -1;
				int peer2 = -1;
				// add 2 links of the host node
				for (int j = 0; j < 2; j++) {
					int peer = getNPPNode(nodes, nodeDegree, edges,
							newNodeIndex, h, h, rand);
					if (j == 0)
						peer1 = peer;
					else
						peer2 = peer; // TODO
					edges.add(h, peer);
					nodeDegree[h]++;
					nodeDegree[peer]++;
				}

			} else if (pi <= 1) {
				// new node: 1 link to a host node
				// host node: 1 links to a peer

				// add link with the new node
				int h = getNPPNode(nodes, nodeDegree, edges, newNodeIndex, -1,
						newNodeIndex, rand);
				edges.add(newNodeIndex, h);
				nodeDegree[newNodeIndex]++;
				nodeDegree[h]++;

				// add link of the host node
				int peer = getNPPNode(nodes, nodeDegree, edges, newNodeIndex,
						h, h, rand);
				edges.add(h, peer);
				nodeDegree[h]++;
				nodeDegree[peer]++;


			}
			t.end();

		}

		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	/**
	 * @param nodes
	 * @param maxId
	 * @param h1
	 * @return
	 */
	private int getNPPNode(Node[] nodes, int[] degrees, Edges e, int maxId,
			int ignoreNode, int srcNode, Random rng) {

		
		int npp = -1;

		double sumK = 0;

		for (int j = 0; j < maxId; j++) {
			int kj = degrees[j];
			sumK += Math.pow(kj, calcExponent(kj));
		}

		double sumK2 = 0;

		while (npp < 0) {
			double takeNode = rng.nextDouble();
			for (int k = 0; k < maxId; k++) {

				int kj = degrees[k];
				sumK2 += Math.pow(kj, calcExponent(kj)) / sumK;

				if (sumK2 > takeNode && k != ignoreNode
						&& !e.contains(srcNode, k)) {
					return npp = k;
				}
			}
		}
		// should not be reachable!
		return npp;
	}

	/**
	 * @param i
	 * @return
	 */
	private double calcExponent(int i) {
		double exponent;

		double logi = Math.log10(i);

		exponent = 1 + (DELTA * logi);

		return exponent;
	}

}
