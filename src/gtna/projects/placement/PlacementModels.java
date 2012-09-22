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
 * PlacementModels.java
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
package gtna.projects.placement;

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.connectivity.WeakConnectivity;
import gtna.networks.Network;
import gtna.networks.etc.GpsNetwork;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.LogDistanceConnector;
import gtna.networks.model.placementmodels.connectors.QUDGConnector;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CirclePlacementModel;
import gtna.networks.model.placementmodels.models.CirclePlacementModel.DistributionType;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.GridPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data.Type;
import gtna.plot.Plotting;
import gtna.util.Config;
import gtna.util.Stats;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class PlacementModels {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		Stats stats = new Stats();
		Config.overwrite("GNUPLOT_PATH", "/sw/bin/gnuplot");

		// PlacementModels.combinations(5);
		// PlacementModels.singles(1000, 50, 5);
		// PlacementModels.singles(1000, 100, 5);
		// PlacementModels.singles(1000, 150, 5);
		// PlacementModels.singles(1000, 200, 5);
		// PlacementModels.singles(1000, 250, 5);
		// PlacementModels.parameters();

		// PlacementModels.single(5);
		// PlacementModels.wifi(1, false);
		// PlacementModels.GpsTest();

		Config.overwrite("MAIN_DATA_FOLDER", "./data/lecture-5/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/lecture-5/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, WEAK_CONNECTIVITY");

		stats.end();
	}

	private static void GpsTest() {
		String name = "darmstadt_all";
		String filename = "/Users/benni/TUD/" + "PlacementModels/city-mesh/"
				+ name + ".txt";
		double[] R = new double[] { 25, 50, 75, 100 };
		// for (double r : R) {
		// Network nw = new GpsNetwork(filename, name, r, 100, 100, null);
		// PlacementModels.plot(nw, "./plots/gps-test/" + name + "-" + r
		// + "_new.pdf");
		// }
	}

	private static void wifi(int times, boolean GET) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/pm-wifi/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/pm-wifi/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, WEAK_CONNECTIVITY, "
				+ "SHORTEST_PATHS, " + "CLUSTERING_COEFFICIENT");
		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new WeakConnectivity(), new ShortestPaths(),
				new ClusteringCoefficient() };

		String[] names = new String[] { "berlin", "chicago", "darmstadt_all",
				"darmstadt_open", "google", "manhattan", "melbourne", "praha",
				"san_francisco" };
		HashMap<String, double[]> Rmap = new HashMap<String, double[]>();
		Rmap.put("berlin", new double[] { 50, 100, 150, 200, 250 });
		Rmap.put("chicago", new double[] { 50, 100, 150, 200, 250 });
		Rmap.put("darmstadt_all", new double[] { 5, 10, 15, 20, 25, 30 });
		Rmap.put("darmstadt_open", new double[] { 50, 100, 150, 200, 250 });
		Rmap.put("google", new double[] { 10, 20, 30, 40, 50 });
		Rmap.put("manhattan", new double[] { 20, 40, 60, 80, 100 });
		Rmap.put("melbourne", new double[] { 50, 100, 150, 200, 250 });
		Rmap.put("praha", new double[] { 50, 100, 150, 200, 250 });
		Rmap.put("san_francisco", new double[] { 50, 100, 150, 200, 250 });

		// for (String name : names) {
		// double[] R = Rmap.get(name);
		// Network[][] nw = new Network[2][R.length];
		// for (int i = 0; i < R.length; i++) {
		// String filename = "/Users/benni/TUD/"
		// + "PlacementModels/city-mesh/" + name + ".txt";
		// nw[0][i] = new GpsNetwork(filename, name, R[i], 100, 100, null);
		// int n = nw[0][i].getNodes();
		// double w = 1000;
		// double h = 1000;
		// PlacementModel p1 = new GridPlacementModel(w, h, 1, 1, false);
		// Partitioner p = new SimplePartitioner();
		// NodeConnector c = new UDGConnector(R[i]);
		//
		// PlacementModel p2_random = new RandomPlacementModel(1000, 1000,
		// false);
		// Network pmc_random = new PlacementModelContainer(n, 1, w, h,
		// p1, p2_random, p, c, null);
		// nw[1][i] = new DescriptionWrapper(pmc_random, "Random " + n);
		// }
		// Series[][] s = GET ? Series.get(nw, metrics) : Series.generate(nw,
		// metrics, times);
		// Plotting.single(s, metrics, name + "-" + nw[0][0].getNodes()
		// + "-singles/");
		// }
	}

	private static void single(int times) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/pm-single/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/pm-single/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, WEAK_CONNECTIVITY, "
				+ "SHORTEST_PATHS, " + "CLUSTERING_COEFFICIENT");
		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new WeakConnectivity(), new ShortestPaths(),
				new ClusteringCoefficient() };
		// RICH_CLUB_CONNECTIVITY

		double w = 1000;
		double h = 1000;
		int[] N = new int[] { 100, 500, 1000, 5000, 10000 };
		HashMap<Integer, double[]> Rmap = new HashMap<Integer, double[]>();
		Rmap.put(100, new double[] { 50, 75, 100, 125, 150, 175, 200 });
		Rmap.put(500, new double[] { 30, 40, 50, 60, 70, 80 });
		Rmap.put(1000, new double[] { 10, 20, 30, 40, 50, 60 });
		Rmap.put(5000, new double[] { 15, 16, 17, 18, 19, 20 });
		Rmap.put(10000, new double[] { 10, 11, 12, 13, 14, 15 });
		for (int n : N) {
			PlacementModel p1 = new GridPlacementModel(w, h, 1, 1, false);
			Partitioner p = new SimplePartitioner();
			double[] R = Rmap.get(n);
			Network[][] nw1 = PlacementModels
					.getSingleRandom(n, R, w, h, p1, p);
			Network[][] nw2 = PlacementModels
					.getSingleCircle(n, R, w, h, p1, p);
			Network[][] nw3 = PlacementModels.getSingleGrid(n, R, w, h, p1, p);
			Network[][] nw4 = PlacementModels.getSingleCommunity(n, R, w, h,
					p1, p);

			boolean GET = true;

			Series[][] s1 = GET ? Series.get(nw1, metrics) : Series.generate(
					nw1, metrics, times);
			// Plot.singlesAvg(s1, times + "/Random-" + n + "/");

			Series[][] s2 = GET ? Series.get(nw2, metrics) : Series.generate(
					nw2, metrics, times);
			// Plot.singlesAvg(s2, times + "/Circle-" + n + "/");

			Series[][] s3 = GET ? Series.get(nw3, metrics) : Series.generate(
					nw3, metrics, times);
			// Plot.singlesAvg(s3, times + "/Grid-" + n + "/");

			Series[][] s4 = GET ? Series.get(nw4, metrics) : Series.generate(
					nw4, metrics, times);
			// Plot.singlesAvg(s4, times + "/Communities-" + n + "/");

			Series[][] s = new Series[s1.length + s2.length + s3.length
					+ s4.length][];
			int index = 0;
			for (Series[] S : s1) {
				s[index++] = S;
			}
			for (Series[] S : s2) {
				s[index++] = S;
			}
			for (Series[] S : s3) {
				s[index++] = S;
			}
			for (Series[] S : s4) {
				s[index++] = S;
			}
			Plotting.single(s, metrics, times + "/combination-" + n + "/");
		}
	}

	private static Network[][] getSingleRandom(int n, double[] R, double w,
			double h, PlacementModel p1, Partitioner p) {
		Network[][] nw = new Network[1][R.length];
		for (int i = 0; i < R.length; i++) {
			NodeConnector c = new UDGConnector(R[i]);
			PlacementModel p2 = new RandomPlacementModel(w, h, false);
			Network pmc = new PlacementModelContainer(n, 1, w, h, w, h, p1, p2, p, c,
					null);
			nw[0][i] = new DescriptionWrapper(pmc, "Random " + n);
			System.out.println(pmc.getDescription());
			System.out.println(nw[0][i].getDescription());
		}
		return nw;
	}

	private static Network[][] getSingleCircle(int n, double[] R, double w,
			double h, PlacementModel p1, Partitioner p) {
		DistributionType[][] dt = new DistributionType[3][];
		dt[0] = new DistributionType[] { DistributionType.FIXED,
				DistributionType.FIXED };
		dt[1] = new DistributionType[] { DistributionType.NORMAL,
				DistributionType.NORMAL };
		dt[2] = new DistributionType[] { DistributionType.UNIFORM,
				DistributionType.UNIFORM };
		Network[][] nw = new Network[dt.length][R.length];
		for (int j = 0; j < dt.length; j++) {
			for (int i = 0; i < R.length; i++) {
				NodeConnector c = new UDGConnector(R[i]);
				PlacementModel p2 = new CirclePlacementModel(
						Math.min(w, h) / 2.0, dt[j][0], dt[j][1], false);
				Network pmc = new PlacementModelContainer(n, 1, w, h, w, h, p1, p2,
						p, c, null);
				nw[j][i] = new DescriptionWrapper(pmc, "Circle " + n + " ("
						+ dt[j][0] + " / " + dt[j][1] + ")");
				System.out.println(pmc.getDescription());
				System.out.println(nw[j][i].getDescription());
			}
		}
		return nw;
	}

	private static Network[][] getSingleGrid(int n, double[] R, double w,
			double h, PlacementModel p1, Partitioner p) {
		Network[][] nw = new Network[1][R.length];
		for (int i = 0; i < R.length; i++) {
			NodeConnector c = new UDGConnector(R[i]);
			int cols = (int) Math.ceil(Math.sqrt(n));
			int rows = cols;
			PlacementModel p2 = new GridPlacementModel(w, h, cols, rows, false);
			Network pmc = new PlacementModelContainer(n, 1, w, h, w, h, p1, p2, p, c,
					null);
			nw[0][i] = new DescriptionWrapper(pmc, "Grid " + n + " (" + cols
					+ " x " + rows + ")");
			System.out.println(pmc.getDescription());
			System.out.println(nw[0][i].getDescription());
		}
		return nw;
	}

	private static Network[][] getSingleCommunity(int n, double[] R, double w,
			double h, PlacementModel p1, Partitioner p) {
		double[] sigma = new double[] { 0.1, 0.25, 0.5, 0.75, 1.0 };
		Network[][] nw = new Network[sigma.length][R.length];
		for (int j = 0; j < sigma.length; j++) {
			for (int i = 0; i < R.length; i++) {
				NodeConnector c = new UDGConnector(R[i]);
				PlacementModel p2 = new CommunityPlacementModel(sigma[j] * w, sigma[j] * h,
						false);
				Network pmc = new PlacementModelContainer(n, 1, w, h, w, h, p1, p2,
						p, c, null);
				nw[j][i] = new DescriptionWrapper(pmc, "Community " + n + " ("
						+ sigma[j] + ")");
				System.out.println(pmc.getDescription());
				System.out.println(nw[0][i].getDescription());
			}
		}
		return nw;
	}

	private static void singles(double s, double r, int times, double w,
			double h) {
		int nodes = 100;
		int cols1 = (int) Math.ceil(Math.sqrt(nodes));
		int rows1 = cols1;

		NodeConnector c = new UDGConnector(r);
		PlacementModel p1 = new GridPlacementModel(s, s, 1, 1, false);
		PlacementModel[] placements2 = PlacementModels.getModels(s, s, s / 2.0,
				0.25, false, cols1, rows1);
		for (PlacementModel p2 : placements2) {
			for (int i = 0; i < times; i++) {
				PlacementModelContainer nw = new PlacementModelContainer(nodes,
						1, w, h, w, h, p1, p2, new SimplePartitioner(), c, null);
				String name = "./plots/singles/" + p2.getKey() + "-" + r + "-"
						+ i;
				PlacementModels.plot(nw, name);
				if (p2.getKey().equals("GRID") || p2.getKey().equals("CIRCLE")) {
					break;
				}
			}
		}
	}

	private static void combinations(int times) {
		int nodes = 512;
		int hotSpots = 25;

		double w1 = 800;
		double h1 = 800;
		double r1 = 300;
		double sigma1 = 0.25;
		int cols1 = (int) Math.ceil(Math.sqrt(hotSpots));
		int rows1 = cols1;

		double w2 = 100;
		double h2 = 100;
		double r2 = 40;
		double sigma2 = 0.25;
		int cols2 = (int) Math.ceil(Math.sqrt(nodes / hotSpots));
		int rows2 = cols2;

		NodeConnector c1 = new LogDistanceConnector(0.025, 5, 15, 1);
		NodeConnector c2 = new QUDGConnector(10, 50, 0.5);
		NodeConnector c3 = new UDGConnector(50);
		NodeConnector[] connectors = new NodeConnector[] { c3 };

		PlacementModel[] placements1 = PlacementModels.getModels(w1, h1, r1,
				sigma1, false, cols1, rows1);
		PlacementModel[] placements2 = PlacementModels.getModels(w2, h2, r2,
				sigma2, false, cols2, rows2);
		
		// int index = 0;
		// for (PlacementModel p1 : placements1) {
		// for (PlacementModel p2 : placements2) {
		// for (NodeConnector c : connectors) {
		// for (int i = 0; i < times; i++) {
		// PlacementModelContainer nw = new PlacementModelContainer(
		// nodes, hotSpots, w1, h1, p1, p2,
		// new SimplePartitioner(), c, null);
		// String name = "./plots/combinations/" + p1.getKey()
		// + "-" + p2.getKey() + "-"
		// + c.getConfigValues()[0] + "-" + i;
		// PlacementModels.plot(nw, name);
		// }
		// }
		// }
		// }
	}

	private static void plot(Network nw, String name) {
		Graph g = nw.generate();
		Gephi ge = new Gephi();
		IdentifierSpace idSpace = (IdentifierSpace) g.getProperty("ID_SPACE_0");
		String filename1 = name + ".pdf";
		String filename2 = name + ".jpg";
		if ((new File(filename2)).exists()) {
			return;
		}
		System.out.println(nw.getFolder());
		System.out.println("=> " + filename1);
		ge.plot(g, idSpace, filename1);
		System.out.println("=> " + filename2);
		PlacementModels.exec("/usr/local/bin/convert " + filename1 + " "
				+ filename2);
		PlacementModels.exec("rm " + filename1);
		System.out.println();
	}

	private static void exec(String cmd) {
		String[] envp = new String[] { Config.get("GNUPLOT_ENVP") };
		Process p_1;
		try {
			p_1 = Runtime.getRuntime().exec(cmd, envp);
			p_1.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static PlacementModel[] getModels(double w, double h, double r,
			double sigma, boolean inCenter, int cols, int rows) {
		PlacementModel p1 = new CirclePlacementModel(r, DistributionType.FIXED,
				DistributionType.FIXED, false);
		PlacementModel p2 = new CommunityPlacementModel(sigma * w, sigma * h, inCenter);
		PlacementModel p3 = new GridPlacementModel(w, h, cols, rows, false);
		PlacementModel p4 = new RandomPlacementModel(w, h, inCenter);
		return new PlacementModel[] { p3, p1, p2, p4 };
	}

	// private static void parameters() {
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/pm-single/");
	// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/pm-single/");
	// Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
	//
	// Config.overwrite("METRICS", "DEGREE_DISTRIBUTION, WEAK_CONNECTIVITY, "
	// + "SHORTEST_PATHS, " + "CLUSTERING_COEFFICIENT");
	// // RICH_CLUB_CONNECTIVITY
	//
	// double w = 1000;
	// double h = 1000;
	// int[] N = new int[] { 100, 500, 1000, 5000, 10000 };
	// HashMap<Integer, int[]> HSmap = new HashMap<Integer, int[]>();
	// // HSmap.put(100, new int[] { 1, 2, 3 });
	// // HSmap.put(500, new int[] { 2, 3, 4, 5, 6, 7 });
	// // HSmap.put(1000, new int[] { 3, 5, 7, 9, 10 });
	// // HSmap.put(5000, new int[] { 7, 11, 15, 19, 22, 23 });
	// // HSmap.put(10000, new int[] { 10, 15, 20, 25, 30, 31, 32 });
	// HSmap.put(100, new int[] { 1 });
	// HSmap.put(500, new int[] { 1 });
	// HSmap.put(1000, new int[] { 1 });
	// HSmap.put(5000, new int[] { 1 });
	// HSmap.put(10000, new int[] { 1 });
	// HashMap<Integer, double[]> Rmap = new HashMap<Integer, double[]>();
	// // Rmap.put(100, new double[] { 50, 60, 70, 80, 90, 100 });
	// // Rmap.put(500, new double[] { 50, 60, 70, 80, 90, 100 });
	// // Rmap.put(1000, new double[] { 10, 12, 14, 16, 18, 20 });
	// // Rmap.put(5000, new double[] { 10, 12, 14, 16, 18, 20 });
	// // Rmap.put(10000, new double[] { 10, 12, 14, 16, 18, 20 });
	// Rmap.put(100, new double[] { 50, 75, 100, 125, 150, 175, 200 });
	// Rmap.put(500, new double[] { 30, 40, 50, 60, 70, 80 });
	// Rmap.put(1000, new double[] { 10, 20, 30, 40, 50, 60 });
	// Rmap.put(5000, new double[] { 15, 16, 17, 18, 19, 20 });
	// Rmap.put(10000, new double[] { 10, 11, 12, 13, 14, 15 });
	// for (int n : N) {
	// int[] HS = HSmap.get(n);
	// double[] R = Rmap.get(n);
	// Network[][] nw1 = new Network[HS.length][R.length];
	// for (int i = 0; i < HS.length; i++) {
	// int hs = HS[i];
	// for (int j = 0; j < R.length; j++) {
	// double r = R[j];
	// System.out.println("N = " + n + " @ " + hs + "x" + hs
	// + " / " + r);
	// double w_ = w / (double) hs;
	// double h_ = h / (double) hs;
	// PlacementModel p1 = new RandomPlacementModel(w, h, false);
	// Partitioner p = new SimplePartitioner();
	// NodeConnector c = new UDGConnector(r);
	//
	// PlacementModel p_r = new RandomPlacementModel(w_, h_, false);
	// Network random = new PlacementModelContainer(n, hs * hs,
	// p1, p_r, p, c, null, null);
	// nw1[i][j] = new DescriptionWrapper(random, "R " + n
	// + " (w=" + w_ + ", h=" + h_ + ")");
	//
	// PlacementModel p_c = new CirclePlacementModel(w_, h_,
	// Math.min(w_, h_) / 2.0, DistributionType.FIXED,
	// DistributionType.FIXED);
	// Network circle = new PlacementModelContainer(n, hs * hs,
	// p1, p_c, p, c, null, null);
	//
	// System.out.println("  " + random.description());
	// }
	// }
	//
	// // for (int i = 0; i < nw1[0][0].configKeys().length; i++) {
	// // System.out.println(nw1[0][0].configKeys()[i] + " => "
	// // + nw1[0][0].configValues()[i]);
	// // }
	// // System.out.println("=> " + nw1[0][0].description());
	//
	// Series[][] s1 = Series.generate(nw1, 1);
	// Plot.singlesAvg(s1, "R-" + n + "/");
	//
	// System.out.println();
	// }
	// }
}
