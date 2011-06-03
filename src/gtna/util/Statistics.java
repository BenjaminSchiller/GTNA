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
 * Statistics.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    "Stefanie Roos";
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-06 : v1 (BS)
 *
 */
package gtna.util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class Statistics {

	public static double[][] probabilityDistribution(double[] values,
			double start, double step) {
		ArrayList<double[]> res = new ArrayList<double[]>();
		Arrays.sort(values);
		double a = start;
		int pos = 0;
		res.add(new double[] { a, 0 });
		while (pos < values.length) {
			a = a + step;
			int counter = 0;
			while (pos < values.length && values[pos] <= a) {
				pos++;
				counter++;
			}
			res
					.add(new double[] { a,
							(double) counter / (double) values.length });
		}
		double[][] pd = new double[res.size()][];
		for (int i = 0; i < res.size(); i++) {
			pd[i] = res.get(i);
		}
		return pd;
	}

	public static double[][] empiricalDistributionFunction(double[] values,
			double start, double step) {
		double[][] pd = probabilityDistribution(values, start, step);
		return empiricalDistributionFunction(pd, start, step);
	}

	public static double[][] empiricalDistributionFunction(double[][] pd,
			double start, double step) {
		double[][] edf = new double[pd.length][2];
		edf[0][0] = pd[0][0];
		edf[0][1] = pd[0][1];
		for (int i = 1; i < pd.length; i++) {
			edf[i][0] = pd[i][0];
			edf[i][1] = pd[i][1] + edf[i - 1][1];
		}
		return edf;
	}
}
