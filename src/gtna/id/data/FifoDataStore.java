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
 * FifoDataStore.java
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

import gtna.id.Identifier;

import java.util.LinkedList;

/**
 * @author benni
 * 
 */
public class FifoDataStore extends UnlimitedDataStore {

	protected LinkedList<Identifier> list;

	protected int max;

	public FifoDataStore(int node, int max) {
		super(node, "FIFO");
		this.max = max;
		this.list = new LinkedList<Identifier>();
	}

	@Override
	public DataStore getEmptyDataStore() {
		return new FifoDataStore(this.node, this.max);
	}

	@Override
	public DataItem addReplica(Identifier id) {
		return this.addReplica(id, new DataItem(id));
	}

	@Override
	public DataItem addReplica(Identifier id, DataItem data) {
		if (this.containsReplica(id)) {
			return null;
		}
		DataItem data2 = super.addReplica(id, data);
		this.list.addLast(id);
		while (this.list.size() > max) {
			Identifier first = this.list.pop();
			this.storage.remove(first);
		}
		return data2;
	}

	@Override
	public DataItem removeReplica(Identifier id) {
		DataItem data = this.storage.remove(id);
		this.list.remove(id);
		return data;
	}

	@Override
	public void accessReplica(Identifier id) {

	}

}
