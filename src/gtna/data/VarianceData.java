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
 * VarianceData.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-07-09 : v1 (BS)
 *
 */
package gtna.data;

import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.util.Config;
import gtna.util.Statistics;
import gtna.util.Util;

import java.util.ArrayList;

public class VarianceData {
	public static void generate(String destFolder, String[] folders) {
		String[] data = Config.getData();
		for (int i = 0; i < data.length; i++) {
			ArrayList<double[][]> values = new ArrayList<double[][]>();
			for (int j = 0; j < folders.length; j++) {
				String filename = folders[j]
						+ Config.get(data[i] + "_DATA_FILENAME")
						+ Config.get("DATA_EXTENSION");
				double[][] currentValues = DataReader.readDouble2D(filename);
				for (int k = 0; k < currentValues.length; k++) {
					double x = currentValues[k][0];
					double value = currentValues[k][1];
					if (k >= values.size()) {
						values.add(init(folders.length));
					}
					values.get(k)[j][0] = x;
					values.get(k)[j][1] = value;
				}
			}
			double[][] variance = new double[values.size()][3];
			for (int j = 0; j < values.size(); j++) {
				double[][] currentValues = values.get(j);
				ArrayList<Double> X = new ArrayList<Double>();
				ArrayList<Double> Y = new ArrayList<Double>();
				for (int k = 0; k < currentValues.length; k++) {
					if (!Double.isNaN(currentValues[k][1])) {
						X.add(currentValues[k][0]);
						Y.add(currentValues[k][1]);
					}
				}
				double[] x = Util.toDoubleArray(X);
				double[] y = Util.toDoubleArray(Y);
				double xAvg = Util.avg(x);
				double yAvg = Util.avg(y);
				double v = Statistics.variance(y, yAvg);
				variance[j][0] = xAvg;
				variance[j][1] = yAvg;
				variance[j][2] = v;
			}
			DataWriter.writeWithoutIndex(variance, data[i], destFolder);
		}
	}

	private static double[][] init(int length) {
		double[][] array = new double[length][2];
		for (int i = 0; i < length; i++) {
			array[i][0] = Double.NaN;
			array[i][1] = Double.NaN;
		}
		return array;
	}
}
