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
 * BreadthFirstSearch.java
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
package gtna.algorithms.shortestPaths;

import gtna.graph.Graph;
import gtna.routing.table.RoutingTables;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author benni
 * 
 */
public class BreadthFirstSearch extends ShortestPathsAlgorithm {

	@Override
	public int[][] getShortestPaths(Graph graph, int start) {
		int[] dist = new int[graph.getNodeCount()];
		int[] previous = new int[graph.getNodeCount()];
		int[] nextHop = new int[graph.getNodeCount()];
		boolean[] seen = new boolean[graph.getNodeCount()];
		Queue<Integer> queue = new LinkedList<Integer>();

		dist[start] = 0;
		previous[start] = RoutingTables.noRoute;
		nextHop[start] = RoutingTables.noRoute;
		seen[start] = true;
		queue.add(start);

		while (!queue.isEmpty()) {
			int current = queue.poll();
			for (int out : graph.getNode(current).getOutgoingEdges()) {
				if (seen[out]) {
					continue;
				}
				dist[out] = dist[current] + 1;
				previous[out] = current;
				if (current == start) {
					nextHop[out] = out;
				} else {
					nextHop[out] = nextHop[current];
				}
				seen[out] = true;
				queue.add(out);
			}
		}

		return new int[][] { dist, previous, nextHop };
	}
}
