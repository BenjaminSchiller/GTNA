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
package gtna.projects;

import gtna.data.Series;
import gtna.networks.Network;
import gtna.networks.model.smallWorld.ScaleFreeUndirected;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;

/**
 * @author benni
 * 
 */
public class PETPlot {
	public static void generatePlotsMulti(int[] Nodes, double[] Alpha, int[] C,
			RoutingAlgorithm[] R, PET.cutoffType type) {
		for (int nodes : Nodes) {
			for (double alpha : Alpha) {
				for (int c : C) {
					int cut = PET.cutoff(nodes, type);
					Network[] nw = new Network[R.length];
					for (int i = 0; i < R.length; i++) {
						nw[i] = new ScaleFreeUndirected(nodes, alpha, c, cut,
								R[i], null);
					}
					String folder = nodes
							+ "/"
							+ (new ScaleFreeUndirected(nodes, alpha, c, cut,
									null, null)).folder();
					Series[] s = Series.get(nw);
					Plot.multiAvg(s, folder);
				}
			}
		}
	}

	public static void generatePlotsSingle(int[] Nodes, double[] Alpha,
			int[] C, RoutingAlgorithm[] R, PET.cutoffType type) {
		for (int nodes : Nodes) {
			for (RoutingAlgorithm r : R) {
				Network[][] nw1 = new Network[Alpha.length][C.length];
				Network[][] nw2 = new Network[C.length][Alpha.length];
				for (int i = 0; i < Alpha.length; i++) {
					for (int j = 0; j < C.length; j++) {
						int cut = PET.cutoff(nodes, type);
						nw1[i][j] = new ScaleFreeUndirected(nodes, Alpha[i],
								C[j], cut, r, null);
						nw2[j][i] = nw1[i][j];
					}
				}
				String folder1 = nodes + "/_alpha-c-" + r.folder() + "/";
				String folder2 = nodes + "/_c-alpha-" + r.folder() + "/";
				Series[][] s1 = Series.get(nw1);
				Series[][] s2 = Series.get(nw2);
				Plot.singlesAvg(s1, folder1);
				Plot.singlesAvg(s2, folder2);
			}
		}
	}

	public static void generatePlotsSingleCombined(int[] Nodes, double[] Alpha,
			int[] C, RoutingAlgorithm[] R, PET.cutoffType type) {
		for (int nodes : Nodes) {
			for (int c : C) {
				Network[][] nw1 = new Network[R.length][Alpha.length];
				for (int i = 0; i < R.length; i++) {
					for (int j = 0; j < Alpha.length; j++) {
						int cut = PET.cutoff(nodes, type);
						nw1[i][j] = new ScaleFreeUndirected(nodes, Alpha[j], c,
								cut, R[i], null);
					}
				}
				String folder1 = nodes + "/_alpha-routing-" + c + "-";
				Series[][] s1 = Series.get(nw1);
				Plot.singlesAvg(s1, folder1);
			}
		}
	}
}
