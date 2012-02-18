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
 * PET.java
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
package gtna.projects;

import gtna.networks.Network;
import gtna.networks.model.smallWorld.KleinbergPowerLaw;
import gtna.networks.model.smallWorld.ScaleFreeUndirectedSD;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.routing.greedyVariations.OneWorseGreedy;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Stats;

import java.util.Date;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class PET {

	public static enum cutoffType {
		N, LOG_SQ_N, SQRT_N
	};

	public static long waitTime = 1000;

	private static String graphs = "./data/graphs/";
	private static String graphsLD = "./data/graphs_ld/";
	private static String data = "./data/";
	private static String plots = "./plots/";

	private static final String metrics = "ROUTING";

	public static boolean whatToDo = false;
	public static boolean checkGraphs = false;

	public static boolean generateGraphs = false;
	public static boolean generateData = true;
	public static boolean plotMulti = false;
	public static boolean plotSingle = false;
	public static boolean plotSingleCombined = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PET.config();

		int[] Nodes = new int[] { 1, 5, 10, 50, 100, 200, 300, 400, 500 };
		double[] Alpha = new double[] { 2.2, 2.3, 2.4 };
		int[] C = new int[] { 1, 2, 5, 10, 20, 50 };
		RoutingAlgorithm[] R = PET.getRA();
		cutoffType type = cutoffType.SQRT_N;

		Nodes = new int[] { 50 };

		HashMap<Integer, Integer> Times = new HashMap<Integer, Integer>();
		int k = 1000;
		Times.put(1 * k, 100);
		Times.put(5 * k, 100);
		Times.put(10 * k, 100);
		Times.put(50 * k, 20);
		Times.put(100 * k, 0);
		Times.put(200 * k, 6);
		Times.put(300 * k, 6);
		Times.put(400 * k, 6);
		Times.put(500 * k, 6);

		for (int i = 0; i < Nodes.length; i++) {
			Nodes[i] *= 1000;
		}

		System.out.println("STARTED - " + new Date(System.currentTimeMillis())
				+ "\n");
		Stats stats = new Stats();

		if (PET.whatToDo) {
			PETTest.whatToDo(Nodes, Alpha, C, R, type, Times);
		} else if (PET.checkGraphs) {
			PETTest.checkGraphs(Nodes, Alpha, C, type, Times);
		} else {
			int threads = 1;
			int offset = 0;
			if (args.length > 1) {
				threads = Integer.parseInt(args[0]);
				offset = Integer.parseInt(args[1]);
			}
			if (PET.generateGraphs) {
				PETGraphs.generateGraphs(Times, Nodes, Alpha, C, type, threads,
						offset);
			}
			if (PET.generateData) {
				PETData.generateData(Times, Nodes, Alpha, C, type, threads,
						offset);
			}
			if (PET.plotMulti) {
				PETPlot.generatePlotsMulti(Nodes, Alpha, C, R, type);
			}
			if (PET.plotSingle) {
				PETPlot.generatePlotsSingle(Nodes, Alpha, C, R, type);
			}
			if (PET.plotSingleCombined) {
				PETPlot.generatePlotsSingleCombined(Nodes, Alpha, C, R, type);
			}
		}
		stats.end();
	}

	public static RoutingAlgorithm[] getRA() {
		RoutingAlgorithm owg = new OneWorseGreedy();
		RoutingAlgorithm dfg = new DepthFirstGreedy();
		return new RoutingAlgorithm[] { owg, dfg };
	}

	public static Network getLD(int n, double alpha, cutoffType type) {
		return new KleinbergPowerLaw(n, 0, alpha, PET.cutoff(n, type), true,
				false, null, null);
	}

	public static Network getSD(int n, double alpha, cutoffType type, int c) {
		return PET.getSDR(n, alpha, type, c, null);
	}

	public static Network getSDR(int n, double alpha, cutoffType type, int c,
			RoutingAlgorithm r) {
		return new KleinbergPowerLaw(n, 0, alpha, PET.cutoff(n, type), true,
				false, r, new Transformation[] { new ScaleFreeUndirectedSD(c) });
	}

	public static String graphFolder(Network nw) {
		return PET.graphs + nw.nodes() + "/" + nw.folder();
	}

	public static String graphFilename(Network nw, int i) {
		return PET.graphFolder(nw) + i + ".txt";
	}

	public static String idSpaceFilename(Network nw) {
		return PET.graphs + nw.nodes() + "/ID_SPACE_0";
	}

	public static String graphLDFolder(Network nw) {
		return PET.graphsLD + nw.nodes() + "/" + nw.folder();
	}

	public static String graphLDFilename(Network nw, int i) {
		return PET.graphLDFolder(nw) + i + ".txt";
	}

	public static String idSpaceLDFilename(Network nw) {
		return PET.graphsLD + nw.nodes() + "/ID_SPACE_0";
	}

	public static int cutoff(int N, cutoffType type) {
		if (type == cutoffType.N) {
			return N;
		} else if (type == cutoffType.LOG_SQ_N) {
			double log = Math.ceil(Math.log(N) / Math.log(2.0));
			return (int) (log * log);
		} else if (type == cutoffType.SQRT_N) {
			return (int) Math.ceil(Math.sqrt(N));
		} else {
			return -1;
		}
	}

	private static void config() {
		Config.overwrite("MAIN_DATA_FOLDER", PET.data);
		Config.overwrite("MAIN_PLOT_FOLDER", PET.plots);
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("METRICS", PET.metrics);

		Config.overwrite("ROUTING_SINGLES_PLOTS",
				"ROUTING_HOPS_AVG, ROUTING_HOPS_MED, "
						+ "ROUTING_HOPS_MAX, ROUTING_RUNTIME");

		Config.overwrite("ROUTING_HOP_DISTRIBUTION_PLOT_LOGSCALE_X", "true");
		Config.overwrite("ROUTING_HOP_DISTRIBUTION_CDF_PLOT_LOGSCALE_X", "true");
		Config.overwrite("ROUTING_HOP_DISTRIBUTION_ABSOLUTE_PLOT_LOGSCALE_X",
				"true");
		Config.overwrite(
				"ROUTING_HOP_DISTRIBUTION_ABSOLUTE_CDF_PLOT_LOGSCALE_X", "true");
		Config.overwrite("ROUTING_BETWEENNESS_CENTRALITY_PLOT_LOGSCALE_X",
				"true");
	}

}
