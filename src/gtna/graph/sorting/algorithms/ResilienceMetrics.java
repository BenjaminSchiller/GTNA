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
 * ResilienceMetrics.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: truong;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.graph.sorting.algorithms;

import java.util.ArrayList;
import java.util.Stack;

import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;

/**
 * @author truong
 * 
 */
public class ResilienceMetrics {
	private Graph g;
	private boolean[] visited;
	private Node[] parent;
	private Stack<Edge> stack;
	private int count;
	private int[] d;
	private int[] low;

	private ArrayList<Node> maxComponent = new ArrayList<Node>();

	/**
	 * @param g2
	 */
	public ResilienceMetrics(Graph g) {
		this.g = g;
	}

	public void biconnectedComponents() {
		count = 0;
		visited = new boolean[g.getNodes().length];
		parent = new Node[g.getNodes().length];
		d = new int[g.getNodes().length];
		low = new int[g.getNodes().length];
		stack = new Stack<Edge>();
		for (Node u : g.getNodes()) {
			visited[u.getIndex()] = false;
			parent[u.getIndex()] = null;
		}
		for (Node u : g.getNodes()) {
			if (!visited[u.getIndex()]) {
				DFSVisit(u);
			}
		}
	}

	/**
	 * @param u
	 */
	private void DFSVisit(Node u) {
		visited[u.getIndex()] = true;
		count++;
		d[u.getIndex()] = count;
		low[u.getIndex()] = d[u.getIndex()];
		for (int index : u.getOutgoingEdges()) {
			Node v = g.getNode(index);
			if (!visited[v.getIndex()]) {
				stack.push(g.getEdges().getEdge(u.getIndex(), v.getIndex()));
				parent[v.getIndex()] = u;
				DFSVisit(v);
				if (low[v.getIndex()] >= d[u.getIndex()]) {
					outputComp(u, v);
				}
				low[u.getIndex()] = min(low[u.getIndex()], low[v.getIndex()]);
			} else if ((parent[u.getInDegree()] != v)
					&& (d[v.getIndex()] < d[u.getIndex()])) {
				// (u,v) is a back edge from u to its ancestor v
				stack.push(g.getEdges().getEdge(u.getIndex(), v.getIndex()));
				low[u.getIndex()] = min(low[u.getIndex()], d[v.getIndex()]);
			}
		}
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	private int min(int i, int j) {
		if (i > j) {
			return j;
		}
		return i;
	}

	/**
	 * @param u
	 * @param v
	 */
	private void outputComp(Node u, Node v) {
		ArrayList<Node> max = new ArrayList<Node>();
		// System.out.println("New Biconnected Component Found");
		while (!stack.isEmpty()) {
			Edge e = stack.pop();
			Node src = g.getNode(e.getSrc());
			Node dst = g.getNode(e.getDst());
			if (!max.contains(src)) {
				max.add(src);
			}
			if (!max.contains(dst)) {
				max.add(dst);
			}
			// System.out.println("" + e);
			if (e.getSrc() == u.getIndex() && e.getDst() == v.getIndex()) {
				if (max.size() > 2) {
					System.out.println("Size = " + max.size());
				}
				// System.out.println("End of this component!");
				if (max.size() > this.maxComponent.size()) {
					this.maxComponent = max;
				}
				return;
			}
		}
		System.out.println("It can have errors!!!");
	}

	public ArrayList<Node> getMaxComponent() {
		return this.maxComponent;
	}
}
