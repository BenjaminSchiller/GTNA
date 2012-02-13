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
 * PlacementModel.java
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
 * 
 * The default implementation of the <code>PlacementModel</code> interface
 * implements basic functionality as well as a convenient method to add
 * additional configuration keys and values. Extending classes only need to
 * implement the <code>place(...)</code> method and set a key with
 * <code>setKey(...)</code>. If additional configuration keys and values are
 * needed, they can be set by calling <code>setAdditionalConfigKeys(...)</code>
 * (...values(...)). These will be returned by <code>getConfigKeys()</code> in
 * addition to the key of the <code>PlacementModel</code> and the value of
 * inCenter.
 * 
 * @author Philipp Neubrand
 * 
 */
public abstract class PlacementModelImpl implements PlacementModel {
	private String key;
	private String[] additionalConfigKeys;
	private String[] additionalConfigValues;
	protected final int maxTries = 100;
	private boolean inCenter;

	/**
	 * Getter for the key of the particular <code>PlacementModel</code>.
	 * 
	 * @return The key of the <code>PlacementModel</code>.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Setter for the key of the particular <code>PlacementModel</code>.
	 * 
	 * @param key
	 *            The new key of the <code>PlacementModel</code>.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Getter for the configuration keys. Returns "KEY", "IN_CENTER" as well as
	 * any additional configuration keys set by
	 * <code>setAdditionalConfigKeys()</code>.
	 * 
	 * @return A string array containing all the configuration keys.
	 */
	public String[] getConfigKeys() {
		return Util.mergeArrays(new String[] { "KEY", "IN_CENTER" },
				additionalConfigKeys);
	}

	/**
	 * Setter for the additional configuration keys. Those will be returned in
	 * addition to "KEY", "IN_CENTER" by <code>getConfigKeys()</code>.
	 * 
	 * @param arr
	 *            The new additional configuration keys.
	 */
	public void setAdditionalConfigKeys(String[] arr) {
		additionalConfigKeys = arr;
	}

	/**
	 * Setter for the additional configuration values. Those will be returned in
	 * addition to the key, the width and the height by
	 * <code>getConfigValues()</code>.
	 * 
	 * @param arr
	 *            The new additional configuration values.
	 */
	public void setAdditionalConfigValues(String[] arr) {
		additionalConfigValues = arr;
	}

	/**
	 * Getter for the configuration values. Returns key and inCenter as well as
	 * any additional configuration keys set by
	 * <code>setAdditionalConfigValues()</code>.
	 * 
	 * @return A string array containing all the configuration values.
	 */
	public String[] getConfigValues() {
		return Util.mergeArrays(new String[] { getKey(),
				Boolean.toString(getInCenter()) }, additionalConfigValues);
	}

	/**
	 * Setter for inCenter.
	 * 
	 * @param inCenter
	 *            The new value for inCenter.
	 */
	public void setInCenter(boolean inCenter) {
		this.inCenter = inCenter;
	}

	/**
	 * Getter for inCenter.
	 * 
	 * @return The value of inCenter.
	 */
	public boolean getInCenter() {
		return inCenter;
	}

}
