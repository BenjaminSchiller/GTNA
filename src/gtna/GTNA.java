package gtna;

import gtna.data.Series;
import gtna.io.GraphReader;
import gtna.networks.Network;
import gtna.networks.canonical.Complete;
import gtna.networks.model.Communities3;
import gtna.networks.model.Gilbert;
import gtna.networks.p2p.CAN;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.twoPhase.TwoPhaseGreedyRegistration;
import gtna.routing.twoPhase.TwoPhaseGreedyRegistrationMultiR;
import gtna.transformation.Transformation;
import gtna.transformation.connectivity.GiantConnectedComponent;
import gtna.transformation.degree.MinDegree;
import gtna.transformation.degree.RandomWithSameDD;
import gtna.transformation.degree.RemoveSmallDegreeNodes;
import gtna.transformation.identifier.OutlierSorting;
import gtna.transformation.identifier.OutlierSortingMultiR;
import gtna.transformation.identifier.RandomID;
import gtna.util.Config;
import gtna.util.Stats;

public class GTNA {
	// register at 0.5 * id as well
	// X % der grš§ten bleiben fest (vorher gleichverteilt / gleicher abstand)

	public static void main(String[] args) throws Exception {
		if (true) {
			Stats stats = new Stats();
			Network nw = new Communities3(new int[] { 5000, 5000 },
					new int[][] { new int[] { 0, 100 }, new int[] { 100, 0 } },
					true, null, null);
			System.out.println(nw.generate());
			stats.end();
			return;
		}

		String SPI = "./data/__graphs/spi-buddy-graph.txt";
		String WOT = "./data/__graphs/WOT/2005-02-25-graph-bi.txt";

		if (false) {
			Stats stats = new Stats();
			Config.overwrite("MAIN_DATA_FOLDER", "./data/TEST-plot/");
			Config.overwrite("MAIN_PLOT_FOLDER", "./plots/TEST-plot/");
			Config.overwrite("METRICS", "DD");

			Series[] s = Series.get(CAN.get(1000, new int[] { 2, 3, 4, 5 }, 1,
					null, null));

			Plot.allMulti(s, "multi/");
			Plot.allSingle(s, "singles/");
			stats.end();
			return;
		}

		if (false) {
			Stats stats = new Stats();
			Config.overwrite("MAIN_DATA_FOLDER", "./data/TEST-routing/");
			Config.overwrite("MAIN_PLOT_FOLDER", "./plots/TEST-routing/");
			Config.overwrite("METRICS", "RL");
			Config.overwrite("RL_ROUTES_PER_NODE", "5");
			int times = 1;

			RoutingAlgorithm r1 = new TwoPhaseGreedyRegistration(50, 20, 2);
			RoutingAlgorithm r2 = new TwoPhaseGreedyRegistrationMultiR(50, 20,
					2);

			Transformation lc = new GiantConnectedComponent();
			Transformation riR = new RandomID(RandomID.RING_NODE, 1);
			Transformation os1 = new OutlierSorting(1);
			Transformation osC = new OutlierSorting(-1);
			Transformation riRmR = new RandomID(RandomID.RING_NODE_MULTI_R, 5);
			Transformation osmR1 = new OutlierSortingMultiR(1);
			Transformation osmRC = new OutlierSortingMultiR(-1);
			Transformation[] t1 = new Transformation[] { lc, riR, os1 };
			Transformation[] t2 = new Transformation[] { lc, riRmR, osmR1 };
			Transformation[] t3 = new Transformation[] { lc, riR, osC };
			Transformation[] t4 = new Transformation[] { lc, riRmR, osmRC };

			// Network n1 = new ReadableFile("WOT", "wot", WOT,
			// GraphReader.OWN_FORMAT, r1, t1);
			// Network n2 = new ReadableFile("WOT", "wot", WOT,
			// GraphReader.OWN_FORMAT, r2, t2);
			Network n1 = new ReadableFile("SPI", "spi", SPI,
					GraphReader.OWN_FORMAT, r1, t1);
			Network n2 = new ReadableFile("SPI", "spi", SPI,
					GraphReader.OWN_FORMAT, r2, t2);

			Series[] s = Series.generate(new Network[] { n1, n2 }, times);

			String f = null;
			if (s.length > 1) {
				f = s[0].network().compareName(s[1].network());
			} else {
				f = s[0].network().name();
			}
			Plot.multiAvg(s, f + "-m/");

			stats.end();
			return;
		}

		Stats stats = new Stats();
		Config.overwrite("MAIN_DATA_FOLDER", "./data/TEST-sorting/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/TEST-sorting/");
		Config.overwrite("METRICS", "ID_SPACE");
		int times = 1;
		int repeat = 10;
		int routesPerNode = 5;
		boolean get = false;
		boolean jpeg = true;

		Config.overwrite("RL_ROUTES_PER_NODE", "" + routesPerNode);
		if (jpeg) {
			Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
			Config.overwrite("PLOT_EXTENSION", ".jpeg");
		}

		RoutingAlgorithm r1 = new Greedy();
		// RoutingAlgorithm r2 = new TwoPhaseGreedy();
		// RoutingAlgorithm r3 = new TwoPhaseGreedyRegistration(60, 20, 2);
		// RoutingAlgorithm r4 = new Lookahead();
		// RoutingAlgorithm r5 = new TwoPhaseLookahead();
		// RoutingAlgorithm r6 = new TwoPhaseLookaheadRegistration();
		RoutingAlgorithm[] r = new RoutingAlgorithm[] { r1 };

		// RoutingAlgorithm r1 = new TwoPhaseGreedyRegistration(1);
		// RoutingAlgorithm r2 = new TwoPhaseGreedyRegistration(3);
		// RoutingAlgorithm r3 = new TwoPhaseGreedyRegistration(5);
		// RoutingAlgorithm r4 = new TwoPhaseGreedyRegistration(10);
		// RoutingAlgorithm r5 = new TwoPhaseGreedyRegistration(15);
		// RoutingAlgorithm r6 = new TwoPhaseGreedyRegistration(50);
		// RoutingAlgorithm[] r = new RoutingAlgorithm[] { r1, r2, r3, r4, r5,
		// r6 };

		// int[] ttl = new int[] { 10 };
		// RoutingAlgorithm[] r = new RoutingAlgorithm[ttl.length];
		// for (int i = 0; i < ttl.length; i++) {
		// r[i] = new TwoPhaseGreedyRegistration(50, ttl[i]);
		// }

		// RoutingAlgorithm r1 = new TwoPhaseGreedyRegistration(10, 20, 0);
		// RoutingAlgorithm r2 = new TwoPhaseGreedyRegistration(20, 20, 0);
		// RoutingAlgorithm r3 = new TwoPhaseGreedyRegistration(30, 20, 0);
		// RoutingAlgorithm r4 = new TwoPhaseGreedyRegistration(40, 20, 0);
		// RoutingAlgorithm r5 = new TwoPhaseGreedyRegistration(50, 20, 0);
		// RoutingAlgorithm r6 = new TwoPhaseGreedyRegistration(60, 20, 0);
		// RoutingAlgorithm[] r = new RoutingAlgorithm[] { r1, r2, r3, r4, r5,
		// r6 };

		Transformation md = new MinDegree(3, 3, true);
		Transformation rsdn = new RemoveSmallDegreeNodes(2, 2);
		Transformation lc = new GiantConnectedComponent();
		Transformation rwsdd = new RandomWithSameDD(true);
		Transformation riR = new RandomID(RandomID.RING_NODE, 1);
		Transformation riRmR = new RandomID(RandomID.RING_NODE_MULTI_R, 5);
		Transformation os1 = new OutlierSorting(1);
		Transformation osC = new OutlierSorting(-1);
		Transformation osmR1 = new OutlierSortingMultiR(1);
		Transformation osmRC = new OutlierSortingMultiR(-1);
		Transformation[] t1 = new Transformation[] { lc, riR, os1 };
		Transformation[] t2 = new Transformation[] { lc, riR, osC };
		Transformation[] t3 = new Transformation[] { lc, riRmR, osmR1 };
		Transformation[] t4 = new Transformation[] { lc, riRmR, osmRC };
		Transformation[][] t = new Transformation[][] { t3 };

		String f = "TEMP";

		Network[] nw = new Network[r.length * t.length];
		for (int i = 0; i < t.length; i++) {
			for (int j = 0; j < r.length; j++) {
				int index = i * r.length + j;
				// nw[index] = new ReadableFile("WOT", "wot", WOT,
				// GraphReader.OWN_FORMAT, r[j], t[i]);
				nw[index] = new ReadableFile("SPI", "spi", SPI,
						GraphReader.OWN_FORMAT, r[j], t[i]);
				// f = "ER";
				// nw[index] = new ErdosRenyi(1000, 20.0, true, r[j], t[i]);
				// f = "CAN";
				// nw[index] = new CAN(1000, 1, 1, r[i], null);
				// f = "Chord";
				// nw[index] = new Chord(1000, 32, 16, 1, r[i], null);
				// f = "BA";
				// nw[index] = new BarabasiAlbert(1000, 20, r[i], t);
				// f = "W";
				// nw[index] = new WattsStrogatz(n, 20, 0.7, r[i], t);
				// f = "G";
				// String gnutella04 =
				// "/Users/benni/TUD/Freenet/p2p-Gnutella04.graph-bidirectional.txt";
				// nw[index] = new ReadableFile("Gnutella04", "gnutella04",
				// gnutella04,
				// GraphReader.OWN_FORMAT, r[i], t);
				// f = "CA92";
				// String ca92 =
				// "/Users/benni/TUD/Freenet/coauthor1992A8P0RankDir.graph.txt";
				// nw[index] = new ReadableFile("Co-Author-92", "coAuthor92",
				// ca92,
				// GraphReader.OWN_FORMAT, r[i], t);
				// f = "CA98";
				// String ca98 =
				// "/Users/benni/TUD/Freenet/coauthor1998A8P0RankDir.graph.txt";
				// nw[index] = new ReadableFile("Co-Author-98", "coAuthor98",
				// ca98,
				// GraphReader.OWN_FORMAT, r[i], t);
			}
		}

		for (int i = 0; i < repeat; i++) {
			Series[] s = null;
			if (get) {
				s = Series.get(nw);
			} else {
				s = Series.generate(nw, times);
			}
			if (s.length > 1) {
				f = s[0].network().compareName(s[1].network());
			} else {
				f = s[0].network().name();
			}
			if (repeat > 1) {
				f += "-" + System.currentTimeMillis();
			}
			Plot.multiAvg(s, f + "-m/");
			// Plot.singlesConf(s, f + "-s/");
		}

		stats.end();
	}
}
