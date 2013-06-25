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
 * ErdosRenyi.java
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
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * Implements a generator for a regular network. 
 * A regular network is a network with a constant node degree.
 * 
 * http://en.wikipedia.org/wiki/Regular_graph
 * 
 * The parameters are 
 * 	- the number of nodes, 
 * 	- the node degree as well as
 * 	- a flag for ring or random topology 
 * 	- a flag for the bidirectionality of edges
 * 
 * Note that in this implementation, loops are not permitted, i.e., there is no
 * edge of the form (a,a).
 * 
 * @author Tim  
 */
public class Regular extends Network {
	private double AVERAGE_DEGREE;

	private boolean BIDIRECTIONAL;

	public static Regular[] get(int[] n, int d, boolean r, boolean b,
			Transformation[] t) {
		Regular[] nw = new Regular[n.length];
		for (int i = 0; i < n.length; i++) {
			nw[i] = new Regular(n[i], d, r, b, t);
		}
		return nw;
	}

	public static Regular[] get(int n, int[] d, boolean[] r, boolean b,
			Transformation[] t) {
		Regular[] nw = new Regular[d.length];
		for (int i = 0; i < d.length; i++) {
			nw[i] = new Regular(n, d[i], r[i], b, t);
		}
		return nw;
	}

	public static Regular[][] get(int[] n, double[] d, boolean b,
			Transformation[] t) {
		Regular[][] nw = new Regular[d.length][n.length];
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < n.length; j++) {
				nw[i][j] = new Regular(n[j], d[i], b, t);
			}
		}
		return nw;
	}

	public Regular(int nodes, int degree, boolean RING, boolean BIDIRECTIONAL,
			Transformation[] t) {
		super("ERDOS_RENYI", nodes, new Parameter[] {
				new DoubleParameter("AVERAGE_DEGREE", AVERAGE_DEGREE),
				new BooleanParameter("BIDIRECTIONAL", BIDIRECTIONAL) }, t);
		this.AVERAGE_DEGREE = AVERAGE_DEGREE;
		this.BIDIRECTIONAL = BIDIRECTIONAL;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Random rand = new Random(System.currentTimeMillis());
		Node[] nodes = Node.init(this.getNodes(), graph);
		int toAdd = (int) (this.AVERAGE_DEGREE * this.getNodes() / 2);
		Edges edges = new Edges(nodes, toAdd);
		while (edges.size() < toAdd) {
			int src = rand.nextInt(nodes.length);
			int dst = rand.nextInt(nodes.length);
			if (src == dst) {
				continue;
			}
			if (this.BIDIRECTIONAL) {
				edges.add(src, dst);
				edges.add(dst, src);
			} else {
				edges.add(src, dst);
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}
