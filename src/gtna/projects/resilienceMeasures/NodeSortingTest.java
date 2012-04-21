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

import java.util.HashMap;
import java.util.Random;

import org.gephi.statistics.plugin.GraphDistance;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.BetweennessCentralityNodeSorter;
import gtna.graph.sorting.CentralityNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.algorithms.ResilienceMetrics;

/**
 * @author truong
 * 
 */
public class NodeSortingTest {
	public static void main(String[] args) {

		NodeSortingTest.biconnectedTest();

	}

	public static void test() {
		System.out.println("Importing...");
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("vietnam.gml");
		System.out.println("Graph imported!");

		// sorting
		CentralityNodeSorter sorter = new CentralityNodeSorter(
				NodeSorter.NodeSorterMode.ASC);
		sorter.setCentrality("BETWEENNESS");
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
		Graph g = u.importGraphFromFile("germany.gml");

		BetweennessCentralityNodeSorter bc = new BetweennessCentralityNodeSorter(
				NodeSorterMode.ASC);

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
}
