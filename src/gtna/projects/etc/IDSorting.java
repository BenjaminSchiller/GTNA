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
package gtna.projects.etc;

/**
 * @author "Benjamin Schiller"
 * 
 */
// TODO adapt to changes
public class IDSorting {
	// private static final String SPI_INPUT_GRAPH =
	// "./resources/SPI-3-LCC/2010-08.spi.txt";
	// private static final String SPI_INPUT_NAME = "SPI";
	// private static final String SPI_INPUT_FOLDER = "spi";
	// private static final String SPI_MAIN_FOLDER = "spi-embedding/";
	// private static final int SPI_TTL = 174;
	// private static final int SPI_NODES = 9223;
	//
	// private static final String WOT_INPUT_GRAPH =
	// "./resources/WOT-1-BD/2005-02-25.wot.txt";
	// private static final String WOT_INPUT_NAME = "WOT";
	// private static final String WOT_INPUT_FOLDER = "wot";
	// private static final String WOT_MAIN_FOLDER = "wot-embedding/";
	// private static final int WOT_TTL = 215;
	// private static final int WOT_NODES = 25487;
	//
	// private static final String INPUT_GRAPH = SPI_INPUT_GRAPH;
	// private static final String INPUT_NAME = SPI_INPUT_NAME;
	// private static final String INPUT_FOLDER = SPI_INPUT_FOLDER;
	// private static final String MAIN_FOLDER = SPI_MAIN_FOLDER;
	// private static final int TTL = SPI_TTL;
	// private static final int NODES = SPI_NODES;
	//
	// // private static final String INPUT_GRAPH = WOT_INPUT_GRAPH;
	// // private static final String INPUT_NAME = WOT_INPUT_NAME;
	// // private static final String INPUT_FOLDER = WOT_INPUT_FOLDER;
	// // private static final String MAIN_FOLDER = WOT_MAIN_FOLDER;
	// // private static final int TTL = WOT_TTL;
	// // private static final int NODES = WOT_NODES;
	//
	// private static final int KLEINBERG_TIMES = 1;
	//
	// private static final int LMC_I = 6000;
	// private static final int LMCA_I = 6000;
	//
	// private static final double P = 0;
	// private static final int C = 10;
	// private static final String[] LMCA = new String[] {
	// LMC.ATTACK_CONTRACTION,
	// LMC.ATTACK_CONVERGENCE, LMC.ATTACK_KLEINBERG };
	// private static final int[] LMC_A = new int[] { 1, 10, 100 };
	//
	// private static final int SW_I = 6000;
	// private static final int SWA_I = 6000;
	// private static final String[] SWA = new String[] {
	// Swapping.ATTACK_CONTRACTION, Swapping.ATTACK_CONVERGENCE,
	// Swapping.ATTACK_KLEINBERG };
	// private static final int[] SW_A = new int[] { 1, 10, 100 };
	//
	// private static final boolean TEST = false;
	// private static final boolean JPEG = true;
	// private static final boolean PLOT_EACH = false;
	//
	// private static boolean GENERATE_GRAPHS = false;
	// private static boolean GENERATE_DATA = false;
	// private static boolean PLOT_DATA = false;
	// private static boolean PRINT_TABLES = false;
	//
	// private static final RoutingAlgorithm RA = new
	// GreedyNextBestBacktracking(
	// TTL);
	//
	// // private static final String metrics = "ID_SPACE";
	// private static final String metrics =
	// "ID_SPACE_DISTANCES, ID_SPACE_HOPS, RL";
	//
	// private static int TIMES = 0;
	//
	// private static int MAX_TIMES = 0;
	//
	// public static void main(String[] args) {
	// // IDSorting.kleinberg();
	// // IDSorting.wot();
	// // IDSorting.spi();
	// // IDSorting.idSpace();
	//
	// args = new String[] { "PLOT" };
	// // args = new String[] { "TABLES", "1" };
	// IDSorting.bigClusterMachine(args);
	//
	// // execute(LMC.MODE_UNRESTRICTED, 0.000001, 1, 0);
	// // execute(LMC.MODE_UNRESTRICTED, 0.000001, 2, 0);
	// // execute(LMC.MODE_UNRESTRICTED, 0.000001, 3, 0);
	// // execute(LMC.MODE_UNRESTRICTED, 0.000001, 4, 0);
	// }
	//
	// public static void bigClusterMachine(String[] args) {
	// if (args.length == 5) {
	// int MODE = Integer.parseInt(args[0]);
	// if (MODE < 1 || 4 < MODE) {
	// System.out.println("invalid value for mode: " + MODE);
	// return;
	// }
	// int INDEX = Integer.parseInt(args[1]);
	// if (INDEX < -3 || 6 < INDEX || INDEX == 0) {
	// System.out.println("invalid value for index: " + INDEX);
	// return;
	// }
	// int T = Integer.parseInt(args[2]);
	// if (T < 1 || 100 < T) {
	// System.out.println("invalid value for times: " + T);
	// }
	// TIMES = T;
	// MAX_TIMES = T;
	// boolean GRAPHS = Boolean.parseBoolean(args[3]);
	// GENERATE_GRAPHS = GRAPHS;
	// boolean DATA = Boolean.parseBoolean(args[4]);
	// GENERATE_DATA = DATA;
	// PLOT_DATA = false;
	// PRINT_TABLES = false;
	// if (INDEX == -1) {
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, MODE, 1);
	// return;
	// } else if (INDEX == -2) {
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, MODE, 2);
	// return;
	// } else if (INDEX == -3) {
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, MODE, 3);
	// return;
	// }
	// int INDEX1 = INDEX * 3 + 1;
	// int INDEX2 = INDEX * 3 + 2;
	// int INDEX3 = INDEX * 3 + 3;
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, MODE, INDEX1);
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, MODE, INDEX2);
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, MODE, INDEX3);
	// } else if (args.length == 1 && args[0].equals("PLOT")) {
	// GENERATE_GRAPHS = false;
	// GENERATE_DATA = false;
	// PLOT_DATA = true;
	// PRINT_TABLES = false;
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, 1, 0);
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, 2, 0);
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, 3, 0);
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, 4, 0);
	// } else if (args.length == 2 && args[0].equals("TABLES")) {
	// GENERATE_GRAPHS = false;
	// GENERATE_DATA = false;
	// PLOT_DATA = false;
	// PRINT_TABLES = true;
	// int MODE = Integer.parseInt(args[1]);
	// execute(LMC.MODE_UNRESTRICTED, 0.000001, MODE, 0);
	// // execute(LMC.MODE_UNRESTRICTED, 0.000001, 1, 0);
	// // execute(LMC.MODE_UNRESTRICTED, 0.000001, 2, 0);
	// // execute(LMC.MODE_UNRESTRICTED, 0.000001, 3, 0);
	// // execute(LMC.MODE_UNRESTRICTED, 0.000001, 4, 0);
	// } else {
	// System.out.println("received " + args.length
	// + " arguments instead of the expected ...");
	// }
	// }
	//
	// public static void execute(String LMC_M, double D, int ATT, int INDEX) {
	// Stats stats = new Stats();
	// // String MAIN_FOLDER = "test-" + LMC_M + "-" + D + "-" + LMC_AS + "/";
	// // String MAIN_FOLDER = "TEST-" + System.currentTimeMillis() + "/";
	// // String MAIN_FOLDER = "ALL-NEW-1/";
	//
	// String LMC_AS = ATT == 1 ? LMC.ATTACKER_SELECTION_SMALLEST
	// : ATT == 2 ? LMC.ATTACKER_SELECTION_MEDIAN
	// : ATT == 3 ? LMC.ATTACKER_SELECTION_LARGEST
	// : LMC.ATTACKER_SELECTION_RANDOM;
	// String SW_AS = ATT == 1 ? Swapping.ATTACKER_SELECTION_SMALLEST
	// : ATT == 2 ? Swapping.ATTACKER_SELECTION_MEDIAN
	// : ATT == 3 ? Swapping.ATTACKER_SELECTION_LARGEST
	// : Swapping.ATTACKER_SELECTION_RANDOM;
	//
	// System.out.println("\n\n\n\n\n");
	// System.out
	// .println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	// System.out.println("~~ " + MAIN_FOLDER + " @ " + LMC_M + "(" + D + ") "
	// + LMC_AS);
	// System.out
	// .println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	//
	// String MAIN_DATA_FOLDER = "./data/" + MAIN_FOLDER;
	// String MAIN_PLOT_FOLDER = "./plots/" + MAIN_FOLDER;
	// // String MAIN_PLOT_FOLDER = "./plots/paper-lmc/" + LMC_M + "-" + D +
	// // "-"
	// // + LMC_AS + "/";
	// String GRAPH_FOLDER = "data/" + MAIN_FOLDER + "_G/";
	//
	// Config.overwrite("MAIN_DATA_FOLDER", MAIN_DATA_FOLDER);
	// Config.overwrite("MAIN_PLOT_FOLDER", MAIN_PLOT_FOLDER);
	// Config.overwrite("METRICS", metrics);
	// if (JPEG) {
	// Config.overwrite("PLOT_EXTENSION", ".jpg");
	// Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
	// }
	// Config.overwrite("GNUPLOT_DEFAULT_KEY", "left top");
	// // ID_SPACE_CIRCLE_SKEWED_PLOT_MODE_AVG = DOTS_ONLY
	// // Config.overwrite("RL_CRLD_PLOT_MODE_AVG", "DOTS_ONLY");
	//
	// /*
	// * Names
	// */
	//
	// HashMap<Transformation[], String> names = new HashMap<Transformation[],
	// String>();
	//
	// /*
	// * Basics
	// */
	//
	// Transformation[] r, lmc, sw;
	// r = new Transformation[] { new RandomID(RandomID.RING_NODE, 0) };
	// lmc = new Transformation[] { r[0], new LMC(LMC_I, LMC_M, P, "" + D, C) };
	// sw = new Transformation[] { r[0], new Swapping(SW_I) };
	//
	// names.put(r, "Random");
	// names.put(lmc, "LMC");
	// names.put(sw, "Swapping");
	//
	// Transformation[][] basic = new Transformation[][] { lmc, sw, r };
	//
	// /*
	// * LMC Attacks
	// */
	//
	// Transformation[][] lmc_cont, lmc_conv, lmc_klei;
	// lmc_cont = new Transformation[LMC_A.length][1];
	// lmc_conv = new Transformation[LMC_A.length][1];
	// lmc_klei = new Transformation[LMC_A.length][1];
	// for (int i = 0; i < LMC_A.length; i++) {
	// lmc_cont[i][0] = new LMC(LMCA_I, LMC_M, P, "" + D, C, LMCA[0],
	// LMC_AS, LMC_A[i]);
	// lmc_conv[i][0] = new LMC(LMCA_I, LMC_M, P, "" + D, C, LMCA[1],
	// LMC_AS, LMC_A[i]);
	// lmc_klei[i][0] = new LMC(LMCA_I, LMC_M, P, "" + D, C, LMCA[2],
	// LMC_AS, LMC_A[i]);
	// names.put(lmc_cont[i], "LMC-" + D + "-Contraction-" + LMC_A[i]
	// + "-" + LMC_AS);
	// names.put(lmc_conv[i], "LMC-" + D + "-Convergence-" + LMC_A[i]
	// + "-" + LMC_AS);
	// names.put(lmc_klei[i], "LMC-" + D + "-Kleinberg-" + LMC_A[i] + "-"
	// + LMC_AS);
	// }
	//
	// /*
	// * SW Attacks
	// */
	//
	// Transformation[][] sw_cont, sw_conv, sw_klei;
	// sw_cont = new Transformation[SW_A.length][1];
	// sw_conv = new Transformation[SW_A.length][1];
	// sw_klei = new Transformation[SW_A.length][1];
	// for (int i = 0; i < SW_A.length; i++) {
	// sw_cont[i][0] = new Swapping(SWA_I, D, SWA[0], SW_AS, SW_A[i]);
	// sw_conv[i][0] = new Swapping(SWA_I, D, SWA[1], SW_AS, SW_A[i]);
	// sw_klei[i][0] = new Swapping(SWA_I, D, SWA[2], SW_AS, SW_A[i]);
	// names.put(sw_cont[i], "SW-" + D + "-Contraction-" + SW_A[i] + "-"
	// + SW_AS);
	// names.put(sw_conv[i], "SW-" + D + "-Convergence-" + SW_A[i] + "-"
	// + SW_AS);
	// names.put(sw_klei[i], "SW-" + D + "-Kleinberg-" + SW_A[i] + "-"
	// + SW_AS);
	// }
	//
	// /*
	// * Generate Graphs / Data / Plots
	// */
	//
	// if (GENERATE_GRAPHS) {
	// createGraphFolders(names, GRAPH_FOLDER);
	//
	// if (INDEX == 1 || INDEX == 0)
	// generateGraphFromInput(r, TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 2 || INDEX == 0)
	// generateGraphFromInput(lmc, TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 3 || INDEX == 0)
	// generateGraphFromInput(sw, TIMES, names, GRAPH_FOLDER);
	//
	// if (INDEX == 4 || INDEX == 0)
	// generateGraphFrom(lmc_cont[0], TIMES, names, lmc, GRAPH_FOLDER);
	// if (INDEX == 5 || INDEX == 0)
	// generateGraphFrom(lmc_cont[1], TIMES, names, lmc, GRAPH_FOLDER);
	// if (INDEX == 6 || INDEX == 0)
	// generateGraphFrom(lmc_cont[2], TIMES, names, lmc, GRAPH_FOLDER);
	//
	// if (INDEX == 7 || INDEX == 0)
	// generateGraphFrom(lmc_conv[0], TIMES, names, lmc, GRAPH_FOLDER);
	// if (INDEX == 8 || INDEX == 0)
	// generateGraphFrom(lmc_conv[1], TIMES, names, lmc, GRAPH_FOLDER);
	// if (INDEX == 9 || INDEX == 0)
	// generateGraphFrom(lmc_conv[2], TIMES, names, lmc, GRAPH_FOLDER);
	//
	// if (INDEX == 10 || INDEX == 0)
	// generateGraphFrom(lmc_klei[0], TIMES, names, lmc, GRAPH_FOLDER);
	// if (INDEX == 11 || INDEX == 0)
	// generateGraphFrom(lmc_klei[1], TIMES, names, lmc, GRAPH_FOLDER);
	// if (INDEX == 12 || INDEX == 0)
	// generateGraphFrom(lmc_klei[2], TIMES, names, lmc, GRAPH_FOLDER);
	//
	// if (INDEX == 13 || INDEX == 0)
	// generateGraphFrom(sw_cont[0], TIMES, names, sw, GRAPH_FOLDER);
	// if (INDEX == 14 || INDEX == 0)
	// generateGraphFrom(sw_cont[1], TIMES, names, sw, GRAPH_FOLDER);
	// if (INDEX == 15 || INDEX == 0)
	// generateGraphFrom(sw_cont[2], TIMES, names, sw, GRAPH_FOLDER);
	//
	// if (INDEX == 16 || INDEX == 0)
	// generateGraphFrom(sw_conv[0], TIMES, names, sw, GRAPH_FOLDER);
	// if (INDEX == 17 || INDEX == 0)
	// generateGraphFrom(sw_conv[1], TIMES, names, sw, GRAPH_FOLDER);
	// if (INDEX == 18 || INDEX == 0)
	// generateGraphFrom(sw_conv[2], TIMES, names, sw, GRAPH_FOLDER);
	//
	// if (INDEX == 19 || INDEX == 0)
	// generateGraphFrom(sw_klei[0], TIMES, names, sw, GRAPH_FOLDER);
	// if (INDEX == 20 || INDEX == 0)
	// generateGraphFrom(sw_klei[1], TIMES, names, sw, GRAPH_FOLDER);
	// if (INDEX == 21 || INDEX == 0)
	// generateGraphFrom(sw_klei[2], TIMES, names, sw, GRAPH_FOLDER);
	// }
	//
	// if (GENERATE_DATA) {
	// // generateData(basic, MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 1 || INDEX == 0)
	// generateData(basic[0], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 2 || INDEX == 0)
	// generateData(basic[1], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 3 || INDEX == 0)
	// generateData(basic[2], MAX_TIMES, names, GRAPH_FOLDER);
	//
	// // generateData(lmc_cont, MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 4 || INDEX == 0)
	// generateData(lmc_cont[0], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 5 || INDEX == 0)
	// generateData(lmc_cont[1], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 6 || INDEX == 0)
	// generateData(lmc_cont[2], MAX_TIMES, names, GRAPH_FOLDER);
	//
	// // generateData(lmc_conv, MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 7 || INDEX == 0)
	// generateData(lmc_conv[0], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 8 || INDEX == 0)
	// generateData(lmc_conv[1], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 9 || INDEX == 0)
	// generateData(lmc_conv[2], MAX_TIMES, names, GRAPH_FOLDER);
	//
	// // generateData(lmc_klei, MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 10 || INDEX == 0)
	// generateData(lmc_klei[0], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 11 || INDEX == 0)
	// generateData(lmc_klei[1], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 12 || INDEX == 0)
	// generateData(lmc_klei[2], MAX_TIMES, names, GRAPH_FOLDER);
	//
	// // generateData(sw_cont, MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 13 || INDEX == 0)
	// generateData(sw_cont[0], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 14 || INDEX == 0)
	// generateData(sw_cont[1], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 15 || INDEX == 0)
	// generateData(sw_cont[2], MAX_TIMES, names, GRAPH_FOLDER);
	//
	// // generateData(sw_conv, MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 16 || INDEX == 0)
	// generateData(sw_conv[0], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 17 || INDEX == 0)
	// generateData(sw_conv[1], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 18 || INDEX == 0)
	// generateData(sw_conv[2], MAX_TIMES, names, GRAPH_FOLDER);
	//
	// // generateData(sw_klei, MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 19 || INDEX == 0)
	// generateData(sw_klei[0], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 20 || INDEX == 0)
	// generateData(sw_klei[1], MAX_TIMES, names, GRAPH_FOLDER);
	// if (INDEX == 21 || INDEX == 0)
	// generateData(sw_klei[2], MAX_TIMES, names, GRAPH_FOLDER);
	// }
	//
	// if (PLOT_DATA) {
	// String f = "/";
	//
	// plot(getData(basic, names, GRAPH_FOLDER), "BASIC" + f);
	//
	// plot(Util.combine(
	// getData(new Transformation[][] { lmc }, names, GRAPH_FOLDER),
	// getData(lmc_cont, names, GRAPH_FOLDER)), "LMC-" + D
	// + "-Contraction-" + LMC_AS + f);
	//
	// plot(Util.combine(
	// getData(new Transformation[][] { lmc }, names, GRAPH_FOLDER),
	// getData(lmc_conv, names, GRAPH_FOLDER)), "LMC-" + LMC_M
	// + "-" + D + "-Convergence-" + LMC_AS + f);
	//
	// plot(Util.combine(
	// getData(new Transformation[][] { lmc }, names, GRAPH_FOLDER),
	// getData(lmc_klei, names, GRAPH_FOLDER)), "LMC-" + LMC_M
	// + "-" + D + "-Kleinberg-" + LMC_AS + f);
	//
	// plot(Util.combine(
	// getData(new Transformation[][] { sw }, names, GRAPH_FOLDER),
	// getData(sw_cont, names, GRAPH_FOLDER)), "SW-Contraction-"
	// + SW_AS + f);
	//
	// plot(Util.combine(
	// getData(new Transformation[][] { sw }, names, GRAPH_FOLDER),
	// getData(sw_conv, names, GRAPH_FOLDER)), "SW-Convergence-"
	// + SW_AS + f);
	//
	// plot(Util.combine(
	// getData(new Transformation[][] { sw }, names, GRAPH_FOLDER),
	// getData(sw_klei, names, GRAPH_FOLDER)), "SW-Kleinberg-"
	// + SW_AS + f);
	//
	// Transformation[][] att1 = new Transformation[][] { lmc,
	// lmc_cont[0], lmc_conv[0], lmc_klei[0], sw, sw_cont[0],
	// sw_conv[0], sw_klei[0], r };
	// plot(getData(att1, names, GRAPH_FOLDER), "ATT-1-" + LMC_AS + f);
	//
	// Transformation[][] att10 = new Transformation[][] { lmc,
	// lmc_cont[1], lmc_conv[1], lmc_klei[1], sw, sw_cont[1],
	// sw_conv[1], sw_klei[1], r };
	// plot(getData(att10, names, GRAPH_FOLDER), "ATT-10-" + LMC_AS + f);
	//
	// Transformation[][] att100 = new Transformation[][] { lmc,
	// lmc_cont[2], lmc_conv[2], lmc_klei[2], sw, sw_cont[2],
	// sw_conv[2], sw_klei[2], r };
	// plot(getData(att100, names, GRAPH_FOLDER), "ATT-100-" + LMC_AS + f);
	//
	// Transformation[][] attWorst = new Transformation[][] { lmc,
	// lmc_cont[lmc_cont.length - 1],
	// lmc_conv[lmc_conv.length - 1],
	// lmc_klei[lmc_klei.length - 1], sw,
	// sw_cont[sw_cont.length - 1], sw_conv[sw_conv.length - 1],
	// sw_klei[sw_klei.length - 1], r };
	// plot(getData(attWorst, names, GRAPH_FOLDER), "ATT-WORST-" + LMC_AS
	// + f);
	//
	// String F = Config.get("MAIN_PLOT_FOLDER");
	// String[] folders = new String[] { "ATT-1", "ATT-10", "ATT-100",
	// "ATT-WORST", "BASIC", "LMC-1.0E-6-Contraction",
	// "LMC-UNRESTRICTED-1.0E-6-Convergence",
	// "LMC-UNRESTRICTED-1.0E-6-Kleinberg", "SW-Contraction",
	// "SW-Convergence", "SW-Kleinberg" };
	// String[] files = new String[] {
	// "idspaceDistances-neighbors-edf-log",
	// "idspaceDistances-neighbors-edf",
	// "idspaceDistances-neighbors-log",
	// "idspaceDistances-neighbors",
	// "idspaceDistances-successor-edf-log",
	// "idspaceDistances-successor-edf",
	// "idspaceDistances-successor-log",
	// "idspaceDistances-successor", "idspaceHops-successor-edf",
	// "idspaceHops-successor", "rl-crld", "rl-rld" };
	// for (int i = 0; i < files.length; i++) {
	// // execute("rm -r ../_" + files[i], F);
	// execute("mkdir ../_" + files[i], F);
	// }
	// for (int i = 0; i < folders.length; i++) {
	// for (int j = 0; j < files.length; j++) {
	// String file = files[j];
	// String folder = folders[i] + "-" + LMC_AS;
	// String src = folder + "/" + file + ".jpg";
	// String dst = "../_" + file + "/" + folder + ".jpg";
	// execute("ln " + src + " " + dst, F);
	// }
	// }
	// }
	//
	// if (PRINT_TABLES) {
	// Transformation[][][] t = new Transformation[9][3][];
	// t[0] = new Transformation[][] { lmc, lmc, lmc };
	// t[1] = new Transformation[][] { lmc_cont[0], lmc_conv[0],
	// lmc_klei[0] };
	// t[2] = new Transformation[][] { lmc_cont[1], lmc_conv[1],
	// lmc_klei[1] };
	// t[3] = new Transformation[][] { lmc_cont[2], lmc_conv[2],
	// lmc_klei[2] };
	// t[4] = new Transformation[][] { r, r, r };
	// t[5] = new Transformation[][] { sw, sw, sw };
	// t[6] = new Transformation[][] { sw_cont[0], sw_conv[0], sw_klei[0] };
	// t[7] = new Transformation[][] { sw_cont[1], sw_conv[1], sw_klei[1] };
	// t[8] = new Transformation[][] { sw_cont[2], sw_conv[2], sw_klei[2] };
	//
	// Transformation[] kleinberg = new Transformation[0];
	// names.put(kleinberg, "kleinberg1d-0-12-1.0-false");
	// t = new Transformation[1][1][1];
	// t[0][0] = kleinberg;
	//
	// String[][] averageEdgeLength = new String[t.length][t[0].length];
	// String[][] successorDistancePercentage = new
	// String[t.length][t[0].length];
	// String[][] routingSuccessRate = new String[t.length][t[0].length];
	// String[][] successorHopsPercentage1 = new String[t.length][t[0].length];
	// String[][] successorHopsPercentage2 = new String[t.length][t[0].length];
	// String[][] successorHopsPercentage3 = new String[t.length][t[0].length];
	//
	// for (int i = 0; i < t.length; i++) {
	// for (int j = 0; j < t[i].length; j++) {
	// if (j > 0 && (i == 0 || i == 4 || i == 5)) {
	// averageEdgeLength[i][j] = averageEdgeLength[i][0];
	// successorDistancePercentage[i][j] = successorDistancePercentage[i][0];
	// routingSuccessRate[i][j] = routingSuccessRate[i][0];
	// successorHopsPercentage1[i][j] = successorHopsPercentage1[i][0];
	// successorHopsPercentage2[i][j] = successorHopsPercentage2[i][0];
	// successorHopsPercentage3[i][j] = successorHopsPercentage3[i][0];
	// continue;
	// }
	// averageEdgeLength[i][j] = getAverageEdgeLength(t[i][j],
	// names, GRAPH_FOLDER);
	// successorDistancePercentage[i][j] = getSuccessorDistancePercentage(
	// t[i][j], names, GRAPH_FOLDER);
	// routingSuccessRate[i][j] = getRoutingSuccessRate(t[i][j],
	// names, GRAPH_FOLDER);
	// successorHopsPercentage1[i][j] = getSuccessorHopsPercentage(
	// t[i][j], names, GRAPH_FOLDER, 1);
	// successorHopsPercentage2[i][j] = getSuccessorHopsPercentage(
	// t[i][j], names, GRAPH_FOLDER, 2);
	// successorHopsPercentage3[i][j] = getSuccessorHopsPercentage(
	// t[i][j], names, GRAPH_FOLDER, 3);
	// }
	// }
	//
	// printTable(averageEdgeLength, "tab:edgeLength",
	// "averageEdgeLength", SW_AS);
	// printTable(successorDistancePercentage, "tab:successorDistance",
	// "Percentage of $d(v, succ(v)) \\leq 1/|V|$", SW_AS);
	// printTable(routingSuccessRate, "tab:routingSuccessRate",
	// "successRate", SW_AS);
	// printTable(successorHopsPercentage1,
	// "tab:successorHopsPercentage1", "successorHopsPercentage1",
	// SW_AS);
	// printTable(successorHopsPercentage2,
	// "tab:successorHopsPercentage2", "successorHopsPercentage2",
	// SW_AS);
	// printTable(successorHopsPercentage3,
	// "tab:successorHopsPercentage3", "successorHopsPercentage3",
	// SW_AS);
	// }
	//
	// stats.end();
	//
	// }
	//
	// public static void plot(Series[] series, String folder) {
	// System.out.println("\nPLOT: " + folder);
	// if (!TEST) {
	// Plot.multiAvg(series, folder);
	// }
	// }
	//
	// private static void printTable(String[][] v, String label, String
	// caption,
	// String AS) {
	// System.out.println("\n");
	// System.out.println("......" + caption + "......" + AS + "......");
	// for (int i = 0; i < v.length; i++) {
	// for (int j = 0; j < v[i].length; j++) {
	// System.out.print("& " + v[i][j] + "	");
	// }
	// System.out.println();
	// }
	// System.out.println("\n");
	// // System.out.println("\n");
	// // System.out.println("\\begin{center}");
	// // System.out.println("\\begin{table*}[ht]");
	// // System.out.println("\\label{" + label + "}");
	// // System.out.println("\\center");
	// // System.out.println("\\caption{" + caption + "}");
	// // System.out.println("\\begin{tabular}{cc rrr}");
	// // System.out.println("\\toprule");
	// // System.out
	// // .println("\\multicolumn{2}{c}{} & \\multicolumn{3}{c}{\\textbf{"
	// // + AS + "}} \\\\");
	// // System.out
	// // .println("& \\textbf{Attackers} & Contr. & Div. & Dist. \\\\");
	// // System.out.println("\\midrule");
	// // System.out.println("\\textbf{LMC} & \\textbf{0} & 	" + v[0][0] +
	// // " &	"
	// // + v[0][1] + " &	" + v[0][2] + " \\\\");
	// // System.out.println("& \\textbf{1} & 				" + v[1][0] + " &	" + v[1][1]
	// // + " &	" + v[1][2] + " \\\\");
	// // System.out.println("& \\textbf{10} & 				" + v[2][0] + " &	" +
	// // v[2][1]
	// // + " &	" + v[2][2] + " \\\\");
	// // System.out.println("& \\textbf{100} & 			" + v[3][0] + " &	" +
	// // v[3][1]
	// // + " &	" + v[3][2] + "	\\\\");
	// // System.out.println("\\midrule");
	// // System.out.println("\\textbf{Rand} & \\textbf{0} & 		" + v[4][0]
	// // + " &	" + v[4][1] + " &	" + v[4][2] + " \\\\");
	// // System.out.println("\\midrule");
	// // System.out.println("\\textbf{SW} & \\textbf{0} & 		" + v[5][0] +
	// // " &	"
	// // + v[5][1] + " &	" + v[5][2] + " \\\\");
	// // System.out.println("& \\textbf{1} & 				" + v[6][0] + " &	" + v[6][1]
	// // + " &	" + v[6][2] + " \\\\");
	// // System.out.println("& \\textbf{10} & 				" + v[7][0] + " &	" +
	// // v[7][1]
	// // + " &	" + v[7][2] + " \\\\");
	// // System.out.println("& \\textbf{100} & 			" + v[8][0] + " &	" +
	// // v[8][1]
	// // + " &	" + v[8][2] + " \\\\");
	// // System.out.println("\\bottomrule");
	// // System.out.println("\\end{tabular}");
	// // System.out.println("$\\;$\\\\$\\;$\\\\");
	// // System.out.println("\\end{table*}");
	// // System.out.println("\\end{center}");
	// // System.out.println("\n");
	// }
	//
	// private static String getAverageEdgeLength(Transformation[] t,
	// HashMap<Transformation[], String> names, String GF) {
	// int counter = 0;
	// String filename = graphFolder(t, names, GF) + counter + ".txt";
	// while ((new File(filename)).exists()) {
	// counter++;
	// filename = graphFolder(t, names, GF) + counter + ".txt";
	// }
	// System.out.println("averageEdgeLength: " + names.get(t));
	// double[] averages = new double[counter];
	// for (int i = 0; i < averages.length; i++) {
	// filename = graphFolder(t, names, GF) + i + ".txt";
	// Graph g = GraphReader.read(filename, GraphReader.RING_NODES,
	// "temp-" + i);
	// Edge[] edges = g.edges();
	// for (int j = 0; j < edges.length; j++) {
	// RingNode src = (RingNode) edges[j].src;
	// RingNode dst = (RingNode) edges[j].dst;
	// averages[i] += src.getID().dist(dst.getID());
	// }
	// averages[i] /= (double) edges.length;
	// }
	// String res = LaTex.formatNumber(Util.avg(averages), 4);
	// return res;
	// }
	//
	// private static String getSuccessorDistancePercentage(Transformation[] t,
	// HashMap<Transformation[], String> names, String GF) {
	// double compare = 1.0 / (double) NODES;
	// int counter = 0;
	// String filename = graphFolder(t, names, GF) + counter + ".txt";
	// while ((new File(filename)).exists()) {
	// counter++;
	// filename = graphFolder(t, names, GF) + counter + ".txt";
	// }
	// System.out.println("successorDistance: " + names.get(t));
	// double[] percentages = new double[counter];
	// for (int i = 0; i < percentages.length; i++) {
	// filename = graphFolder(t, names, GF) + i + ".txt";
	// Graph g = GraphReader.read(filename, GraphReader.RING_NODES,
	// "temp-" + i);
	//
	// double[] distances = IDSpaceDistances.computeSuccessorDists(g);
	// double count = 0;
	// for (int j = 0; j < distances.length; j++) {
	// if (distances[j] <= compare) {
	// count++;
	// }
	// }
	// percentages[i] = count / NODES;
	// }
	// String res = LaTex.formatNumber(Util.avg(percentages) * 100, 2);
	// return res;
	// }
	//
	// private static String getRoutingSuccessRate(Transformation[] t,
	// HashMap<Transformation[], String> names, String GRAPH_FOLDER) {
	// Series s = getData(t, names, GRAPH_FOLDER);
	// double[][] v = DataReader.readDouble2D(s.avgDataFolder()
	// + "rl-crld.txt");
	// return LaTex.formatNumber(v[v.length - 1][1] * 100, 2);
	// }
	//
	// private static String getSuccessorHopsPercentage(Transformation[] t,
	// HashMap<Transformation[], String> names, String GRAPH_FOLDER,
	// int hops) {
	// Series s = getData(t, names, GRAPH_FOLDER);
	// double[][] v = DataReader.readDouble2D(s.avgDataFolder()
	// + "idspaceHops-successor-edf.txt");
	// return LaTex.formatNumber(v[hops][1] * 100, 2);
	// }
	//
	// public static void createGraphFolders(
	// HashMap<Transformation[], String> names, String GF) {
	// Iterator<Transformation[]> iter = names.keySet().iterator();
	// while (iter.hasNext()) {
	// String folder = graphFolder(iter.next(), names, GF);
	// if (!(new File(folder)).exists()) {
	// Filewriter.generateFolders(folder + "test.txt");
	// }
	// }
	// }
	//
	// public static String graphFolder(Transformation[] ts,
	// HashMap<Transformation[], String> names, String GF) {
	// return GF + names.get(ts) + "/";
	// }
	//
	// public static Series[] getData(Transformation[][] tss,
	// HashMap<Transformation[], String> names, String GF) {
	// Series[] s = new Series[tss.length];
	// for (int i = 0; i < tss.length; i++) {
	// s[i] = getData(tss[i], names, GF);
	// }
	// return s;
	// }
	//
	// public static Series getData(Transformation[] ts,
	// HashMap<Transformation[], String> names, String GF) {
	// ReadableFolder nw1 = new ReadableFolder(names.get(ts), names.get(ts),
	// graphFolder(ts, names, GF), GraphReader.RING_NODES, RA, null);
	// Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
	// return Series.get(nw2);
	// }
	//
	// public static Series[] generateData(Transformation[][] tss, int maxTimes,
	// HashMap<Transformation[], String> names, String GF) {
	// Series[] s = new Series[tss.length];
	// for (int i = 0; i < tss.length; i++) {
	// s[i] = generateData(tss[i], maxTimes, names, GF);
	// }
	// return s;
	// }
	//
	// public static Series generateData(Transformation[] ts, int maxTimes,
	// HashMap<Transformation[], String> names, String GF) {
	// ReadableFolder nw1 = new ReadableFolder(names.get(ts), names.get(ts),
	// graphFolder(ts, names, GF), GraphReader.RING_NODES, RA, null);
	// Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
	// int times = Math.min(maxTimes, nw1.getFiles().length);
	// System.out.println("\nDATA: " + names.get(ts) + " (" + times + ")");
	// if (!TEST) {
	// Series s = Series.generate(nw2, times);
	// if (PLOT_EACH) {
	// Plot.multiAvg(s, "_separate/" + names.get(ts) + "/");
	// }
	// return s;
	// } else {
	// if (PLOT_EACH) {
	// System.out.println("PLOT: " + "_separate/" + names.get(ts)
	// + "/");
	// }
	// return null;
	// }
	// }
	//
	// public static void generateGraphFromInput(Transformation[][] tss,
	// int times, HashMap<Transformation[], String> names, String GF) {
	// for (Transformation[] ts : tss) {
	// generateGraphFromInput(ts, times, names, GF);
	// }
	// }
	//
	// public static void generateGraphFromInput(Transformation[] ts, int times,
	// HashMap<Transformation[], String> names, String GF) {
	// System.out.println("\nGRAPHS: " + names.get(ts));
	// for (int i = 0; i < times; i++) {
	// Timer timer = new Timer("  " + i + " @ "
	// + (new Time(System.currentTimeMillis())).toString());
	// Network nw1 = new ReadableFile(INPUT_NAME, INPUT_FOLDER,
	// INPUT_GRAPH, GraphReader.OWN_FORMAT, null, ts);
	// Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
	// String filename = graphFolder(ts, names, GF) + i + ".txt";
	// if (!(new File(filename)).exists()) {
	// if (!TEST) {
	// Graph g = nw2.generate();
	// for (Transformation t : ts) {
	// g = t.transform(g);
	// }
	// GraphWriter
	// .write(g, filename, GraphWriter.TO_STRING_FORMAT);
	// }
	// System.out.print("            ");
	// } else {
	// System.out.print("  [exists]  ");
	// }
	// timer.end();
	// }
	// }
	//
	// public static void generateGraphFrom(Transformation[][] tss, int times,
	// HashMap<Transformation[], String> names, Transformation[] from,
	// String GF) {
	// for (Transformation[] ts : tss) {
	// generateGraphFrom(ts, times, names, from, GF);
	// }
	// }
	//
	// public static void generateGraphFrom(Transformation[] ts, int times,
	// HashMap<Transformation[], String> names, Transformation[] from,
	// String GF) {
	// System.out.println("\nGRAPHS " + names.get(ts) + " from "
	// + names.get(from));
	// for (int i = 0; i < times; i++) {
	// Timer timer = new Timer("  " + i + " @ "
	// + (new Time(System.currentTimeMillis())).toString());
	// String src = graphFolder(from, names, GF) + i + ".txt";
	// if (!(new File(src)).exists()) {
	// System.out.print("  [skipping]");
	// } else {
	// Network nw1 = new ReadableFile(names.get(from),
	// names.get(from), src, GraphReader.RING_NODES, null, ts);
	// Network nw2 = new DescriptionWrapper(nw1, names.get(ts));
	// String filename = graphFolder(ts, names, GF) + i + ".txt";
	// if (!(new File(filename)).exists()) {
	// if (!TEST) {
	// Graph g = nw2.generate();
	// for (Transformation t : ts) {
	// g = t.transform(g);
	// }
	// GraphWriter.write(g, filename,
	// GraphWriter.TO_STRING_FORMAT);
	// }
	// System.out.print("            ");
	// } else {
	// System.out.print("  [exists]  ");
	// }
	// }
	// timer.end();
	// }
	// }
	//
	// public static boolean execute(String cmd, String dir) {
	// try {
	// Process p = null;
	// if (dir == null) {
	// p = Runtime.getRuntime().exec(cmd);
	// // System.out.println("EXECUTING: " + cmd);
	// } else {
	// p = Runtime.getRuntime().exec(cmd, null, new File(dir));
	// // System.out.println("EXECUTING: " + cmd + " IN " + dir);
	// }
	// if (true) {
	// InputStream stderr = p.getInputStream();
	// // InputStream stderr = p.getErrorStream();
	// InputStreamReader isr = new InputStreamReader(stderr);
	// BufferedReader br = new BufferedReader(isr);
	// String line = null;
	// while ((line = br.readLine()) != null) {
	// Output.writeln(line);
	// }
	// }
	// p.waitFor();
	// return true;
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// return false;
	// }
	//
	// public static void kleinberg() {
	// Stats stats = new Stats();
	//
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/kleinberg/");
	// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/kleinberg/");
	// Config.overwrite("METRICS",
	// "DD, CC, RCC, SPL, ID_SPACE_DISTANCES, ID_SPACE_HOPS, RL");
	// Config.overwrite("PLOT_EXTENSION", ".jpg");
	// Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
	//
	// Network k0 = new Kleinberg1D(NODES, 0, 12, 1.0, false, RA, null);
	// Network k1 = new Kleinberg1D(NODES, 0, 6, 1.0, true, RA, null);
	// Network k2 = new Kleinberg1D(NODES, 1, 10, 1.0, false, RA, null);
	// Network k3 = new Kleinberg1D(NODES, 1, 5, 1.0, true, RA, null);
	//
	// Network[] k = new Network[] { k0 };
	// // Network[] k = new Network[] { k0, k1, k2, k3 };
	//
	// // for (int i = 0; i < k.length; i++) {
	// // Graph g = k[i].generate();
	// // NodeImpl[] nodes = NodeSorting.degreeDesc(g.nodes, new Random());
	// // System.out.println("median degree of " + k[i].description() +
	// // " ---> "
	// // + nodes[nodes.length / 2]);
	// // }
	//
	// // Series[] s = Series.get(k);
	// // Series[] s = Series.generate(k, KLEINBERG_TIMES);
	// // Plot.multiAvg(s, "kleinberg-1d-" + NODES + "/");
	// GraphWriter.write(k0.generate(),
	// "./data/spi-embedding/_G/kleinberg1d-0-12-1.0-false/0.txt",
	// GraphWriter.TO_STRING_FORMAT);
	//
	// stats.end();
	// return;
	// }
	//
	// public static void wot() {
	// Stats stats = new Stats();
	//
	// Graph g = GraphReader.read(WOT_INPUT_GRAPH);
	// Node[] nodes = NodeSorting.degreeDesc(g.nodes, new Random());
	// System.out.println("Median degree node: " + nodes[nodes.length / 2]);
	//
	// Config.overwrite("METRICS", "DD, CC, RCC, SPL");
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/wot/");
	// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/wot/");
	// Config.overwrite("PLOT_EXTENSION", ".jpg");
	// Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
	//
	// Network nw = new ReadableFile(WOT_INPUT_NAME, WOT_INPUT_FOLDER,
	// WOT_INPUT_GRAPH, GraphReader.OWN_FORMAT, null, null);
	// Series s = Series.generate(nw, 1);
	//
	// Plot.allMulti(s, "multi-1/");
	// Config.overwrite("DD_PLOT_DATA", "DDO");
	// Plot.allMulti(s, "multi-2/");
	// Config.overwrite("DD_PLOT_LOGSCALE_X", "true");
	// Plot.allMulti(s, "multi-3/");
	// Plot.allSingle(new Series[] { s }, "singles/");
	//
	// stats.end();
	// return;
	// }
	//
	// public static void spi() {
	// Stats stats = new Stats();
	//
	// Graph g = GraphReader.read(SPI_INPUT_GRAPH);
	// Node[] nodes = NodeSorting.degreeDesc(g.nodes, new Random());
	// System.out.println("Median degree node: " + nodes[nodes.length / 2]);
	//
	// Config.overwrite("METRICS", "DD, CC, RCC, SPL");
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/spi/");
	// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/spi/");
	// Config.overwrite("PLOT_EXTENSION", ".jpg");
	// Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
	//
	// Network nw = new ReadableFile(SPI_INPUT_NAME, SPI_INPUT_FOLDER,
	// SPI_INPUT_GRAPH, GraphReader.OWN_FORMAT, null, null);
	// Series s = Series.generate(nw, 1);
	//
	// Plot.allMulti(s, "multi-1/");
	// Config.overwrite("DD_PLOT_DATA", "DDO");
	// Plot.allMulti(s, "multi-2/");
	// Config.overwrite("DD_PLOT_LOGSCALE_X", "true");
	// Plot.allMulti(s, "multi-3/");
	// Plot.allSingle(new Series[] { s }, "singles/");
	//
	// stats.end();
	// return;
	// }
	//
	// public static void idSpace() {
	// /**
	// * ID Space computation
	// */
	// // if (true) {
	// // Config.overwrite("METRICS", "ID_SPACE");
	// // String main = "/Users/benni/TUD/GTNA/data/ALL-NEW-1/_G/";
	// // File graphs = new File(main);
	// // String[] folders = graphs.list();
	// // for (int i = 0; i < folders.length; i++) {
	// // if (folders[i].equals(".DS_Store")) {
	// // continue;
	// // }
	// // for (int j = 0; j < 100; j++) {
	// // Config.overwrite("MAIN_DATA_FOLDER", "./data/ID-SPACE/" + j
	// // + "/");
	// // Config.overwrite("MAIN_PLOT_FOLDER", "./plots/ID-SPACE/"
	// // + j + "/");
	// // String filename = main + folders[i] + "/" + j + ".txt";
	// // Network nw = new ReadableFile(folders[i], folders[i],
	// // filename, GraphReader.RING_NODES, null, null);
	// // Series s = Series.generate(nw, 1);
	// // Plot.multiAvg(s, folders[i] + "/");
	// // }
	// // for (int j = 0; j < 100; j++) {
	// // String src = "./plots/ID-SPACE/" + j + "/" + folders[i]
	// // + "/idspace-circleBinned.pdf";
	// // String dst = "./plots/ID-SPACES/" + folders[i] + "/" + j
	// // + ".pdf";
	// // String cmd = "mv " + src + " " + dst;
	// // System.out.println(cmd);
	// // execute(cmd, "./");
	// // }
	// // }
	// // return;
	// // }
	//
	// /**
	// * ID Space plotting
	// */
	// if (true) {
	// int SW = 0;
	// int LMC = 10;
	// int SWA = 31;
	//
	// Config.overwrite("METRICS", "ID_SPACE");
	//
	// String filename =
	// "/Users/benni/TUD/GTNA/data/spi-embedding/_G/LMC/0.txt";
	//
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/spi-idspace/" + SW
	// + "/");
	// Network swapping = new ReadableFile("Swapping", "Swapping",
	// filename, GraphReader.RING_NODES, null, null);
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/spi-idspace/" + LMC
	// + "/");
	// Network lmc = new ReadableFile("LMC", "LMC", filename,
	// GraphReader.RING_NODES, null, null);
	// Config.overwrite("MAIN_DATA_FOLDER", "./data/spi-idspace/" + SWA
	// + "/");
	// Network swa = new ReadableFile("SW-1.0E-6-Contraction-1-MEDIAN",
	// "SW-1.0E-6-Contraction-1-MEDIAN", filename,
	// GraphReader.RING_NODES, null, null);
	// Series s1 = Series.get(swapping);
	// Series s2 = Series.get(lmc);
	// Series s3 = Series.get(swa);
	//
	// Config.overwrite("MAIN_PLOT_FOLDER", "./plots/ID-SPACES/_combined/");
	// Config.overwrite("GNUPLOT_CMD_ETC", Config.get("GNUPLOT_CMD_ETC")
	// + "\nset xrange [-1:1]\nset yrange [-1:1]");
	// Config.overwrite("AVERAGE_PLOT_DOT_WIDTH", "4");
	// Config.overwrite("PLOT_EXTENSION", ".jpg");
	// Config.overwrite("GNUPLOT_CMD_TERMINAL", "set terminal jpeg");
	// Plot.multiAvg(new Series[] { s1, s2, s3 }, SW + "-" + LMC + "-"
	// + SWA + "/");
	//
	// return;
	// }
	// }
}
