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
 * Models.java
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
package gtna.projects.resilienceSampling;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.Regular;
import gtna.networks.model.WattsStrogatz;
import gtna.networks.model.randomGraphs.PowerLawRandomGraph;
import gtna.networks.model.smallWorld.Kleinberg1D;
import gtna.plot.Plotting;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Models {

	public static final int times = 1;

	public static final int nodes = 50000;

	public static int[] edges = new int[] { 1, 2, 3, 4, 5, 10, 20, 50 };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER",
				"/Users/benni/Downloads/criticalPoints/_models_data/" + nodes
						+ "/");
		Config.overwrite("MAIN_PLOT_FOLDER",
				"/Users/benni/Downloads/criticalPoints/_models_plots/" + nodes
						+ "/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		ws(0.1);
		ws(0.5);
		ws(1.0);
		ba();
		er(true);
		er(false);
		pl(true);
		pl(false);
		reg(true, true);
		reg(true, false);
		// reg(false, true);
		// reg(false, false);
	}

	public static void reg(boolean ring, boolean undirected) {
		Network[] nw = new Network[edges.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new Regular(nodes, edges[i], ring, undirected, null);
		}
		directed(nw, "reg-" + ring + "-" + undirected);
	}

	public static void pl(boolean directed) {
		double[] alpha = new double[] { 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8,
				2.9 };
		Network[] nw = new Network[alpha.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new PowerLawRandomGraph(nodes, alpha[i], 1, 500, directed,
					null);
		}
		directed(nw, "pl-" + directed);
	}

	public static void ws(double alpha) {
		Network[] nw = new Network[edges.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new WattsStrogatz(nodes, edges[i], alpha, null);
		}
		directed(nw, "ws-" + alpha);
	}

	public static void ba() {
		Network[] nw = new Network[edges.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new BarabasiAlbert(nodes, edges[i], null);
		}
		directed(nw, "ba");
	}

	public static void er(boolean undirected) {
		Network[] nw = new Network[edges.length];
		for (int i = 0; i < nw.length; i++) {
			nw[i] = new ErdosRenyi(nodes, edges[i], undirected, null);
		}
		if (undirected) {
			undirected(nw, "er-u");
		} else {
			directed(nw, "er-d");
		}
	}

	public static void directed(Network[] nw, String name) {
		Series[] s = Series.generate(nw, Analysis.m_d, times);
		Plotting.multi(s, Analysis.m_d, name + "-multi/");
		Plotting.single(s, Analysis.m_d, name + "-single/");
	}

	public static void undirected(Network[] nw, String name) {
		Series[] s = Series.generate(nw, Analysis.m_u, times);
		Plotting.multi(s, Analysis.m_u, name + "/");
		Plotting.single(s, Analysis.m_u, name + "-single/");
	}

}
