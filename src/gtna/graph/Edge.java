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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * Edge.java
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
package gtna.graph;

public class Edge {
	public Node src;

	public Node dst;

	public Edge(Node src, Node dst) {
		this.src = src;
		this.dst = dst;
	}

	// @Deprecated
	// public static void computeEdges(NodeImpl[] nodes, Edge[] edges) {
	// int[] in = new int[nodes.length];
	// int[] out = new int[nodes.length];
	// for (int i = 0; i < edges.length; i++) {
	// out[edges[i].src.index]++;
	// in[edges[i].dst.index]++;
	// }
	// computeEdges(nodes, edges, in, out);
	// }

	// @Deprecated
	// public static void computeEdges(NodeImpl[] nodes, Edge[] edges, int[] in,
	// int[] out) {
	// for (int i = 0; i < nodes.length; i++) {
	// nodes[i].in = new NodeImpl[in[i]];
	// nodes[i].out = new NodeImpl[out[i]];
	// in[i] = 0;
	// out[i] = 0;
	// }
	// for (int i = 0; i < edges.length; i++) {
	// NodeImpl src = edges[i].src;
	// NodeImpl dst = edges[i].dst;
	// src.out[out[src.index]] = dst;
	// dst.in[in[dst.index]] = src;
	// out[src.index]++;
	// in[dst.index]++;
	// }
	// }

	public String toString() {
		return toString(this.src, this.dst);
	}

	public static String toString(Node src, Node dst) {
		return toString(src.index(), dst.index());
	}

	public static String toString(int index1, int index2) {
		return index1 + " => " + index2;
	}
}
