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
 * Test2.java
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
import java.util.Random;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.CentralityNodeSorter;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.CentralityNodeSorter.CentralityMode;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.NodeSorting;
import gtna.graph.sorting.algorithms.GraphSPall;
import gtna.graph.sorting.algorithms.GraphSPallFloyd;
import gtna.metrics.BiconnectedComponent;
import gtna.metrics.ApproxEffectiveDiameter;
import gtna.metrics.ExactEffectiveDiameter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.GLP;
import gtna.networks.model.IG;
import gtna.networks.model.PARG;
import gtna.networks.model.PFP;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;

/**
 * @author truong
 * 
 */
public class Test2 {
	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			Test2.PARGTest();
		}
	}

	public static void readFileTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		String graphFile = ".\\caida2007.gtna";
		Network nw1 = new ReadableFile("CAIDA", "CAIDA", graphFile, null);
		int N = 12160;

		Network nw2 = new PFP(N, 20, 0.3, 0.1, 0.021, null);
		Network[] networks = new Network[] { nw1, nw2 };

		Metric metric = new DegreeDistribution();
		Metric[] metrics = new Metric[] { metric };

		Series[] s = Series.generate(networks, metrics, 20);
		Plotting.multi(s, metrics, "compare/");
	}

	public static void effectiveDiameter() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		Network nw = new PFP(1000, 20, 0.3, 0.1, 0.021, null);
		Network[] networks = new Network[] { nw };

		NodeSorter sorter = new DegreeNodeSorter(NodeSorterMode.DESC);
		Metric m1 = new ApproxEffectiveDiameter(128, 7, sorter);
		Metric m2 = new ExactEffectiveDiameter(sorter);
		Metric[] metrics = new Metric[] { m2 };

		Series[] s = Series.generate(networks, metrics, 1);
		Plotting.multi(s, metrics, "effectiveDiameter/");

	}

	public static void allPairsTest() {
		Network nw = new PFP(100, 10, 0.3, 0.1, 0.021, null);
		Graph g = nw.generate();
		GraphSPallFloyd allpairs = new GraphSPallFloyd(g);
		System.out.println("" + allpairs.dist(1, 90));
		System.out.println("" + allpairs.dist(90, 1));
	}

	public static void PFPTest() {
		int N = 11122;
		int startNodes = 20;
		double p = 0.3;
		double q = 0.1;
		double delta = 0.020846;
		Network nw = new PFP(N, startNodes, p, q, delta, null);

		Graph g = nw.generate();

		System.out.println("Calculate Centrality...");

		CentralityNodeSorter sorter = new CentralityNodeSorter(
				CentralityMode.BETWEENNESS, NodeSorterMode.DESC);
		sorter.sort(g, new Random());

		double averageBC = 0;
		double maxBC = 0;
		int maxDegree = 0;

		for (Node node : g.getNodes()) {
			double c = sorter.getCentrality(node) / N;
			averageBC += c;
			if (maxBC < c)
				maxBC = c;
			if (maxDegree < node.getDegree()) {
				maxDegree = node.getDegree();
			}
		}

		averageBC = averageBC / N;

		// characteristic path length
		double characteristicLength = sorter.getSumOfDistance() / (N * (N - 1));

		// Rich-club connectivity
		int r = (int) (0.01 * N);
		DegreeNodeSorter degreeSorter = new DegreeNodeSorter(
				NodeSorterMode.DESC);
		Node[] order = degreeSorter.sort(g, new Random());
		double richClub = 0;
		for (int i = 0; i < r - 1; i++) {
			for (int j = i; j < r; j++) {
				Node u = order[i];
				Node v = order[j];
				if (u.isConnectedTo(v))
					richClub++;
			}
		}
		richClub = richClub / (r * (r - 1) / 2);

		System.out.println("Characteristic Path Length = "
				+ characteristicLength);
		System.out.println("Average BC* = "
				+ (averageBC + characteristicLength));
		System.out.println("Max BC = " + maxBC);
		System.out.println("Max Degree = " + maxDegree);
		System.out.println("Rich Club = " + richClub);
	}

	public static void GLPTest() {
		int N = 8613;
		int m0 = 5;
		int M = 2; // do not impact the model
		double p = 0.4695;
		double beta = 0.6447;
		Network nw = new GLP(N, m0, M, p, beta, null);

		Graph g = nw.generate();

		System.out.println("Calculate Centrality...");

		CentralityNodeSorter sorter = new CentralityNodeSorter(
				CentralityMode.BETWEENNESS, NodeSorterMode.DESC);
		sorter.sort(g, new Random());

		// characteristic path length
		double characteristicLength = sorter.getCharacteristicPathLength();

		// Rich-club connectivity
		int r = (int) (0.01 * N);
		DegreeNodeSorter degreeSorter = new DegreeNodeSorter(
				NodeSorterMode.DESC);
		Node[] order = degreeSorter.sort(g, new Random());
		double richClub = 0;
		for (int i = 0; i < r - 1; i++) {
			for (int j = i; j < r; j++) {
				Node u = order[i];
				Node v = order[j];
				if (u.isConnectedTo(v))
					richClub++;
			}
		}
		richClub = richClub / (r * (r - 1) / 2);

		System.out.println("Characteristic Path Length = "
				+ characteristicLength);
		System.out.println("Rich Club = " + richClub);
		System.out.println("Nodes = " + g.getNodes().length);
		System.out.println("Edges = " + g.getEdges().getEdges().size());
	}

	public static void PARGTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		int N = 3000;
		int startNodes = 10;
		double N_add = 0.006;
		double N_del = 4.8;
		double N_cut = 0.2;

		Network nw = new PARG(N, startNodes, N_add, N_del, N_cut, null);
		Graph g = nw.generate();
		System.out.println("Edges = " + g.getEdges().getEdges().size());

		int maxDegree = 0;
		for (Node n : g.getNodes()) {
			if (maxDegree < n.getDegree())
				maxDegree = n.getDegree();
		}
		System.out.println("Max Degree = " + maxDegree);

		// Rich-club connectivity
		int r = (int) (0.01 * N);
		DegreeNodeSorter degreeSorter = new DegreeNodeSorter(
				NodeSorterMode.DESC);
		Node[] order = degreeSorter.sort(g, new Random());
		double richClub = 0;
		for (int i = 0; i < r - 1; i++) {
			for (int j = i; j < r; j++) {
				Node u = order[i];
				Node v = order[j];
				if (u.isConnectedTo(v))
					richClub++;
			}
		}
		richClub = richClub / (r * (r - 1) / 2);
		System.out.println("1% = " + richClub);

		r = (int) (0.02 * N);
		richClub = 0;
		for (int i = 0; i < r - 1; i++) {
			for (int j = i; j < r; j++) {
				Node u = order[i];
				Node v = order[j];
				if (u.isConnectedTo(v))
					richClub++;
			}
		}
		richClub = richClub / (r * (r - 1) / 2);
		System.out.println("2% = " + richClub);

		try {
			Utils.exportToGML(g, "PARG");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot export graph to file!");
		}

	}

	public static void IGTest() {

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		int N = 11461;
		int startNodes = 10;
		double p = 0.4;
		Network nw = new IG(N, startNodes, p, null);

		Metric m = new DegreeDistribution();
		Metric[] metrics = new Metric[] { m };

		Series s = Series.generate(nw, metrics, 5);
		Plotting.multi(s, metrics, "\\IG/");
	}
	
	public static void compareTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");
		
		// String graphFile = ".\\caida2012.gtna";
		// Network nw = new PFP(100, 10, 0.3, 0.1, 0.020846, null);
		// Network nw = new GLP(12160, 10, 1.13, 0.4695, 0.6447, null);
		Network nw = new IG(12160, 10, 0.4, null);
		
		Metric m = new DegreeDistribution();
		Metric[] metrics = new Metric[] { m };

		Series s = Series.generate(nw, metrics, 1);
		Plotting.multi(s, metrics, "\\Compare/");
	}
}
