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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * MetricImpl.java
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
package gtna.metrics;

import gtna.util.Config;

public abstract class MetricImpl implements Metric{
	private String key;

	public MetricImpl(String key) {
		this.key = key;
	}

	public String[] dataPlots() {
		return Config.keys(this.key + "_DATA_PLOTS");
	}

	public String[] singlesPlots() {
		return Config.keys(this.key + "_SINGLES_PLOTS");
	}

	public String[] dataKeys() {
		return Config.keys(this.key + "_DATA_KEYS");
	}

	public String[] singlesKeys() {
		return Config.keys(this.key + "_SINGLES_KEYS");
	}

	public String name() {
		return Config.get(key + "_NAME");
	}

	public String key() {
		return this.key;
	}
}
