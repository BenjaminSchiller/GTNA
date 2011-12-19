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

/**
 * @author Flipp
 * 
 */
public abstract class AbstractPartitioner {
	private String key;

	private String[] additionalConfigKeys;

	private String[] additionalConfigValues;

	public void setAdditionalConfigKeys(String[] additionalConfigKeys) {
		this.additionalConfigKeys = additionalConfigKeys;
	}

	public void setAdditionalConfigValues(String[] additionalConfigValues) {
		this.additionalConfigValues = additionalConfigValues;
	}

	/**
	 * @param nodes
	 * @param hotspots
	 * @return
	 */
	public abstract int[] partition(int nodes, int hotspots);

	/**
	 * @return
	 */
	public String[] getConfigKeys() {
		return Util.addToArray(new String[] { "KEY" }, additionalConfigKeys);
	}

	/**
	 * @return
	 */
	public String[] getConfigValues() {
		return Util.addToArray(new String[] { getKey() },
				additionalConfigValues);
	}

	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
