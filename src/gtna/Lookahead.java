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
import gtna.networks.p2p.chord.Chord;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomRingIDSpace;
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
		boolean skipExistingFolders = false;

		Config.overwrite("METRICS", "R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/lookahead/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/lookahead/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skipExistingFolders);

		Lookahead.testLookahead(generate, times);

		stats.end();
	}

	private static void testLookahead(boolean generate, int times) {
		boolean wot = false;

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

		int nodes = 5000;
		int bits = 20;
		boolean uniform = false;

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
		ll = new Transformation[] { nfll };
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
		nw[0] = new Chord(nodes, bits, uniform, greedy, null);
		for (int i = 0; i < ts.length; i++) {
			// nw[i + 1] = new ReadableFile(name, folder, graph, lookahead,
			// ts[i]);
			nw[i + 1] = new Chord(nodes, bits, uniform, lookahead, ts[i]);
		}

		Series[] s = generate ? Series.generate(nw, times) : Series.get(nw);

		Plot.multiAvg(s, "chord/");

	}
}
