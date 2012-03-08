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
 * DeBruijn.java
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

/**
 * Implements a network generator for De Bruijn graph, a deterministic network
 * topology with small diameter despite low node degrees.
 * 
 * http://en.wikipedia.org/wiki/De_Bruijn_graph
 * 
 * Parameters are the base and the dimensions. This results in networks with
 * base^{dimensions} nodes.
 * 
 * @author benni
 * 
 */
public class DeBruijn extends Network {
	private int BASE;

	public DeBruijn(int BASE, int DIMENSIONS, Transformation[] t) {
		super("DE_BRUIJN", numberOfNodes(BASE, DIMENSIONS), new Parameter[] {
				new IntParameter("BASE", BASE),
				new IntParameter("DIMENSIONS", DIMENSIONS) }, t);
		this.BASE = BASE;
	}

	public static DeBruijn[] get(int[] b, int d, Transformation[] t) {
		DeBruijn[] nw = new DeBruijn[b.length];
		for (int i = 0; i < b.length; i++) {
			nw[i] = new DeBruijn(b[i], d, t);
		}
		return nw;
	}

	public static DeBruijn[] get(int b, int[] d, Transformation[] t) {
		DeBruijn[] nw = new DeBruijn[d.length];
		for (int i = 0; i < d.length; i++) {
			nw[i] = new DeBruijn(b, d[i], t);
		}
		return nw;
	}

	public static int numberOfNodes(int base, int dimensions) {
		int mod = 1;
		for (int i = 0; i < dimensions; i++) {
			mod *= base;
		}
		return mod;
	}

	public Graph generate() {
		Graph graph = new Graph(this.getDescription());
		Node[] nodes = Node.init(this.getNodes(), graph);
		Edges edges = new Edges(nodes, this.getNodes() * this.BASE - this.BASE);
		for (int i = 0; i < nodes.length; i++) {
			int shiftedId = (i * this.BASE) % nodes.length;
			for (int j = 0; j < this.BASE; j++) {
				if (i != shiftedId) {
					edges.add(i, shiftedId);
				}
				shiftedId++;
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}
