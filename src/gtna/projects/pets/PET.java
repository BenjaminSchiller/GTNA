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
package gtna.projects.pets;

import gtna.networks.Network;
import gtna.networks.model.smallWorld.Kleinberg1D;
import gtna.networks.model.smallWorld.KleinbergPowerLaw;
import gtna.networks.model.smallWorld.ScaleFreeDirectedSD;
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

	private static final boolean scaleFree = true;
	private static final boolean bidirectional = false;

	private static final String name = (PET.scaleFree ? "kleinberg-"
			: "kleinberg1-")
			+ (PET.bidirectional ? "bidirectional" : "unidirectional");

	private static final String data = "./data/" + PET.name + "/";
	private static final String graphs = PET.data + "graphs/";
	private static final String graphsLD = PET.data + "graphs_ld/";
	private static final String plots = "./plots/" + PET.name + "/";

	private static final String metrics = "DEGREE_DISTRIBUTION, ROUTING";

	public static boolean whatToDo = false;
	public static boolean checkGraphs = false;

	public static boolean generateGraphs = false;
	public static boolean generateData = false;

	public static boolean plotMulti = false;
	public static boolean plotSingle = true;

	public static boolean diff = false;

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

		Nodes = new int[] { 1, 5, 10, 50, 100, 200, 300, 400, 500 };
		Nodes = new int[] { 100 };

		HashMap<Integer, Integer> Times = new HashMap<Integer, Integer>();
		int k = 1000;
		Times.put(1 * k, 100);
		Times.put(5 * k, 100);
		Times.put(10 * k, 100);
		Times.put(50 * k, 100);
		Times.put(100 * k, 100);
		Times.put(200 * k, 12);
		Times.put(300 * k, 12);
		Times.put(400 * k, 12);
		Times.put(500 * k, 12);

		if (!PET.scaleFree) {
			Alpha = new double[] { 0.0 };
		}
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
				// PETPlot.generatePlotsMulti(Nodes, Alpha, C, R, type);
				PETPlot.multiAlpha(Nodes, Alpha, C, R, type);
				PETPlot.multiC(Nodes, Alpha, C, R, type);
			}
			if (PET.plotSingle) {
				PETPlot.single_alpha_c(Nodes, Alpha, C, R, type);
				PETPlot.single_c_alpha(Nodes, Alpha, C, R, type);
				PETPlot.single_nodes_alpha(Nodes, Alpha, C, R, type);
				PETPlot.single_nodes_c(Nodes, Alpha, C, R, type);
			}
			if (PET.diff || PET.plotMulti || PET.plotSingle) {
				String[] singleKeys = new String[] { "ROUTING_HOPS_AVG",
						"ROUTING_HOPS_MED", "ROUTING_HOPS_MAX" };
				for (String singleKey : singleKeys) {
					PETComputation.diff(Nodes, Alpha, C, R, type, singleKey);
				}
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
		if (PET.scaleFree) {
			return new KleinbergPowerLaw(n, 0, alpha, PET.cutoff(n, type),
					PET.bidirectional, false, null, null);
		} else {
			return new Kleinberg1D(n, 0, 1, 1, PET.bidirectional, false, null);
		}
	}

	public static Network getSD(int n, double alpha, cutoffType type, int c) {
		return PET.getSDR(n, alpha, type, c, null);
	}

	public static Network getSDR(int n, double alpha, cutoffType type, int c,
			RoutingAlgorithm r) {
		Transformation[] t = new Transformation[] { PET.getSDTransformation(c) };
		if (PET.scaleFree) {
			return new KleinbergPowerLaw(n, 0, alpha, PET.cutoff(n, type),
					PET.bidirectional, false, r, t);
		} else {
			return new Kleinberg1D(n, 0, 1, 1, PET.bidirectional, false, t);
		}
	}

	private static Transformation getSDTransformation(int c) {
		if (PET.bidirectional) {
			return new ScaleFreeUndirectedSD(c);
		} else {
			return new ScaleFreeDirectedSD(c);
		}
	}

	public static double getAlpha(Network nw) {
		if (PET.scaleFree) {
			return ((KleinbergPowerLaw) nw).getAlpha();
		} else {
			return 0;
		}
	}

	public static String graphFolder(Network nw) {
		return PET.graphs + nw.getNodes() + "/" + nw.getFolder();
	}

	public static String graphFilename(Network nw, int i) {
		return PET.graphFolder(nw) + i + ".txt";
	}

	public static String idSpaceFilename(Network nw) {
		return PET.graphs + nw.getNodes() + "/ID_SPACE_0";
	}

	public static String graphLDFolder(Network nw) {
		return PET.graphsLD + nw.getNodes() + "/" + nw.getFolder();
	}

	public static String graphLDFilename(Network nw, int i) {
		return PET.graphLDFolder(nw) + i + ".txt";
	}

	public static String idSpaceLDFilename(Network nw) {
		return PET.graphsLD + nw.getNodes() + "/ID_SPACE_0";
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

		Config.overwrite("EXECUTE_TRANSFORMATIONS", "false");
	}

}
