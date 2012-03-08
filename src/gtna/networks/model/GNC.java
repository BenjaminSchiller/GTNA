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
 * GNC.java
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
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * Implements a network generator for GNC, the Growing Network with copying.
 * This network growth model was introduced by Krapivsky and Redner in their
 * paper "Network growth by copying" (2005) which is based on their work with GN
 * and GNR. In each step, a new node joins the network. It connects to a random
 * bootstrap node already in the system and established connections to all
 * neighbors of that node ("copies" the boostrap node's links).
 * 
 * Parameters are a flag for the bidirectionality of links and a flag for the
 * creation of an reverse edge to the bootstrap node. If bidirectionality is
 * true, every edge is added in both directions. if edgeBack is true, an edge
 * between bootstrap node and joining node is established at all times
 * independent from the bidirectionality flag. Note that in the original model
 * proposed by Krapivsky and Redner both parameters would be set to false since
 * all connections are unidirectional.
 * 
 * @author benni
 * 
 */
public class GNC extends Network {
	private boolean BIDIRECTIONAL = false;

	private boolean EDGE_BACK = true;

	public GNC(int nodes, boolean BIDIRECTIONAL, boolean EDGE_BACK,
			Transformation[] t) {
		super("GNC", nodes, new Parameter[] {
				new BooleanParameter("BIDIRECTIONAL", BIDIRECTIONAL),
				new BooleanParameter("EDGE_BACK", EDGE_BACK) }, t);
		this.BIDIRECTIONAL = BIDIRECTIONAL;
		this.EDGE_BACK = EDGE_BACK;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, 100);
		for (int i = 1; i < nodes.length; i++) {
			int bootstrap = rand.nextInt(i);
			edges.add(i, bootstrap);
			if (this.BIDIRECTIONAL || this.EDGE_BACK) {
				edges.add(bootstrap, i);
			}
			int[] out = nodes[bootstrap].getOutgoingEdges();
			for (int j = 0; j < out.length; j++) {
				edges.add(i, out[j]);
				if (this.BIDIRECTIONAL) {
					edges.add(out[j], i);
				}
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}
