/*
 * ===========================================================
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
 * ZhouMOndragon.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Tim Grube;
 * Contributors:    -;
 * 
 * Changes since 2013-09-02
 * ---------------------------------------
 */
package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.DeterministicRandom;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.Random;

/**
 * Implements the so-called Rich Club network described by Zhou and Mondragon in
 * their publication "The Rich-Club Phenomenon In The Internet" (2003)
 * 
 * Parameters are the initial network size and the probabilities for adding a
 * new node, a new edge or both
 * 
 * @author tim
 * 
 */
public class ZhouMondragon extends Network {
	private int INIT_NETWORK_SIZE = 10;

	private double p;
	private double q;

	private Random rng;

	public static ZhouMondragon[] get(int nodes, double[] newEdgeProbability,
			Transformation[] t) {
		int non = newEdgeProbability.length;
		ZhouMondragon[] nw = new ZhouMondragon[non];
		for (int i = 0; i < non; i++) {
			nw[i] = new ZhouMondragon(nodes, newEdgeProbability[i], t);
		}
		return nw;
	}

	public static ZhouMondragon[] get(int[] nodes, double newEdgeProbability,
			Transformation[] t) {
		ZhouMondragon[] nw = new ZhouMondragon[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw[i] = new ZhouMondragon(nodes[i], newEdgeProbability, t);
		}
		return nw;
	}

	public ZhouMondragon(int nodes, double newEdgeProbability,
			Transformation[] t) {
		super("ZHOU_MONDRAGON", nodes, new Parameter[] { new DoubleParameter(
				"EDGE_PROBABILITY", newEdgeProbability) }, t);
		this.p = newEdgeProbability;

	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		rng = new DeterministicRandom(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);

		Graph temp = new ErdosRenyi(INIT_NETWORK_SIZE, 3, true, null)
				.generate();
		Edges edges = new Edges(nodes, nodes.length);

		int[] in = new int[this.getNodes()];
		int[] out = new int[this.getNodes()];

		Arrays.fill(in, 0);
		Arrays.fill(out, 0);

		for (int i = 0; i < temp.getNodes().length; i++) {
			in[i] = temp.getNodes()[i].getInDegree();
			out[i] = temp.getNodes()[i].getOutDegree();
			int[] Out = temp.getNodes()[i].getOutgoingEdges();
			for (int j = 0; j < Out.length; j++) {
				edges.add(i, Out[j]);
			}
		}

		boolean addedNode = false;
		double c;

		for (int i = INIT_NETWORK_SIZE; i < nodes.length; i++) {
			addedNode = false;
			while (!addedNode) {
				c = rng.nextDouble();
				if (c > (p)) {
					edges = addNewNode(i, Math.abs(rng.nextInt()), c, in, out,
							edges);
					addedNode = true;
				} else {
					edges = addNewEdge(i, in, out, c, edges);
					addedNode = false;
				}
			}
		}

		edges.fill();

		graph.setNodes(nodes);
		return graph;
	}

	/**
	 * @param i
	 * @param in
	 * @param out
	 * @return
	 */
	private Edges addNewNode(int i, int ri, double c, int[] in, int[] out,
			Edges edges) {
		int di = ri % INIT_NETWORK_SIZE;

		for (int j = 0; j < di; j++) {
			edges = addNewEdge(i, in, out, c, edges);
		}

		return edges;
	}

	/**
	 * 
	 * @param s
	 *            index of the <b>source</b> of the new edge
	 * @param out
	 * @param in
	 * @param dn
	 *            networkdegree
	 * @param edges
	 *            current edges of the graph
	 * @return
	 */
	private Edges addNewEdge(int s, int[] in, int[] out, double c, Edges edges) {
		int dn = edges.size(); // network degree

		int i = 0;
		while (true) {
			if (i < in.length) {
				int d = rng.nextInt(s);
				int dd = in[d]+out[d]; // (potential) destination degree

				if (s != d && !edges.contains(s, d)) {
					double np = (double) dd / (double) dn;
					if (c < np) {
						edges.add(s, d);
						out[s]++;
						in[d]++;
						return edges;
					} else {
						i++;
					}
				}
			} else {
				i = 0;
				c = rng.nextDouble();
				System.err.println("Choosen new c for adding a new Edge: " + c);
			}
		}
	}
}
