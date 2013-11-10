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
 * CcnRoutingGeneration.java
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
package gtna.projects.ccnRouting;

import gtna.graph.Graph;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.WattsStrogatz;

/**
 * @author benni
 * 
 */
public class CcnRoutingGeneration {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		generate(new ErdosRenyi(100, 2, true, null), "er-100-2");
		generate(new ErdosRenyi(100, 5, true, null), "er-100-5");
		generate(new ErdosRenyi(100, 10, true, null), "er-100-10");
		generate(new WattsStrogatz(100, 2, 0.5, null), "ws-100-2");
		generate(new WattsStrogatz(100, 5, 0.5, null), "ws-100-5");
		generate(new WattsStrogatz(100, 10, 0.5, null), "ws-100-10");
	}

	public static void generate(Network nw, String name) {
		Graph g = nw.generate();
		GraphWriter w = new GtnaGraphWriter();
		w.write(g, CcnRoutingSetup.graphFolder + name + ".gtna");
	}

}
