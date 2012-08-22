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
 * Lookahead.java
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
package gtna.projects.etc;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.p2p.chord.Chord;
import gtna.networks.p2p.chord.Chord.IDSelection;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.lookahead.LookaheadMinVia;
import gtna.routing.lookahead.LookaheadSequential;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomRingIDSpace;
import gtna.transformation.lookahead.NeighborsFirstLookaheadList;
import gtna.transformation.lookahead.NeighborsFirstObfuscatedLookaheadList;
import gtna.transformation.lookahead.NeighborsGroupedLookaheadList;
import gtna.transformation.lookahead.RandomLookaheadList;
import gtna.util.Config;
import gtna.util.Stats;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class LookaheadRouting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Stats stats = new Stats();

		Config.overwrite("METRICS", "R");
		Metric[] metrics = new Metric[] {};
		Config.overwrite("MAIN_DATA_FOLDER", "./data/lookahead/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/lookahead/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		Config.overwrite("PLOT_EXTENSION", ".png");
		Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal png");

		Config.overwrite("SINGLES_PLOT_LINE_WIDTH", "2");
		Config.overwrite("SINGLES_PLOT_POINT_WIDTH", "2");
		Config.overwrite("SINGLES_PLOT_WHISKER_WIDTH", "2");
		Config.overwrite("AVERAGE_PLOT_LINE_WIDTH", "2");
		Config.overwrite("AVERAGE_PLOT_POINT_WIDTH", "2");
		Config.overwrite("AVERAGE_PLOT_DOT_WIDTH", "2");
		Config.overwrite("CONF_PLOT_LINE_WIDTH", "2");
		Config.overwrite("CONF_PLOT_WHISKER_WIDTH", "2");

		Config.overwrite("R_HOP_DISTRIBUTION_CDF_PLOT_KEY", "right bottom");
		Config.overwrite("R_HOP_DISTRIBUTION_ABSOLUTE_CDF_PLOT_KEY",
				"right bottom");

		boolean generate = false;
		int times = 10;
		int nodes = 1000;

		// LookaheadRouting.obfuscation(generate, times, Input.Chord, nodes);
		LookaheadRouting
				.obfuscation(generate, times, Input.SPI, nodes, metrics);
		// LookaheadRouting.obfuscation(generate, times, Input.ErdosRenyi,
		// nodes);
		// LookaheadRouting.lookahead(generate, times, nodes);

		// Config.overwrite("METRICS", "DD");
		// Network nw = new ErdosRenyi(100, 10, true, null, null);
		// Series s = Series.generate(nw, 3);
		// Plot.multiAvg(s, "test/");

		stats.end();
	}

	private enum Input {
		Chord, WOT, SPI, ErdosRenyi
	}

	// private static void test() {
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/test/");
	// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/test/");
	//
	// Transformation t1 = new RandomRingIDSpaceSimple();
	// Transformation t2 = new NeighborsFirstObfuscatedLookaheadList(.1E-5,
	// .1E-6, false);
	// Transformation t3 = new NeighborsFirstLookaheadList(false);
	//
	// Network nw1 = new ErdosRenyi(1000, 20, true,
	// new LookaheadSequential(50), new Transformation[] { t1, t3 });
	// Network nw2 = new ErdosRenyi(1000, 20, true,
	// new LookaheadSequential(50), new Transformation[] { t1, t2 });
	//
	// Network[] nw = new Network[] { nw1, nw2 };
	//
	// Series[] s = Series.generate(nw, 1);
	// Plot.multiAvg(s, "multi/");
	// }

	private static void lookahead(boolean generate, int times, int nodes,
			Metric[] metrics) {
		int chordNodes = nodes;
		int chordBits = 20;
		IDSelection chordUniform = Chord.IDSelection.UNIFORM;

		Transformation nf = new NeighborsFirstLookaheadList(false);
		Transformation nfr = new NeighborsFirstLookaheadList(true);
		Transformation ng = new NeighborsGroupedLookaheadList(false);
		Transformation ngr = new NeighborsGroupedLookaheadList(true);
		Transformation r = new RandomLookaheadList();
		Transformation[] ll = new Transformation[] { nf, nfr, r, ng, ngr };

		HashMap<Transformation, String> map = new HashMap<Transformation, String>();
		map.put(nf, "NF");
		map.put(nfr, "NFR");
		map.put(ng, "NG");
		map.put(ngr, "NGR");
		map.put(r, "R");

		RoutingAlgorithm g = new Greedy(50);
		RoutingAlgorithm ls = new LookaheadSequential(50);
		RoutingAlgorithm lmv = new LookaheadMinVia(50);

		for (Transformation l : ll) {
			Network nw1 = new Chord(chordNodes, chordBits, 1, chordUniform,
					null);
			Network nw2 = new Chord(chordNodes, chordBits, 1, chordUniform,
					new Transformation[] { l });
			Network nw3 = new Chord(chordNodes, chordBits, 1, chordUniform,
					new Transformation[] { l });
			Network[] nw = new Network[] { nw1, nw2, nw3 };
			Series[] s = generate ? Series.generate(nw, metrics, times)
					: Series.get(nw, metrics);
			// Plot.multiAvg(s, "LRA-" + map.get(l) + "-multi-avg/");
			// Plot.multiConf(s, "LRA-" + map.get(l) + "-multi-conf/");
		}

		Network[] sls = new Network[ll.length + 1];
		sls[0] = new Chord(chordNodes, chordBits, 1, chordUniform, null);
		sls[0] = new DescriptionWrapper(sls[0], "G");
		Network[] slmv = new Network[ll.length + 1];
		slmv[0] = new Chord(chordNodes, chordBits, 1, chordUniform, null);
		slmv[0] = new DescriptionWrapper(sls[0], "G");
		for (int i = 0; i < ll.length; i++) {
			sls[i + 1] = new Chord(chordNodes, chordBits, 1, chordUniform,
					new Transformation[] { ll[i] });
			sls[i + 1] = new DescriptionWrapper(sls[i + 1], map.get(ll[i]));
			slmv[i + 1] = new Chord(chordNodes, chordBits, 1, chordUniform,
					new Transformation[] { ll[i] });
			slmv[i + 1] = new DescriptionWrapper(slmv[i + 1], map.get(ll[i]));
		}
		Plotting.multi(Series.get(sls, metrics), metrics, "LRA-LS-multi/");
		Plotting.multi(Series.get(slmv, metrics), metrics, "LRA-LMV-multi/");
		LookaheadRouting.blafasel(Series.get(sls, metrics), "LS", metrics);
		LookaheadRouting.blafasel(Series.get(slmv, metrics), "LMV", metrics);
	}

	private static void blafasel(Series[] s1, String S, Metric[] metrics) {
		for (int i = 1; i <= s1.length; i++) {
			Series[] s = new Series[i];
			System.arraycopy(s1, 0, s, 0, i);
			Plotting.multi(s, metrics, "LRA-" + S + "-multi-" + i + "/");
			String m = Config.get("MAIN_PLOT_FOLDER");
			String e = Config.get("PLOT_EXTENSION");
			try {
				Runtime.getRuntime().exec(
						"mv " + m + "LRA-" + S + "-multi-avg-" + i
								+ "/r-hopDistributionAbsolute-cdf" + e + " "
								+ m + "LRA-" + S
								+ "-multi-avg/r-hopDistributionAbsolute-cdf-"
								+ i + e);
				Runtime.getRuntime().exec(
						"mv " + m + "LRA-" + S + "-multi-conf-" + i
								+ "/r-hopDistributionAbsolute-cdf" + e + " "
								+ m + "LRA-" + S
								+ "-multi-conf/r-hopDistributionAbsolute-cdf-"
								+ i + e);
				Runtime.getRuntime().exec(
						"rm -r " + m + "LRA-" + S + "-multi-avg-" + i + "/");
				Runtime.getRuntime().exec(
						"rm -r " + m + "LRA-" + S + "-multi-conf-" + i + "/");
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	private static void obfuscation(boolean generate, int times, Input input,
			int nodes, Metric[] metrics) {
		int chordNodes = nodes;
		int chordBits = 20;
		IDSelection chordUniform = Chord.IDSelection.RANDOM;

		int[] obfuscationBI = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
				12, 13, 14, 15, 16, 17, 18, 19, 20 };

		int erNodes = nodes;
		double erDegree = 20;
		boolean erBD = true;

		String spiGraph = "./temp/test/spi.txt";
		String spiName = "SPI";
		String spiFolder = "spi";

		String wotGraph = "./temp/test/wot.txt";
		String wotName = "WOT";
		String wotFolder = "wot";

		double[] obfuscationD = new double[] { 0.0, .1E-10, .1E-9, .1E-8,
				.1E-7, .1E-6, .1E-5, .1E-4, .1E-3, .1E-2, .1E-1 };
		// double[] obfuscationD = new double[] { 0.0, .1E-10, .1E-9, .1E-8,
		// .1E-7, .1E-6, .1E-5, .1E-4, .1E-3, .1E-2, .1E-1, .1 };

		Transformation t1 = new RandomRingIDSpace(true);

		Transformation nf = new NeighborsFirstLookaheadList(false);
		Transformation nfr = new NeighborsFirstLookaheadList(true);
		Transformation ng = new NeighborsGroupedLookaheadList(false);
		Transformation ngr = new NeighborsGroupedLookaheadList(true);
		Transformation r = new RandomLookaheadList();
		Transformation[] ll = new Transformation[] { nf, nfr, ng, ngr, r };

		HashMap<Transformation, String> map = new HashMap<Transformation, String>();
		map.put(nf, "NF");
		map.put(nfr, "NFR");
		map.put(ng, "NG");
		map.put(ngr, "NGR");
		map.put(r, "R");

		if (input == Input.ErdosRenyi || input == Input.SPI
				|| input == Input.WOT) {
			ll = new Transformation[obfuscationD.length];
			for (int i = 0; i < obfuscationD.length; i++) {
				ll[i] = new NeighborsFirstObfuscatedLookaheadList(0.0,
						obfuscationD[i], false);
				map.put(ll[i], "NFO-" + obfuscationD[i]);
			}
		} else if (input == Input.Chord) {
			ll = new Transformation[obfuscationBI.length];
			for (int i = 0; i < obfuscationBI.length; i++) {
				ll[i] = new NeighborsFirstObfuscatedLookaheadList(0,
						obfuscationBI[i], false);
				map.put(ll[i], "NFO-" + obfuscationBI[i]);
			}
		}

		Transformation[][] LL = new Transformation[ll.length][];
		for (int i = 0; i < ll.length; i++) {
			if (input == Input.Chord) {
				LL[i] = new Transformation[] { ll[i] };
			} else if (input == Input.SPI || input == Input.WOT
					|| input == Input.ErdosRenyi) {
				LL[i] = new Transformation[] { t1, ll[i] };
			}
		}

		RoutingAlgorithm greedy = new Greedy(50);
		RoutingAlgorithm lookahead = new LookaheadSequential(50);

		Network[] nw = new Network[ll.length + 1];
		if (input == Input.Chord) {
			nw[0] = new Chord(chordNodes, chordBits, 1, chordUniform, null);
		} else if (input == Input.SPI) {
			nw[0] = new ReadableFile(spiName, spiFolder, spiGraph,
					new Transformation[] { t1 });
		} else if (input == Input.WOT) {
			nw[0] = new ReadableFile(wotName, wotFolder, wotGraph,
					new Transformation[] { t1 });
		} else if (input == Input.ErdosRenyi) {
			nw[0] = new ErdosRenyi(erNodes, erDegree, erBD,
					new Transformation[] { t1 });
		}
		nw[0] = new DescriptionWrapper(nw[0], "Greedy");
		for (int i = 0; i < LL.length; i++) {
			int index = 0;
			if (input == Input.Chord) {
				nw[i + 1] = new Chord(chordNodes, chordBits, 1, chordUniform,
						LL[i]);
			} else if (input == Input.SPI) {
				nw[i + 1] = new ReadableFile(spiName, spiFolder, spiGraph,
						LL[i]);
				index = 1;
			} else if (input == Input.WOT) {
				nw[i + 1] = new ReadableFile(wotName, wotFolder, wotGraph,
						LL[i]);
				index = 1;
			} else if (input == Input.ErdosRenyi) {
				nw[i + 1] = new ErdosRenyi(erNodes, erDegree, erBD, LL[i]);
				index = 1;
			}
			nw[i + 1] = new DescriptionWrapper(nw[i + 1], map.get(LL[i][index])
					+ " - " + lookahead.getNameShort());
		}

		Series[] s1 = generate ? Series.generate(nw, metrics, times) : Series
				.get(nw, metrics);
		String folderAvgStd = "obfuscation-" + input + "-" + nw[0].getNodes()
				+ "-multi-avg/";
		String folderConfStd = "obfuscation-" + input + "-" + nw[0].getNodes()
				+ "-multi-conf/";
		Plotting.multi(s1, metrics, folderAvgStd);

		for (int i = 1; i <= s1.length; i++) {
			Series[] s2 = new Series[i];
			System.arraycopy(s1, 0, s2, 0, i);
			String folderAvg = "obfuscation-" + input + "-" + nw[0].getNodes()
					+ "-multi-avg-" + i + "/";
			String folderConf = "obfuscation-" + input + "-" + nw[0].getNodes()
					+ "-multi-conf-" + i + "/";
			Plotting.multi(s2, metrics, folderAvg);
			String e = Config.get("PLOT_EXTENSION");
			String fromAvg = Config.get("MAIN_PLOT_FOLDER") + folderAvg
					+ "r-hopDistributionAbsolute-cdf" + e;
			String toAvg = Config.get("MAIN_PLOT_FOLDER") + folderAvgStd
					+ "r-hopDistributionAbsolute-cdf-" + i + e;
			String fromConf = Config.get("MAIN_PLOT_FOLDER") + folderConf
					+ "r-hopDistributionAbsolute-cdf" + e;
			String toConf = Config.get("MAIN_PLOT_FOLDER") + folderConfStd
					+ "r-hopDistributionAbsolute-cdf-" + i + e;
			try {
				Runtime.getRuntime().exec("mv " + fromAvg + " " + toAvg);
				Runtime.getRuntime().exec("mv " + fromConf + " " + toConf);
				Runtime.getRuntime().exec(
						"rm -r " + Config.get("MAIN_PLOT_FOLDER") + folderAvg);
				Runtime.getRuntime().exec(
						"rm -r " + Config.get("MAIN_PLOT_FOLDER") + folderConf);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

		// Series[] s2 = new Series[s1.length];
		// for (int i = 1; i < s1.length; i++) {
		// s2[i - 1] = s1[i];
		// }
		// Plot.singlesAvg(s2, "singles-avg-" + input + "/");
		// Plot.singlesConf(s2, "singles-conf-" + input + "/");

	}
}
