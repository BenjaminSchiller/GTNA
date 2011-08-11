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
 * DHTs.java
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
import gtna.routing.greedy.GreedyBI;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class DHTs {
	public static void main(String[] args) {
		Stats stats = new Stats();
		chordTest();
		stats.end();
	}

	public static void chordTest() {
		Config.overwrite("METRICS", "DD, R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/chord/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/chord/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		RoutingAlgorithm r = new GreedyBI();

		Network nw0 = new Chord(16, 5, r, null);
		// Network nw1 = new Chord(1000, 16, r, null);
		// Network nw2 = new Chord(1000, 32, r, null);
		// Network nw3 = new Chord(1000, 64, r, null);
		// Network nw4 = new Chord(1024, 128, r, null);

		Network[] nw = new Network[] { nw0 };
		// Network[] nw = new Network[] { nw1, nw2, nw3, nw4 };
		Series[] s = Series.generate(nw, 1);
		Plot.multiConf(s, "multi/");

	}
}
