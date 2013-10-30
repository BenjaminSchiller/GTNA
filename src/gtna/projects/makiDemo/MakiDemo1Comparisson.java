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
 * MakiDemo1.java
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
import gtna.graph.Graph;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.io.graphReader.CaidaGraphReader;
import gtna.io.graphReader.GraphReader;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.connectivity.RichClubConnectivity;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni 4 sec
 */
public class MakiDemo1Comparisson extends MakiDemo {

	protected static int times = 1;

	// 1 sec
	protected static boolean readWrite = true;

	// 300 sec
	protected static boolean generate = false;

	// 3 sec
	protected static boolean plot = true;

	protected static boolean open = true;

	/**
	 * In this demo, the Internet topology as measured by CAIDA in 2007 is read
	 * from the source file and transformed into GTNA's own format. For the
	 * static topology, degree distribution, shortest paths, rich-club
	 * connectivity, nd fragmentation after degree-based node removal are
	 * measured. The Internet's topology is compared to four network models, BA,
	 * IG, FPF, and GLP. The results of these computations are plotted for all
	 * five networks.
	 */
	public static void main(String[] args) throws InterruptedException {
		Stats stats = new Stats();

		/**
		 * Config
		 */
		config(1, true, "png", 3);
		config();

		/**
		 * Metrics
		 */
		Metric degreeDistribution = new DegreeDistribution();
		Metric shortestPaths = new ShortestPaths();
		Metric richClubConnectivity = new RichClubConnectivity();
		Metric degreeFragmentation = new WeakFragmentation(
				new DegreeNodeSorter(NodeSorterMode.DESC), Resolution.PERCENT);

		Metric[] metrics = new Metric[] { degreeDistribution, shortestPaths,
				richClubConnectivity, degreeFragmentation };

		/**
		 * Reading CAIDA graph from source file
		 */
		Graph caidaGraph = null;
		String src = "resources/caida-2007-09-13.txt";
		if (readWrite) {
			GraphReader reader = new CaidaGraphReader();
			System.out.println("reading CAIDA graph from " + src);
			caidaGraph = reader.read(src);
		}

		/**
		 * Writing CAIDA graph in GTNA format
		 */
		String dst = "data/caida-2007-09-13.gtna.txt";
		if (readWrite) {
			GraphWriter writer = new GtnaGraphWriter();
			System.out.println("writing CAIDA graph to " + dst);
			writer.write(caidaGraph, dst);
		}

		/**
		 * Creating network instances
		 */
		Network caida = new DescriptionWrapper(new ReadableFile("CAIDA",
				"caida-2007", dst, null), "CAIDA 2007-09-13");
		Network ba = new DescriptionWrapper(new BarabasiAlbert(12190, 2, null),
				"Barabasi Albert (BA)");
		Network ig = new DescriptionWrapper(new IG(12190, 10, 0.4, null),
				"Interactive Growth (IG)");
		Network pfp = new DescriptionWrapper(new PFP(12190, 10, 0.3, 0.1,
				0.020846, null), "Positive-Feedback Preference (PFP)");
		Network glp = new DescriptionWrapper(new GLP(12190, 10, 1.13, 0.4695,
				0.6447, null), "Generalized Linear Preference (GLP)");

		/**
		 * Performing computations
		 */
		Network[] nw = new Network[] { caida, ba, ig, pfp, glp };
		for (Network network : nw) {
			if (generate) {
				Series.generate(network, metrics, times);
			}
		}

		/**
		 * Getting the results
		 */
		Series[] s = null;
		if (plot) {
			s = Series.get(nw, metrics);
		}

		/**
		 * Plotting the results
		 */
		if (plot) {
			Plotting.multi(s, metrics, "multi/");
		}

		/**
		 * Opening files
		 */
		if (open) {
			open(new String[] { Config.get("MAIN_DATA_FOLDER"),
					Config.get("MAIN_PLOT_FOLDER") });
			Thread.sleep(1000);
			openPlots(new String[] {
					"multi/FRAGMENTATION-WEAK-DEGREE_DESC-PERCENT/fragmentation-largestComponentSize",
					"multi/RICH_CLUB_CONNECTIVITY/rcc-richClubConnectivity",
					"multi/SHORTEST_PATHS/sp-shortestPathLengthDistribution",
					"multi/DEGREE_DISTRIBUTION/dd-degreeDistribution-cdf" });
			Thread.sleep(1000);
			open(new String[] { src});
			Thread.sleep(1000);
			open(new String[] { dst });
		}

		stats.end();
	}

	protected static void config() {
		Config.overwrite(
				"DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION_PLOT_LOGSCALE", "x");
		Config.overwrite(
				"DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION_PLOT_LOGSCALE", "x");
		Config.overwrite(
				"DEGREE_DISTRIBUTION_OUT_DEGREE_DISTRIBUTION_PLOT_LOGSCALE",
				"x");
		Config.overwrite(
				"DEGREE_DISTRIBUTION_DEGREE_DISTRIBUTION_CDF_PLOT_LOGSCALE",
				"x");
		Config.overwrite(
				"DEGREE_DISTRIBUTION_IN_DEGREE_DISTRIBUTION_CDF_PLOT_LOGSCALE",
				"x");
		Config.overwrite(
				"DEGREE_DISTRIBUTION_OUT_DEGREE_DISTRIBUTION_CDF_PLOT_LOGSCALE",
				"x");
		Config.overwrite(
				"RICH_CLUB_CONNECTIVITY_RICH_CLUB_CONNECTIVITY_PLOT_LOGSCALE",
				"x");
		Config.overwrite("FRAGMENTATION_LARGEST_COMPONENT_SIZE_PLOT_LOGSCALE",
				"x");
		Config.overwrite(
				"FRAGMENTATION_LARGEST_COMPONENT_SIZE_FRACTION_PLOT_LOGSCALE",
				"x");
	}

}
