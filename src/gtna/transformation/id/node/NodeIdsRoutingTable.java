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
 * RandomNodeIdentifierSpace.java
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
package gtna.transformation.id.node;

import gtna.algorithms.shortestPaths.BreadthFirstSearch;
import gtna.algorithms.shortestPaths.ShortestPathsAlgorithm;
import gtna.graph.Graph;
import gtna.id.node.NodeIdentifierSpace;
import gtna.routing.table.NodeRoutingTable;
import gtna.routing.table.RoutingTable;
import gtna.routing.table.RoutingTables;
import gtna.transformation.Transformation;

/**
 * @author benni
 * 
 */
public class NodeIdsRoutingTable extends Transformation {

	public NodeIdsRoutingTable() {
		super("NODE_IDS_ROUTING_TABLE");
	}

	@Override
	public Graph transform(Graph g) {
		RoutingTable[] tables = new RoutingTable[g.getNodeCount()];
		ShortestPathsAlgorithm spa = new BreadthFirstSearch();
		for (int i = 0; i < tables.length; i++) {
			int[][] temp = spa.getShortestPaths(g, i);
			tables[i] = new NodeRoutingTable(i, temp[2]);
		}
		RoutingTables rt = new RoutingTables(tables);
		g.addProperty(g.getNextKey("ROUTING_TABLES"), rt);

		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("ID_SPACE_0", NodeIdentifierSpace.class);
	}

}
