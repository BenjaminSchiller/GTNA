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
 * Routing.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
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
import gtna.plot.GraphPlotter;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedy.GreedyBacktracking;
import gtna.transformation.Transformation;
import gtna.transformation.gd.FruchtermanReingold;
import gtna.util.Config;

/**
 * @author "Benjamin Schiller"
 * 
 */
// TODO adapt to changes
public class Routing {
	public static void main(String[] args) {
		Config.overwrite("METRICS", "DD, SP, R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/nico/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/nico/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + false);

		Transformation[] t1 = new Transformation[] { new FruchtermanReingold(1,
				new double[] { 100, 100 }, false, 100, new GraphPlotter(true)) };
		RoutingAlgorithm r1 = new Greedy();
		Transformation[] t2 = new Transformation[] { new FruchtermanReingold(1,
				new double[] { 100, 100 }, false, 100, new GraphPlotter(true)) };
		RoutingAlgorithm r2 = new GreedyBacktracking();
		Network nw1 = new ErdosRenyi(100, 5.0, true, r1, t1);
		Network nw2 = new ErdosRenyi(100, 5.0, true, r2, t2);
		Series[] s = Series.generate(new Network[] { nw1, nw2 }, 4);
		// Series s = Series.get(nw);

		Plot.allMulti(s, "multi/");

	}

	// public static String SPI = "./resources/SPI-3-LCC/2010-08.spi.txt";
	//
	// public static String OUTPUT = "./data/routing/graph.txt";
	//
	// /**
	// * @param args
	// */
	// public static void main(String[] args) {
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/routing/");
	// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/routing/");
	// Config.overwrite("METRICS", "RL");
	// Config.overwrite("PLOT_EXTENSION", ".jpg");
	// Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
	//
	// Transformation rid = new RandomID(RandomID.RING_NODE, 1);
	// Transformation lmc = new LMC(1000, LMC.MODE_UNRESTRICTED, 0,
	// "0.000001", 10);
	// Transformation[] ts = new Transformation[] { rid, lmc };
	//
	// RoutingAlgorithm r1 = new Greedy();
	// RoutingAlgorithm r2 = new Lookahead();
	// RoutingAlgorithm r3 = new GreedyNextBestBacktracking(100);
	// RoutingAlgorithm r4 = new TwoPhaseGreedyRegistration(1, 6, 1);
	// RoutingAlgorithm r5 = new TwoPhaseGreedyRegistration(10, 6, 1);
	// RoutingAlgorithm r6 = new TwoPhaseGreedyRegistration(1, 6, 10);
	// RoutingAlgorithm r7 = new TwoPhaseGreedyRegistration(10, 6, 10);
	//
	// RoutingAlgorithm[] r = new RoutingAlgorithm[] { r1, r2, r3, r4, r5, r6,
	// r7 };
	//
	// // Network init = new ReadableFile("SPI", "spi", SPI,
	// // GraphReader.OWN_FORMAT, null, null);
	// // Graph g = init.generate();
	// // for (Transformation t : ts) {
	// // g = t.transform(g);
	// // }
	// // GraphWriter.write(g, OUTPUT, GraphWriter.TO_STRING_FORMAT);
	//
	// Network[] nw = new Network[r.length];
	// for (int i = 0; i < r.length; i++) {
	// nw[i] = new ReadableFile("SPI", "spi", OUTPUT,
	// GraphReader.RING_NODES, r[i], null);
	// }
	//
	// Network[] nw1 = new Network[] { nw[0], nw[1], nw[2] };
	// Network[] nw2 = nw;
	//
	// Series[] s1 = Series.get(nw1);
	// Series[] s2 = Series.get(nw2);
	//
	// Config.overwrite("RL_CRLD_PLOT_KEY", "right bottom");
	// Plot.multiAvg(s1, "multi-1/");
	// Plot.multiAvg(s2, "multi-2/");
	// }
}
