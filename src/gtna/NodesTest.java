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
 * NodesTest.java
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
package gtna;

import gtna.communities.Communities;
import gtna.communities.Community;
import gtna.communities.Role;
import gtna.communities.Roles;
import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.IDSpace;
import gtna.id.Partition;
import gtna.io.GraphReader;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedy.GreedyBacktracking;
import gtna.routing.lookahead.Lookahead;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpace;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.util.Config;
import gtna.util.Stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class NodesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// testGraph();
		// int total = 5000000;
		// testHash(total);
		// testHash1(total);
		// testHash2(50000000);
		// testHash3(50000000);
		// testHash1(50000000);
		// testHash2(50000000);
		// testHash3(50000000);
		// testNW();
		// testMetrics();
		// testID();
		// testRouting();
		// testCommunities();
		// testRoles();
		testIDSpaces();
		// testGraphIO();
	}

	private static void testGraphIO() {
		Network nw = new ErdosRenyi(10, 3, true, null, null);
		Transformation t = new RandomPlaneIDSpaceSimple();
		Graph g1 = nw.generate();
		Graph g2 = nw.generate();
		g2 = t.transform(g2);

		String f1 = "temp/test/graph-1.txt";
		String f2 = "temp/test/graph-2.txt";
		String f3 = "temp/test/graph-3.txt";
		String f4 = "temp/test/graph-4.txt";

		GraphWriter.writeWithProperties(g1, f1);
		GraphWriter.writeWithProperties(g2, f2);

		Graph g3 = GraphReader.readWithProperties(f1);
		Graph g4 = GraphReader.readWithProperties(f2);
		GraphWriter.writeWithProperties(g3, f3);
		GraphWriter.writeWithProperties(g4, f4);
	}

	private static void testIDSpaces() {
		Transformation t1 = new RandomPlaneIDSpaceSimple(1, 100, 500, true);
		Transformation t2 = new RandomPlaneIDSpaceSimple(2, 10, 0.5, false);
		Transformation t3 = new RandomRingIDSpaceSimple(1, 1.0, true);
		Transformation t4 = new RandomRingIDSpaceSimple(2, 111.0, true);
		Transformation t5 = new RandomRingIDSpace(1, 2.0, true);
		Transformation t6 = new RandomRingIDSpace(2, 222.0, true);

		Transformation[] t = new Transformation[] { t1, t2, t3, t4, t5, t6 };
		Network nw = new ErdosRenyi(10, 3, true, null, t);
		Graph g1 = nw.generate();
		for (Transformation trans : t) {
			g1 = trans.transform(g1);
		}
		GraphWriter.writeWithProperties(g1, "./temp/test/g1.txt");
		Graph g2 = GraphReader.readWithProperties("./temp/test/g1.txt");
		GraphWriter.writeWithProperties(g2, "./temp/test/g2.txt");
	}

	private static void testCommunities() {
		ArrayList<Integer> c0 = new ArrayList<Integer>();
		c0.add(0);
		c0.add(2);
		c0.add(1);
		ArrayList<Integer> c1 = new ArrayList<Integer>();
		c1.add(3);
		c1.add(4);
		ArrayList<Integer> c2 = new ArrayList<Integer>();
		Community[] cc = new Community[] { new Community(0, c0),
				new Community(1, c1), new Community(2, c2) };
		String key = "weoifghewg";
		Communities c = new Communities(cc);
		c.write("temp/test/c1.txt", key);
		Graph graph = new Graph("TEST");
		Communities cc2 = new Communities();
		cc2.read("temp/test/c1.txt", graph);
		cc2.write("temp/test/c2.txt", key);
	}

	private static void testRoles() {
		ArrayList<Role> roles = new ArrayList<Role>();
		roles.add(new Role(Role.KINLESS_NODE, new int[] { 0, 1, 2, 5 }));
		roles.add(new Role(Role.PERIPHERAL, new int[] { 3, 4, 6 }));
		Roles r = new Roles(roles);
		r.write("temp/test/r1.txt", "woigehwieg");
		Graph graph = new Graph("TEST");
		Roles r2 = new Roles();
		r2.read("temp/test/r1.txt", graph);
		r2.write("temp/test/r2.txt", "oweiugz8998");
	}

	private static void testRouting() {
		Config.overwrite("METRICS", "R");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/testRouting/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/testRouting/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		Transformation[] t1 = new Transformation[] { new RandomRingIDSpaceSimple() };
		Transformation[] t2 = new Transformation[] { new RandomPlaneIDSpaceSimple() };
		RoutingAlgorithm r1 = new Greedy(30);
		RoutingAlgorithm r2 = new GreedyBacktracking(30);
		RoutingAlgorithm r3 = new Lookahead(30);
		// Network nw1 = new ReadableFile("SPI", "spi",
		// "./resources/SPI-3-LCC/2010-08.spi.txt", r1, t);
		// Network nw2 = new ReadableFile("SPI", "spi",
		// "./resources/SPI-3-LCC/2010-08.spi.txt", r2, t);
		Network nw_1 = new ErdosRenyi(1000, 20, true, r1, t1);
		Network nw_2 = new ErdosRenyi(1000, 20, true, r2, t1);
		Network nw_3 = new ErdosRenyi(1000, 20, true, r3, t1);
		Network nw_4 = new ErdosRenyi(1000, 20, true, r1, t2);
		Network nw_5 = new ErdosRenyi(1000, 20, true, r2, t2);
		Network nw_6 = new ErdosRenyi(1000, 20, true, r3, t2);
		Network nw1 = new DescriptionWrapper(nw_1, "Greedy - 1D");
		Network nw2 = new DescriptionWrapper(nw_4, "Greedy - 2D");
		Network nw3 = new DescriptionWrapper(nw_2, "Backtracking - 1D");
		Network nw4 = new DescriptionWrapper(nw_5, "Backtracking - 2D");
		Network nw5 = new DescriptionWrapper(nw_3, "Lookahead - 1D");
		Network nw6 = new DescriptionWrapper(nw_6, "Lookahead - 2D");
		Network[] nw = new Network[] { nw1, nw2, nw3, nw4, nw5, nw6 };
		Network[][] nwnw = new Network[][] { new Network[] { nw1 },
				new Network[] { nw2 }, new Network[] { nw3 },
				new Network[] { nw4 }, new Network[] { nw5 },
				new Network[] { nw6 } };

		Series[] s1 = Series.generate(nw, 5);
		Series[][] s2 = Series.get(nwnw);
		Plot.multiAvg(s1, "multi/");
		Plot.singlesAvg(s2, "singles/");
	}

	private static void testID() {
		Network nw = new ErdosRenyi(2, 2, true, null, null);
		Transformation t = new RandomRingIDSpaceSimple(4, 1, true);
		Graph g = nw.generate();
		g = t.transform(g);
		int r = 0;
		Random rand = new Random();
		while (g.hasProperty("ID_SPACE_" + r)) {
			IDSpace idSpace = (IDSpace) g.getProperty("ID_SPACE_" + r);
			System.out.println(idSpace);
			for (Partition p : idSpace.getPartitions()) {
				System.out.println("  " + p);
			}
			for (int i = 0; i < 10; i++) {
				System.out.println("  => " + idSpace.randomID(rand));
			}
			r++;
		}
	}

	private static void testMetrics() {
		Config.overwrite("METRICS", "DD, SP, CC");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/testMetrics/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/testMetrics/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Network nw = new ErdosRenyi(1000, 20, true, null, null);
		// Series s = Series.generate(nw, 1);
		// Plot.multiAvg(s, "multi-avg/");
		// Plot.multiConf(s, "multi-conf/");
		// Plot.multiVar(s, "multi-var/");
		Network[] nw2 = ErdosRenyi.get(1000, new double[] { 1, 5, 10, 15, 20 },
				true, null, null);
		Series[] s2 = Series.generate(nw2, 1);
		Plot.singlesAvg(s2, "single-avg/");
		Plot.multiAvg(s2, "multi-avg/");
	}

	private static void testNW() {
		Network nw = null;
		// nw = new Star(10, null, null);
		// nw = new Ring(10, null, null);
		// nw = new Complete(5, null, null);
		// nw = new BarabasiAlbert(100, 2, null, null);
		// nw = new Communities3(new int[] { 100, 200 }, new double[][] {
		// new double[] { 0.5, 0.1 }, new double[] { 0.1, 0.5 } },
		// Communities3.RANDOM_ORDER, false, null, null);
		// nw = new DeBruijn(2, 3, null, null);
		nw = new ErdosRenyi(10, 2, true, null, null);
		// nw = new Gilbert(10, 100, true, null, null);
		// nw = new GN(10, false, null, null);
		// nw = new CAN(10, 2, 1, null, null);
		Graph graph = nw.generate();
		for (Node n : graph.getNodes()) {
			System.out.println(n);
		}
		// GraphWriter.write(graph, "./temp/test1.txt");
		// Graph graph2 = GraphReader.read("./temp/test1.txt");
		// GraphWriter.write(graph2, "./temp/test2.txt");
		// Edge[] edges = graph.generateOutgoingEdges();
		// for (Edge e : edges) {
		// System.out.println(e);
		// }
	}

	private static void testHash(int total) {
		Stats stats = new Stats();
		for (int i = 0; i < total; i++) {
			Hashtable<String, String> table = new Hashtable<String, String>();
			table.put("abc", "oierhgio eghewi ehgihoehoghgoehgoewhogeho");
			String blafasel = table.get("abc");
		}
		stats.end();
	}

	private static void testHash1(int total) {
		Stats stats1 = new Stats();
		Random rand = new Random();
		HashMap<String, String> table = new HashMap<String, String>();
		HashSet<String> keys = new HashSet<String>();
		for (int i = 0; i < total; i++) {
			String key = "" + rand.nextInt();
			String value = "" + i;
			keys.add(key);
			table.put(key, value);
		}
		String bla = "";
		stats1.end();
		Stats stats2 = new Stats();
		for (String key : keys) {
			String value = table.get(key);
			bla += value;
		}
		bla = bla.substring(10);
		stats2.end();
	}

	private static void testHash2(int total) {
		Stats stats1 = new Stats();
		Random rand = new Random();
		HashMap<Integer, String> table = new HashMap<Integer, String>();
		HashSet<Integer> keys = new HashSet<Integer>();
		for (int i = 0; i < total; i++) {
			int key = rand.nextInt();
			String value = "" + i;
			keys.add(key);
			table.put(key, value);
		}
		stats1.end();
		Stats stats2 = new Stats();
		String bla = "";
		for (int key : keys) {
			String value = table.get(key);
			bla += value;
		}
		bla = bla.substring(10);
		stats2.end();
	}

	private static void testHash3(int total) {
		Stats stats1 = new Stats();
		Random rand = new Random();
		HashMap<String, String> table = new HashMap<String, String>();
		HashSet<String> keys = new HashSet<String>();
		for (int i = 0; i < total; i++) {
			String key = "" + rand.nextInt()
					+ "eiugeguehgiowehghiowhgohgeiohiohehewgheiogehoioh";
			String value = "" + i;
			keys.add(key);
			table.put(key, value);
		}
		stats1.end();
		Stats stats2 = new Stats();
		String bla = "";
		for (String key : keys) {
			String value = table.get(key);
			bla += value;
		}
		bla = bla.substring(10);
		stats2.end();
	}

	private static void testGraph() {
		int[] N = new int[] { 10000, 20000, 30000, 40000, 50000 };
		double[] D = new double[] { 10, 100 };
		for (double d : D) {
			System.out.println("\n");
			for (int n : N) {
				NodesTest.testER(n, d);
			}
		}
	}

	private static void testER(int n, double d) {
		Stats stats = new Stats();
		Network er = new ErdosRenyi(n, d, true, null, null);
		System.out.println("\n" + er.description());
		Graph g = er.generate();
		stats.end();
		er = null;
		g = null;
		for (int i = 0; i < 10; i++) {
			System.gc();
		}
	}

}
