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
import gtna.algorithms.shortestPaths.Dijkstra;
import gtna.algorithms.shortestPaths.ShortestPathsAlgorithm;
import gtna.graph.Graph;
import gtna.id.data.DataStore;
import gtna.id.node.NodeIdentifierSpace;
import gtna.id.node.NodePartition;
import gtna.routing.table.NodeRoutingTable;
import gtna.routing.table.RoutingTable;
import gtna.routing.table.RoutingTables;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author benni
 * 
 */
public class NodeIdentifierSpaceRoutingTable extends Transformation {

	protected DataStore dataStore;

	public NodeIdentifierSpaceRoutingTable(DataStore dataStore,
			int dataItemsPerNode) {
		super("NODE_IDENTIFIER_SPACE_ROUTING_TABLE",
				new Parameter[] { new StringParameter("DATA_STORE",
						dataStore.getKey()) });
		this.dataStore = dataStore;
	}

	@Override
	public Graph transform(Graph g) {
		NodePartition[] p = new NodePartition[g.getNodeCount()];
		for (int i = 0; i < p.length; i++) {
			p[i] = new NodePartition(i);
		}
		NodeIdentifierSpace ids = new NodeIdentifierSpace(p);
		g.addProperty(g.getNextKey("ID_SPACE"), ids);

		RoutingTable[] tables = new RoutingTable[g.getNodeCount()];
		ShortestPathsAlgorithm spa = new Dijkstra();
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
		return true;
	}

}
