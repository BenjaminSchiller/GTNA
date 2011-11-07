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
 * StorageList.java
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
package gtna.id.storage;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;

import java.util.ArrayList;

/**
 * @author benni
 * 
 */
public class StorageList implements GraphProperty {
	private Storage[] storageList;

	public StorageList(Storage[] storage) {
		this.storageList = storage;
	}

	public StorageList(ArrayList<Storage> list) {
		this.storageList = new Storage[list.size()];
		for (int i = 0; i < list.size(); i++) {
			this.storageList[i] = list.get(i);
		}
	}

	public Storage[] getStorageList() {
		return this.storageList;
	}

	public Storage get(int index) {
		return this.storageList[index];
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
