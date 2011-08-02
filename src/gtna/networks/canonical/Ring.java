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
 * Ring.java
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
package gtna.networks.canonical;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.NetworkImpl;
import gtna.routingOld.RoutingAlgorithm;
import gtna.transformation.Transformation;

/**
 * Implements the network generator for a ring network of given size. In a ring,
 * every node has exactly two undirected connection to its predecessor and
 * successor in a logical ring, forming one cycle.
 * 
 * @author benni
 * 
 */
public class Ring extends NetworkImpl implements Network {
	public Ring(int nodes, RoutingAlgorithm ra, Transformation[] t) {
		super("RING", nodes, new String[] {}, new String[] {}, ra, t);
	}

	public Graph generate() {
		Graph graph = new Graph(this.description());
		Node[] nodes = Node.init(this.nodes(), graph);
		Edges edges = new Edges(nodes, this.nodes() * 2);
		for (int i = 0; i < nodes.length; i++) {
			edges.add(i, (i + 1) % nodes.length);
			edges.add((i + 1) % nodes.length, i);
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}
