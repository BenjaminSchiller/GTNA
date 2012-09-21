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
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.connectivity.StrongConnectivity;
import gtna.metrics.connectivity.WeakConnectivity;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Example2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/retreat2012-example2/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/retreat2012-example2/");

		int times = 1;

		Metric dd = new DegreeDistribution();
		Metric sp = new ShortestPaths();
		Metric wc = new WeakConnectivity();
		Metric sc = new StrongConnectivity();

		Metric[] metrics = new Metric[] { dd, sp, wc, sc };

		String filename = "resources/caida/original/"
				+ "cycle-aslinks.l7.t1.c000027.20070913.txt.gtna";
		Network nw = new ReadableFile("CAIDA", "caida-folder-blafasel",
				filename, null);

		Series s = Series.generate(nw, metrics, times);

		Plotting.multi(s, metrics, "multi/");
	}
}
