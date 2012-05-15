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
 * BasicDataStorageList.java
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
import gtna.id.Partition;
import gtna.id.data.DataStorage;
import gtna.id.data.DataStorageList;
import gtna.transformation.Transformation;

/**
 * @author benni
 * 
 */
public class BasicDataStorage extends Transformation {

	public BasicDataStorage() {
		super("BASIC_DATA_STORAGE");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Graph transform(Graph g) {
		for (GraphProperty gp : g.getProperties("ID_SPACE")) {
			g.addProperty(g.getNextKey("DATA_STORAGE"), BasicDataStorage
					.getBasicDataStorage((IdentifierSpace) gp));
		}
		return g;
	}

	@SuppressWarnings("rawtypes")
	public static DataStorageList getBasicDataStorage(IdentifierSpace ids) {
		DataStorage[] list = new DataStorage[ids.getPartitions().length];
		int index = 0;

		for (Partition p : ids.getPartitions()) {
			list[index++] = new DataStorage(p.getRepresentativeID());
		}

		return new DataStorageList(list);
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("ID_SPACE_0")
				&& g.getProperty("ID_SPACE_0") instanceof IdentifierSpace;
	}

}
