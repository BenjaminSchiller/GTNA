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
 */
@SuppressWarnings("rawtypes")
public class DataStorage {
	private Map<Identifier, DataItem> storage;

	public DataStorage() {
		this.storage = new HashMap<Identifier, DataItem>();
	}

	public DataStorage(Identifier id) {
		this();
		this.storage.put(id, new DataItem(id));
	}

	public void add(Identifier id, DataItem data) {
		this.storage.put(id, data);
	}

	public void add(Identifier id) {
		this.add(id, new DataItem(id));
	}

	public void remove(Identifier id) {
		this.storage.remove(id);
	}

	public boolean containsId(Identifier id) {
		return this.storage.containsKey(id);
	}

	public int size() {
		return this.storage.size();
	}

	public Set<Identifier> getIdentifiers() {
		return this.storage.keySet();
	}

	public Collection<DataItem> getDataItems() {
		return this.storage.values();
	}
}
