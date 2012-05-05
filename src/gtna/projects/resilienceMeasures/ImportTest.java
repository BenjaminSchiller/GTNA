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
 * ImportTest.java
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
import gtna.graph.sorting.CentralityNodeSorter;
import gtna.graph.sorting.ClosenessCentralityNodeSorter;
import gtna.graph.sorting.NodeSorter;

/**
 * @author truong
 * 
 */
public class ImportTest {
	public static void main(String[] args) {
		Utils u = new Utils();
		Graph g = u.importGraphFromFile("germany.gml");

		// print centrality from gephi

		// sorting
		NodeSorter sorter = new ClosenessCentralityNodeSorter(
				NodeSorter.NodeSorterMode.ASC);
		sorter.sort(g, new Random());

		// print centrality from gtna
		System.out.println("GTNA Centrality:");
		for (Node n : g.getNodes()) {
			System.out
					.println(""
							+ n.toString()
							+ " = "
							+ ((ClosenessCentralityNodeSorter) sorter)
									.getCentrality(n));
		}
	}
}
