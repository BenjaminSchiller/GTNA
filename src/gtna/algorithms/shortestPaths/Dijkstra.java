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
 * Dijkstra.java
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

import java.util.HashSet;
import java.util.Set;

/**
 * @author benni
 * 
 * http://en.wikipedia.org/wiki/Dijkstra's_algorithm#Pseudocode
 */
public class Dijkstra extends ShortestPathsAlgorithm {

	@Override
	public int[][] getShortestPaths(Graph graph, int start) {
		int[] dist = new int[graph.getNodeCount()];
		int[] previous = new int[graph.getNodeCount()];
		int[] nextHop = new int[graph.getNodeCount()];
		Set<Integer> Q = new HashSet<Integer>();

		for (int i = 0; i < graph.getNodeCount(); i++) {
			dist[i] = Integer.MAX_VALUE;
			previous[i] = RoutingTables.noRoute;
			nextHop[i] = RoutingTables.noRoute;
			Q.add(i);
		}

		dist[start] = 0;
		nextHop[start] = start;

		Set<Integer> neighbors = new HashSet<Integer>();
		for (int out : graph.getNode(start).getOutgoingEdges()) {
			neighbors.add(out);
		}

		while (!Q.isEmpty()) {
			int u = Dijkstra.getMinElement(Q, dist);
			Q.remove(u);

			if (dist[u] == Integer.MAX_VALUE)
				break;

			for (int v : graph.getNode(u).getOutgoingEdges()) {
				int alt = dist[u] + 1;
				if (alt < dist[v]) {
					dist[v] = alt;
					previous[v] = u;
					if (u == start) {
						nextHop[v] = v;
					} else {
						nextHop[v] = nextHop[u];
					}
				}
			}
		}

		return new int[][] { dist, previous, nextHop };
	}

	private static int getMinElement(Set<Integer> Q, int[] dist) {
		int min = Q.iterator().next();
		for (int element : Q) {
			if (dist[element] < dist[min]) {
				min = element;
			}
		}
		return min;
	}
}
