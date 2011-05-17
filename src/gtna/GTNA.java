package gtna;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.networks.Network;
import gtna.networks.p2p.CAN;
import gtna.plot.Plot;
import gtna.util.Config;
import gtna.util.Stats;

public class GTNA {

	public static void main(String[] args) throws Exception {
		// starts a basic statistic to keep track of the computations duration
		// and used memory
		Stats stats = new Stats();

		// overwrites the standard data output folder
		Config.overwrite("MAIN_DATA_FOLDER", "./data/example1/");
		// overwrites the standard plot output folder
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/example1/");
		// overwrites the set and order of metrics to compute
		// here, only the clustering coefficient and degree didstribution
		// metrics are computed
		Config.overwrite("METRICS", "CC, DD");

		// CAN network with 100 nodes, d=3, and r=2
		// neither a transformation nor a routing algorithm are given
		Network can = new CAN(100, 3, 2, null, null);

		// CAN networks with different numbers of nodes
		Network[] cans = CAN.get(new int[] { 100, 200, 300 }, 2, 4, null, null);

		// generates the graph of a possible instance
		// the properties of this graph are determined by the configuration
		Graph g = can.generate();

		// generates a series for the specified network
		// the series represents the average of 12 independent runs
		// i.e. can.generate() is called 12 times to generate different
		// instances
		// then, all metric specified in METRICS are computed for these graphs
		Series s = Series.generate(can, 12);

		// generates an array of series for the specified networks
		Series[] s2 = Series.generate(cans, 5);

		// plots the average values for all multi-scalar metrics computed for
		// the single CAN network
		Plot.multiAvg(s, "can-avg/");
		// plots the confidence intervals for all multi-scalar metrics computed
		// for the single CAN network
		Plot.multiConf(s, "can-conf/");
		// plots averages, confidence intervals and generates a txt file to
		// easily display all plots in a text document
		Plot.allMulti(s, "can/");

		// plots all multi-scalar metrics for the list of CAN networks
		Plot.allMulti(s2, "cans-multi/");
		// plots all single-scalar metrics for the list of CAN networks
		// the network size is recognized as the difference in their
		// configuration and used as the value on the x-axis
		Plot.allSingle(s2, "can-singles/");

		// outputs the collected statistics
		stats.end();
	}
}
