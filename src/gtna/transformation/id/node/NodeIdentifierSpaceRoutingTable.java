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

import gtna.algorithms.shortestPaths.Dijkstra;
import gtna.graph.Graph;
import gtna.id.data.DataStorage;
import gtna.id.data.DataStorageList;
import gtna.id.node.NodeIdentifier;
import gtna.id.node.NodeIdentifierSpace;
import gtna.id.node.NodePartition;
import gtna.routing.table.NodeRoutingTable;
import gtna.routing.table.RoutingTable;
import gtna.routing.table.RoutingTables;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author benni
 * 
 */
public class NodeIdentifierSpaceRoutingTable extends Transformation {

	private int dataItemsPerNode;

	public NodeIdentifierSpaceRoutingTable() {
		super("NODE_IDENTIFIER_SPACE_ROUTING_TABLE");
		this.dataItemsPerNode = 0;
	}

	public NodeIdentifierSpaceRoutingTable(int dataItemsPerNode) {
		super("NODE_IDENTIFIER_SPACE_ROUTING_TABLE",
				new Parameter[] { new IntParameter("DATA_ITEMS_PER_NODE",
						dataItemsPerNode) });
		this.dataItemsPerNode = dataItemsPerNode;
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
		for (int i = 0; i < tables.length; i++) {
			int[][] temp = Dijkstra.getShortestPaths(g, i);
			tables[i] = new NodeRoutingTable(i, temp[2]);
		}
		RoutingTables rt = new RoutingTables(tables);
		g.addProperty(g.getNextKey("ROUTING_TABLES"), rt);

		if (this.dataItemsPerNode < 2) {
			return g;
		}

		DataStorage[] ds = new DataStorage[g.getNodeCount()];
		for (int i = 0; i < ds.length; i++) {
			ds[i] = new DataStorage();
			for (int data = 0; data < this.dataItemsPerNode; data++) {
				ds[i].add(new NodeIdentifier(i, data));
			}
		}
		DataStorageList dsl = new DataStorageList(ds);
		g.addProperty(g.getNextKey("DATA_STORAGE"), dsl);

		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}
