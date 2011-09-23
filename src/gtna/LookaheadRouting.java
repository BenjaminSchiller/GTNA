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
package gtna;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.p2p.chord.Chord;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.lookahead.LookaheadSequential;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomRingIDSpace;
import gtna.transformation.lookahead.NeighborsFirstLookaheadList;
import gtna.transformation.lookahead.NeighborsFirstObfuscatedLookaheadList;
import gtna.transformation.lookahead.NeighborsGroupedLookaheadList;
import gtna.transformation.lookahead.RandomLookaheadList;
import gtna.util.Config;
import gtna.util.Stats;

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

		boolean generate = true;
		int times = 1;
		boolean skipExistingFolders = true;

		Input input = Input.Chord;

		Config.overwrite("METRICS", "R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/lookahead/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/lookahead/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skipExistingFolders);

		// Config.overwrite("GNUPLOT_CMD_TERMINAL",
		// "set terminal post eps enhanced color dashed \"Helvetica\" 10");
		// Config.overwrite("PLOT_EXTENSION", ".eps");

		Config.overwrite("R_HOP_DISTRIBUTION_CDF_PLOT_KEY", "right bottom");
		Config.overwrite("R_HOP_DISTRIBUTION_ABSOLUTE_CDF_PLOT_KEY",
				"right bottom");

		LookaheadRouting.testLookahead(generate, times, input);

		stats.end();
	}

	private enum Input {
		Chord, WOT, SPI, ErdosRenyi
	}

	private static void testLookahead(boolean generate, int times, Input input) {
		int chordNodes = 1000;
		int chordBits = 40;
		boolean chordUniform = false;

		int erNodes = 1000;
		double erDegree = 20;
		boolean erBD = true;

		String spiGraph = "./temp/test/spi.txt";
		String spiName = "SPI";
		String spiFolder = "spi";

		String wotGraph = "./temp/test/wot.txt";
		String wotName = "WOT";
		String wotFolder = "wot";

		Transformation t1 = new RandomRingIDSpace();

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
			Transformation nfo0 = new NeighborsFirstObfuscatedLookaheadList(
					0.0, 0.0, false);
			Transformation nfo1 = new NeighborsFirstObfuscatedLookaheadList(
					.1E0, .1E1, false);
			Transformation nfo2 = new NeighborsFirstObfuscatedLookaheadList(
					.1E1, .1E2, false);
			Transformation nfo3 = new NeighborsFirstObfuscatedLookaheadList(
					.1E2, .1E3, false);
			Transformation nfo4 = new NeighborsFirstObfuscatedLookaheadList(
					.1E3, .1E4, false);
			Transformation nfo5 = new NeighborsFirstObfuscatedLookaheadList(
					.1E4, .1E5, false);
			Transformation nfo6 = new NeighborsFirstObfuscatedLookaheadList(
					.1E5, .1E6, false);
			Transformation nfo7 = new NeighborsFirstObfuscatedLookaheadList(
					.1E6, .1E7, false);
			Transformation nfo8 = new NeighborsFirstObfuscatedLookaheadList(
					.1E7, .1E8, false);
			Transformation nfo9 = new NeighborsFirstObfuscatedLookaheadList(
					.1E8, .1E9, false);
			ll = new Transformation[] { nfo0, nfo1, nfo2, nfo3, nfo4, nfo5,
					nfo6, nfo7, nfo8, nfo9 };
			ll = new Transformation[] { nfo0 };
			map.put(nfo0, "NFO-0");
			map.put(nfo1, "NFO-1");
			map.put(nfo2, "NFO-2");
			map.put(nfo3, "NFO-3");
			map.put(nfo4, "NFO-4");
			map.put(nfo5, "NFO-5");
			map.put(nfo6, "NFO-6");
			map.put(nfo7, "NFO-7");
			map.put(nfo8, "NFO-8");
			map.put(nfo9, "NFO-9");
		} else if (input == Input.Chord) {
			Transformation nfo0 = new NeighborsFirstObfuscatedLookaheadList(0,
					0, false);
			Transformation nfo1 = new NeighborsFirstObfuscatedLookaheadList(0,
					1, false);
			Transformation nfo2 = new NeighborsFirstObfuscatedLookaheadList(1,
					2, false);
			Transformation nfo3 = new NeighborsFirstObfuscatedLookaheadList(2,
					3, false);
			Transformation nfo4 = new NeighborsFirstObfuscatedLookaheadList(3,
					4, false);
			Transformation nfo5 = new NeighborsFirstObfuscatedLookaheadList(4,
					5, false);
			Transformation nfo6 = new NeighborsFirstObfuscatedLookaheadList(5,
					6, false);
			Transformation nfo7 = new NeighborsFirstObfuscatedLookaheadList(6,
					7, false);
			Transformation nfo8 = new NeighborsFirstObfuscatedLookaheadList(7,
					8, false);
			Transformation nfo9 = new NeighborsFirstObfuscatedLookaheadList(8,
					9, false);
			Transformation nfo10 = new NeighborsFirstObfuscatedLookaheadList(9,
					10, false);
			Transformation nfo11 = new NeighborsFirstObfuscatedLookaheadList(
					10, 11, false);
			Transformation nfo12 = new NeighborsFirstObfuscatedLookaheadList(
					11, 12, false);
			Transformation nfo13 = new NeighborsFirstObfuscatedLookaheadList(
					12, 13, false);
			Transformation nfo14 = new NeighborsFirstObfuscatedLookaheadList(
					13, 14, false);
			Transformation nfo15 = new NeighborsFirstObfuscatedLookaheadList(
					14, 15, false);
			Transformation nfo16 = new NeighborsFirstObfuscatedLookaheadList(
					15, 16, false);
			Transformation nfo17 = new NeighborsFirstObfuscatedLookaheadList(
					16, 17, false);
			Transformation nfo18 = new NeighborsFirstObfuscatedLookaheadList(
					17, 18, false);
			Transformation nfo19 = new NeighborsFirstObfuscatedLookaheadList(
					18, 19, false);
			Transformation nfo20 = new NeighborsFirstObfuscatedLookaheadList(
					19, 20, false);
			ll = new Transformation[] { nfo0, nfo1, nfo2, nfo3, nfo4, nfo5,
					nfo6, nfo7, nfo8, nfo9, nfo10, nfo11, nfo12, nfo13, nfo14,
					nfo15, nfo16, nfo16, nfo17, nfo18, nfo19, nfo20 };
			map.put(nfo0, "NFO-0");
			map.put(nfo1, "NFO-1");
			map.put(nfo2, "NFO-2");
			map.put(nfo3, "NFO-3");
			map.put(nfo4, "NFO-4");
			map.put(nfo5, "NFO-5");
			map.put(nfo6, "NFO-6");
			map.put(nfo7, "NFO-7");
			map.put(nfo8, "NFO-8");
			map.put(nfo9, "NFO-9");
			map.put(nfo10, "NFO-10");
			map.put(nfo11, "NFO-11");
			map.put(nfo12, "NFO-12");
			map.put(nfo13, "NFO-13");
			map.put(nfo14, "NFO-14");
			map.put(nfo15, "NFO-15");
			map.put(nfo16, "NFO-16");
			map.put(nfo17, "NFO-17");
			map.put(nfo18, "NFO-18");
			map.put(nfo19, "NFO-19");
			map.put(nfo20, "NFO-20");
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
			nw[0] = new Chord(chordNodes, chordBits, chordUniform, greedy, null);
		} else if (input == Input.SPI) {
			nw[0] = new ReadableFile(spiName, spiFolder, spiGraph, greedy,
					new Transformation[] { t1 });
		} else if (input == Input.WOT) {
			nw[0] = new ReadableFile(wotName, wotFolder, wotGraph, greedy,
					new Transformation[] { t1 });
		} else if (input == Input.ErdosRenyi) {
			nw[0] = new ErdosRenyi(erNodes, erDegree, erBD, greedy,
					new Transformation[] { t1 });
		}
		nw[0] = new DescriptionWrapper(nw[0], "Greedy");
		for (int i = 0; i < LL.length; i++) {
			int index = 0;
			if (input == Input.Chord) {
				nw[i + 1] = new Chord(chordNodes, chordBits, chordUniform,
						lookahead, LL[i]);
			} else if (input == Input.SPI) {
				nw[i + 1] = new ReadableFile(spiName, spiFolder, spiGraph,
						lookahead, LL[i]);
				index = 1;
			} else if (input == Input.WOT) {
				nw[i + 1] = new ReadableFile(wotName, wotFolder, wotGraph,
						lookahead, LL[i]);
				index = 1;
			} else if (input == Input.ErdosRenyi) {
				nw[i + 1] = new ErdosRenyi(erNodes, erDegree, erBD, lookahead,
						LL[i]);
				index = 1;
			}
			nw[i + 1] = new DescriptionWrapper(nw[i + 1], map.get(LL[i][index])
					+ " - Lookahead");
		}

		Series[] s = generate ? Series.generate(nw, times) : Series.get(nw);

		Plot.multiAvg(s, "avg-" + input + "/");
		Plot.multiConf(s, "conf-" + input + "/");

	}
}
