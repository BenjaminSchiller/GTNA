package gtna;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorting;
import gtna.io.GraphReader;
import gtna.networks.Network;
import gtna.networks.p2p.CAN;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.twoPhase.TwoPhaseGreedyRegistration;
import gtna.routing.twoPhase.TwoPhaseGreedyRegistrationMultiR;
import gtna.transformation.Transformation;
import gtna.transformation.connectivity.LargestConnectedComponent;
import gtna.transformation.degree.MinDegree;
import gtna.transformation.degree.RandomWithSameDD;
import gtna.transformation.degree.RemoveSmallDegreeNodes;
import gtna.transformation.identifier.OutlierSorting;
import gtna.transformation.identifier.OutlierSortingMultiR;
import gtna.transformation.identifier.RandomID;
import gtna.transformation.identifier.SpanningTreeSorting;
import gtna.util.Config;
import gtna.util.Stats;

import java.util.Random;

public class GTNA {
	// register at 0.5 * id as well
	// X % der größten bleiben fest (vorher gleichverteilt / gleicher abstand)

	public static void main(String[] args) throws Exception {
		String SPI = "./resources/spi-buddy-graph.txt";
		String WOT = "./resources/WOT/2005-02-25-graph-bi.txt";

		if (false) {
			Graph g = GraphReader.read(SPI);
			Node[] nodes1 = NodeSorting.degreeDesc(g.nodes, new Random(System
					.currentTimeMillis() + 1));
			Node[] nodes2 = NodeSorting.degreeDesc(g.nodes, new Random(System
					.currentTimeMillis() + 2));
			Node[] nodes3 = NodeSorting.degreeDesc(g.nodes, new Random(System
					.currentTimeMillis() + 3));
			Node[] nodes4 = NodeSorting.degreeDesc(g.nodes, new Random(System
					.currentTimeMillis() + 4));
			Node[] nodes5 = NodeSorting.degreeDesc(g.nodes, new Random(System
					.currentTimeMillis() + 5));
			Node[] nodes6 = NodeSorting.degreeDesc(g.nodes, new Random(System
					.currentTimeMillis() + 6));
			for (int i = 0; i < 100; i++) {
				System.out
						.print((nodes1[i].in().length + nodes1[i].out().length)
								+ ":	");
				System.out.print(nodes1[i].index() + "	");
				System.out.print(nodes2[i].index() + "	");
				System.out.print(nodes3[i].index() + "	");
				System.out.print(nodes4[i].index() + "	");
				System.out.print(nodes5[i].index() + "	");
				System.out.print(nodes6[i].index() + "	");
				System.out.println();
			}

			return;
		}

		// if(true){
		// Graph g = GraphReader.read(SPI);
		// Transformation stt = new SpanningTreeSorting();
		// stt.transform(g);
		// return;
		// }

		if (true) {
			Stats stats = new Stats();
			Config.overwrite("MAIN_DATA_FOLDER", "./data/hierarchicalSorting/");
			Config
					.overwrite("MAIN_PLOT_FOLDER",
							"./plots/hierarchicalSorting/");
			Config.overwrite("METRICS", "RL");
			Config.overwrite("RL_ROUTES_PER_NODE", "5");
			int times = 1;

			RoutingAlgorithm r1 = new TwoPhaseGreedyRegistration(50, 20, 0);
			RoutingAlgorithm r2 = new TwoPhaseGreedyRegistration(50, 20, 1);
			RoutingAlgorithm r3 = new TwoPhaseGreedyRegistration(50, 20, 2);

			Transformation lc = new GiantConnectedComponent();
			Transformation riR = new RandomID(RandomID.RING_NODE, 1);
			Transformation os1 = new OutlierSorting(1);
			Transformation osC = new OutlierSorting(-1);
			Transformation riRmR = new RandomID(RandomID.RING_NODE_MULTI_R, 5);
			Transformation osmR1 = new OutlierSortingMultiR(1);
			Transformation osmRC = new OutlierSortingMultiR(-1);
			Transformation sts = new SpanningTreeSorting(0);
			Transformation[] t1 = new Transformation[] { lc, sts };
			// Transformation[] t2 = new Transformation[] { lc, riR, os1 };
			// Transformation[] t2 = new Transformation[] { lc, riRmR, osmR1 };
			// Transformation[] t3 = new Transformation[] { lc, riR, osC };
			// Transformation[] t4 = new Transformation[] { lc, riRmR, osmRC };

			// i= 1141 problem...
			Transformation[][] t = new Transformation[9222][];
			for (int i = 0; i < t.length; i++) {
				t[i] = new Transformation[] { lc, new SpanningTreeSorting(i) };
			}
			RoutingAlgorithm[] r = new RoutingAlgorithm[] { r1 };

			Network[] n = new Network[t.length * r.length];
			int index = 0;
			for (int i = 0; i < r.length; i++) {
				for (int j = 0; j < t.length; j++) {
					n[index++] = new ReadableFile("SPI", "spi", SPI,
							GraphReader.OWN_FORMAT, r[i], t[j]);
				}
			}

//			Series[] s = Series.generate(n, times);
			 Series[] s = Series.get(n);
			Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
			Config.overwrite("PLOT_EXTENSION", ".jpeg");
			Config.overwrite("RL_FR_PLOT_MODE_AVG", "DOTS_ONLY");
			Plot.singlesAvg(s, "singles/");

			stats.end();
			return;
		}

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

			Transformation lc = new LargestConnectedComponent();
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
		Transformation lc = new LargestConnectedComponent();
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
