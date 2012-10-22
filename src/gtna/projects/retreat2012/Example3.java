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
 * Example2.java
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
package gtna.projects.retreat2012;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.p2p.chord.Chord;
import gtna.networks.p2p.chord.Chord.IDSelection;
import gtna.plot.Plotting;
import gtna.routing.greedy.Greedy;
import gtna.routing.lookahead.LookaheadSimple;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Example3 {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/retreat2012-example3/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/retreat2012-example3/");

		int times = 1;

		Metric greedy = new Routing(new Greedy());
		Metric lookahead = new Routing(new LookaheadSimple());

		Metric[] metrics = new Metric[] { greedy, lookahead };

		Network[] nw = Chord.get(new int[] { 1000, 2000, 3000, 4000 }, 128, 0,
				IDSelection.RANDOM, null);

		Series[] series = Series.generate(nw, metrics, times);

		Plotting.multi(series, metrics, "multi/");
	}
}
