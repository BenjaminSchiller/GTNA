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
 * Aggregation.java
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
import gtna.io.DataWriter;
import gtna.io.Filewriter;
import gtna.metrics.Metric;
import gtna.util.Config;

import java.util.Arrays;

/**
 * @author benni
 * 
 */
public class Aggregation {
	public static boolean aggregate(Series s, int times) {
		double interval = Config.getDouble("CONFIDENCE_INTERVAL");
		double z = 1.96;
		if (interval == 0.95) {
			z = 1.96;
		} else if (interval == 0.99) {
			z = 2.576;
		}
		int runs = times;
		if (Config.getBoolean("AGGREGATE_ALL_AVAILABLE_RUNS")) {
			runs = s.getRunFolders().length;
		}
		for (Metric m : s.getMetrics()) {
			if (!Aggregation.aggregate(s, m, z, runs)) {
				return false;
			}
		}
		return Aggregation.aggregateRuntimes(s, z, runs)
				&& Aggregation.aggregateEtc(s, z, runs);
	}

	private static boolean aggregate(Series s, Metric m, double z, int runs) {
		for (String key : m.getDataKeys()) {
			if (!Aggregation.aggregateMulti(s, m, z, runs, key)) {
				return false;
			}
		}
		return Aggregation.aggregateSingle(s, m, z, runs);
	}

	private static boolean aggregateMulti(Series s, Metric m, double z,
			int runs, String key) {
		double[][][] data = new double[runs][][];
		for (int run = 0; run < runs; run++) {
			data[run] = DataReader.readDouble2D(s.getFilenameRun(run, m, key));
		}
		boolean cdf = Config.getBoolean(key + "_DATA_IS_CDF");
		double[] x = Aggregation.extractX(data);
		double[][] values = Aggregation.extractValues(data, x, cdf);
		String filename = s.getMultiFilename(m, key);
		Aggregation.writeAggregation(x, values, z, filename);
		return true;
	}

	private static boolean aggregateFrom(String[] from, String to, double z,
			int runs) {
		SingleList first = SingleList.read(null, from[0]);
		String[] keys = first.getKeys();
		return Aggregation.aggregateSingle(from, to, keys, z);
	}

	private static boolean aggregateSingle(Series s, Metric m, double z,
			int runs) {
		String[] from = new String[runs];
		for (int run = 0; run < runs; run++) {
			from[run] = s.getSinglesFilenameRun(run, m);
		}
		return Aggregation
				.aggregateFrom(from, s.getSinglesFilename(m), z, runs);
	}

	private static boolean aggregateRuntimes(Series s, double z, int runs) {
		String[] from = new String[runs];
		for (int run = 0; run < runs; run++) {
			from[run] = s.getRuntimesFilenameRun(run);
		}
		return Aggregation
				.aggregateFrom(from, s.getRuntimesFilename(), z, runs);
	}

	private static boolean aggregateEtc(Series s, double z, int runs) {
		String[] from = new String[runs];
		for (int run = 0; run < runs; run++) {
			from[run] = s.getEtcFilename(run);
		}
		return Aggregation.aggregateFrom(from, s.getEtcFilename(), z, runs);
	}

	private static boolean aggregateSingle(String[] from, String to,
			String[] keys, double z) {
		Filewriter fw = new Filewriter(to);
		String delimiter = Config.get("DATA_WRITER_DELIMITER");
		SingleList[] lists = new SingleList[from.length];
		for (int i = 0; i < from.length; i++) {
			lists[i] = SingleList.read(null, from[i]);
		}
		for (String key : keys) {
			double[] values = new double[lists.length];
			for (int i = 0; i < lists.length; i++) {
				values[i] = lists[i].get(key).getValue();
			}
			double[] aggr = Aggregation.aggregate(values, z);
			StringBuffer buff = new StringBuffer();
			for (double v : aggr) {
				if (buff.length() == 0) {
					buff.append(v);
				} else {
					buff.append(delimiter + v);
				}
			}
			fw.writeln(key + "=" + buff.toString());
		}
		return fw.close();
	}

