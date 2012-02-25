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
 * DegreeDescNodeSorter.java
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
package gtna.graph.sorting;

import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class InDegreeNodeSorter extends NodeSorter {
	public InDegreeNodeSorter(NodeSorterMode mode) {
		super("IN_DEGREE", mode);
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	@Override
	public Node[] sort(Graph g, Random rand) {
		Node[] sorted = this.clone(g.getNodes());
		Arrays.sort(sorted, new InDegreeAsc());
		this.randomize(sorted, rand);
		if (this.mode == NodeSorterMode.DESC) {
			sorted = this.reverse(sorted);
		}
		return sorted;
	}

	private class InDegreeAsc implements Comparator<Node> {
		public int compare(Node n1, Node n2) {
			return n1.getInDegree() - n2.getInDegree();
		}
	}

	@Override
	public boolean isPropertyEqual(Node n1, Node n2) {
		return n1.getInDegree() == n2.getInDegree();
	}

}
