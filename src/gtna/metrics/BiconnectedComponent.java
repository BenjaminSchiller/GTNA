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
 * BiconnectedComponent.java
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
package gtna.metrics;

import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorter;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

/**
 * @author truong
 * 
 */
public class BiconnectedComponent extends Metric {

	// variables for algorithm
	private Graph g;
	private boolean[] visited;
	private Node[] parent;
	private Stack<Edge> stack;
	private int count;
	private int[] d;
	private int[] low;

	private ArrayList<Node> maxComponent;
	private double[] maxComponentSize;
	private Timer runtime;
	private NodeSorter sorter;

	/**
	 * @param key
	 */
	public BiconnectedComponent(NodeSorter sorter) {
		super("BICONNECTED_COMPONENT", new Parameter[] { new StringParameter(
				"SORTER", sorter.getKey()) });
		this.sorter = sorter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.runtime = new Timer();

		this.g = g;
		this.maxComponentSize = new double[g.getNodes().length];

		/*
		 * count = 0; visited = new boolean[g.getNodes().length]; parent = new
		 * Node[g.getNodes().length]; d = new int[g.getNodes().length]; low =
		 * new int[g.getNodes().length]; stack = new Stack<Edge>(); for (Node u
		 * : g.getNodes()) { visited[u.getIndex()] = false; parent[u.getIndex()]
		 * = null; } for (Node u : g.getNodes()) { if (!visited[u.getIndex()]) {
		 * DFSVisit(u); } }
		 */

		Random rand = new Random();
		Node[] sorted = sorter.sort(this.g, rand);
		for (int i = 0; i < sorted.length; i++) {
			System.out.println("\nIter = " + i);
			calculate();
			this.maxComponentSize[i] = (double) this.maxComponent.size();
			System.out.println("Size = " + this.maxComponent.size());
			// remove node
			// TODO:
			/*
			 * Node[] newNodes = new Node[this.g.getNodes().length - 1]; boolean
			 * deleted = false; for (int j = 0; j < newNodes.length; j++) { if
			 * (deleted) { newNodes[j] = this.g.getNode(j + 1); } else { if
			 * (sorted[i] == this.g.getNode(j)) { deleted = true; newNodes[j] =
			 * this.g.getNode(j + 1); } else { newNodes[j] = this.g.getNode(j);
			 * } } }
			 * 
			 * this.g = new Graph("New Graph"); this.g.setNodes(newNodes);
			 * this.g.generateEdges();
			 */
			Node node = sorted[i];
			for (int index : node.getIncomingEdges()) {
				node.removeIn(index);
				node.removeOut(index);
				g.getNode(index).removeIn(node.getIndex());
				g.getNode(index).removeOut(node.getIndex());
			}

		}

		this.runtime.end();
	}

	public void calculate() {
		maxComponent = new ArrayList<Node>();
		System.out.println("Number of Nodes = " + this.g.getNodes().length);
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
				low[u.getIndex()] = Math.min(low[u.getIndex()],
						low[v.getIndex()]);
			} else if ((parent[u.getInDegree()] != v)
					&& (d[v.getIndex()] < d[u.getIndex()])) {
				// (u,v) is a back edge from u to its ancestor v
				stack.push(g.getEdges().getEdge(u.getIndex(), v.getIndex()));
				low[u.getIndex()] = Math
						.min(low[u.getIndex()], d[v.getIndex()]);
			}
		}
	}

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
				// System.out.println("End of this component!");
				if (max.size() > this.maxComponent.size()) {
					this.maxComponent = max;
				}
				return;
			}
		}
		System.out.println("It can have errors!!!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(maxComponentSize,
				"BICONNECTED_COMPONENT_MAX_COMPONENT_SIZE", folder);
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single RT = new Single("BICONNECTED_COMPONENT_RUNTIME",
				this.runtime.getRuntime());
		return new Single[] { RT };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

}
