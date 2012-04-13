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

import java.util.Random;

import org.gephi.statistics.plugin.GraphDistance;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.BetweennessCentralityNodeSorter;
import gtna.graph.sorting.ClosenessCentralityNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;

/**
 * @author truong
 * 
 */
public class NodeSortingTest {
	public static void main(String[] args) {
		/*
		 * System.out.println("Generating graph..."); Network nw = new
		 * ErdosRenyi(1000, 10, true, null); Graph g = nw.generate();
		 */
		System.out.println("Importing...");
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("vietnam_modified.gml");
		System.out.println("Graph imported!");

		// nodes
		Node[] nodes = g.getNodes();

		// sorting
		NodeSorter sorter = new ClosenessCentralityNodeSorter(
				NodeSorter.NodeSorterMode.ASC);
		Node[] sorted = sorter.sort(g, new Random());

		// original
		System.out.println("Origninal:");
		for (int i = 0; i < nodes.length; i++) {
			System.out.println(i + ": " + nodes[i]);
		}

		// sorted
		System.out.println("-----");
		System.out.println("Sorted:");
		for (int i = 0; i < sorted.length; i++) {
			System.out.println(i
					+ ": "
					+ sorted[i]
					+ " -- "
					+ ((ClosenessCentralityNodeSorter) sorter)
							.getCentrality(g.getNode(i)));
		}

		// print centrality from gtna
		System.out.println("GTNA Centrality:");
		for (Node n : g.getNodes()) {
			System.out.println(""
					+ n.toString()
					+ " = "
					+ ((ClosenessCentralityNodeSorter) sorter)
							.getCentrality(n));
		}

		// print centrality from gephi
		u.printCentrality(GraphDistance.CLOSENESS);

	}
}
