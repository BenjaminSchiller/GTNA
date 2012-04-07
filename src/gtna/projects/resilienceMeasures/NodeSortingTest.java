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

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;

/**
 * @author truong
 *
 */
public class NodeSortingTest {
	public static void main(String[] args) {
		Network nw = new ErdosRenyi(20, 4, true, null);
		Graph g = nw.generate();
		
		// nodes
		Node[] nodes = g.getNodes();
		
		// sorting
		NodeSorter sorter = new DegreeNodeSorter(NodeSorter.NodeSorterMode.ASC);
		Node[] sorted = sorter.sort(g, new Random());
		
		for (int i = 0; i < nodes.length; i++) {
			System.out.println(i + ": " + nodes[i]);
		}
		System.out.println("-----");
		for (int i = 0; i < sorted.length; i++) {
			System.out.println(i + ": " + sorted[i]);
		}
	}
}
