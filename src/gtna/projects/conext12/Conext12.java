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
 * Conext12.java
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
package gtna.projects.conext12;

import gtna.communities.Role;
import gtna.data.Series;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.GuimeraRolesNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.graph.sorting.WsnRolesNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.communities.Communities;
import gtna.metrics.communities.Roles;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.GuimeraRolesTransformation;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Conext12 {

	public static final boolean GENERATE = false;

	public static final boolean PLOT = true;

	public static int counter = 0;
	public static int mod = 1;
	public static int index = 0;

	public static int times = 100;

	public static int[] nodes = new int[] { 1000, 2000, 3000, 4000, 5000, 6000,
			7000, 8000, 9000, 10000 };

	public static int[][] NAC = new int[][] { new int[] { 1, 3 },
			new int[] { 2, 3 }, new int[] { 2, 4 }, new int[] { 3, 4 } };

	public static double z = 1.0;

	public static void main(String[] args) {

		if (args.length == 2) {
			mod = Integer.parseInt(args[0]);
			index = Integer.parseInt(args[1]);
		}

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("GNUPLOT_TERMINAL", "png");
		Config.overwrite("PLOT_EXTENSION", ".png");
		Config.overwrite("MAIN_DATA_FOLDER", "data/conext12/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/conext12/");

		/*
		 * BASIC
		 */
		Metric[] m_basic = Conext12.getBasicMetrics();

		Network[] nw_guimera_hs = Conext12.getGuimeraNetworks("hotspot");
		Network[] nw_guimera_r = Conext12.getGuimeraNetworks("random");

		if (GENERATE) {
			Conext12.generate(nw_guimera_hs, m_basic);
			Conext12.generate(nw_guimera_r, m_basic);
		}

		if (PLOT) {
			Series[] s_guimera_hs = Series.get(nw_guimera_hs, m_basic);
			Series[] s_guimera_r = Series.get(nw_guimera_r, m_basic);
			Series[][] s_guimera = new Series[][] { s_guimera_hs, s_guimera_r };

			// Plotting.multi(s_guimera_hs, m_basic, "basic-hotspot/");
			// Plotting.multi(s_guimera_r, m_basic, "basic-random/");
			// Plotting.single(s_guimera, m_basic, "basic-single/");
		}

		/*
		 * FRAGMENTATION
		 */
		Metric[] m_fragmentation = Conext12.getFragmentationsMetrics();
		Network[][] nw_wsn_hs = Conext12.getWsnNetworks("hotspot", true);
		Network[][] nw_wsn_r = Conext12.getWsnNetworks("random", true);

		if (GENERATE) {
			Conext12.generate(nw_wsn_hs, m_fragmentation);
			Conext12.generate(nw_wsn_r, m_fragmentation);
		}

		if (PLOT) {
			for (int n : nodes) {
				Network[] nw_hs = Conext12
						.getWsnNetworks("hotspot", n, z, true);
				Network[] nw_r = Conext12.getWsnNetworks("random", n, z, true);
				Series[] s_hs = Series.get(nw_hs, m_fragmentation);
				Series[] s_r = Series.get(nw_r, m_fragmentation);

				// Plotting.multi(s_hs, m_fragmentation,
				// "fragmentation-hotspot-"
				// + n + "/");
				// Plotting.multi(s_r, m_fragmentation, "fragmentation-random-"
				// + n + "/");
			}

			Series[][] s_wsn_hs = Series.get(nw_wsn_hs, m_fragmentation);
			Series[][] s_wsn_r = Series.get(nw_wsn_r, m_fragmentation);
			// Plotting.single(s_wsn_hs, m_fragmentation,
			// "fragmentation-hotspot-s/");
			// Plotting.single(s_wsn_r, m_fragmentation,
			// "fragmentation-random-s/");

			for (Series[] s : s_wsn_hs) {
				Plotting.single(s, m_fragmentation, "fragmentation-hotspot-s-"
						+ s[0].getNetwork().getFolderName() + "/");
			}
			for (Series[] s : s_wsn_r) {
				 Plotting.single(s, m_fragmentation, "fragmentation-random-s-"
				 + s[0].getNetwork().getFolderName() + "/");
			}

			Metric[] m_roles = new Metric[] { new Roles(Role.RoleType.WSN) };
			// Plotting.multi(s_wsn_hs, m_roles, "roles-hotspot/");
			// Plotting.multi(s_wsn_r, m_roles, "roles-random/");
		}
	}

	/*
	 * SERIES GENERATION
	 */

	private static void generate(Network[][] NWs, Metric[] metrics) {
		for (Network[] NW : NWs) {
			Conext12.generate(NW, metrics);
		}
	}

	private static void generate(Network[] NW, Metric[] metrics) {
		for (Network nw : NW) {
			Conext12.generate(nw, metrics);
		}
	}

	private static void generate(Network nw, Metric[] metrics) {
		if ((counter % mod) == index) {
			System.out.println("GENERATING " + nw.getDescription());
			Series.generate(nw, metrics, times);
		}
		counter++;
	}

	/*
	 * METRICS
	 */

	private static Metric[] getBasicMetrics() {
		NodeSorter[] ns = Conext12.getGuimeraNodeSorters();
		Metric[] m_fragmentation = Conext12.getFragmentationMetrics(ns);

		Metric[] metrics = new Metric[m_fragmentation.length + 3];
		metrics[0] = new DegreeDistribution();
		metrics[1] = new Communities();
		metrics[2] = new Roles(Role.RoleType.GUIMERA);
		for (int i = 0; i < m_fragmentation.length; i++) {
			metrics[i + 3] = m_fragmentation[i];
		}

		return metrics;
	}

	private static Metric[] getFragmentationsMetrics() {
		NodeSorter[] ns = Conext12.getWsnNodeSorters();
		Metric[] m_fragmentation = Conext12.getFragmentationMetrics(ns);

		Metric[] metrics = new Metric[m_fragmentation.length + 1];
		metrics[0] = new Roles(Role.RoleType.WSN);
		for (int i = 0; i < m_fragmentation.length; i++) {
			metrics[i + 1] = m_fragmentation[i];
		}

		return metrics;
	}

	/*
	 * WSN NETWORK
	 */

	private static Network[][] getWsnNetworks(String type, boolean hubs) {
		Network[][] nw = new Network[nodes.length][];
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < NAC.length; j++) {
				nw[i] = Conext12.getWsnNetworks(type, nodes[i], z, hubs);
			}
		}

		Network[][] nw2 = new Network[nw[0].length][nw.length];
		for (int i = 0; i < nw.length; i++) {
			for (int j = 0; j < nw[i].length; j++) {
				nw2[j][i] = nw[i][j];
			}
		}
		return nw2;
	}

	private static Network[] getWsnNetworks(String type, int nodes, double z,
			boolean hubs) {
		Network[] nw = new Network[NAC.length];
		for (int i = 0; i < NAC.length; i++) {
			nw[i] = Conext12.getWsnNetwork(type, nodes, z, NAC[i], hubs);
		}
		return nw;
	}

	private static Network getWsnNetwork(String type, int nodes, double z,
			int[] nac, boolean hubs) {
		Transformation[] t = new Transformation[] { new WsnRolesTransformation(
				z, nac[0], nac[1], hubs) };
		Network nw = Conext12.getNetwork(type, nodes, t);
		return new DescriptionWrapper(nw, type + "-" + nodes + " (" + z + " "
				+ nac[0] + " " + nac[1] + " " + hubs + ")");
	}

	/*
	 * GUIMERA NETWORK
	 */

	private static Network[] getGuimeraNetworks(String type) {
		Network[] nw = new Network[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw[i] = Conext12.getGuimeraNetwork(type, nodes[i]);
		}
		return nw;
	}

	private static Network getGuimeraNetwork(String type, int nodes) {
		Transformation[] t = new Transformation[] { new GuimeraRolesTransformation() };
		Network nw = Conext12.getNetwork(type, nodes, t);
		return new DescriptionWrapper(nw, type + "-" + nodes + " (G)");
	}

	/*
	 * NETWORK
	 */

	public static final String SOURCE_DATA_FOLDER = "data/res/";

	// public static final String SOURCE_DATA_FOLDER =
	// "/home/benni/results-florian-transformed/";

	public static Network getNetwork(String type, int nodes, Transformation[] t) {
		String srcFolder = SOURCE_DATA_FOLDER + type + "/" + nodes + "/";
		return new ReadableFolder(type, type, srcFolder, ".gtna", t);
	}

	/*
	 * SORTING & FRAGMENTATION METRICS
	 */

	public static NodeSorter[] getWsnNodeSorters() {
		NodeSorter s1 = new DegreeNodeSorter(NodeSorterMode.DESC);
		NodeSorter s2 = new RandomNodeSorter();

		NodeSorter s3 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.HS_HB_HC_S_B_C);
		NodeSorter s4 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.HS_HB_S_B_HC_C);
		NodeSorter s5 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.HS_S_HB_B_HC_C);
		NodeSorter s6 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.S_B_HS_HB_HC_C);
		NodeSorter s7 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.S_HS_B_HB_HC_C);

		return new NodeSorter[] { s1, s2, s3, s4, s5, s6, s7 };
	}

	public static NodeSorter[] getGuimeraNodeSorters() {
		NodeSorter s1 = new DegreeNodeSorter(NodeSorterMode.DESC);
		NodeSorter s2 = new RandomNodeSorter();

		GuimeraRolesNodeSorter s3 = new GuimeraRolesNodeSorter(
				GuimeraRolesNodeSorter.P_SC_UP);
		GuimeraRolesNodeSorter s4 = new GuimeraRolesNodeSorter(
				GuimeraRolesNodeSorter.P_UP_SC);
		GuimeraRolesNodeSorter s5 = new GuimeraRolesNodeSorter(
				GuimeraRolesNodeSorter.SC_P_UP);
		GuimeraRolesNodeSorter s6 = new GuimeraRolesNodeSorter(
				GuimeraRolesNodeSorter.SC_UP_P);
		GuimeraRolesNodeSorter s7 = new GuimeraRolesNodeSorter(
				GuimeraRolesNodeSorter.UP_P_SC);
		GuimeraRolesNodeSorter s8 = new GuimeraRolesNodeSorter(
				GuimeraRolesNodeSorter.UP_SC_P);

		return new NodeSorter[] { s1, s2, s3, s4, s5, s6, s7, s8 };
	}

	public static Metric[] getFragmentationMetrics(NodeSorter[] sorters) {
		Metric[] m = new Metric[sorters.length];
		for (int i = 0; i < sorters.length; i++) {
			m[i] = new WeakFragmentation(sorters[i],
					Fragmentation.Resolution.PERCENT);
		}

		return m;
	}

}
