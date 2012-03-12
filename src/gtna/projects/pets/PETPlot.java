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
 * PETPlot.java
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
import gtna.networks.util.DescriptionWrapper;
import gtna.routing.RoutingAlgorithm;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class PETPlot {
	public static void multiAlpha(int[] Nodes, double[] Alpha, int[] C,
			RoutingAlgorithm[] R, PET.cutoffType type) {
		for (int nodes : Nodes) {
			for (double alpha : Alpha) {
				Network[] nw = new Network[R.length * C.length];
				int index = 0;
				for (RoutingAlgorithm r : R) {
					for (int c : C) {
						nw[index++] = PET.getSDR(nodes, alpha, type, c, r);
					}
				}
				String folder = "multi-alpha/" + nodes + "-" + alpha + "/";
				// Series[] s = Series.get(nw);
				// Plot.multiAvg(s, folder);
				Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
						+ folder);
				Config.overwrite("GNUPLOT_TEMP_FILE",
						Config.get("MAIN_PLOT_FOLDER") + folder + "gnuplot.txt");
				// Plot.allMulti(s, folder);
			}
		}
	}

	public static void multiC(int[] Nodes, double[] Alpha, int[] C,
			RoutingAlgorithm[] R, PET.cutoffType type) {
		for (int nodes : Nodes) {
			for (int c : C) {
				Network[] nw = new Network[R.length * Alpha.length];
				int index = 0;
				for (RoutingAlgorithm r : R) {
					for (double alpha : Alpha) {
						nw[index++] = PET.getSDR(nodes, alpha, type, c, r);
					}
				}
				String folder = "multi-c/" + nodes + "-" + c + "/";
				// Series[] s = Series.get(nw);
				// Plot.multiAvg(s, folder);
				Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
						+ folder);
				Config.overwrite("GNUPLOT_TEMP_FILE",
						Config.get("MAIN_PLOT_FOLDER") + folder + "gnuplot.txt");
				// Plot.allMulti(s, folder);
			}
		}
	}

	public static void single_c_alpha(int[] Nodes, double[] Alpha, int[] C,
			RoutingAlgorithm[] R, PET.cutoffType type) {
		for (int nodes : Nodes) {
			Network[][] nw = new Network[Alpha.length * R.length][C.length];
			int index = 0;
			for (RoutingAlgorithm r : R) {
				for (double alpha : Alpha) {
					for (int i = 0; i < C.length; i++) {
						Network nw_ = PET.getSDR(nodes, alpha, type, C[i], r);
						String name = nodes + " - " + alpha + " ("
								+ r.getNameShort() + ")";
						nw[index][i] = new DescriptionWrapper(nw_, name);
					}
					index++;
				}
			}
			String folder = "single-c-alpha/" + nodes + "/";
			// Series[][] s = Series.get(nw);
			// Plot.singlesAvg(s, folder);
			Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
					+ folder);
			Config.overwrite("GNUPLOT_TEMP_FILE",
					Config.get("MAIN_PLOT_FOLDER") + folder + "gnuplot.txt");
			// Plot.allSingle(s, folder);
		}
	}

	public static void single_alpha_c(int[] Nodes, double[] Alpha, int[] C,
			RoutingAlgorithm[] R, PET.cutoffType type) {
		for (int nodes : Nodes) {
			Network[][] nw = new Network[C.length * R.length][Alpha.length];
			int index = 0;
			for (RoutingAlgorithm r : R) {
				for (int c : C) {
					for (int i = 0; i < Alpha.length; i++) {
						Network nw_ = PET.getSDR(nodes, Alpha[i], type, c, r);
						String name = nodes + " - " + c + " ("
								+ r.getNameShort() + ")";
						nw[index][i] = new DescriptionWrapper(nw_, name);
					}
					index++;
				}
			}
			String folder = "single-alpha-c/" + nodes + "/";
			// Series[][] s = Series.get(nw);
			// Plot.singlesAvg(s, folder);
			Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
					+ folder);
			Config.overwrite("GNUPLOT_TEMP_FILE",
					Config.get("MAIN_PLOT_FOLDER") + folder + "gnuplot.txt");
			// Plot.allSingle(s, folder);
		}
	}

	public static void single_nodes_alpha(int[] Nodes, double[] Alpha, int[] C,
			RoutingAlgorithm[] R, PET.cutoffType type) {
		for (int c : C) {
			Network[][] nw = new Network[Alpha.length * R.length][Nodes.length];
			int index = 0;
			for (RoutingAlgorithm r : R) {
				for (double alpha : Alpha) {
					for (int i = 0; i < Nodes.length; i++) {
						Network nw_ = PET.getSDR(Nodes[i], alpha, type, c, r);
						String name = c + " - " + alpha + " ("
								+ r.getNameShort() + ")";
						nw[index][i] = new DescriptionWrapper(nw_, name);
					}
					index++;
				}
			}
			String folder = "single-nodes-alpha/" + c + "/";
			// Series[][] s = Series.get(nw);
			// Plot.singlesAvg(s, folder);
			Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
					+ folder);
			Config.overwrite("GNUPLOT_TEMP_FILE",
					Config.get("MAIN_PLOT_FOLDER") + folder + "gnuplot.txt");
			// Plot.allSingle(s, folder);
		}
	}

	public static void single_nodes_c(int[] Nodes, double[] Alpha, int[] C,
			RoutingAlgorithm[] R, PET.cutoffType type) {
		for (double alpha : Alpha) {
			Network[][] nw = new Network[C.length * R.length][Nodes.length];
			int index = 0;
			for (RoutingAlgorithm r : R) {
				for (int c : C) {
					for (int i = 0; i < Nodes.length; i++) {
						Network nw_ = PET.getSDR(Nodes[i], alpha, type, c, r);
						String name = c + " - " + alpha + " ("
								+ r.getNameShort() + ")";
						nw[index][i] = new DescriptionWrapper(nw_, name);
					}
					index++;
				}
			}
			String folder = "single-nodes-c/" + alpha + "/";
			// Series[][] s = Series.get(nw);
			// Plot.singlesAvg(s, folder);
			Config.overwrite("TEMP_FOLDER", Config.get("MAIN_PLOT_FOLDER")
					+ folder);
			Config.overwrite("GNUPLOT_TEMP_FILE",
					Config.get("MAIN_PLOT_FOLDER") + folder + "gnuplot.txt");
			// Plot.allSingle(s, folder);
		}
	}
}
