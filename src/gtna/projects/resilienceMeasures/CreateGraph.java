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
 * CreateGraph.java
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

import gtna.graph.Graph;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.GLP;
import gtna.networks.model.IG;
import gtna.networks.model.PFP;

/**
 * @author truong
 * 
 */
public class CreateGraph {
	public static void main(String[] args) {
		/**
		 * args[0] = 0: BarabasiAlbert 1: IG 2: PFP 3: GLP
		 * 
		 * args[1] = Number of nodes
		 * 
		 * args[2] = Number of network to create
		 * 
		 * args[3] = Name of file
		 */
		int nw = Integer.parseInt(args[0]);
		int N = Integer.parseInt(args[1]);
		int create = Integer.parseInt(args[2]);

		Network network;
		switch (nw) {
		case 0:
			network = new BarabasiAlbert(N, 2, null);
			break;
		case 1:
			network = new IG(N, 10, 0.4, null);
			break;
		case 2:
			network = new PFP(N, 10, 0.3, 0.1, 0.020846, null);
			break;
		case 3:
			network = new GLP(N, 10, 1.13, 0.4695, 0.6447, null);
			break;
		default:
			System.out
					.println("Error: network parameter can only be 0, 1, 2 oder 3");
			System.out
					.println("0: BarabasiAlbert -- 1: IG -- 2: PFP -- 3: GLP");
			return;
		}
		for (int i = 0; i < create; i++) {
			Graph graph = network.generate();
			String fileName = "" + args[3] + "_" + intToString(i, 3) + ".gtna";
			(new GtnaGraphWriter()).write(graph, fileName);
		}
		System.out.println("Done!");
	}

	private static String intToString(int n, int length) {
		String result = String.valueOf(n);
		while (result.length() < length) {
			result = "0" + result;
		}
		return result;
	}
}
