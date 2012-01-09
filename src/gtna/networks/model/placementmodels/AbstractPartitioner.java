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
 * Partitioner.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.networks.model.placementmodels;

import gtna.util.Util;

/**
 * Abstract implementation of the <code>IPartitioner</code> interface,
 * implementing all the common functionality of Partitioners. The only abstract
 * method left to be implemented is
 * <code>partition(int nodes, int hotspots)</code>, defining the actual
 * behaviour of the particular Partitioner.
 * 
 * @author Philipp Neubrand
 * 
 */
public abstract class AbstractPartitioner implements IPartitioner {
	// contains the key for the partitioner
	private String key;

	// contains all additional config keys, this array will be returned in
	// addition to "KEY"
	private String[] additionalConfigKeys;

	// contains all additional config values, this array will be returned in
	// addition to the key of this partitioner
	private String[] additionalConfigValues;

	/**
	 * Setter for additionalConfigKeys. Should be called in the Constructor of
	 * the implementing class to store any additional configuration keys for the
	 * particular Partitioner.
	 * 
	 * @param additionalConfigKeys
	 *            A String array containing all the additional configuration
	 *            keys.
	 */
	public void setAdditionalConfigKeys(String[] additionalConfigKeys) {
		this.additionalConfigKeys = additionalConfigKeys;
	}

	/**
	 * Setter for additionalConfigValues. Should be called in the Constructor of
	 * the implementing class to store any additional configuration values for
	 * the particular Partitioner.
	 * 
	 * @param additionalConfigKeys
	 *            A String array containing all the additional configuration
	 *            values.
	 */
	public void setAdditionalConfigValues(String[] additionalConfigValues) {
		this.additionalConfigValues = additionalConfigValues;
	}

	/**
	 * Default implementation of <code>getConfigKeys</code>. Will return "KEY"
	 * as well as any additional configuration keys stored in
	 * <code>additionalConfigKeys</code>.
	 */
	public String[] getConfigKeys() {
		return Util.mergeArrays(new String[] { "KEY" }, additionalConfigKeys);
	}

	/**
	 * Default implementation of <code>getConfigValues</code>. Will return the
	 * key as well as any additional configuration values stored in
	 * <code>additionalConfigValues</code>.
	 */
	public String[] getConfigValues() {
		return Util.mergeArrays(new String[] { key }, additionalConfigValues);
	}

	/**
	 * Setter for the key of the particular Partitioner. Should be called in the
	 * Constructor and set to a distinct value.
	 * 
	 * @param key
	 *            The key for this Partitioner.
	 */
	public void setKey(String key) {
		this.key = key;
	}

}
