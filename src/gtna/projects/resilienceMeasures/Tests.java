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
 * NodeSortingTest.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: truong;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.resilienceMeasures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.gephi.statistics.plugin.GraphDistance;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.CentralityNodeSorter;
import gtna.graph.sorting.ClosenessCentralityNodeSorter;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.EigenvectorCentralityNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.CentralityNodeSorter.CentralityMode;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.graph.sorting.algorithms.ResilienceMetrics;
import gtna.metrics.BiconnectedComponent;
import gtna.metrics.ApproxEffectiveDiameter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.StrongFragmentation;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.GLP;
import gtna.networks.model.PFP;
import gtna.networks.model.PFP;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;

/**
 * @author truong
 * 
 */
public class Tests {
	public static void main(String[] args) {

		/*
		 * Utils u = new Utils(); Graph g = u.importGraphFromFile("Erdos.gml");
		 * GraphWriter.write(g, "Erdos.gtna");
		 */

		Tests.PFPPlotTest();
	}

	public static void test() {
		System.out.println("Importing...");
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("vietnam.gml");
		System.out.println("Graph imported!");

		// sorting
		CentralityNodeSorter sorter = new CentralityNodeSorter(
				CentralityMode.CLOSENESS, NodeSorter.NodeSorterMode.ASC);
		// sorter.setCentrality("BETWEENNESS");
		Node[] sorted = sorter.sort(g, new Random());
		sorted = sorter.resort("BETWEENNESS", new Random());

		// sorted
		System.out.println("-----");
		System.out.println("Sorted:");
		for (int i = 0; i < sorted.length; i++) {
			System.out.println(sorted[i] + " -- "
					+ sorter.getCentrality(sorted[i]));
		}

		// print centrality from gtna and gephi
		HashMap<Integer, Double> gephiCentrality = u
				.gephiCentrality(GraphDistance.BETWEENNESS);
		System.out.println("GTNA Centrality:");
		for (Node n : g.getNodes()) {
			System.out.println("" + n.toString() + " = "
					+ sorter.getCentrality(n) + " -- gephi: "
					+ (gephiCentrality.get(n.getIndex())));
		}

	}

	public static void speedTest() {
		Date start = new Date();
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("PFP.gml");

		CentralityNodeSorter bc = new CentralityNodeSorter(
				CentralityMode.BETWEENNESS, NodeSorterMode.ASC);

		Node[] sorted = bc.sort(g, new Random());

		System.out.println("Sorted:");
		for (int i = 0; i < sorted.length; i++) {
			System.out.println(sorted[i].toString() + " -- "
					+ bc.getCentrality(sorted[i]));
		}
		long time = (new Date()).getTime() - start.getTime();
		System.out.println("Runtime = " + time / 1000 + "second(s)");
	}

	public static void biconnectedTest() {
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("internetRouter.gml");

		ResilienceMetrics rm = new ResilienceMetrics(g);
		rm.biconnectedComponents();
		System.out.println("Size of max component = "
				+ rm.getMaxComponent().size());
	}

	public static void eigenVectorTest() {
		System.out.println("Importing...");
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("internetRouter.gml");
		System.out.println("Graph imported!");

		// sorting
		EigenvectorCentralityNodeSorter sorter = new EigenvectorCentralityNodeSorter(
				NodeSorter.NodeSorterMode.ASC);
		Node[] sorted = sorter.sort(g, new Random());

		// sorted
		System.out.println("-----");
		System.out.println("Sorted:");
		for (int i = 0; i < sorted.length; i++) {
			System.out.println(sorted[i] + " -- "
					+ sorter.getCentrality(sorted[i]));
		}
	}

	public static void eigenVectorCorrectnessTest() {
		System.out.println("Importing...");
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("internetRouter.gml");
		System.out.println("Graph imported!");

		// sorting
		EigenvectorCentralityNodeSorter sorter = new EigenvectorCentralityNodeSorter(
				NodeSorter.NodeSorterMode.ASC);
		sorter.setNumRuns(65000);
		Node[] sorted = sorter.sort(g, new Random());

		EigenvectorCentralityNodeSorter sorter1 = new EigenvectorCentralityNodeSorter(
				NodeSorter.NodeSorterMode.ASC);
		sorter1.setNumRuns(70000);
		Node[] sorted1 = sorter1.sort(g, new Random());

		// sorted
		Node[] nodes = g.getNodes();
		System.out.println("-----");
		System.out.println("Sorted:");

		double err = 0;
		double errSum = 0;
		for (int i = 0; i < nodes.length; i++) {
			double p = sorter.getCentrality(sorted1[i]);
			double p1 = sorter1.getCentrality(sorted1[i]);
			double temp = (Math.abs(p - p1)) / Math.max(p, p1) * 100;
			System.out.println("" + temp);
			err = Math.max(err, temp);
			errSum += temp;
		}
		System.out.println("Max Err = " + err);
		System.out.println("Average Err = " + (errSum / nodes.length));
	}

	public static void PFPTest() {
		int N = 9204;
		int startNodes = 20;
		double p = 0.3;
		double q = 0.1;
		double delta = 0.021;
		Network nw = new PFP(N, startNodes, p, q, delta, null);

		System.out.println("generating...");
		Graph g = nw.generate();
		System.out.println("generated!");
		int maxDegree = 0;
		for (Node n : g.getNodes()) {
			if (maxDegree < n.getDegree()) {
				maxDegree = n.getDegree();
			}
		}

		try {
			Utils.exportToGML(g, "PFP");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot export graph to file!");
		}

	}

