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
import java.util.HashMap;
import java.util.Random;

import org.gephi.statistics.plugin.GraphDistance;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.BetweennessCentralityNodeSorter;
import gtna.graph.sorting.ClosenessCentralityNodeSorter;
import gtna.graph.sorting.EigenvectorCentralityNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.algorithms.ResilienceMetrics;
import gtna.networks.Network;
import gtna.networks.model.GLP;
import gtna.networks.model.PositiveFeedbackPreference;

/**
 * @author truong
 * 
 */
public class Tests {
	public static void main(String[] args) {
		Tests.speedTest();
	}

	public static void test() {
		System.out.println("Importing...");
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("vietnam.gml");
		System.out.println("Graph imported!");

		// sorting
		BetweennessCentralityNodeSorter sorter = new BetweennessCentralityNodeSorter(
				"BETWEENNESS", NodeSorter.NodeSorterMode.ASC);
		// sorter.setCentrality("BETWEENNESS");
		Node[] sorted = sorter.sort(g, new Random());

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
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("power.gml");

		BetweennessCentralityNodeSorter bc = new BetweennessCentralityNodeSorter(
				"BETWEENNESS", NodeSorterMode.ASC);

		Node[] sorted = bc.sort(g, new Random());

		System.out.println("Sorted:");
		for (int i = 0; i < sorted.length; i++) {
			System.out.println(sorted[i].toString() + " -- "
					+ bc.getCentrality(sorted[i]));
		}
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
		int N = 1000;
		double p = 0.4;
		double delta = 0.021;
		Network nw = new PositiveFeedbackPreference(N, p, delta, null);
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
			Utils.exportToGML(g, "PFP");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Cannot export graph to file!");
		}
	}

	public static void GLPTest() {
		int N = 2000;
		int m0 = 50;
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
}
