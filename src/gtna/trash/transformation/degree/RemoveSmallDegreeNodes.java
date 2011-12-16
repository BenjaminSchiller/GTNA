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
 * RemoveSmallDegreeNodes.java
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
 * Transforms the given graph by removing all nodes whose in- or out-degree is
 * smaller than the given minimum in- and out-degree, i.e., node.in.length <
 * minIn || node.out.length < minOut.
 * 
 * @author benni
 * 
 */
// TODO reimplement RemoveSmallDegreeNodes
public class RemoveSmallDegreeNodes {
	// public class RemoveSmallDegreeNodes extends TransformationImpl implements
	// Transformation {
	// private int minIn;
	//
	// private int minOut;
	//
	// public RemoveSmallDegreeNodes(int minIn, int minOut) {
	// super("REMOVE_SMALL_DEGREE_NODES",
	// new String[] { "MIN_IN", "MIN_OUT" }, new String[] {
	// "" + minIn, "" + minOut });
	// this.minIn = minIn;
	// this.minOut = minOut;
	// }
	//
	// public boolean applicable(Graph g) {
	// return true;
	// }
	//
	// public Graph transform(Graph g) {
	// int counter = 0;
	// Hashtable<Integer, Integer> ids = new Hashtable<Integer, Integer>();
	// for (int i = 0; i < g.nodes.length; i++) {
	// if (g.nodes[i].in().length >= this.minIn
	// || g.nodes[i].out().length >= this.minOut) {
	// ids.put(i, counter++);
	// }
	// }
	// Edge[] old = g.edges();
	// Node[] nodes = Node.init(counter);
	// Edges edges = new Edges(nodes, 0);
	// for (int i = 0; i < old.length; i++) {
	// if (ids.containsKey(old[i].src.index())
	// && ids.containsKey(old[i].dst.index())) {
	// Node src = nodes[ids.get(old[i].src.index())];
	// Node dst = nodes[ids.get(old[i].dst.index())];
	// edges.add(src, dst);
	// }
	// }
	// edges.fill();
	// return new Graph(g.name, nodes, g.timer);
	// }
}
