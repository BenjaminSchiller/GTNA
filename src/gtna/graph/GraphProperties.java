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
 * GraphProperties.java
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

// TODO reimplement using new interfaces
public class GraphProperties {
	/**
	 * Evaluates if the given graph is bidirectional, i.e., there exists an edge
	 * (b,a) for every edge (a,b)
	 * 
	 * @param g
	 * @return true if the graph is bidirectional, false otherwise
	 */
	// public static boolean bidirectional(Graph g) {
	// Edges edgeSet = new Edges(g.nodes, g.edges);
	// edgeSet.add(g.edges());
	// Edge[] edges = g.edges();
	// for (int i = 0; i < edges.length; i++) {
	// Edge back = new Edge(edges[i].dst, edges[i].src);
	// if (!edgeSet.contains(back)) {
	// return false;
	// }
	// }
	// return true;
	// }

	/**
	 * Evaluates if the graph contains duplicate edges, i.e., (a,b) is found
	 * twice.
	 * 
	 * @param g
	 * @return true if the graph contains duplicate edges, false otherwise
	 */
	// public static boolean duplicateEdges(Graph g) {
	// for (int i = 0; i < g.nodes.length; i++) {
	// boolean[] out = new boolean[g.nodes.length];
	// Node[] Out = g.nodes[i].out();
	// for (int j = 0; j < Out.length; j++) {
	// if (out[Out[j].index()]) {
	// return true;
	// }
	// out[Out[j].index()] = true;
	// }
	// boolean[] in = new boolean[g.nodes.length];
	// Node[] In = g.nodes[i].in();
	// for (int j = 0; j < In.length; j++) {
	// if (in[In[j].index()]) {
	// return true;
	// }
	// in[In[j].index()] = true;
	// }
	// }
	// return false;
	// }

	/**
	 * Evaluates if the outgoing and incoming edges of all nodes are set
	 * correctly, i.e., for every outgoing edge (a,b) at node a there exists an
	 * incoming edge (a,b) at node b.
	 * 
	 * @param g
	 * @return true if the outgoing and incoming edges of all nodes are
	 *         consistent, false otherwise
	 */
	// public static boolean outEqualsIn(Graph g) {
	// for (int i = 0; i < g.nodes.length; i++) {
	// Node[] out = g.nodes[i].out();
	// for (int j = 0; j < out.length; j++) {
	// if (!out[j].hasIn(g.nodes[i])) {
	// return false;
	// }
	// }
	// Node[] in = g.nodes[i].in();
	// for (int j = 0; j < in.length; j++) {
	// if (!in[j].hasOut(g.nodes[i])) {
	// return false;
	// }
	// }
	// }
	// return true;
	// }

	/**
	 * Evaluates if the given graph contains loops, i.e., edges of the form
	 * (a,a).
	 * 
	 * @param g
	 * @return true if the graph contains loops, false otherwise
	 */
	// public static boolean containsLoops(Graph g) {
	// for (int i = 0; i < g.nodes.length; i++) {
	// Node[] out = g.nodes[i].out();
	// for (int j = 0; j < out.length; j++) {
	// if (out[j].index() == i) {
	// return true;
	// }
	// }
	// Node[] in = g.nodes[i].in();
	// for (int j = 0; j < in.length; j++) {
	// if (in[j].index() == i) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }

	/**
	 * Evaluates if the indices of all nodes are set correctly, i.e.,
	 * g.nodes[i].index() == i
	 * 
	 * @param g
	 * @return true if the indices of all nodes are set correctly, false
	 *         otherwise
	 */
	// public static boolean indicesCorrect(Graph g) {
	// for (int i = 0; i < g.nodes.length; i++) {
	// if (g.nodes[i].index() != i) {
	// return false;
	// }
	// }
	// return true;
	// }
}