	public static void Erdos() {
		int N = 20;
		Network nw = new ErdosRenyi(N, 6, true, null);

		System.out.println("generating...");
		Graph g = nw.generate();
		System.out.println("generated!");
		int maxDegree = 0;
		for (Node n : g.getNodes()) {
			if (maxDegree < n.getDegree()) {
				maxDegree = n.getDegree();
			}
		}
		System.out.println("==========");
		System.out.println("Nodes = " + g.getNodes().length);
		System.out.println("Edges = " + g.getEdges().getEdges().size() / 2);
		System.out.println("Max Degree = " + maxDegree / 2);

		try {
			Utils.exportToGML(g, "Erdos");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot export graph to file!");
		}

	}

	public static void GLPTest() {
		int N = 2000;
		int m0 = 100;
		int m = 2;
		double p = 0.4695;
		double beta = 0.6447;
		Network nw = new GLP(N, m0, m, p, beta, null);
		System.out.println("generating...");
		Graph g = nw.generate();
		System.out.println("generated!");
		int maxDegree = 0;
		for (Node n : g.getNodes()) {
			if (maxDegree < n.getDegree()) {
				maxDegree = n.getDegree();
			}
		}
		System.out.println("==========");
		System.out.println("Nodes = " + g.getNodes().length);
		System.out.println("Edges = " + g.getEdges().getEdges().size() / 2);
		System.out.println("Max Degree = " + maxDegree);

		try {
			Utils.exportToGML(g, "GLP");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot export graph to file!");
		}
	}

	public static void testCCinBC() {
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("germany.gml");

		CentralityNodeSorter bc = new CentralityNodeSorter(
				CentralityMode.CLOSENESS, NodeSorterMode.ASC);

		Node[] sorted = bc.sort(g, new Random());

		ClosenessCentralityNodeSorter cc = new ClosenessCentralityNodeSorter(
				NodeSorterMode.ASC);

		Node[] sorted1 = cc.sort(g, new Random());

		System.out.println("Sorted:");
		for (int i = 0; i < sorted.length; i++) {
			System.out.println(sorted[i].toString() + " -- "
					+ bc.getCentrality(sorted[i]) + " -- "
					+ cc.getCentrality(sorted[i]));
		}

		System.out.println("Resort using Betweenness:");
		sorted = bc.resort("BETWEENNESS", new Random());
		for (int i = 0; i < sorted.length; i++) {
			System.out.println(sorted[i].toString() + " -- "
					+ bc.getCentrality(sorted[i]));
		}
	}

	public static void GLPPlotTest() {

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		int N = 20000;
		int m0 = 100;
		int M = 2;
		double p = 0.4695;
		double beta = 0.6447;
		Network nw = new GLP(N, m0, M, p, beta, null);

		Metric m = new DegreeDistribution();
		Metric[] metrics = new Metric[] { m };
		Series s = Series.generate(nw, metrics, 3);
		Plotting.multi(s, metrics, "GLP/");
	}

	public static void PFPPlotTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		int N = 11122;
		int startNodes = 20;
		double p = 0.3;
		double q = 0.1;
		double delta = 0.020846;
		Network nw = new PFP(N, startNodes, p, q, delta, null);

		Network[] networks = new Network[] { nw };

		Metric m = new DegreeDistribution();
		Metric[] metrics = new Metric[] { m };
		Series[] s = Series.generate(networks, metrics, 10);
		Plotting.multi(s, metrics, "PFP/");

	}

	public static void effectiveDiameter() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		// Network nw = new ReadableFile("ERDOS", "", "Erdos.gtna", null);

		Network nw = new ErdosRenyi(100, 6, true, null);
		Network[] networks = new Network[] { nw };

		Metric m = new ApproxEffectiveDiameter(128, 7, new RandomNodeSorter());
		Metric[] metrics = new Metric[] { m };
		Series[] s = Series.generate(networks, metrics, 1);
		Plotting.multi(s, metrics, "EFFECTIVE_DIAMETER/");
	}

	public static void read() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		Network nw = new ReadableFile("ERDOS", "TEST", "Erdos.gtna", null);

		Network[] networks = new Network[] { nw };

		Metric m = new DegreeDistribution();
		Metric[] metrics = new Metric[] { m };
		Series[] s = Series.generate(networks, metrics, 1);
		Plotting.multi(s, metrics, "EFFECTIVE_DIAMETER/");
	}

	public static void getB() {
		boolean[] bools = new boolean[] { false, true, false, false, true,
				false, false, true, false };
		int length = 3;
		// TODO: lowest order zero bit? left or right???
		ArrayList<boolean[]> temp = Tests.extract(bools, length);
		int myK = temp.size();
		int sumB = 0;
		for (boolean[] bool : temp) {
			sumB += Tests.getLowestOrderZeroBit(bool);
		}
		System.out.println("" + ((double) sumB) / myK);
	}

	private static ArrayList<boolean[]> extract(boolean[] bools, int length) {
		ArrayList<boolean[]> result = new ArrayList<boolean[]>();
		int index = 0;
		while (index + length <= bools.length) {
			boolean[] temp = new boolean[length];
			for (int i = 0; i < length; i++) {
				temp[i] = bools[index + i];
			}
			index += length;
			result.add(temp);
		}
		return result;
	}

	private static int getLowestOrderZeroBit(boolean[] bool) {
		int result = 0;
		while (bool[bool.length - 1 - result]) {
			result++;
			if (result == bool.length) {
				break;
			}
		}
		return result;
	}

}
