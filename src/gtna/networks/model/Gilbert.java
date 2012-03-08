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
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

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
public class Gilbert extends Network {
	private double p;

	private boolean bidirectional;

	public Gilbert(int nodes, double p, boolean bidirectional,
			Transformation[] t) {
		super("GILBERT", nodes, new Parameter[] {
				new DoubleParameter("C", p * nodes),
				new BooleanParameter("BIDIRECTIONAL", bidirectional) }, t);
		this.p = p;
		this.bidirectional = bidirectional;
	}

	public static Gilbert[] get(int nodes, double[] p, boolean bidirectional,
			RoutingAlgorithm ra, Transformation[] t) {
		Gilbert[] nw = new Gilbert[p.length];
		for (int i = 0; i < p.length; i++) {
			nw[i] = new Gilbert(nodes, p[i], bidirectional, t);
		}
		return nw;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes,
				(int) (this.p * this.getNodes() * this.getNodes()));
		// double P = this.bidirectional ? this.p / 2 : this.p;
		double P = this.p;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i == j) {
					continue;
				}
				if (rand.nextDouble() <= P) {
					edges.add(i, j);
					if (this.bidirectional) {
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
