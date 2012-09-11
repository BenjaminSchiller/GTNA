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
 * DataStorageGraphProperty.java
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
package gtna.id.data;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * @author benni
 * 
 */
public class DataStorageList implements GraphProperty {
	private DataStorage[] list;

	private Set<DataItem> dataItems;

	public DataStorageList() {
		this.list = new DataStorage[0];
		this.dataItems = new HashSet<DataItem>();
	}

	public DataStorageList(DataStorage[] list) {
		this.list = list;
		this.dataItems = new HashSet<DataItem>();
		for (DataStorage ds : list) {
			this.dataItems.addAll(ds.getDataItems());
		}
	}

	public DataStorage[] getList() {
		return this.list;
	}

	public Set<DataItem> getDataItems() {
		return this.dataItems;
	}

	public DataStorage getStorageForNode(int index) {
		return this.list[index];
	}

	@Override
	public boolean write(String filename, String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub
	}

}
