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
import gtna.graph.Graph;
import gtna.io.GraphReader;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.p2p.chord.Chord;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomRingIDSpace;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.lookahead.NeighborsFirstLookaheadList;
import gtna.transformation.lookahead.NeighborsFirstObfuscatedLookaheadList;
import gtna.transformation.lookahead.NeighborsGroupedLookaheadList;
import gtna.transformation.lookahead.NeighborsGroupedObfuscatedLookaheadList;
import gtna.transformation.lookahead.RandomLookaheadList;
import gtna.transformation.lookahead.RandomObfuscatedLookaheadList;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class Lookahead {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Stats stats = new Stats();

		boolean generate = true;
		int times = 1;
		boolean wot = false;
		boolean skipExistingFolders = false;

		Config.overwrite("METRICS", "R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/nico/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/nico/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skipExistingFolders);

		Network nw1 = new ReadableFile("SPI", "spi", "./temp/test/spi.txt",
				null, null);
		Graph g = nw1.generate();
		Transformation t = new RandomRingIDSpaceSimple();
		g = t.transform(g);
		GraphWriter.writeWithProperties(g, "./data/nico/graph.txt");
		
		Graph g2 = GraphReader.readWithProperties("./data/nico/graph.txt");
		GraphWriter.writeWithProperties(g2, "./data/nico/graph2.txt");
		
//		Network nw2 = new ReadableFile("SPI", "spi", "./temp/test/spi.txt",
//				new gtna.routing.lookahead.Lookahead(50), new Transformation[] {
//						new RandomRingIDSpace(), new RandomLookaheadList() });
//		Series[] s = Series.generate(new Network[] { nw1, nw2 }, 1);
//		Plot.multiAvg(s, "spi/");

		// Series[] s = Series.generate(CAN.get(1000, new int[]{2, 3, 4, 5}, 1,
		// null, null), 2);

		// Plot.singlesAvg(s, "singles-new/");

		// Network nw1 = new CAN(1000, 3, 1, null, null);
		// Network nw2 = new CAN(1000, 4, 1, null, null);
		//
		// Series s1 = Series.get(nw1);
		// Series s2 = Series.get(nw2);
		//
		// Plot.multiAvg(new Series[] { s1, s2 }, "multi/");
		// Plot.singlesAvg(new Series[] { s1, s2 }, "single/");

		// Lookahead.testLookahead();

		// boolean generate = true;
		// int times = 1;
		// boolean wot = false;
		// boolean skipExistingFolders = false;
		//
		// Config.overwrite("METRICS", "R");
		// Config.overwrite("MAIN_DATA_FOLDER", "./data/lookahead/");
		// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/lookahead/");
		// Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		// Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" +
		// skipExistingFolders);
		//
		// Network nw1 = new ErdosRenyi(1000, 20, true, new Greedy(),
		// new Transformation[] { new RandomRingIDSpace() });
		// Network nw2 = new Chord(1000, 20, true, new Greedy(), null);
		// Network[] nw = new Network[] { nw1, nw2 };
		// Series[] s = Series.generate(nw, 1);
		// Plot.allMulti(s, "TEST/");

		// double[] r = new double[] { 1.0, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3,
		// 0.2, 0.1 };
		// double[] number = new double[r.length];
		// double[] alpha = new double[r.length];
		// for (int i = 0; i < r.length; i++) {
		// double v = 0.25 * r[i];
		// alpha[i] = Math.asin(v) * 180.0 / Math.PI;
		// number[i] = Math.floor(360.0 / (2 * alpha[i]));
		// }
		// for (int i = 0; i < r.length; i++) {
		// System.out.println("r = " + r[i] + " => a = " + alpha[i]);
		// }
		// for (int i = 0; i < r.length; i++) {
		// System.out.println("r = " + r[i] + " => # = " + number[i]);
		// }

		stats.end();
	}

	private static void testLookahead() {
		boolean generate = true;
		int times = 1;
		boolean wot = false;
		boolean skipExistingFolders = true;

		Config.overwrite("METRICS", "R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/lookahead/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/lookahead/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skipExistingFolders);

		String spiGraph = "./temp/test/spi.txt";
		String wotGraph = "./temp/test/wot.txt";
		String graph = spiGraph;
		String name = "SPI";
		String folder = "spi";
		if (wot) {
			graph = wotGraph;
			name = "WOT";
			folder = "wot";
		}

		Transformation t1 = new RandomRingIDSpace();

		Transformation nfll = new NeighborsFirstLookaheadList();
		Transformation nfoll = new NeighborsFirstObfuscatedLookaheadList(0.0,
				0.0);
		Transformation ngll = new NeighborsGroupedLookaheadList();
		Transformation ngoll = new NeighborsGroupedObfuscatedLookaheadList(0.0,
				0.0);
		Transformation rll = new RandomLookaheadList();
		Transformation roll = new RandomObfuscatedLookaheadList(0.0, 0.0);
		Transformation[] ll = new Transformation[] { nfll, nfoll, ngll, ngoll,
				rll, roll };
		ll = new Transformation[] { rll, roll };
		ll = new Transformation[] { nfll, ngll, rll };

		Transformation[][] ts = new Transformation[ll.length][];
		for (int i = 0; i < ll.length; i++) {
			// ts[i] = new Transformation[] { t1, ll[i] };
			ts[i] = new Transformation[] { ll[i] };
		}

		RoutingAlgorithm greedy = new Greedy(50);
		RoutingAlgorithm lookahead = new gtna.routing.lookahead.Lookahead(50);

		Network[] nw = new Network[ll.length + 1];
		// nw[0] = new ReadableFile(name, folder, graph, greedy,
		// new Transformation[] { t1 });
		nw[0] = new Chord(1000, 100, false, greedy, null);
		for (int i = 0; i < ts.length; i++) {
			// nw[i + 1] = new ReadableFile(name, folder, graph, lookahead,
			// ts[i]);
			nw[i + 1] = new Chord(1000, 100, false, lookahead, ts[i]);
		}

		Series[] s = generate ? Series.generate(nw, times) : Series.get(nw);

		Plot.multiAvg(s, "all/");

	}
}
