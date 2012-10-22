/*
 * ===========================================================
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
 * RichClubConnectivity.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna.metrics.connectivity;

import gtna.data.Single;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.sorting.NodeSorting;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;

import java.util.HashMap;
import java.util.Random;

public class RichClubConnectivity extends Metric {
	private double[] rcc;

	public RichClubConnectivity() {
		super("RICH_CLUB_CONNECTIVITY");
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		int[] order = NodeSorting.byDegreeDesc(g.getNodes(), new Random());
		Edges edges = g.getEdges();
		int edgeCount = 0;
		this.rcc = new double[order.length + 1];
		for (int p = 2; p <= order.length; p++) {
			int newNode = order[p - 1];
			for (int i = 0; i < p - 1; i++) {
				int old = order[i];
				if (edges.contains(newNode, old)) {
					edgeCount++;
				}
				if (edges.contains(old, newNode)) {
					edgeCount++;
				}
			}
			this.rcc[p] = (double) edgeCount / (double) (p * (p - 1));
		}
	}

	public Single[] getSingles() {
		return new Single[0];
	}

	public boolean writeData(String folder) {
		DataWriter.writeWithIndex(this.rcc,
				"RICH_CLUB_CONNECTIVITY_RICH_CLUB_CONNECTIVITY", folder);
		return true;
	}

}