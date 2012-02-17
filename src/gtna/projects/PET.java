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
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.networks.model.smallWorld.ScaleFreeUndirectedLD;
import gtna.networks.model.smallWorld.ScaleFreeUndirectedMinLD;
import gtna.networks.model.smallWorld.ScaleFreeUndirectedSD;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.routing.greedyVariations.OneWorseGreedy;
import gtna.routing.greedyVariations.RestrictedDFE;
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

	public static long waitTime = 60000;

	public static String graphs = "./data/graphs3/";
	public static String graphsLD = "./data/graphs3_ld/";
	public static String data = "./data/PET3/";
	public static String plots = "./plots/PET3/";

	public static boolean minLD = false;
	public static boolean newModel = true;

	public static boolean whatToDo = false;
	public static boolean checkGraphs = false;

	public static boolean generateGraphs = false;
	public static boolean generateData = false;
	public static boolean plotMulti = false;
	public static boolean plotSingle = false;
	public static boolean plotSingleCombined = true;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", PET.data);
		Config.overwrite("MAIN_PLOT_FOLDER", PET.plots);
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, ROUTING");
		Config.overwrite("METRICS", "ROUTING");
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

		int[] Nodes = new int[] { 1000, 5000, 10000, 50000, 100000 };
		double[] Alpha = new double[] { 2.0, 2.2, 2.4, 2.6, 2.8, 3.0 };
		int[] C = new int[] { 1, 2, 5, 10, 20, 50 };

		Nodes = new int[] { 1000, 5000, 10000, 50000 };
		Alpha = new double[] { 2.0, 2.2, 2.4, 2.6, 2.8, 3.0 };
		C = new int[] { 1, 2, 5, 10, 20, 50 };

		Nodes = new int[] { 1000, 5000, 10000, 50000 };
		Alpha = new double[] { 2.0, 2.2, 2.4, 2.6, 2.8, 3.0 };
		C = new int[] { 1, 2, 5, 20, 50 };

		// Nodes = new int[] { 300000 };
		// Alpha = new double[] { 3.0 };
		// C = new int[] { 1, 2, 5, 10, 20, 50 };

		HashMap<Integer, Integer> Times = new HashMap<Integer, Integer>();
		Times.put(1000, 10);
		Times.put(5000, 10);
		Times.put(10000, 10);
		Times.put(50000, 10);

		RoutingAlgorithm[] R = PET.getRoutingAlgorithms();
		cutoffType type = cutoffType.SQRT_N;

		if (PET.whatToDo) {
			PETTest.whatToDo(Nodes, Alpha, C, R, type, Times);
		} else if (PET.checkGraphs) {
			PETTest.checkGraphs(Nodes, Alpha, C, type, Times);
		} else if (args.length == 2) {
			int threads = Integer.parseInt(args[0]);
			int offset = Integer.parseInt(args[1]);
			System.out.println("STARTED " + offset + "/" + threads + " - "
					+ new Date(System.currentTimeMillis()));
			Stats stats = new Stats();
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
			stats.end();
		} else {
			System.out.println("STARTED - "
					+ new Date(System.currentTimeMillis()));
			Stats stats = new Stats();
			if (PET.plotMulti) {
				PETPlot.generatePlotsMulti(Nodes, Alpha, C, R, type);
			}
			if (PET.plotSingle) {
				PETPlot.generatePlotsSingle(Nodes, Alpha, C, R, type);
			}
			if (PET.plotSingleCombined) {
				PETPlot.generatePlotsSingleCombined(Nodes, Alpha, C, R, type);
			}
			stats.end();
		}
	}

	public static RoutingAlgorithm[] getRoutingAlgorithms() {
		RoutingAlgorithm owg = new OneWorseGreedy();
		RoutingAlgorithm dfg = new DepthFirstGreedy();
		RoutingAlgorithm rdfe = new RestrictedDFE();
		// return new RoutingAlgorithm[] { owg, dfg, rdfe };
		return new RoutingAlgorithm[] { owg, dfg };
		// return new RoutingAlgorithm[] { rdfe };
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
		Network nwLD = PET.toLD(nw);
		return PET.graphsLD + nw.nodes() + "/" + nwLD.folder();
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

	public static Network toLD(Network nw) {
		ScaleFreeUndirected nwLD = (ScaleFreeUndirected) nw;
		if (PET.newModel) {
			return new KleinbergPowerLaw(nw.nodes(), 0, nwLD.getAlpha(),
					nwLD.getCutoff(), true, false, nwLD.routingAlgorithm(),
					nwLD.transformations());
		} else if (PET.minLD) {
			return new ScaleFreeUndirectedMinLD(nw.nodes(), nwLD.getAlpha(),
					nwLD.getCutoff(), 1, nwLD.routingAlgorithm(),
					nwLD.transformations());
		} else {
			return new ScaleFreeUndirectedLD(nw.nodes(), nwLD.getAlpha(),
					nwLD.getCutoff(), nwLD.routingAlgorithm(),
					nwLD.transformations());
		}
	}

	public static Transformation toSD(Network nw) {
		ScaleFreeUndirected nw_ = (ScaleFreeUndirected) nw;
		return new ScaleFreeUndirectedSD(nw_.getC());
	}

}
