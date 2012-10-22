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
 * MakiDemo2.java
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
package gtna.projects.makiDemo;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.GridPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.util.Config;
import gtna.util.Stats;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class Immi1 {

	public static void main(String[] args) throws IOException,
			InterruptedException {
		Stats stats = new Stats();
		single(5);
		stats.end();
	}

	private static void single(int times) {
		// Konfiguration der Pfade
		Config.overwrite("MAIN_DATA_FOLDER", "./data/pm-single/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/pm-single/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("GNUPLOT_PATH",
				"C:/Program Files (x86)/gnuplot/bin/gnuplot.exe");

		// Konfiguration der Metriken
		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, "
				+ "SHORTEST_PATHS, " + "CLUSTERING_COEFFICIENT");
		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new ShortestPaths(), new ClusteringCoefficient() };

		// Feldgrˆﬂe
		double w = 1000;
		double h = 1000;
		// Anzahl der Knoten
		int[] N = new int[] { 800, 900, 1000 };

		// Parameter Verbindungsmodel / Reichweite
		HashMap<Integer, double[]> Rmap = new HashMap<Integer, double[]>();
		Rmap.put(800, new double[] { 90 });
		Rmap.put(900, new double[] { 90 });
		Rmap.put(1000, new double[] { 90 });

		for (int n : N) {
			// Platzierungsmodelle und Parameter
			PlacementModel p1 = new GridPlacementModel(w, h, 4, 4, false);
			Partitioner p = new SimplePartitioner();
			double[] R = Rmap.get(n);
			double[] sigma = new double[] { 0.03, 0.04, 0.05, 0.06, 0.07, 0.08 };
			Network[][] nw4 = getMultiCommunity(n, R, w, h, p1, p, sigma);

			int i_l = nw4.length;
			int j_l = nw4[0].length;
			for (int i = 0; i < i_l; i++)
				for (int j = 0; j < j_l; j++) {
					// Community Detection
					Immi2.plot(nw4[i][j], "cd_" + n + "_" + sigma[i] + "_"
							+ R[j], 1, false, true);
				}

			// Generierung der Metriken / Plots
			boolean GET = false;
			Series[][] s4 = GET ? Series.get(nw4, metrics) : Series.generate(
					nw4, metrics, times);
			Plotting.single(s4, metrics, times + "/combination-" + n + "/");
			Plotting.multi(s4, metrics, times + "/combination-m-" + n + "/");
			Plotting.multi(s4, metrics, times + "/combination-mv-" + n + "/",
					Type.confidence1, Style.candlesticks);
		}
	}

	private static Network[][] getMultiCommunity(int n, double[] R, double w,
			double h, PlacementModel p1, Partitioner p, double[] sigma) {

		Network[][] nw = new Network[sigma.length][R.length];
		for (int j = 0; j < sigma.length; j++) {
			for (int i = 0; i < R.length; i++) {
				NodeConnector c = new UDGConnector(R[i]);
				PlacementModel p2 = new CommunityPlacementModel(sigma[j] * w,
						sigma[j] * h, false);
				Network pmc = new PlacementModelContainer(n, 16, w, h, w, h,
						p1, p2, p, c, null);
				nw[j][i] = new DescriptionWrapper(pmc, "Community " + n + " ("
						+ sigma[j] + ")");
				System.out.println(pmc.getDescription());
				System.out.println(nw[0][i].getDescription());
			}
		}
		return nw;
	}

	public static void exec(String cmd) {
		// String[] envp = new sString[] { Config.get("GNUPLOT_ENVP") };
		Process p_1;
		try {
			p_1 = Runtime.getRuntime().exec(cmd);
			p_1.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
