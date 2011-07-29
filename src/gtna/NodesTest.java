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

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.plot.Plot;
import gtna.util.Config;
import gtna.util.Stats;

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
	public static void main(String[] args) {
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
		testMetrics();
	}

	private static void testMetrics() {
		Config.overwrite("METRICS", "SP");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/testMetrics/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/testMetrics/");
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Network nw = new ErdosRenyi(1000, 20, true, null, null);
		// Series s = Series.generate(nw, 1);
		// Plot.multiAvg(s, "multi-avg/");
		// Plot.multiConf(s, "multi-conf/");
		// Plot.multiVar(s, "multi-var/");
		Network[] nw2 = ErdosRenyi.get(1000, new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
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
