/*
 * ===========================================================
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
 * RoutingAlgorithmImpl.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.routing;

import gtna.util.Config;

/**
 * Implements all basic features of a routing algorithm. It is recommended to
 * always extend this class when implementing a new routing algorithm.
 * 
 * @author benni
 * 
 */
public abstract class RoutingAlgorithmImpl implements RoutingAlgorithm {
	private String key;

	private String[] configKeys;

	private String[] configValues;

	/**
	 * 
	 * @param key
	 *            key of the routing algorithm, used for the configuration of
	 *            the routing algorithm
	 * @param configKeys
	 *            keys of the routing algorithm's parameters, also used for the
	 *            configuration
	 * @param configValues
	 *            value for each parameter
	 */
	public RoutingAlgorithmImpl(String key, String[] configKeys,
			String[] configValues) {
		this.key = key;
		this.configKeys = configKeys;
		this.configValues = configValues;
	}

	public String key() {
		return this.key;
	}

	public String name() {
		return this.name("");
	}

	public String nameLong() {
		return this.name("_LONG");
	}

	public String nameShort() {
		return this.name("_SHORT");
	}

	private String name(String key) {
		String name = Config.get(this.key + "_NAME" + key);
		for (int i = 0; i < this.configKeys.length; i++) {
			name += " "
					+ Config.get(this.key + "_" + this.configKeys[i] + "_NAME"
							+ key);
			name += "=" + this.configValues[i];
		}
		return name;
	}

	public String folder() {
		String folder = Config.get(this.key + "_FOLDER");
		for (int i = 0; i < this.configValues.length; i++) {
			folder += "-" + this.configValues[i];
		}
		return folder;
	}

	public String[] configKeys() {
		return this.configKeys;
	}

	public String[] configValues() {
		return this.configValues;
	}

	public String compareName(RoutingAlgorithm ra, String key) {
		if (!this.getClass().equals(ra.getClass())) {
			return Config.get("ROUTING_ALGORITHM_NOT_COMPARABLE");
		}
		for (int i = 0; i < this.configValues.length; i++) {
			if (!this.configValues[i].equals(ra.configValues()[i])) {
				return Config.get(this.key + "_" + this.configKeys[i] + "_NAME"
						+ key);
			}
		}
		return Config.get("ROUTING_ALGORITHM_COMPARE_SAME_NAME");
	}

	public String compareValue(RoutingAlgorithm ra) {
		if (!this.getClass().equals(ra.getClass())) {
			return Config.get("ROUTING_ALGORITHM_NOT_COMPARABLE");
		}
		for (int i = 0; i < this.configValues.length; i++) {
			if (!this.configValues[i].equals(ra.configValues()[i])) {
				return this.configValues[i];
			}
		}
		return Config.get("ROUTING_ALGORITHM_COMPARE_SAME_VALUE");
	}
}
