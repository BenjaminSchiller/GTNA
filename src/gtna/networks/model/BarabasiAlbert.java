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

import java.util.Random;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.Timer;

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
public class BarabasiAlbert extends NetworkImpl implements Network {
	private int INIT_NETWORK_SIZE = 10;

	private int EDGES_PER_NODE = 3;

	public BarabasiAlbert(int nodes, int EDGES_PER_NODE, RoutingAlgorithm ra,
			Transformation[] t) {
		super("BARABASI_ALBERT", nodes, new String[] { "EDGES_PER_NODE" },
				new String[] { "" + EDGES_PER_NODE }, ra, t);
		this.EDGES_PER_NODE = EDGES_PER_NODE;
	}

	public Graph generate() {
		Timer timer = new Timer();
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.nodes());
		int[] in = new int[nodes.length];
		int[] out = new int[nodes.length];

		int initNodes = Math.max(this.INIT_NETWORK_SIZE,
				this.EDGES_PER_NODE + 5);
		int initEdges = initNodes * this.EDGES_PER_NODE;
		Graph temp = new ErdosRenyi(initNodes, initEdges, true, this
				.routingAlgorithm(), this.transformations()).generate();
		Edges edges = new Edges(nodes, initEdges + (nodes.length - initNodes)
				* this.EDGES_PER_NODE);
		for (int i = 0; i < temp.nodes.length; i++) {
			in[i] = temp.nodes[i].in().length;
			out[i] = temp.nodes[i].out().length;
			Node[] Out = temp.nodes[i].out();
			for (int j = 0; j < Out.length; j++) {
				edges.add(nodes[i], nodes[Out[j].index()]);
			}
		}

		int edgeCounter = initEdges;
		for (int i = initNodes; i < nodes.length; i++) {
			int added = 0;
			while (added < this.EDGES_PER_NODE) {
				int dest = rand.nextInt(i);
				if (edges.contains(i, dest)) {
					continue;
				}
				double pi = (double) (in[i] + out[i])
						/ (double) (2 * edgeCounter);
				if (rand.nextDouble() <= pi) {
					in[i]++;
					out[i]++;
					in[dest]++;
					out[dest]++;
					edgeCounter++;
					added++;
				}
			}
		}

		edges.fill();

		timer.end();
		Graph g = new Graph(this.description(), nodes, timer);
		return g;
	}
}
