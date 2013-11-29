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
 * LRUDataStore.java
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class LfuDataStore extends UnlimitedDataStore {

	protected ArrayList<LruElement> list;

	protected HashMap<Identifier, LruElement> map;

	protected int max;

	public LfuDataStore(int node, int max) {
		super(node, "LFU");
		this.max = max;
		this.list = new ArrayList<LruElement>(max);
		this.map = new HashMap<Identifier, LruElement>(max);
	}

	@Override
	public DataStore getEmptyDataStore() {
		return new LfuDataStore(this.node, this.max);
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
		LruElement element = new LruElement(data.getId());
		this.list.add(element);
		this.map.put(id, element);
		this.storage.put(id, data);
		Collections.sort(this.list);
		// System.out.println(this.list);

		while (this.list.size() > max) {
			Identifier removeId = this.list.get(this.list.size() - 1).getId();
			this.list.remove(this.list.size() - 1);
			this.map.remove(removeId);
			this.storage.remove(removeId);
		}
		return data2;
	}

	@Override
	public DataItem removeReplica(Identifier id) {
		DataItem data = this.storage.remove(id);
		this.list.remove(this.map.get(id));
		this.map.remove(id);
		return data;
	}

	@Override
	public void accessReplica(Identifier id) {
		if (this.containsReplica(id)) {
			this.map.get(id).access();
			Collections.sort(this.list);
		}
	}

	public static class LruElement implements Comparable<LruElement> {
		private Identifier id;
		private int accesses;

		public LruElement(Identifier id) {
			this.id = id;
			this.accesses = 0;
		}

		public void access() {
			this.accesses++;
		}

		public Identifier getId() {
			return this.id;
		}

		public int getAccesses() {
			return this.accesses;
		}

		@Override
		public int compareTo(LruElement o) {
			return o.getAccesses() - this.accesses;
		}

		public String toString() {
			return "" + this.accesses;
		}
	}

}
