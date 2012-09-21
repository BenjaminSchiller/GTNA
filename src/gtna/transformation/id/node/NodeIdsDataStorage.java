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

import gtna.graph.Graph;
import gtna.id.data.DataStore;
import gtna.id.data.DataStoreList;
import gtna.id.node.NodeIdentifier;
import gtna.id.node.NodeIdentifierSpace;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author benni
 * 
 */
public class NodeIdsDataStorage extends Transformation {

	private int dataItemsPerNode;

	protected DataStore store;

	public NodeIdsDataStorage(DataStore store) {
		super("NODE_IDS_DATA_STORAGE");
		this.store = store;
		this.dataItemsPerNode = 1;
	}

	public NodeIdsDataStorage(DataStore store, int dataItemsPerNode) {
		super("NODE_IDS_DATA_STORAGE", new Parameter[] { new IntParameter(
				"DATA_ITEMS_PER_NODE", dataItemsPerNode) });
		this.store = store;
		this.dataItemsPerNode = dataItemsPerNode;
	}

	@Override
	public Graph transform(Graph g) {
		DataStore[] ds = new DataStore[g.getNodeCount()];
		for (int i = 0; i < ds.length; i++) {
			ds[i] = this.store.getEmptyDataStore();
			for (int data = 0; data < this.dataItemsPerNode; data++) {
				ds[i].addSource(new NodeIdentifier(i));
			}
		}
		DataStoreList dsl = new DataStoreList(ds);
		g.addProperty(g.getNextKey("DATA_STORAGE"), dsl);

		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("ID_SPACE_0", NodeIdentifierSpace.class);
	}

}
