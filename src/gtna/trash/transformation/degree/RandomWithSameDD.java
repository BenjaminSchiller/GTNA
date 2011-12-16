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
 * RandomWithSameDD.java
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
package gtna.trash.transformation.degree;


/**
 * Transforms the given graph by generating a random graph with the exact same
 * degree distribution. If the parameter bidirectional is true, only
 * bidirectional links are created, otherwise unidirectional links are
 * generated. Note that this transformation creates new node objects. Therefore,
 * all information except for the node adjacency is lost.
 * 
 * @author benni
 * 
 */
// TODO reimplement RandomWithSameDD
public class RandomWithSameDD {
	// public class RandomWithSameDD extends TransformationImpl implements
	// Transformation {
	// private boolean bidirectional;
	//
	// public RandomWithSameDD(boolean bidirectional) {
	// super("RANDOM_WITH_SAME_DD", new String[] { "BIDIRECTIONAL" },
	// new String[] { "" + bidirectional });
	// this.bidirectional = bidirectional;
	// }
	//
	// public boolean applicable(Graph g) {
	// return true;
	// }
	//
	// public Graph transform(Graph g) {
	// Node[] nodes = Node.init(g.nodes.length);
	// Edges edges = this.random(src(g), dst(g), nodes, g.nodes);
	// while (edges == null) {
	// edges = this.random(src(g), dst(g), nodes, g.nodes);
	// }
	// edges.fill();
	// return new Graph(g.name, nodes, g.timer);
	// }
	//
	// private Edges random(ArrayList<Integer> src, ArrayList<Integer> dst,
	// Node[] nodes, Node[] orig) {
	// Edges edges = new Edges(nodes, src.size());
	// Random rand = new Random(System.currentTimeMillis());
	// int counter = 0;
	// while (src.size() > 0) {
	// if (counter > 1000) {
	// return null;
	// }
	// int si = rand.nextInt(src.size());
	// int di = rand.nextInt(dst.size());
	// int s = src.get(si);
	// int d = dst.get(di);
	// if (s == d || edges.contains(s, d)) {
	// counter++;
	// continue;
	// }
	// edges.add(nodes[s], nodes[d]);
	// src.remove((Integer) s);
	// dst.remove((Integer) d);
	// if (this.bidirectional) {
	// edges.add(nodes[d], nodes[s]);
	// src.remove((Integer) d);
	// dst.remove((Integer) s);
	// }
	// }
	// return edges;
	// }
	//
	// private ArrayList<Integer> src(Graph g) {
	// ArrayList<Integer> src = new ArrayList<Integer>(g.edges);
	// for (int i = 0; i < g.nodes.length; i++) {
	// for (int j = 0; j < g.nodes[i].out().length; j++) {
	// src.add(i);
	// }
	// }
	// return src;
	// }
	//
	// private ArrayList<Integer> dst(Graph g) {
	// ArrayList<Integer> dst = new ArrayList<Integer>(g.edges);
	// for (int i = 0; i < g.nodes.length; i++) {
	// for (int j = 0; j < g.nodes[i].in().length; j++) {
	// dst.add(i);
	// }
	// }
	// return dst;
	// }
}
