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
 * RandomDataStorage.java
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
package gtna.transformation.id.data;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.IdentifierSpace;
import gtna.id.data.DataStorage;
import gtna.id.data.DataStorageList;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RandomDataStorage extends Transformation {

	private int randomItemsPerNode;

	public RandomDataStorage(int randomItemsPerNode) {
		super("RANDOM_DATA_STORAGE", new Parameter[] { new IntParameter(
				"RANDOM_ITEMS_PER_NODE", randomItemsPerNode) });
		this.randomItemsPerNode = randomItemsPerNode;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Graph transform(Graph g) {
		for (GraphProperty gp : g.getProperties("ID_SPACE")) {
			IdentifierSpace ids = (IdentifierSpace) gp;
			DataStorageList dsl = BasicDataStorage.getBasicDataStorage(ids);
			this.addRandomDataItems(dsl, ids, this.randomItemsPerNode);
			g.addProperty(g.getNextKey("DATA_STORAGE"), dsl);
		}
		return g;
	}

	@SuppressWarnings("rawtypes")
	private void addRandomDataItems(DataStorageList dsl, IdentifierSpace ids,
			int randomItemsPerNode) {
		Random rand = new Random();

		for (DataStorage ds : dsl.getList()) {
			while (ds.size() < randomItemsPerNode) {
				ds.add(ids.randomID(rand));
			}
		}
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("ID_SPACE_0")
				&& g.getProperty("ID_SPACE_0") instanceof IdentifierSpace;
	}

}
