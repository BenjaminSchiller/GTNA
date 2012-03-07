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
 * DataAggregation.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-12 : v1 (BS)
 *
 */
package gtna.data;

import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Config;

import java.io.FileWriter;
import java.io.IOException;

public class DataAggregation {
	public static void aggregate(Network nw, String dst, String[] src,
			Metric[] metrics) {
		aggregateMultiScalar(nw, dst, src, metrics);
		aggregateSingleScalar(nw, dst, src);
	}

	public static void aggregate(Network nw, String dst, String src, int times,
			Metric[] metrics) {
		aggregateMultiScalar(nw, dst, src, times, metrics);
		aggregateSingleScalar(nw, dst, src, times);
	}

	public static void aggregate(Network nw, String dst, int times,
			Metric[] metrics) {
		aggregateMultiScalar(nw, dst, times, metrics);
		aggregateSingleScalar(nw, dst, times);
	}

	public static void aggregateMultiScalar(Network nw, String dst,
			String[] src, Metric[] metrics) {
		System.out.println("MULTI(" + src.length + " @ " + dst);
		String dstAvg = dst + Config.get("SERIES_AVERAGE_DATA_FOLDER");
		String dstConf = dst + Config.get("SERIES_CONFIDENCE_DATA_FOLDER");
		String dstVar = dst + Config.get("SERIES_VARIANCE_DATA_FOLDER");
		String[] s = new String[src.length];
		for (int i = 0; i < s.length; i++) {
			s[i] = src[i] + Config.get("GRAPH_DATA_FOLDER");
		}
		AverageData.generate(dstAvg, s, metrics);
		ConfidenceData.generate(dstConf, s, metrics);
		// VarianceData.generate(dstVar, s);
	}

	public static void aggregateMultiScalar(Network nw, String dst, String src,
			int times, Metric[] metrics) {
		aggregateMultiScalar(nw, dst, src(src, times), metrics);
	}

	public static void aggregateMultiScalar(Network nw, String dst, int times,
			Metric[] metrics) {
		aggregateMultiScalar(nw, dst, dst, times, metrics);
	}

	public static void aggregateSingleScalar(Network nw, String dst,
			String[] src) {
		System.out.println("SINGLES(" + src.length + " @ " + dst);
		String dstAverage = dst + Config.get("SERIES_AVERAGE_FILENAME");
		String dstSingles = dst + Config.get("SERIES_SINGLES_FILENAME");
		String dstOutput = dst + Config.get("SERIES_OUTPUT_FILENAME");
		String[] s = new String[src.length];
		for (int i = 0; i < s.length; i++) {
			s[i] = src[i];
		}

		Singles[] Summaries = new Singles[s.length];
		for (int i = 0; i < s.length; i++) {
			Summaries[i] = new Singles(s[i]
					+ Config.get("GRAPH_SINGLES_FILENAME"));
		}
		Singles averageSingles = Singles.average(Summaries);
		averageSingles.write(dstAverage);

		Singles[][] summaries = new Singles[1][s.length];
		for (int i = 0; i < s.length; i++) {
			summaries[0][i] = new Singles(s[i]
					+ Config.get("GRAPH_SINGLES_FILENAME"));
		}
		String[] names = new String[] { nw.getName() };
		Singles.write(summaries, dstSingles, names);

		FileWriter fw;
		try {
			fw = new FileWriter(dstOutput);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void aggregateSingleScalar(Network nw, String dst,
			String src, int times) {
		aggregateSingleScalar(nw, dst, src(src, times));
	}

	public static void aggregateSingleScalar(Network nw, String dst, int times) {
		aggregateSingleScalar(nw, dst, dst, times);
	}

	private static String[] src(String src, int times) {
		String[] s = new String[times];
		for (int i = 0; i < s.length; i++) {
			s[i] = src + i + Config.get("FILESYSTEM_FOLDER_DELIMITER");
		}
		return s;
	}
}
