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
 * BiconnectedComponent2.java
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
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorter;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Timer;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

/**
 * @author truong
 * 
 */
public class BiconnectedComponent extends Metric {

	private Timer runtime;
	private double[][] maxBicomponentSize;

	private NodeSorter sorter;
	private boolean[] excludedNode;
	private double mixedPercent;
	/*
	 * nodes 0 -> (mixed - 1): single deleted. nodes mixed -> (N - 1):
	 * percentage deleted
	 */
	private int mixed;
	private int numberOfRound;
	private int excluded;

	// variables for the biconnected component size algorithm
	private ArrayList<Node> maxComponent;
	private int count;
	private boolean[] visited;
	private Node[] parent;
	private int[] d;
	private int[] low;
	private Stack<Point> stack;

	public BiconnectedComponent(NodeSorter sorter, double mixedPercent) {
		super("BICONNECTED_COMPONENT", new Parameter[] { new StringParameter(
				"SORTER", sorter.getKey()) });
		this.sorter = sorter;
		this.mixedPercent = mixedPercent;
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

		// init variables
		int N = g.getNodes().length;
		this.mixed = (int) (this.mixedPercent * N);
		this.excludedNode = new boolean[N];
		for (int i = 0; i < N; i++) {
			this.excludedNode[i] = false;
		}
		this.excluded = 0;
		this.numberOfRound = mixed + 100;
		this.maxBicomponentSize = new double[numberOfRound][2];

		Node[] sorted = this.sorter.sort(g, new Random());
		for (int i = 0; i < numberOfRound; i++) {
			// compute
			this.computeBiconnectedSize(g);
			this.maxBicomponentSize[i][0] = this.excluded;
			System.out.println("Size = " + this.maxComponent.size());
			this.maxBicomponentSize[i][1] = (double) this.maxComponent.size();

			// exclude node(s)
			if (i < mixed) {
				System.out.println("Remove: " + i);
				this.excludeNode(i, sorted);
			} else {
				int percent = i - this.mixed + 1;
				int next = this.mixed - 1 + (percent * (N - this.mixed)) / 100;
				System.out.println("The last excluded Node = "
						+ (this.excluded - 1));
				System.out.println("Remove until = " + (next - 1));
				for (int j = this.excluded; j < next; j++) {
					this.excludeNode(j, sorted);
				}
			}
		}

		this.runtime.end();
	}

	private void computeBiconnectedSize(Graph g) {
		// TODO:
		// int N = g.getNodes().length - this.excluded;
		this.maxComponent = new ArrayList<Node>();
		this.count = 0;
		this.stack = new Stack<Point>();

		this.visited = new boolean[g.getNodes().length];
		this.parent = new Node[g.getNodes().length];
		this.d = new int[g.getNodes().length];
		this.low = new int[g.getNodes().length];

		for (int i = 0; i < g.getNodes().length; i++) {
			if (this.excludedNode[i]) {
				continue;
			}
			Node u = g.getNode(i);
			this.visited[u.getIndex()] = false;
			this.parent[u.getIndex()] = null;
		}
		for (int i = 0; i < g.getNodes().length; i++) {
			if (this.excludedNode[i]) {
				continue;
			}
			Node u = g.getNode(i);
			if (!this.visited[u.getIndex()]) {
				this.DFSvisit(g, u);
			}
		}
	}

	private void DFSvisit(Graph g, Node u) {
		this.visited[u.getIndex()] = true;
		this.count++;
		this.d[u.getIndex()] = count;
		this.low[u.getIndex()] = count;
		for (int index : u.getOutgoingEdges()) {
			if (this.excludedNode[index]) {
				continue;
			}
			Node v = g.getNode(index);
			if (!this.visited[v.getIndex()]) {
				this.stack.push(new Point(u.getIndex(), v.getIndex()));
				this.parent[v.getIndex()] = u;
				this.DFSvisit(g, v);
				if (this.low[v.getIndex()] >= this.d[u.getIndex()]) {
					this.output(g, u, v);
				}
				this.low[u.getIndex()] = Math.min(this.low[u.getIndex()],
						this.low[v.getIndex()]);
			} else if ((this.parent[u.getIndex()] != v)
					&& (this.d[v.getIndex()] < this.d[u.getIndex()])) {
				this.stack.push(new Point(u.getIndex(), v.getIndex()));
				this.low[u.getIndex()] = Math.min(this.low[u.getIndex()],
						this.d[v.getIndex()]);
			}
		}
	}

	private void output(Graph g, Node u, Node v) {
		ArrayList<Node> max = new ArrayList<Node>();
		while (!this.stack.isEmpty()) {
			Point e = stack.pop();
			Node src = g.getNode(e.x);
			Node dst = g.getNode(e.y);
			if (!max.contains(src)) {
				max.add(src);
			}
			if (!max.contains(dst)) {
				max.add(dst);
			}

			if (src == u && dst == v) {
				if (max.size() > this.maxComponent.size()) {
					this.maxComponent = max;
				}
				return;
			}
		}
		System.out
				.println("Maybe we have an error when computing biconnected!");
	}

	private void excludeNode(int index, Node[] sorted) {
		Node nodeToExclude = sorted[index];
		this.excludedNode[nodeToExclude.getIndex()] = true;
		this.excluded++;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.maxBicomponentSize,
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
