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
 * EigenVector.java
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
package gtna.graph.sorting.algorithms;

import java.util.HashMap;

import gtna.graph.Graph;
import gtna.graph.Node;

/**
 * @author truong
 * 
 */
public class EigenVector {
	private Graph g;
	private int numRuns = 1000;

	// private double sumChange;

	public void setNumRuns(int numRuns) {
		this.numRuns = numRuns;
	}

	public void execute() {
		int N = g.getNodes().length;
		double[] tmp = new double[N];
		double[] centralities = new double[N];

		int count = 0;

		HashMap<Integer, Node> indicies = new HashMap<Integer, Node>();
		HashMap<Node, Integer> invIndices = new HashMap<Node, Integer>();
		for (Node u : g.getNodes()) {
			indicies.put(count, u);
			invIndices.put(u, count);
			centralities[count] = 1;
			count++;
		}

		for (int s = 0; s < numRuns; s++) {
			double max = 0;
			for (int i = 0; i < N; i++) {
				Node u = indicies.get(i);
				for (int id : u.getOutgoingEdges()) {
					tmp[i] += centralities[id];
				}
				max = Math.max(max, tmp[i]);
			}
			// sumChange = 0;
			for (int k = 0; k < N; k++) {
				if (max != 0) {
					// sumChange += Math.abs(centralities[k] - (tmp[k] / max));
					centralities[k] = tmp[k] / max;
				}
			}
		}
	}
}
