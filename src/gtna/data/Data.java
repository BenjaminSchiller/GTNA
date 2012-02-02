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
 * Data.java
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
package gtna.data;

import gtna.io.DataReader;
import gtna.util.Config;

/**
 * @author benni
 *
 */
public class Data {
	protected static void fill(double[][] values, double[][] x, double[][] y,
			boolean cdf) {
		for (int i = 0; i < values.length; i++) {
			values[i][0] = x[i][0];
			if (i < y.length) {
				values[i][1] = y[i][1];
			} else if (i == 0) {
				values[i][1] = 0;
			} else if (cdf) {
				values[i][1] = values[i - 1][1];
			} else {
				values[i][1] = 0;
			}
		}
	}

	protected static double[][][] readValues(String data, String[] folders) {
		double[][][] values = new double[folders.length][][];
		for (int i = 0; i < folders.length; i++) {
			String filename = folders[i] + Config.get(data + "_DATA_FILENAME")
					+ Config.get("DATA_EXTENSION");
			values[i] = DataReader.readDouble2D(filename);
		}
		return values;
	}

	protected static int maxLength(double[][][] values) {
		int max = 0;
		for (double[][] v : values) {
			if (v.length > max) {
				max = v.length;
			}
		}
		return max;
	}

	protected static double[][] max(double[][][] values) {
		double[][] max = values[0];
		for (double[][] v : values) {
			if (v.length > max.length) {
				max = v;
			}
		}
		return max;
	}
}
