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
 * AverageData.java
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
package gtna.data;

import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.util.Config;

public class AverageData extends Data {
	public static void generate(String destFolder, String[] folders,
			Metric[] metrics) {
		for (Metric m : metrics) {
			String temp2 = destFolder + m.getFolder() + "/";
			String[] temp1 = new String[folders.length];
			for (int i = 0; i < folders.length; i++) {
				temp1[i] = folders[i] + m.getFolder() + "/";
			}
			for (String d : m.dataKeys()) {
				boolean cdf = Config.getBoolean(d + "_DATA_IS_CDF");
				AverageData.generate(temp2, temp1, d, cdf);
			}
		}
		// String[] data = Config.getData(metrics);
		// for (String d : data) {
		// boolean cdf = Config.getBoolean(d + "_DATA_IS_CDF");
		// AverageData.generate(destFolder, folders, d, cdf);
		// }
	}

	private static void generate(String destFolder, String[] folders,
			String data, boolean cdf) {
		double[][][] v1 = AverageData.readValues(data, folders);
		int maxLength = AverageData.maxLength(v1);
		double[][] max = AverageData.max(v1);
		double[][][] values = new double[v1.length][maxLength][2];
		for (int i = 0; i < v1.length; i++) {
			AverageData.fill(values[i], max, v1[i], cdf);
		}
		double[][] avg = AverageData.computeAverage(values);
		DataWriter.writeWithoutIndex(avg, data, destFolder);
	}

	private static double[][] computeAverage(double[][][] values) {
		double[][] avg = new double[values[0].length][2];
		for (int i = 0; i < values[0].length; i++) {
			double y = 0;
			for (int j = 0; j < values.length; j++) {
				y += values[j][i][1];
			}
			y /= (double) values.length;
			avg[i][0] = values[0][i][0];
			avg[i][1] = y;
		}
		return avg;
	}
}
