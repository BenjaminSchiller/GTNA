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
 * GiantConnectedComponent.java
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

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.partition.Partition;
import gtna.transformation.Transformation;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class GiantConnectedComponent extends Transformation {

	public GiantConnectedComponent() {
		super("GIANT_CONNECTED_COMPONENT");
	}

	@Override
	public Graph transform(Graph g) {
		Partition p = (Partition) g.getProperty("PARTITION_0");
		Graph graph = new Graph(g.getName());
		int[] lc = p.getLargestComponent();
		Node[] nodes = Node.init(lc.length, graph);
		Edges edges = new Edges(nodes, nodes.length);
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < lc.length; i++) {
			map.put(lc[i], i);
		}
		for (Node oldNode : g.getNodes()) {
			if (!map.containsKey(oldNode.getIndex())) {
				continue;
			}
			int src = map.get(oldNode.getIndex());
			for (int oldOut : oldNode.getOutgoingEdges()) {
				if (!map.containsKey(oldOut)) {
					continue;
				}
				int dst = map.get(oldOut);
				edges.add(src, dst);
			}
		}
		edges.fill();
		graph.setNodes(nodes);
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("PARTITION_0");
	}

}
