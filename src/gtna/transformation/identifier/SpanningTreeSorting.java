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
 * SpanningTreeSorting.java
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
package gtna.transformation.identifier;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.graph.sorting.NodeSorting;
import gtna.routing.node.RingNode;
import gtna.routing.node.identifier.RingID;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class SpanningTreeSorting extends TransformationImpl implements
		Transformation {
	private int index;

	public SpanningTreeSorting(int index) {
		super("SPANNING_TREE_SORTING", new String[] { "INDEX" },
				new String[] { "" + index });
		this.index = index;
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		NodeImpl[] nodes = new NodeImpl[g.nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new RingNode(i, -1);
		}
		Edge[] original = g.edges();
		Edges edges = new Edges(nodes, original.length);
		for (int i = 0; i < original.length; i++) {
			NodeImpl src = nodes[original[i].src.index()];
			NodeImpl dst = nodes[original[i].dst.index()];
			edges.add(src, dst);
		}
		edges.fill();
		// NodeImpl max = nodes[0];
		// int d = max.out().length + max.in().length;
		// for (int i = 1; i < nodes.length; i++) {
		// int D = nodes[i].out().length + nodes[i].in().length;
		// if (D > d) {
		// max = nodes[i];
		// d = D;
		// }
		// }

		// NodeImpl max = nodes[this.index];

		Node[] sorted = NodeSorting.degreeDesc(g.nodes, new Random(0));
		NodeImpl max = (NodeImpl) sorted[this.index];

		double[] start = this.init(nodes.length, -1);
		double[] end = this.init(nodes.length, -1);

		start[max.index()] = 0;
		end[max.index()] = 1024;

		Queue<NodeImpl> stack = new LinkedList<NodeImpl>();
		stack.add(max);
		int round = 0;
		while (!stack.isEmpty()) {
			round++;
			NodeImpl n = stack.poll();
			double counter = 0;
			NodeImpl[] out = n.out();
			for (int i = 0; i < out.length; i++) {
				if (start[out[i].index()] == -1) {
					counter++;
				}
			}

			// System.out.println("Parent: " + n.index() + " @ " + counter);
			// System.out.println("   @  [" + start[n.index()] + ", "
			// + end[n.index()] + "]");

			double dist = end[n.index()] - start[n.index()];
			double width = dist / (counter + 1.0);
			end[n.index()] = start[n.index()] + width;

			// System.out.println("   @  " + dist);
			// System.out.println("   @  " + width);
			// System.out.println("   => [" + start[n.index()] + ", "
			// + end[n.index()] + "]");

			int index = 0;
			for (int i = 0; i < out.length; i++) {
				NodeImpl o = out[i];
				if (start[o.index()] == -1) {
					start[o.index()] = start[n.index()]
							+ ((double) index + 1.0) * width;
					end[o.index()] = start[o.index()] + width;
					stack.add(o);
					index++;
					// System.out.println("   => [" + start[o.index()] + ", "
					// + end[o.index()] + "] (" + index + ")");
				}
			}
			// if (round > 10) {
			// break;
			// }
		}

		HashMap<Double, NodeImpl> ids = new HashMap<Double, NodeImpl>(
				nodes.length);
		for (int i = 0; i < nodes.length; i++) {
			double id = start[i] + (end[i] - start[i]) / 2.0;
			((RingNode) nodes[i]).setID(new RingID(id));

			if (ids.containsKey(id)) {
				System.out.println(i + ": " + id + " already contained @ "
						+ ids.get(id).index());
			} else {
				ids.put(id, nodes[i]);
			}
		}

		return new Graph(g.name, nodes, g.timer);
	}

	private double[] init(int size, double value) {
		double[] init = new double[size];
		for (int i = 0; i < size; i++) {
			init[i] = value;
		}
		return init;
	}
}