	private static boolean writeAggregation(double[] x, double[][] values,
			double z, String filename) {
		double[][] data = new double[x.length][];
		for (int i = 0; i < x.length; i++) {
			data[i] = Aggregation.aggregate(x[i], values[i], z);
		}
		return DataWriter.write(data, filename, false);
	}

	private static double[][] extractValues(double[][][] data, double[] x,
			boolean cdf) {
		double[][] values = new double[x.length][data.length];
		for (int i = 0; i < x.length; i++) {
			for (int run = 0; run < data.length; run++) {
				if (data[run].length > i) {
					values[i][run] = data[run][i][1];
				} else if (cdf && i > 0) {
					values[i][run] = values[i - 1][run];
				} else {
					values[i][run] = 0;
				}
			}
		}
		return values;
	}

	private static double[] extractX(double[][][] data) {
		double[][] max = new double[0][0];
		for (double[][] d : data) {
			if (d.length > max.length) {
				max = d;
			}
		}
		double[] x = new double[max.length];
		for (int i = 0; i < max.length; i++) {
			x[i] = max[i][0];
		}
		return x;
	}

	private static double[] aggregate(double[] values, double z) {
		double[] avgMinMax = Aggregation.computeAvgMinMax(values);
		double avg = avgMinMax[0];
		double min = avgMinMax[1];
		double max = avgMinMax[2];
		double med = Aggregation.computeMed(values);
		double[] varLowUp = Aggregation.computeVarLowUp(values, avgMinMax[0]);
		double var = varLowUp[0];
		double varLow = varLowUp[1];
		double varUp = varLowUp[2];
		double[] confLowUp = Aggregation.computeConfLowUp(values, avgMinMax[0],
				z, var);
		double confLow = confLowUp[0];
		double confUp = confLowUp[1];
		// 0 avg
		// 1 med
		// 2 min
		// 3 max
		// 4 var
		// 5 varLow
		// 6 varUp
		// 7 confLow
		// 8 confUp
		return new double[] { avg, med, min, max, var, varLow, varUp, confLow,
				confUp };
	}

	private static double[] aggregate(double x, double[] values, double z) {
		double[] data = Aggregation.aggregate(values, z);
		double[] v = new double[data.length + 1];
		v[0] = x;
		for (int i = 0; i < data.length; i++) {
			v[i + 1] = data[i];
		}
		return v;
	}

	public static double[] computeAvgMinMax(double[] values) {
		double avg = 0;
		double min = values[0];
		double max = values[0];
		for (double v : values) {
			avg += v;
			if (v < min) {
				min = v;
			}
			if (v > max) {
				max = v;
			}
		}
		avg /= values.length;
		return new double[] { avg, min, max };
	}

	public static double computeMed(double[] values) {
		double[] v = values.clone();
		Arrays.sort(v);
		return v[v.length / 2];
	}

	public static double[] computeVarLowUp(double[] values, double avg) {
		double var = 0;
		double varLow = 0;
		double varUp = 0;
		int countLow = 0;
		int countUp = 0;
		for (double v : values) {
			var += Math.pow(v - avg, 2);
			if (v < avg) {
				varLow += Math.pow(v - avg, 2);
				countLow++;
			} else if (v > avg) {
				varUp += Math.pow(v - avg, 2);
				countUp++;
			}
		}
		var /= values.length;
		if (countLow == 0) {
			varLow = 0;
		} else {
			varLow /= countLow;
		}
		if (countUp == 0) {
			varUp = 0;
		} else {
			varUp /= countUp;
		}
		return new double[] { var, varLow, varUp };
	}

	public static double[] computeConfLowUp(double values[], double avg,
			double z, double var) {
		double dev = Math.sqrt(var);
		double confLow = avg - z * dev / Math.sqrt(values.length);
		double confUp = avg + z * dev / Math.sqrt(values.length);
		return new double[] { confLow, confUp };
	}
}
