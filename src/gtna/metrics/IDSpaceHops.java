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
 * IDSpace.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-07-03 : v1 (BS)
 *
 */
package gtna.metrics;

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.routing.node.RingNode;
import gtna.util.Statistics;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class IDSpaceHops extends MetricImpl implements Metric {
	private double[][] successor;

	private double[][] successorEDF;

	public IDSpaceHops() {
		super("ID_SPACE_HOPS");
	}

	private void initEmpty() {
		this.successor = new double[][] { new double[] { 0.0, 0.0 } };
		this.successorEDF = new double[][] { new double[] { 0.0, 0.0 } };
	}

	public void computeData(Graph g, Network n, Hashtable<String, Metric> m) {
		if (!(g.nodes[0] instanceof RingNode)) {
			this.initEmpty();
		}
		double[] hops = this.computeSuccessorHops(g);
		this.successor = Statistics.probabilityDistribution(hops, 0, 1);
		this.successorEDF = Statistics.empiricalDistributionFunction(
				this.successor, 0, 1);
	}

	private double[] computeSuccessorHops(Graph g) {
		RingNode[] nodes = new RingNode[g.nodes.length];
		for (int i = 0; i < g.nodes.length; i++) {
			nodes[i] = (RingNode) g.nodes[i];
		}
		Arrays.sort(nodes, new SuccessorSorting());
		double[] hops = new double[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			RingNode node = nodes[i];
			RingNode succ = nodes[(i + 1) % nodes.length];
			hops[i] = this.hops(g, node, succ);
		}
		return hops;
	}

	private int hops(Graph g, NodeImpl from, NodeImpl to) {
		int[] hops = new int[g.nodes.length];
		for (int i = 0; i < hops.length; i++) {
			hops[i] = -1;
		}
		hops[from.index()] = 0;
		LinkedList<NodeImpl> list = new LinkedList<NodeImpl>();
		list.add(from);
		while (!list.isEmpty()) {
			NodeImpl curr = list.removeFirst();
			NodeImpl[] out = curr.out();
			for (int i = 0; i < out.length; i++) {
				if (to.index() == out[i].index()) {
					return hops[curr.index()] + 1;
				}
				if (hops[out[i].index()] == -1) {
					hops[out[i].index()] = hops[curr.index()] + 1;
					list.addLast(out[i]);
				}
			}
		}
		return -1;
	}

	public Value[] getValues(Value[] values) {
		return new Value[] {};
	}

	public boolean writeData(String folder) {
		DataWriter.writeWithoutIndex(this.successor, "ID_SPACE_HOPS_SUCCESSOR",
				folder);
		DataWriter.writeWithoutIndex(this.successorEDF,
				"ID_SPACE_HOPS_SUCCESSOR_EDF", folder);
		return true;
	}

	private static class SuccessorSorting implements Comparator<RingNode> {
		public int compare(RingNode a, RingNode b) {
			if (a.getID().pos == b.getID().pos) {
				return 0;
			} else if (a.getID().pos < b.getID().pos) {
				return -1;
			} else {
				return 1;
			}
		}
	}

}