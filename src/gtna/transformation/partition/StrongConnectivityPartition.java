/* ===========================================================
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
 * StrongConnectivityPartition.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.partition;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.transformation.Transformation;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author benni uses the algorithm of Tarjan to compute the strongly connected
 *         components of the given graph http://en.wikipedia.org/wiki/Tarjan
 *         's_strongly_connected_components_algorithm
 */
public class StrongConnectivityPartition extends Transformation {

	public StrongConnectivityPartition() {
		super("STRONG_CONNECTIVITY_PARTITION");
	}

	private static int index;

	@Override
	public Graph transform(Graph g) {
		Partition p = getStrongPartition(g);

		g.addProperty(g.getNextKey("STRONG_CONNECTIVITY_PARTITION"), p);
		g.addProperty(g.getNextKey("PARTITION"), p);

		return g;
	}

	public static Partition getStrongPartition(Graph g) {
		ArrayList<ArrayList<Integer>> components = new ArrayList<ArrayList<Integer>>();
		boolean[] found = new boolean[g.getNodeCount()];

		for (int i = 0; i < g.getNodeCount(); i++) {
			if (found[i]) {
				continue;
			}
			ArrayList<Integer> component = getStronglyConnectedNodes(
					g.getNode(i), g, found);
			components.add(component);
			// if (component.size() == 111881) {
			// break;
			// }
		}

		return new Partition(components);
	}

	private static ArrayList<Integer> getStronglyConnectedNodes(Node n,
			Graph g, boolean[] found) {
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		nodes.add(n.getIndex());
		found[n.getIndex()] = true;

		boolean[] forward = new boolean[g.getNodeCount()];
		Stack<Node> stack = new Stack<Node>();

		stack.push(n);
		forward[n.getIndex()] = true;

		while (!stack.isEmpty()) {
			Node current = stack.pop();
			for (int out : current.getOutgoingEdges()) {
				if (forward[out] == true) {
					continue;
				}
				forward[out] = true;
				stack.push(g.getNode(out));
			}
		}

		boolean[] backward = new boolean[g.getNodeCount()];

		stack.push(n);
		backward[n.getIndex()] = true;

		while (!stack.isEmpty()) {
			Node current = stack.pop();
			for (int in : current.getIncomingEdges()) {
				if (backward[in] == true) {
					continue;
				}
				backward[in] = true;
				stack.push(g.getNode(in));
				if (forward[in]) {
					found[in] = true;
					nodes.add(in);
				}
			}
		}

		return nodes;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}
