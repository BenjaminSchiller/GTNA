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
 * RemoveDeadNodes.java
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
package gtna.transformation.degree;


/**
 * Transforms the given graph by removing all nodes with no incoming of outgoing
 * edges.
 * 
 * @author benni
 * 
 */
// TODO reimplement RemoveDeadNodes
public class RemoveDeadNodes {
	// public class RemoveDeadNodes extends TransformationImpl implements
	// Transformation {
	// public RemoveDeadNodes() {
	// super("REMOVE_DEAD_NODES", new String[] {}, new String[] {});
	// }
	//
	// public boolean applicable(Graph g) {
	// return true;
	// }
	//
	// public Graph transform(Graph g) {
	// Hashtable<Integer, Integer> ids = new Hashtable<Integer, Integer>(
	// g.nodes.length);
	// int index = 0;
	// for (int i = 0; i < g.nodes.length; i++) {
	// if (g.nodes[i].out().length > 0 || g.nodes[i].in().length > 0) {
	// ids.put(i, index++);
	// }
	// }
	// Node[] nodes = new Node[index];
	// for (int i = 0; i < g.nodes.length; i++) {
	// if (ids.containsKey(i)) {
	// nodes[ids.get(i)] = g.nodes[i];
	// nodes[ids.get(i)].setIndex(ids.get(i));
	// }
	// }
	// g.nodes = nodes;
	// g.computeDegrees();
	// g.computeEdges();
	// return g;
	// }

}
