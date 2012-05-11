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
 * GraphUtils.java
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
package gtna.util;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.Map;

/**
 * @author benni
 * 
 */
public class GraphUtils {

	/**
	 * Generates a subgraph of the given graph induced by the indexMap.
	 * 
	 * @param g
	 *            graph
	 * @param indexMap
	 *            mapping of node index in the original graph to new indexes
	 *            (new indexes are expected to be between 0 and
	 *            indexMap.size()-1) [key = original index, value = index in
	 *            subgraph].
	 * @return subgraph induced by the given indexMap
	 */
	public static Graph subgraph(Graph g, Map<Integer, Integer> indexMap) {
		Graph graph = new Graph("Subgraph of " + g.getName());
		Node[] nodes = Node.init(indexMap.size(), graph);
		Edges edges = new Edges(nodes, nodes.length);

		for (Node node : g.getNodes()) {
			if (!indexMap.containsKey(node.getIndex())) {
				continue;
			}

			for (int out : node.getOutgoingEdges()) {
				if (!indexMap.containsKey(out)) {
					continue;
				}
				edges.add(indexMap.get(node.getIndex()), indexMap.get(out));
			}
		}

		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}
}
