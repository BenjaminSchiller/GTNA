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
import java.util.Set;

/**
 * @author benni
 * 
 *         Implements the basic skeleton and abstract methods for a DataStore
 *         that is used by the DataStorage GraphProperty to store (replicated)
 *         data in a systems, e.g., for routing purposes.
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class DataStore {

	protected int node;

	/**
	 * 
	 * @param node
	 *            index of the node this data store belongs to
	 */
	public DataStore(int node) {
		this.node = node;
	}

	public int getNode() {
		return this.node;
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
	 * @return true if data for the given $id is stored; false otherwise
	 */
	public abstract boolean contains(Identifier id);

	/**
	 * 
	 * @param data
	 * @return true if the $data is stored; false otherwise
	 */
	public abstract boolean contains(DataItem data);

	/**
	 * creates an (empty) data item for the specified identifier
	 * 
	 * @param id
	 * @return the created data item
	 */
	public abstract DataItem add(Identifier id);

	/**
	 * adds the given data item $data to the storage and maps the given
	 * identifier $id to it
	 * 
	 * @param id
	 * @param data
	 * @return the added data item $data
	 */
	public abstract DataItem add(Identifier id, DataItem data);

	/**
	 * removes the mapping for the given identifier $id
	 * 
	 * @param id
	 * @return true if the mapping was removed successfully; false otherwise
	 */
	public abstract DataItem remove(Identifier id);

	/**
	 * 
	 * @param id
	 * @return data item to which the given identifier $id is mapped
	 */
	public abstract DataItem get(Identifier id);

	/**
	 * Calling this methods indicates to the underlying storage system that the
	 * data item associated with the identifier $id is accessed. this
	 * information can be used for managing an last access list, order of stored
	 * items, etc.
	 * 
	 * @param id
	 */
	public abstract void access(Identifier id);

	/**
	 * 
	 * @return current size of the data store, i.e., number of stores data items
	 */
	public abstract int size();

	public abstract Set<Identifier> getIdentifiers();

	public abstract Collection<DataItem> getData();
}
