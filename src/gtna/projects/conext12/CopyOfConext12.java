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
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.GuimeraRolesTransformation;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.util.Config;
import gtna.util.Timer;
import gtna.util.Util;

/**
 * @author benni
 * 
 */
public class CopyOfConext12 {

	public static final boolean GENERATE_SERIES = false;

	public static final boolean PLOT = true;

	public static int counter = 0;
	public static int mod = 1;
	public static int index = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length == 2) {
			mod = Integer.parseInt(args[0]);
			index = Integer.parseInt(args[1]);
		}

		int[] nodes = new int[] { 1000, 2000, 3000, 4000, 5000, 6000, 7000,
				8000, 9000, 10000 };
		int times = 35;

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("GNUPLOT_TERMINAL", "png");
		Config.overwrite("PLOT_EXTENSION", ".png");

		CopyOfConext12.basic(nodes, times);
		CopyOfConext12.fragmentation(nodes, times, 0.5, 1, 2, false);

		int[][] NAC = new int[][] { new int[] { 1, 2 }, new int[] { 1, 3 },
				new int[] { 1, 4 }, new int[] { 1, 5 }, new int[] { 2, 3 },
				new int[] { 2, 4 }, new int[] { 2, 5 }, new int[] { 3, 4 },
				new int[] { 3, 5 }, new int[] { 4, 5 } };
		// double[] Z = new double[] { 0.5, 0.75, 1.0, 1.25, 1.5 };
		double[] Z = new double[] { 0.5, 1.0, 1.5 };

		for (int[] nac : NAC) {
			for (double z : Z) {
				CopyOfConext12.fragmentation(nodes, times, z, nac[0], nac[1], true);
			}
		}
	}
	
	public static void plotFragmentation(double[] Z, int[][] NAC, boolean hubs){
		Config.overwrite("MAIN_DATA_FOLDER", "data/conext12-fragmentation/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/conext12-fragmentation/");
		
		Metric[] metrics = CopyOfConext12.getFragmentationMetrics();
		
		
	}
	
	private static Metric[] getFragmentationMetrics(){
		NodeSorter[] nodeSorters = CopyOfConext12.getWsnNodeSorters();
		Metric[] m_fragmentation = CopyOfConext12
				.getFragmentationMetrics(nodeSorters);

		Metric[] metrics = new Metric[m_fragmentation.length + 1];
		metrics[0] = new Roles(Role.RoleType.WSN);
		for (int i = 0; i < m_fragmentation.length; i++) {
			metrics[i + 1] = m_fragmentation[i];
		}
		
		return metrics;
	}

	public static void fragmentation(int[] nodes, int times, double z,
			int commonNAC, int bridgeNAC, boolean hubs) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/conext12-fragmentation/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/conext12-fragmentation-"
				+ z + "-" + commonNAC + "-" + bridgeNAC + "-" + hubs + "/");

		Metric[] metrics = CopyOfConext12.getFragmentationMetrics();

		Transformation[] t = new Transformation[] { new WsnRolesTransformation(
				z, commonNAC, bridgeNAC, hubs) };

		CopyOfConext12.generate(nodes, metrics, t, times);
	}

	public static void basic(int[] nodes, int times) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/conext12-basic/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/conext12-basic/");

		NodeSorter[] nodeSorters = CopyOfConext12.getGuimeraNodeSorters();
		Metric[] m_fragmentation = CopyOfConext12
				.getFragmentationMetrics(nodeSorters);

		Metric[] metrics = new Metric[m_fragmentation.length + 3];
		metrics[0] = new DegreeDistribution();
		metrics[1] = new Communities();
		metrics[2] = new Roles(Role.RoleType.GUIMERA);
		for (int i = 0; i < m_fragmentation.length; i++) {
			metrics[i + 3] = m_fragmentation[i];
		}

		Transformation[] t = new Transformation[] { new GuimeraRolesTransformation() };

		CopyOfConext12.generate(nodes, metrics, t, times);
	}

	public static void generate(int[] nodes, Metric[] metrics,
			Transformation[] t, int times) {
		Network[] nw_r = CopyOfConext12.getNetwork("random", nodes, t);
		Network[] nw_hs = CopyOfConext12.getNetwork("hotspot", nodes, t);
		Network[] nw = Util.combine(nw_r, nw_hs);

		if (GENERATE_SERIES) {
			Timer timer = new Timer();
			for (Network NW : nw) {
				if ((counter % mod) == index) {
					System.out.println("GENERATING " + NW.getDescription());
					Series.generate(NW, metrics, times);
				}
				counter++;
			}
			timer.end("SERIES GENERATION");
		}

		if (!PLOT) {
			return;
		}

		Timer timer = new Timer();

		Series[] s_r = Series.get(nw_r, metrics);
		Series[] s_hs = Series.get(nw_hs, metrics);
		Series[] s_all = Series.get(nw, metrics);

		Plotting.multi(s_r, metrics, "multi-random/");
		Plotting.multi(s_hs, metrics, "multi-hotspot/");
		Plotting.multi(s_all, metrics, "multi-combined/");

		Plotting.single(new Series[][] { s_r, s_hs }, metrics, "singles/");

		for (int n : nodes) {
			Series[] s_r_ = Series.get(
					CopyOfConext12.getNetwork("random", new int[] { n }, t), metrics);
			Series[] s_hs_ = Series
					.get(CopyOfConext12.getNetwork("hotspot", new int[] { n }, t),
							metrics);
			Series[] s_all_ = Util.combine(s_r_, s_hs_);

			Plotting.multi(s_r, metrics, "multi-random-" + n + "/");
			Plotting.multi(s_hs, metrics, "multi-hotspot-" + n + "/");
			Plotting.multi(s_all_, metrics, "multi-combined-" + n + "/");
		}

		timer.end("PLOTTING");
	}

	// public static final String DATA_FOLDER = "resources/results/";
	public static final String DATA_FOLDER = "/home/benni/results-florian-transformed/";

	public static Network[] getNetwork(String type, int[] nodes,
			Transformation[] t) {
		Network[] nw = new Network[nodes.length];

		for (int i = 0; i < nodes.length; i++) {
			String srcFolder = DATA_FOLDER + type + "/" + nodes[i] + "/";
			nw[i] = new ReadableFolder(type, type, srcFolder, ".gtna", t);
		}

		return nw;
	}

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
