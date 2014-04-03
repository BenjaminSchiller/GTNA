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
 * NodeValueList.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2014-02-04
 * ---------------------------------------
 *
 */
package gtna.data;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import gtna.util.ArrayUtils;
import gtna.util.Distribution;
import gtna.util.Statistics;

/**
 * @author Tim
 * 
 */
public class NodeValueList {
	private String key;
	public double[] nodes;
	public double[] values;

	public NodeValueList(String key, double[] values) {
		this(key, null, values);
	}

	public NodeValueList(String key, double[] nodes, double[] values) {
		this.key = key;
		if (nodes != null)
			this.nodes = nodes;
		else {
			this.nodes = new double[values.length];
			for (int i = 0; i < values.length; i++) {
				this.nodes[i] = i;
			}
		}
		this.values = values;
	}

	public String getKey() {
		return this.key;
	}

	public double[] getValues() {
		return this.values;
	}

	public double[] getNodes() {
		return this.nodes;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.key);
		sb.append("\n");

		for (int i = 0; i < this.values.length; i++) {
			sb.append(this.nodes[i] + " -> " + this.values[i]);
			sb.append("\n");
		}

		return sb.toString();
	}

	/**
	 * 
	 * @param nob
	 *            Number of Bins
	 * @return
	 */
	public Distribution toDistribution(double binsize) {

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for (double d : this.values) {
			min = (min > d) ? d : min;
			max = (max < d) ? d : max;
		}

		return this.toDistribution(binsize, new double[] {min, max});

	}

	public Distribution toDistribution(double binsize, double[] borders) {
		double[][] binned;

		double up = (borders[0] >= borders[1]) ? borders[0] : borders[1];
		double down = (borders[0] >= borders[1]) ? borders[1] : borders[0];
		

		binned = Statistics.binnedDistribution(this.values, down, up, (int)Math.floor((up-down)/binsize));
		
	

		Distribution d = new Distribution(this.key + "-distribution",
				binned);
		return d;

	}

}
