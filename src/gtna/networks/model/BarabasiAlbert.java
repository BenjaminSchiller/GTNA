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
 * BarabasiAlbert.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.networks.model;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Arrays;
import java.util.Random;

/**
 * Implements the so-called Barabasi Albert network growth model described by
 * Barabasi and Albert in their publication
 * "Emergence of scaling in random networks" (1999).
 * 
 * http://en.wikipedia.org/wiki/Barabasi–Albert_model
 * 
 * Parameters are the initial network size and the number of edges per added
 * node.
 * 
 * @author benni
 * 
 */
public class BarabasiAlbert extends Network {
	private int INIT_NETWORK_SIZE = 10;

	private int EDGES_PER_NODE = 3;

	public static BarabasiAlbert[] get(int nodes, int[] edgesPerNode,
			Transformation[] t) {
		BarabasiAlbert[] nw = new BarabasiAlbert[edgesPerNode.length];
		for (int i = 0; i < edgesPerNode.length; i++) {
			nw[i] = new BarabasiAlbert(nodes, edgesPerNode[i], t);
		}
		return nw;
	}

	public static BarabasiAlbert[] get(int[] nodes, int edgesPerNode,
			Transformation[] t) {
		BarabasiAlbert[] nw = new BarabasiAlbert[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw[i] = new BarabasiAlbert(nodes[i], edgesPerNode, t);
		}
		return nw;
	}

	public BarabasiAlbert(int nodes, int EDGES_PER_NODE, Transformation[] t) {
		super("BARABASI_ALBERT", nodes, new Parameter[] { new IntParameter(
				"EDGES_PER_NODE", EDGES_PER_NODE) }, t);
		this.EDGES_PER_NODE = EDGES_PER_NODE;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		int[] in = new int[nodes.length];
		int[] out = new int[nodes.length];

		int initNodes = Math.max(this.INIT_NETWORK_SIZE,
				this.EDGES_PER_NODE + 5);
		int initEdges = initNodes * this.EDGES_PER_NODE;
		int ed = 0;
		Graph temp = new ErdosRenyi(initNodes, this.EDGES_PER_NODE, true, null)
				.generate();
		Edges edges = new Edges(nodes, initEdges + (nodes.length - initNodes)
				* this.EDGES_PER_NODE);
		for (int i = 0; i < temp.getNodes().length; i++) {
			in[i] = temp.getNodes()[i].getInDegree();
			out[i] = temp.getNodes()[i].getOutDegree();
			int[] Out = temp.getNodes()[i].getOutgoingEdges();
			for (int j = 0; j < Out.length; j++) {
				edges.add(i, Out[j]);
				ed++;
			}
		}

		// System.out.println("init = " + initEdges + " ed=" +ed);
		int edgeCounter = edges.size();
		for (int i = initNodes; i < nodes.length; i++) {
			int added = 0;
			double[] rands = new double[this.EDGES_PER_NODE];
			for (int j = 0; j < rands.length; j++) {
				rands[j] = rand.nextDouble() * 2 * edgeCounter;
			}
			Arrays.sort(rands);

			// System.out.println("i=" + i + " rands:");
			// for (int j = 0; j < rands.length; j++){
			// System.out.println("" + rands[j] + " edges=" + edgeCounter);
			// }
			double sum2 = 0;
			int current = i - 1;
			while (added < rands.length && current > -1) {
				sum2 = sum2 + (double) (in[current] + out[current]);
				// System.out.println("Sum " + sum2 + " i="+i +
				// " edgeCounter = " + edgeCounter + " current=" + current);
				if (sum2 >= rands[added]) {
					edges.add(i, current);
					edges.add(current, i);
					added++;
					in[i]++;
					out[i]++;
					in[current]++;
					out[current]++;
					edgeCounter = edgeCounter + 2;

				}
				current--;
			}
			// System.out.println("Added " + added);

		}

		edges.fill();

		graph.setNodes(nodes);
		return graph;
	}
}
