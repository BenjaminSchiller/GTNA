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
 * NodeSorter.java
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
package gtna.graph.sorting;

import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.Random;

/**
 * @author benni
 * 
 */
public abstract class NodeSorter {
	public enum NodeSorterMode {
		ASC, DESC
	};

	private String key;

	protected NodeSorterMode mode;

	public NodeSorter(String key) {
		this.key = key;
		this.mode = null;
	}

	public NodeSorter(String key, NodeSorterMode mode) {
		this.key = key;
		this.mode = mode;
	}

	public abstract Node[] sort(Graph g, Random rand);

	public abstract boolean applicable(Graph g);

	protected abstract boolean isPropertyEqual(Node n1, Node n2);

	public String getKey() {
		return this.mode == null ? this.key : this.key + "_" + this.mode;
	}

	protected Node[] clone(Node[] nodes) {
		Node[] clone = new Node[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			clone[i] = nodes[i];
		}
		return clone;
	}

	protected Node[] randomize(Node[] nodes, Random rand) {
		int from = 0;
		for (int i = 1; i < nodes.length; i++) {
			if (!this.isPropertyEqual(nodes[from], nodes[i])) {
				int to = i - 1;
				if (from != to) {
					this.randomize(nodes, rand, from, to);
				}
				from = i;
			}
		}
		if (from < nodes.length - 1) {
			this.randomize(nodes, rand, from, nodes.length - 1);
		}
		return nodes;
	}

	protected Node[] randomize(Node[] nodes, Random rand, int from, int to) {
		// System.out.println("switching " + from + " to " + to);
		for (int i = from; i < to; i++) {
			int FROM = i;
			int TO = i + rand.nextInt(to - i + 1);
			// System.out.println(FROM + " <==> " + TO);
			Node temp = nodes[FROM];
			nodes[FROM] = nodes[TO];
			nodes[TO] = temp;
		}
		return nodes;
	}

	protected Node[] reverse(Node[] nodes) {
		for (int i = 0; i < nodes.length / 2; i++) {
			int with = nodes.length - 1 - i;
			Node temp = nodes[i];
			nodes[i] = nodes[with];
			nodes[with] = temp;
		}
		return nodes;
	}
}
