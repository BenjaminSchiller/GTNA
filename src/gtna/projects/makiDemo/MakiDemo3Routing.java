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
 * MakiDemo3.java
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
package gtna.projects.makiDemo;

import gtna.data.Series;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.p2p.chord.Chord;
import gtna.networks.p2p.chord.Chord.IDSelection;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.routing.greedy.Greedy;
import gtna.routing.lookahead.LookaheadSimple;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni 56 sec
 */
public class MakiDemo3Routing extends MakiDemo {

	protected static int times = 5;

	// 47 sec
	protected static boolean generate = true;

	// 4 sec
	protected static boolean plot = true;

	protected static boolean open = true;

	/**
	 * In this demo, instances of the Distributed Hash Table 'Chord' are created
	 * with a varying configuration parameter, the number of successor links (5
	 * values). For each generated network, degree distribution as well as
	 * fragmentation after random failure and removal based on the node degree
	 * are computed. Afterwards, routing using a greedy as well as a
	 * lookahead-based routing algorithm are simulates in order to compare the
	 * performance of the respective routing algorithm. All computations and
	 * simulations are repeated 10 times. The results are plotted not as average
	 * values but as the confidence intervals of the respective dataset. 2
	 */
	public static void main(String[] args) throws InterruptedException {
		Stats stats = new Stats();

		/**
		 * Config
		 */
		config(3, true, "pdf", 3);

		/**
		 * Metrics
		 */
		Metric degreeDistribution = new DegreeDistribution();
		Metric degreeFragmentation = new WeakFragmentation(
				new DegreeNodeSorter(NodeSorterMode.DESC), Resolution.PERCENT);
		Metric randomFragmentation = new WeakFragmentation(
				new RandomNodeSorter(), Resolution.PERCENT);
		Metric greedyRouting = new Routing(new Greedy());
		Metric lookaheadRouting = new Routing(new LookaheadSimple());

		Metric[] metrics = new Metric[] { degreeDistribution,
				degreeFragmentation, randomFragmentation, greedyRouting,
				lookaheadRouting };

		/**
		 * Network properties
		 */
		int nodes = 1000;
		int bits = 32;
		int[] successors = new int[] { 1, 2, 5, 10, 20 };
		IDSelection selection = IDSelection.RANDOM;
		Transformation[] t = null;

		/**
		 * Creating network instances
		 */
		Network[] nw = Chord.get(nodes, bits, successors, selection, t);

		/**
		 * Performing computations / simulations
		 */
		if (generate) {
			Series.generate(nw, metrics, times);
		}

		/**
		 * Getting the results
		 */
		Series[] s0 = Series.get(nw, metrics);
		Series s1 = Series.get(new Chord(nodes, bits, successors[0], selection,
				t), metrics);

		/**
		 * Plotting the results
		 */
		if (plot) {
			Plotting.multi(s0, new Metric[] { degreeDistribution,
					degreeFragmentation, randomFragmentation, greedyRouting,
					lookaheadRouting }, "multi-all/", Type.confidence1,
					Style.candlesticks);
			Plotting.multi(s1,
					new Metric[] { greedyRouting, lookaheadRouting },
					"multi-one/", Type.confidence1, Style.candlesticks);
		}

		/**
		 * Opening files
		 */
		if (open) {
			open(new String[] { Config.get("MAIN_DATA_FOLDER"),
					Config.get("MAIN_PLOT_FOLDER") });
			Thread.sleep(1000);

			openPlots(new String[] {
					"multi-one/ROUTING_GROUPED/r-hopDistributionAbsolute-cdf",
					"multi-all/ROUTING-GREEDY/r-hopDistributionAbsolute-cdf",
					"multi-all/FRAGMENTATION-WEAK-DEGREE_DESC-PERCENT/"
							+ "fragmentation-largestComponentSize-fraction",
					"multi-all/FRAGMENTATION-WEAK-RANDOM-PERCENT/"
							+ "fragmentation-largestComponentSize-fraction",
					"multi-all/DEGREE_DISTRIBUTION/dd-degreeDistribution" });
		}

		stats.end();
	}
}
