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
 * RemoveLargestNode.java
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
package gtna.transformation.remove;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.util.parameter.Parameter;

/**
 * @author benni
 * 
 */
public class RemoveLargestNode extends RemoveNodes {
	public RemoveLargestNode() {
		super("REMOVE_LARGEST_NODE", new Parameter[0]);
	}

	@Override
	public boolean[] getNodeSet(Graph g) {
		Node[] nodes = g.getNodes();
		int maxIndex = 0;
		for (Node node : nodes) {
			if (node.getDegree() > g.getNode(maxIndex).getDegree()) {
				maxIndex = node.getIndex();
			}
		}

		boolean[] remove = new boolean[nodes.length];
		remove[maxIndex] = true;

		return remove;
	}
}
