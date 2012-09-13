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
 * UnlimitedDataStorage.java
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author benni
 * 
 *         Implements an unlimited data store where data is never removed,
 *         independent of the number of data items stored.
 * 
 */
@SuppressWarnings("rawtypes")
public class UnlimitedDataStore extends DataStore {

	protected Map<Identifier, DataItem> storage;

	public UnlimitedDataStore(int node) {
		super(node);
		this.storage = new HashMap<Identifier, DataItem>();
	}

	@Override
	public DataStore getEmptyDataStore() {
		return new UnlimitedDataStore(this.node);
	}

	@Override
	public boolean contains(Identifier id) {
		return this.storage.containsKey(id);
	}

	@Override
	public boolean contains(DataItem data) {
		return this.storage.containsValue(data);
	}

	@Override
	public DataItem add(Identifier id) {
		return this.add(id, new DataItem(id));
	}

	@Override
	public DataItem add(Identifier id, DataItem data) {
		if (this.contains(id)) {
			return null;
		}
		return this.storage.put(id, data);
	}

	@Override
	public DataItem remove(Identifier id) {
		return this.storage.remove(id);
	}

	@Override
	public DataItem get(Identifier id) {
		return this.storage.get(id);
	}

	@Override
	public void access(Identifier id) {

	}

	@Override
	public int size() {
		return this.storage.size();
	}

	@Override
	public Set<Identifier> getIdentifiers() {
		return this.storage.keySet();
	}

	@Override
	public Collection<DataItem> getData() {
		return this.storage.values();
	}
}
