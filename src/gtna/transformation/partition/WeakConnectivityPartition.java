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
 * WeakConnectivity.java
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
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author benni
 * 
 */
public class WeakConnectivityPartition extends Transformation {

	public WeakConnectivityPartition() {
		super("WEAK_CONNECTIVITY_PARTITION");
	}

	@Override
	public Graph transform(Graph g) {
		boolean[] seen = new boolean[g.getNodes().length];
		Partition p = WeakConnectivityPartition.getWeakPartition(g, seen);
		g.addProperty(g.getNextKey("WEAK_CONNECTIVITY_PARTITION"), p);
		g.addProperty(g.getNextKey("PARTITION"), p);
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	public static Partition getWeakPartition(Graph g) {
		return WeakConnectivityPartition.getWeakPartition(g,
				new boolean[g.getNodes().length]);
	}

	public static Partition getWeakPartition(Graph g, boolean[] seen) {
		ArrayList<ArrayList<Integer>> components = new ArrayList<ArrayList<Integer>>();
		for (int start = 0; start < seen.length; start++) {
			if (seen[start]) {
				continue;
			}
			seen[start] = true;
			ArrayList<Integer> current = new ArrayList<Integer>();
			Queue<Integer> queue = new LinkedList<Integer>();
			queue.add(start);
			seen[start] = true;
			while (!queue.isEmpty()) {
				Node node = g.getNode(queue.poll());
				current.add(node.getIndex());
				for (int out : node.getOutgoingEdges()) {
					if (!seen[out]) {
						queue.add(out);
						seen[out] = true;
					}
				}
				for (int in : node.getIncomingEdges()) {
					if (!seen[in]) {
						queue.add(in);
						seen[in] = true;
					}
				}
			}
			components.add(current);
		}
		return new Partition(components);
	}

}
