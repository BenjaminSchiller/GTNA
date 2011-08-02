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
 * Gilbert.java
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
import gtna.networks.NetworkImpl;
import gtna.routingOld.RoutingAlgorithm;
import gtna.transformation.Transformation;

import java.util.Random;

/**
 * Implements a network generator for the so-called Gilbert model G(n,p). It
 * creates a random graph G(n,p) with a given number of nodes (n). Every
 * possible edge between two nodes ist created with the given possibility (p).
 * While this construction results in basically the same random networks as the
 * Erdos-Renyi model, the number of edges is not fixed fluctuates around the
 * average value of p*n*n.
 * 
 * http://en.wikipedia.org/wiki/Gilbert_Model
 * 
 * Parameters are the number of edges and a flag for the bidirectionality of
 * edges.
 * 
 * Note that in this implementation, loops are not permitted, i.e., there is no
 * edge of the form (a,a).
 * 
 * @author benni
 * 
 */
public class Gilbert extends NetworkImpl implements Network {
	private double EDGES;

	private boolean BIDIRECTIONAL;

	public Gilbert(int nodes, double EDGES, boolean BIDIRECTIONAL,
			RoutingAlgorithm ra, Transformation[] t) {
		super("GILBERT", nodes, new String[] { "EDGES", "BIDIRECTIONAL" },
				new String[] { "" + EDGES, "" + BIDIRECTIONAL }, ra, t);
		this.EDGES = EDGES;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
	}

	public Graph generate() {
		Graph graph = new Graph(this.description());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.nodes(), graph);
		double p = (double) this.EDGES / (double) (this.nodes() * this.nodes());
		if (this.BIDIRECTIONAL) {
			p /= 2;
		}
		Edges edges = new Edges(nodes, (int) (this.EDGES * 1.05));
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i == j) {
					continue;
				}
				if (rand.nextDouble() < p) {
					edges.add(i, j);
					if (this.BIDIRECTIONAL) {
						edges.add(j, i);
					}
				}
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}
