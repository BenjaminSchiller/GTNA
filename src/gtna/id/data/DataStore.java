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
 * DataStorage.java
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
 *         Implements the basic skeleton and abstract methods for a DataStore
 *         that is used by the DataStorage GraphProperty to store (replicated)
 *         data in a systems, e.g., for routing purposes.
 * 
 */
public abstract class DataStore {

	protected int node;

	protected String key;

	protected Map<Identifier, DataItem> sourceData;

	/**
	 * 
	 * @param node
	 *            index of the node this data store belongs to
	 */
	public DataStore(int node, String key) {
		this.node = node;
		this.key = key;
		this.sourceData = new HashMap<Identifier, DataItem>();
	}

	public int getNode() {
		return this.node;
	}
	
	public String getKey(){
		return this.key;
	}

	/**
	 * 
	 * @return empty data store of the same type and configuration as the one
	 *         calling the method
	 */
	public abstract DataStore getEmptyDataStore();

	/**
	 * 
	 * @param id
	 * @return true if data for the given $id is stored as a replica; false
	 *         otherwise
	 */
	public abstract boolean containsReplica(Identifier id);

	/**
	 * 
	 * @param id
	 * @return true if source data for the given $id is stored; false otherwise
	 */
	public boolean containsSource(Identifier id) {
		return this.sourceData.containsKey(id);
	}

	public boolean contains(Identifier id) {
		return this.containsReplica(id) || this.containsSource(id);
	}

	/**
	 * 
	 * @param data
	 * @return true if the $data is stored as a replica; false otherwise
	 */
	public abstract boolean containsReplica(DataItem data);

	/**
	 * 
	 * @param data
	 * @return true if the data stores contains $data as source; false otherwise
	 */
	public boolean containsSource(DataItem data) {
		return this.sourceData.containsValue(data);
	}

	public boolean contains(DataItem data) {
		return this.containsReplica(data) || this.containsSource(data);
	}

	/**
	 * creates an (empty) data item for the specified identifier and stores it
	 * as a replica
	 * 
	 * @param id
	 * @return the created data item
	 */
	public abstract DataItem addReplica(Identifier id);

	/**
	 * creates an (empty) data item for the speficied identifier and stores it
	 * as source data
	 * 
	 * @param id
	 * @return the created data item
	 */
	public DataItem addSource(Identifier id) {
		return this.sourceData.put(id, new DataItem(id));
	}

	/**
	 * adds the given data item $data to the storage as replica and maps the
	 * given identifier $id to it
	 * 
	 * @param id
	 * @param data
	 * @return the added data item $data
	 */
	public abstract DataItem addReplica(Identifier id, DataItem data);

	/**
	 * add the given data item $data to the storage as source and maps the given
	 * identifier $id to it
	 * 
	 * @param id
	 * @param data
	 * @return
	 */
	public DataItem addSource(Identifier id, DataItem data) {
		return this.sourceData.put(id, data);
	}

	/**
	 * removes the mapping for the given identifier $id from the replica store
	 * 
	 * @param id
	 * @return true if the mapping was removed successfully; false otherwise
	 */
	public abstract DataItem removeReplica(Identifier id);

	/**
	 * removes the mapping for the given identifier $id from the source store
	 * 
	 * @param id
	 * @return true if the mapping was removed successfully; false otherwise
	 */
	public DataItem removeSource(Identifier id) {
		return this.sourceData.remove(id);
	}

	/**
	 * 
	 * @param id
	 * @return data item to which the given identifier $id is mapped in the
	 *         replica store
	 */
	public abstract DataItem getReplica(Identifier id);

	/**
	 * 
	 * @param id
	 * @return data item to which the given identifier $id is mapped in the
	 *         source data store
	 */
	public DataItem getSource(Identifier id) {
		return this.sourceData.get(id);
	}

	/**
	 * Calling this method indicates to the underlying storage system that the
	 * data item associated with the identifier $id is accessed. this
	 * information can be used for managing an last access list, order of stored
	 * items, etc.
	 * 
	 * @param id
	 */
	public abstract void accessReplica(Identifier id);

	/**
	 * Calling this method indicates to the underlying storage system that the
	 * data item associated with the identifier $id is accessed. this
	 * information can be used for managing an last access list, order of stored
	 * items, etc.
	 * 
	 * @param id
	 */
	public void accessSource(Identifier id) {

	}

	public void access(Identifier id) {
		this.accessReplica(id);
		this.accessSource(id);
	}

	/**
	 * 
	 * @return current size of the replica store, i.e., number of data items
	 *         stored as replicas items
	 */
	public abstract int sizeOfReplicaStore();

	/**
	 * 
	 * @return current size of the source data store, i.e., number of data items
	 *         stored as source data
	 */
	public int sizeOfSourceStore() {
		return this.sourceData.size();
	}

	public int sizeOfStore() {
		return this.sizeOfReplicaStore() + this.sizeOfSourceStore();
	}

	public abstract Set<Identifier> getReplicaIdentifiers();

	public Set<Identifier> getSourceIdentifiers() {
		return this.sourceData.keySet();
	}

	public Set<Identifier> getIdentifiers() {
		Set<Identifier> ids = this.getReplicaIdentifiers();
		ids.addAll(this.getSourceIdentifiers());
		return ids;
	}

	public abstract Collection<DataItem> getReplicaData();

	public Collection<DataItem> getSourceData() {
		return this.sourceData.values();
	}

	public Collection<DataItem> getData() {
		Collection<DataItem> data = this.getReplicaData();
		data.addAll(this.getSourceData());
		return data;
	}
}
