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
 * IDSorting.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-12 : v1 (BS)
 *
 */
package gtna;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.io.Filewriter;
import gtna.io.GraphReader;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.GreedyNextBestBacktracking;
import gtna.transformation.Transformation;
import gtna.transformation.identifier.LocalMC;
import gtna.transformation.identifier.RandomID;
import gtna.transformation.sorting.lmc.LMC;
import gtna.transformation.sorting.swapping.Swapping;
import gtna.util.Config;
import gtna.util.Stats;
import gtna.util.Timer;

import java.io.File;
import java.sql.Time;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class IDSorting {
	private static final String SPI = "./resources/SPI-3-LCC/2010-08.spi.txt";

	private static final int LMC_I = 2000;
	private static final int LMCA_I = 2000;

	private static final double P = 0.5;
	private static final int C = 10;
	private static final String[] LMCA = new String[] { LMC.ATTACK_CONTRACTION,
			LMC.ATTACK_CONVERGENCE, LMC.ATTACK_KLEINBERG };
	private static final int[] LMC_A = new int[] { 1, 10, 100 };

	private static final int SW_I = 2000;
	private static final int SWA_I = 2000;
	private static final String[] SWA = new String[] {
			Swapping.ATTACK_CONTRACTION, Swapping.ATTACK_CONVERGENCE,
			Swapping.ATTACK_KLEINBERG };
	private static final int[] SW_A = new int[] { 1, 2, 3, 10 };

	private static final int TTL = 174;

	private static final boolean TEST = false;
	private static final boolean JPEG = true;
	private static final boolean PLOT_EACH = false;

	private static final boolean GENERATE_GRAPHS = true;
	private static final boolean GENERATE_DATA = true;
	private static final boolean PLOT_DATA = true;

	private static final RoutingAlgorithm RA = new GreedyNextBestBacktracking(
			TTL);

	// private static final String metrics = "ID_SPACE";
	private static final String metrics = "ID_SPACE, ID_SPACE_DISTANCES, ID_SPACE_HOPS, RL";

	private static final int TIMES = 1;

	private static final int MAX_TIMES = 1;

	public static void main(String[] args) {
		Stats stats = new Stats();
		
		execute(LMC.MODE_1, "0.0001", LMC.ATTACKER_SELECTION_MEDIAN);

		// execute(LMC.MODE_1, "0.0001", LMC.ATTACKER_SELECTION_SMALLEST);
		// execute(LMC.MODE_1, "0.0001", LMC.ATTACKER_SELECTION_MEDIAN);
		// execute(LMC.MODE_1, "0.0001", LMC.ATTACKER_SELECTION_LARGEST);
		// execute(LMC.MODE_1, "0.0001", LMC.ATTACKER_SELECTION_RANDOM);

		// execute(LMC.MODE_2, "0.001", LMC.ATTACKER_SELECTION_SMALLEST);
		// execute(LMC.MODE_2, "0.001", LMC.ATTACKER_SELECTION_MEDIAN);
		// execute(LMC.MODE_2, "0.001", LMC.ATTACKER_SELECTION_LARGEST);
		// execute(LMC.MODE_2, "0.001", LMC.ATTACKER_SELECTION_RANDOM);

		// execute(LMC.MODE_2, "0.0001", LMC.ATTACKER_SELECTION_SMALLEST);
		// execute(LMC.MODE_2, "0.0001", LMC.ATTACKER_SELECTION_MEDIAN);
		// execute(LMC.MODE_2, "0.0001", LMC.ATTACKER_SELECTION_LARGEST);
		// execute(LMC.MODE_2, "0.0001", LMC.ATTACKER_SELECTION_RANDOM);

		// execute(LMC.MODE_2, "0.000001", LMC.ATTACKER_SELECTION_SMALLEST);
		// execute(LMC.MODE_2, "0.000001", LMC.ATTACKER_SELECTION_MEDIAN);
		// execute(LMC.MODE_2, "0.000001", LMC.ATTACKER_SELECTION_LARGEST);
		// execute(LMC.MODE_2, "0.000001", LMC.ATTACKER_SELECTION_RANDOM);
		
		stats.end();
	}

	public static void execute(String LMC_M, String D, String LMC_AS) {
		// if (true) {
		// Graph g = GraphReader.read(SPI);
		// Transformation r = new RandomID(RandomID.RING_NODE, 0);
		// Transformation lmc = new LMC(10, LMC.MODE_1, 0.5, "0.0001", 10);
		// g = r.transform(g);
		// g = lmc.transform(g);
		//
		// System.out.println(lmc.folder());
		// System.out.println(lmc.name());
		//
		// return;
		// }

		// String MAIN_FOLDER = "test-" + LMC_M + "-" + D + "-" + LMC_AS + "/";
		// String MAIN_FOLDER = "TEST-" + System.currentTimeMillis() + "/";
		String MAIN_FOLDER = "TEST/";

		System.out.println("\n\n\n\n\n");
		System.out
				.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("~~ " + MAIN_FOLDER + " @ " + LMC_M + "(" + D + ") "
				+ LMC_AS);
		System.out
				.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		String MAIN_DATA_FOLDER = "./data/" + MAIN_FOLDER;
		String MAIN_PLOT_FOLDER = "./plots/" + MAIN_FOLDER + LMC_M + "-" + D
				+ "-" + LMC_AS + "/";
		String GRAPH_FOLDER = "data/" + MAIN_FOLDER + "_G/";

		Config.overwrite("MAIN_DATA_FOLDER", MAIN_DATA_FOLDER);
		Config.overwrite("MAIN_PLOT_FOLDER", MAIN_PLOT_FOLDER);
		Config.overwrite("METRICS", metrics);
		if (JPEG) {
			Config.overwrite("PLOT_EXTENSION", ".jpg");
			Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
		}

		/*
		 * Names
		 */

		HashMap<Transformation[], String> names = new HashMap<Transformation[], String>();

		/*
		 * Basics
		 */

		// Transformation[] r, lmc, sw;
		// r = new Transformation[] { new RandomID(RandomID.RING_NODE, 0) };
		// lmc = new Transformation[] { r[0], new LMC(LMC_I, LMC_M, P, D, C) };
		// sw = new Transformation[] { r[0], new Swapping(SW_I) };
		//
		// names.put(r, "Random");
		// names.put(lmc, "LMC-" + LMC_M + "-" + D);
		// names.put(sw, "Swapping");
		//
		// Transformation[][] basic = new Transformation[][] { r, lmc, sw };

		Transformation[] lmcR, lmcU, lmcOldR, lmcOldU;
		Transformation r = new RandomID(RandomID.RING_NODE, 0);
		lmcR = new Transformation[] { r, new LMC(LMC_I, LMC.MODE_2, P, D, C) };
		lmcU = new Transformation[] { r, new LMC(LMC_I, LMC.MODE_1, P, D, C) };
		lmcOldR = new Transformation[] { r,
				new LocalMC(LMC_I, 2, P, Double.parseDouble(D), C, false, null) };
		lmcOldU = new Transformation[] { r,
				new LocalMC(LMC_I, 1, P, Double.parseDouble(D), C, false, null) };

		names.put(lmcR, "LMC-R");
		names.put(lmcU, "LMC-U");
		names.put(lmcOldR, "LMC-OLD-R");
		names.put(lmcOldU, "LMC-OLD-U");

		// public LocalMC(int maxIter, int mode, double pRand, double delta, int
		// C,
		// boolean deg1, String file) {

		Transformation[][] basic = new Transformation[][] { lmcR, lmcU,
				lmcOldR, lmcOldU };

		/*
		 * LMC Attacks
		 */

		Transformation[][] lmc_cont, lmc_conv, lmc_klei;
		lmc_cont = new Transformation[LMC_A.length][1];
		lmc_conv = new Transformation[LMC_A.length][1];
		lmc_klei = new Transformation[LMC_A.length][1];
		for (int i = 0; i < LMC_A.length; i++) {
			lmc_cont[i][0] = new LMC(LMCA_I, LMC_M, P, D, C, LMCA[0], LMC_AS,
					LMC_A[i]);
			lmc_conv[i][0] = new LMC(LMCA_I, LMC_M, P, D, C, LMCA[1], LMC_AS,
					LMC_A[i]);
			lmc_klei[i][0] = new LMC(LMCA_I, LMC_M, P, D, C, LMCA[2], LMC_AS,
					LMC_A[i]);
			names.put(lmc_cont[i], "LMC-" + LMC_M + "-" + D + "-Contraction-"
					+ LMC_A[i] + "-" + LMC_AS);
			names.put(lmc_conv[i], "LMC-" + LMC_M + "-" + D + "-Convergence-"
					+ LMC_A[i] + "-" + LMC_AS);
			names.put(lmc_klei[i], "LMC-" + LMC_M + "-" + D + "-Kleinberg-"
					+ LMC_A[i] + "-" + LMC_AS);
		}

		/*
		 * SW Attacks
		 */

		Transformation[][] sw_cont, sw_conv, sw_klei;
		sw_cont = new Transformation[SW_A.length][1];
		sw_conv = new Transformation[SW_A.length][1];
		sw_klei = new Transformation[SW_A.length][1];
		for (int i = 0; i < SW_A.length; i++) {
			sw_cont[i][0] = new Swapping(SWA_I, SWA[0], SW_A[i]);
			sw_conv[i][0] = new Swapping(SWA_I, SWA[1], SW_A[i]);
			sw_klei[i][0] = new Swapping(SWA_I, SWA[2], SW_A[i]);
			names.put(sw_cont[i], "SW-Contraction-" + SW_A[i]);
			names.put(sw_conv[i], "SW-Convergence-" + SW_A[i]);
			names.put(sw_klei[i], "SW-Kleinberg-" + SW_A[i]);
		}

		/*
		 * Generate Graphs / Data / Plots
		 */

		if (GENERATE_GRAPHS) {
			createGraphFolders(names, GRAPH_FOLDER);
			generateGraphFromSPI(basic, TIMES, names, GRAPH_FOLDER);
			// generateGraphFrom(lmc_cont, TIMES, names, lmc, GRAPH_FOLDER);
			// generateGraphFrom(lmc_conv, TIMES, names, lmc, GRAPH_FOLDER);
			// generateGraphFrom(lmc_klei, TIMES, names, lmc, GRAPH_FOLDER);
			// generateGraphFrom(sw_cont, TIMES, names, sw, GRAPH_FOLDER);
			// generateGraphFrom(sw_conv, TIMES, names, sw, GRAPH_FOLDER);
			// generateGraphFrom(sw_klei, TIMES, names, sw, GRAPH_FOLDER);
		}

		if (GENERATE_DATA) {
			generateData(basic, MAX_TIMES, names, GRAPH_FOLDER);
			// generateData(lmc_cont, MAX_TIMES, names, GRAPH_FOLDER);
			// generateData(lmc_conv, MAX_TIMES, names, GRAPH_FOLDER);
			// generateData(lmc_klei, MAX_TIMES, names, GRAPH_FOLDER);
			// generateData(sw_cont, MAX_TIMES, names, GRAPH_FOLDER);
			// generateData(sw_conv, MAX_TIMES, names, GRAPH_FOLDER);
			// generateData(sw_klei, MAX_TIMES, names, GRAPH_FOLDER);
		}

		if (PLOT_DATA) {
			plot(getData(basic, names, GRAPH_FOLDER), "BASIC/");
		}

	}

	public static void plot(Series[] series, String folder) {
		System.out.println("\nPLOT: " + folder);
		if (!TEST) {
			Plot.multiAvg(series, folder);
		}
	}

	public static void createGraphFolders(
			HashMap<Transformation[], String> names, String GF) {
		Iterator<Transformation[]> iter = names.keySet().iterator();
		while (iter.hasNext()) {
			String folder = graphFolder(iter.next(), names, GF);
			if (!(new File(folder)).exists()) {
				Filewriter.generateFolders(folder + "test.txt");
			}
		}
	}

	public static String graphFolder(Transformation[] ts,
			HashMap<Transformation[], String> names, String GF) {
		return GF + names.get(ts) + "/";
	}

	public static Series[] getData(Transformation[][] tss,
			HashMap<Transformation[], String> names, String GF) {
		Series[] s = new Series[tss.length];
		for (int i = 0; i < tss.length; i++) {
			s[i] = getData(tss[i], names, GF);
		}
		return s;
	}

	public static Series getData(Transformation[] ts,
			HashMap<Transformation[], String> names, String GF) {
		ReadableFolder nw1 = new ReadableFolder(names.get(ts), names.get(ts),
				graphFolder(ts, names, GF), GraphReader.RING_NODES, RA, null);
		Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
		return Series.get(nw2);
	}

	public static Series[] generateData(Transformation[][] tss, int maxTimes,
			HashMap<Transformation[], String> names, String GF) {
		Series[] s = new Series[tss.length];
		for (int i = 0; i < tss.length; i++) {
			s[i] = generateData(tss[i], maxTimes, names, GF);
		}
		return s;
	}

	public static Series generateData(Transformation[] ts, int maxTimes,
			HashMap<Transformation[], String> names, String GF) {
		ReadableFolder nw1 = new ReadableFolder(names.get(ts), names.get(ts),
				graphFolder(ts, names, GF), GraphReader.RING_NODES, RA, null);
		Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
		int times = Math.min(maxTimes, nw1.getFiles().length);
		System.out.println("\nDATA: " + names.get(ts) + " (" + times + ")");
		if (!TEST) {
			Series s = Series.generate(nw2, times);
			if (PLOT_EACH) {
				Plot.multiAvg(s, "_separate/" + names.get(ts) + "/");
			}
			return s;
		} else {
			return null;
		}
	}

	public static void generateGraphFromSPI(Transformation[][] tss, int times,
			HashMap<Transformation[], String> names, String GF) {
		for (Transformation[] ts : tss) {
			generateGraphFromSPI(ts, times, names, GF);
		}
	}

	public static void generateGraphFromSPI(Transformation[] ts, int times,
			HashMap<Transformation[], String> names, String GF) {
		System.out.println("\nGRAPHS: " + names.get(ts));
		for (int i = 0; i < times; i++) {
			Timer timer = new Timer("  " + i + " @ "
					+ (new Time(System.currentTimeMillis())).toString());
			Network nw1 = new ReadableFile("SPI", "spi", SPI,
					GraphReader.OWN_FORMAT, null, ts);
			Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
			String filename = graphFolder(ts, names, GF) + i + ".txt";
			if (!(new File(filename)).exists()) {
				if (!TEST) {
					Graph g = nw2.generate();
					for (Transformation t : ts) {
						g = t.transform(g);
					}
					GraphWriter
							.write(g, filename, GraphWriter.TO_STRING_FORMAT);
				}
				System.out.print("            ");
			} else {
				System.out.print("  [exists]  ");
			}
			timer.end();
		}
	}

	public static void generateGraphFrom(Transformation[][] tss, int times,
			HashMap<Transformation[], String> names, Transformation[] from,
			String GF) {
		for (Transformation[] ts : tss) {
			generateGraphFrom(ts, times, names, from, GF);
		}
	}

	public static void generateGraphFrom(Transformation[] ts, int times,
			HashMap<Transformation[], String> names, Transformation[] from,
			String GF) {
		System.out.println("\nGRAPHS " + names.get(ts) + " from "
				+ names.get(from));
		for (int i = 0; i < times; i++) {
			Timer timer = new Timer("  " + i + " @ "
					+ (new Time(System.currentTimeMillis())).toString());
			String src = graphFolder(from, names, GF) + i + ".txt";
			if (!(new File(src)).exists()) {
				System.out.print("  [skipping]");
			} else {
				Network nw1 = new ReadableFile(names.get(from),
						names.get(from), src, GraphReader.RING_NODES, null, ts);
				Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
				String filename = graphFolder(ts, names, GF) + i + ".txt";
				if (!(new File(filename)).exists()) {
					if (!TEST) {
						Graph g = nw2.generate();
						for (Transformation t : ts) {
							g = t.transform(g);
						}
						GraphWriter.write(g, filename,
								GraphWriter.TO_STRING_FORMAT);
					}
					System.out.print("            ");
				} else {
					System.out.print("  [exists]  ");
				}
			}
			timer.end();
		}
	}
}
