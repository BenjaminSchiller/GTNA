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
public class UnlimitedDataStore extends DataStore {

	protected Map<Identifier, DataItem> storage;
	
	public UnlimitedDataStore(int node){
		this(node, "UNLIMITED");
	}

	protected UnlimitedDataStore(int node, String key) {
		super(node, key);
		this.storage = new HashMap<Identifier, DataItem>();
	}

	@Override
	public DataStore getEmptyDataStore() {
		return new UnlimitedDataStore(this.node);
	}

	@Override
	public boolean containsReplica(Identifier id) {
		return this.storage.containsKey(id);
	}

	@Override
	public boolean containsReplica(DataItem data) {
		return this.storage.containsValue(data);
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
		return this.storage.put(id, data);
	}

	@Override
	public DataItem removeReplica(Identifier id) {
		return this.storage.remove(id);
	}

	@Override
	public DataItem getReplica(Identifier id) {
		return this.storage.get(id);
	}

	@Override
	public void accessReplica(Identifier id) {

	}

	@Override
	public int sizeOfReplicaStore() {
		return this.storage.size();
	}

	@Override
	public Set<Identifier> getReplicaIdentifiers() {
		return this.storage.keySet();
	}

	@Override
	public Collection<DataItem> getReplicaData() {
		return this.storage.values();
	}
}
