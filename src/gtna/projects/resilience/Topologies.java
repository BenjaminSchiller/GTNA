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
 * Topologies.java
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
package gtna.projects.resilience;

import gtna.graph.Graph;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;

/**
 * @author benni
 * 
 */
public class Topologies {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		double d_avg = 4.3;
		// caida: 12k - 20k

		int[] nodes = new int[] { 500, 1000, 2000, 4000, 6000, 8000, 10000,
				12000, 14000, 16000, 18000, 20000 };
		for (int node : nodes) {
			Network er1 = new ErdosRenyi(node, d_avg, true, null);
			Network er2 = new ErdosRenyi(node, d_avg, false, null);
			Network pl1 = new PowerLawRandomGraph(node, 2.3, 1,
					Integer.MAX_VALUE, false, null);
			Network pl2 = new PowerLawRandomGraph(node, 2.3, 1,
					Integer.MAX_VALUE, true, null);
			Network[] nw = new Network[] { er1, er2, pl1, pl2 };
			for (Network network : nw) {
				Graph g = network.generate();
				(new GtnaGraphWriter()).write(g,
						"temp/_networks/" + network.getFolderName() + ".gtna");
			}
		}
	}
}
