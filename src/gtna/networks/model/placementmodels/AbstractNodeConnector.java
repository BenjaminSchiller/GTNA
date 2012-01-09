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
 * NodeConnector.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Philipp Neubrand;
 * Contributors:    -;
 *
 * ---------------------------------------
 */
package gtna.networks.model.placementmodels;

import gtna.util.Util;

/**
 * An <code>AbstractNodeConnector</code> is the abstract implementation of the
 * <code>INodeConnector</code> interface. It implements all but the actual
 * <code>connect(...)</code> method. Mainly, it provides a way to add additional
 * configuration keys and values which will be returned by the
 * <code>getConfigKeys()</code>/<code>getConfigValues()</code> in addition to
 * the key of the NodeConnector. This is done by setting those additional keys
 * and values by calling
 * <code>setAdditionalConfigKeys/Values(String[] arr)</code>
 * 
 * @author Philipp Neubrand
 * 
 */
public abstract class AbstractNodeConnector implements INodeConnector {
	private String key;

	private String[] additionalConfigKeys;

	private String[] additionalConfigValues;

	/**
	 * Sets additional configuration keys of this NodeConnector, to be returned
	 * by getConfigKeys().
	 * 
	 * @param additionalConfigKeys
	 *            The additional configuration keys to be set.
	 */
	public void setAdditionalConfigKeys(String[] additionalConfigKeys) {
		this.additionalConfigKeys = additionalConfigKeys;
	}

	/**
	 * Sets additional configuration values of this NodeConnector, to be
	 * returned by getConfigKeys().
	 * 
	 * @param additionalConfigValues
	 *            The additional configuration values to be set.
	 */
	public void setAdditionalConfigValues(String[] additionalConfigValues) {
		this.additionalConfigValues = additionalConfigValues;
	}

	/**
	 * Sets the key of the current NodeConnector. Will be returned as the first
	 * key/value pair for the in the configuration keys and values.
	 * 
	 * @param key
	 *            The key of the NodeConnector.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns a String array containing "KEY" as the first value and all values
	 * set with <code>setAdditionalConfigKeys()</code>.
	 */
	@Override
	public String[] getConfigKeys() {
		return Util.mergeArrays(new String[] { "KEY" }, additionalConfigKeys);
	}

	/**
	 * Returns a String array containing the key of this NodeConnector followed
	 * by all values set by <code>setAdditionalConfigValues()</code>.
	 */
	@Override
	public String[] getConfigValues() {
		return Util.mergeArrays(new String[] { key }, additionalConfigValues);
	}
}
