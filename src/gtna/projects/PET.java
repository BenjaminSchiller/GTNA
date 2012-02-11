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
 * PET.java
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
package gtna.projects;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class PET {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Stats stats = new Stats();
		
		// int nodes, double alpha, int C, int cutoff
		int nodes = 10000;
		double alpha = 2.5;
		int c = 1;
		int cutoff = 100;

		int[] Nodes = new int[] { 1000, 2000, 3000, 4000, 5000 };
		double[] Alpha = new double[] { 2.0, 2.2, 2.4, 2.6, 2.8, 3.0 };
		int[] C = new int[] { 1, 2, 3, 4, 5 };
		int[] Cutoff = new int[] { 0, 1, 2, 3, 4, 5 };

		RoutingAlgorithm ra = new Greedy();
		Transformation[] t = new Transformation[] {};

		Network[] nw_nodes = ScaleFreeUndirected.get(Nodes, alpha, c, cutoff,
				ra, t);
		Network[] nw_alpha = ScaleFreeUndirected.get(nodes, Alpha, c, cutoff,
				ra, t);
		Network[] nw_c = ScaleFreeUndirected
				.get(nodes, alpha, C, cutoff, ra, t);
		Network[] nw_cutoff = ScaleFreeUndirected.get(nodes, alpha, c, Cutoff,
				ra, t);

		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, ROUTING");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/PET/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/PET/");

		int times = 1;

		// Series[] s_nodes = Series.generate(nw_nodes, times);
		// Plot.multiAvg(s_nodes, "test-nodes/");
		Series[] s_alpha = Series.generate(nw_alpha, times);
		Plot.multiAvg(s_alpha, "test-alpha/");
		// Series[] s_c = Series.generate(nw_c, times);
		// Plot.multiAvg(s_c, "test-c/");
		// Series[] s_cutoff = Series.generate(nw_cutoff, times);
		// Plot.multiAvg(s_cutoff, "test-cutoff/");
		
		stats.end();
	}

}
