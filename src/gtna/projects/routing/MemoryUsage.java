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
 * MemoryUsage.java
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
package gtna.projects.routing;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class MemoryUsage {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean skip = true;
		boolean generate = true;
		int times = 1;

		Type type = Type.maximum;
		Style style = Style.linespoint;

		int k = 1024;

		Config.overwrite("MAIN_DATA_FOLDER", "data/memory2/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/memory2/");
		Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
				+ "_scripts/");

		Config.overwrite("TIMES_TO_CALL_GC_BEFORE_EACH_RUN", "10");
		Config.overwrite("GNUPLOT_TERMINAL", "png");
		Config.overwrite("PLOT_EXTENSION", ".png");

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skip);

		Metric dd = new DegreeDistribution();
		Metric[] metrics = new Metric[] { dd };

		int[] edges0 = new int[] { 0 * k, 1 * k, 2 * k, 4 * k, 8 * k, 16 * k,
				32 * k, 64 * k, 128 * k, 256 * k, 512 * k, 1024 * k, 2048 * k };
		int[] nodes0 = new int[] { 4 * k, 8 * k, 16 * k, 32 * k, 64 * k,
				128 * k, 256 * k, 512 * k, 1024 * k, 2048 * k };
		// edges0 = new int[] { 1024 * k, 2048 * k, 3072 * k };
		// nodes0 = new int[] { 1024 * k, 2048 * k };
		Network[][] nw0 = new Network[edges0.length][nodes0.length];
		for (int i = 0; i < edges0.length; i++) {
			for (int j = 0; j < nodes0.length; j++) {
				int n = nodes0[j];
				double d = (double) edges0[i] / (double) n;
				nw0[i][j] = new ErdosRenyi(n, d, true, null);
			}
		}
		Series[][] s0 = generate ? Series.generate(nw0, metrics, times)
				: Series.get(nw0, metrics);

		Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
				+ "_scripts-nodes/");
		Plotting.singleBy(s0, new Metric[0], "nodes-0/", dd, "NODES", type,
				style);

		Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
				+ "_scripts-edges/");
		Plotting.singleBy(reverse(s0), new Metric[0], "edges-0/", dd, "EDGES",
				type, style);
	}

	public static Series[][] reverse(Series[][] s) {
		Series[][] r = new Series[s[0].length][s.length];
		for (int i = 0; i < s.length; i++) {
			for (int j = 0; j < s[0].length; j++) {
				r[j][i] = s[i][j];
			}
		}
		return r;
	}
}
