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
 * Evaluation.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.ReadableFile;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.GreedyBacktracking;
import gtna.transformation.Transformation;
import gtna.transformation.gd.*;
import gtna.transformation.spanningtree.BFS;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author Nico
 * 
 */
public class GDAEvaluation {
	public static void main(String[] args) {
		Stats stats = new Stats();

		Config.overwrite("METRICS", "R");
		// R for routing
		Config.overwrite("MAIN_DATA_FOLDER", "./data/GDAevaluation/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/GDAevaluation/");
		Config.overwrite("GNUPLOT_PATH", "C:\\Cygwin\\bin\\gnuplot.exe");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + true);

		Transformation[] sTArray;

		BFS bfs = new BFS("hd");
		RoutingAlgorithm rA = new GreedyBacktracking(25);
		Transformation[] t = new Transformation[] { new CanonicalCircularCrossing(1, 100, true, null),
				new SixTollis(1, 100, true, null), new WetherellShannon(100, 100, null), new Knuth(100, 100, null),
				new MelanconHerman(100, 100, null), new BubbleTree(100, 100, null),
				new FruchtermanReingold(1, new double[] { 100, 100 }, false, 100, null) };

		for (Transformation sT : t) {
			if (sT instanceof HierarchicalAbstract) {
				sTArray = new Transformation[] { bfs, sT };
			} else {
				sTArray = new Transformation[] { sT };
			}
			Network[] nw = new Network[] { new ErdosRenyi(500, 10, true, rA, sTArray),
					new BarabasiAlbert(500, 10, rA, sTArray),
					new ReadableFile("CAIDA", "./data", "cycle-aslinks.l7.t1.c001749.20111206.txt.gtna", rA, sTArray)
			/*
			 * Missing here: WOT and SPI
			 */
			};
			Series[] s = Series.generate(nw, 50);
		}

		stats.end();
	}
}
