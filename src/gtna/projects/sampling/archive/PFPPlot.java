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
 * SamplingThesis.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.sampling.archive;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.Assortativity;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.centrality.BetweennessCentrality;
import gtna.metrics.centrality.PageRank;
import gtna.networks.Network;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.util.Config;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class PFPPlot {

	/**
	 * 
	 */
	private static final String NETWORK = "PFP";
	/**
	 * 
	 */
	private static final String CONFIG1 = "set palette rgbformulae 7,5,15";
	/**
	 * 
	 */
	private static final String TERMINAL = "pdf dashed";
	/**
	 * 
	 */
	private static final Style PLOT_STYLE = Style.candlesticks;
	/**
	 * 
	 */
	private static final Type PLOT_TYPE = Type.confidence2;
	/**
	 * 
	 */
	private static final String LINEWIDTH = "2";

	private static String base = "./";
	private static String samDir = "samples";
	private static String netDir = "networks";
	private static String metDir = "metrics";
	private static String plotDir = "pfp-plots";

	private static void fillLists(String dir, int v, double sd, String sa,
			String i, String network, int start, int end) {
		dirs.add(dir);
		size.add(v);
		scaledown.add(sd);
		sampling.add(sa);
		instances.add(i);
		net.add(network);
		startIndex.add(start);
		endIndex.add(end);
	}

	private static String suffix = ".gtna";
	private static String name;
	private static LinkedList<Integer> startIndex = new LinkedList<Integer>();
	private static LinkedList<Integer> endIndex = new LinkedList<Integer>();
	private static String targetdir;
	private static boolean aggregate = false;
	private static LinkedList<String> dirs = new LinkedList<String>();
	private static LinkedList<Integer> size = new LinkedList<Integer>();
	private static LinkedList<Double> scaledown = new LinkedList<Double>();
	private static LinkedList<String> sampling = new LinkedList<String>();
	private static LinkedList<String> instances = new LinkedList<String>();
	private static LinkedList<String> net = new LinkedList<String>();
	private static String plotdir;
	private static String skipping = "false";

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// String[] n = new String[] { "ER", "BA", "WS", "CK", "REG", "RING",
		// "CL" };

		String[] n = new String[] { "RING", "CL" };
		String[] uniform = new String[] { "UNIFORMSAMPLING" };

		String[] random = new String[] { "RANDOMWALK",
				"RANDOMWALK_DEGREECORRECTION", "RANDOMWALK_MULTIPLE",
				"FRONTIERSAMPLING", "RANDOMSTROLL",
				"RANDOMSTROLL_DEGREECORRECTION", "RANDOMJUMP" };
		String[] bfs = new String[] { "BFS", "SNOWBALLSAMPLING",
				"RESPONDENTDRIVENSAMPLING", "FORESTFIRE" };
		String[] dfs = new String[] { "DFS" };

		String[] randomExtended = new String[] { "RANDOMWALK", "DFS",
				"RANDOMWALK_DEGREECORRECTION", "RANDOMWALK_MULTIPLE",
				"FRONTIERSAMPLING", "RANDOMSTROLL",
				"RANDOMSTROLL_DEGREECORRECTION", "RANDOMJUMP" };

		assembleAlgorithmsAndCreatePlots(random, bfs, randomExtended, false);

		Config.appendToList("GNUPLOT_CONFIG_2", "set logscale x");

		assembleAlgorithmsAndCreatePlots(random, bfs, randomExtended, true);

	}

	/**
	 * @param random
	 * @param bfs
	 * @param randomExtended
	 */
	private static void assembleAlgorithmsAndCreatePlots(String[] random,
			String[] bfs, String[] randomExtended, boolean logscale) {
		/*
		 *  *\ SINGLE * \*
		 */

		String[] samER1 = new String[] { "BFS", "DFS", "RANDOMWALK",
				"UNIFORMSAMPLING" };
		String samER1group = "combined";
		Set<Metric> metricsER1 = new HashSet<Metric>();
		metricsER1.add(new ShortestPaths());
		metricsER1.add(new ClusteringCoefficient());
		metricsER1.add(new Assortativity());
		metricsER1.add(new DegreeDistribution());
		createSinglePlots(NETWORK, samER1, samER1group, new String[] { "0" },
				metricsER1, logscale);

		createSinglePlots(NETWORK, random, "rw-class", new String[] { "0" },
				metricsER1, logscale);
		createSinglePlots(NETWORK, bfs, "bfs-class", new String[] { "0" },
				metricsER1, logscale);
		createSinglePlots(NETWORK, randomExtended, "rw+dfs",
				new String[] { "0" }, metricsER1, logscale);

		/*
		 *  *\ MULTI * \*
		 */

		/* \ BFS \ */
		String[] samERM1 = new String[] { "BFS" };
		String samERM1group = "bfs";
		HashSet<Metric> metricsERM = new HashSet<Metric>();
		metricsERM.add(new ShortestPaths());
		metricsERM.add(new DegreeDistribution());
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "RESPONDENTDRIVENSAMPLING" };
		samERM1group = "rds";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "SNOWBALLSAMPLING" };
		samERM1group = "ss";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "FORESTFIRE" };
		samERM1group = "ff";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);

		/* \ RW \ */
		samERM1 = new String[] { "RANDOMWALK" };
		samERM1group = "rw";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "RANDOMWALK_DEGREECORRECTION" };
		samERM1group = "rwdc";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "RANDOMWALK_MULTIPLE" };
		samERM1group = "rwm";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "FRONTIERSAMPLING" };
		samERM1group = "fs";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "RANDOMSTROLL" };
		samERM1group = "rs";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "RANDOMSTROLL_DEGREECORRECTION" };
		samERM1group = "rsdc";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
		samERM1 = new String[] { "RANDOMJUMP" };
		samERM1group = "rj";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);

		/* \ US \ */
		samERM1 = new String[] { "UNIFORMSAMPLING" };
		samERM1group = "us";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);

		/* \ DFS \ */
		samERM1 = new String[] { "DFS" };
		samERM1group = "dfs";
		createMultiPlots(NETWORK, samERM1, samERM1group, new String[] { "0" },
				metricsERM, logscale);
	}

	/**
	 * 
	 * @param netw
	 *            network ("BA")
	 * @param sam
	 *            sampling algorithms ("BFS", "RANDOMWALK", "UNIFORMSAMPLING")
	 * @param group
	 *            just for naming and sorting
	 * @param is
	 *            network instance (0-4)
	 * @param metrics
	 *            which metrics should be plotted
	 */
	private static void createSinglePlots(String netw, String[] sam,
			String group, String[] is, Set<Metric> metrics, boolean logscale) {
		init();
		
		if(logscale)
			return;

		double[] scale = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,
				0.9 };

		skipping = "true";
		aggregate = true;

		String network = netw;

		if (is.length == 1)
			plotdir = base + plotDir + "/" + network + "/" + group + "/"
					+ is[0] + "/"; // TODO
		else
			plotdir = base + plotDir + "/" + network + "/" + group + "/"
					+ is[0] + "-" + is[is.length - 1] + "/"; // TODO
		File f = new File(plotdir);
		if (!f.isDirectory()) {
			f.mkdir();
		}

		String s = "";
		if ("CL".equalsIgnoreCase(netw)) {
			s = "1000";
		} else {
			s = "10000";
		}

		for (int k = 0; k < is.length; k++) {
			String instance = is[k];
			for (int j = 0; j < sam.length; j++) {
				String sampling = sam[j];
				for (int i = 0; i < 9; i++) {
					fillLists(base + samDir + "/" + s + "/" + network + "/"
							+ scale[i] + "/" + sampling + "/" + instance,
							10000, scale[i], sampling, instance, network, 0, 15);
				}
				fillLists(base + netDir + "/" + s + "/" + network + "/"
						+ instance, 10000, 1.0, "original", instance, network,
						0, 0);
			}
		}

		Collection<Network> rfc = new ArrayList<Network>();
		for (int i = 0; i < dirs.size(); i++) {
			String d = dirs.get(i);
			if (is.length == 1)
				name = getAbbrev(sampling.get(i).trim());
			else
				name = getAbbrev(sampling.get(i).trim()) + " ("
						+ instances.get(i) + ")";

			ReadableFolder rf = new ReadableFolder(name, net.get(0), d, suffix,
					null);

			System.out.println("RF: " + d + " - size:" + rf.getFiles().length);

			DescriptionWrapper dwrf = new DescriptionWrapper(rf, name);
			rfc.add(dwrf);
		}

		Network[] rfa = rfc.toArray(new Network[0]);

		if (!aggregate) {
			System.err.println("aggegrate should be true");
		} else {
			Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
			Series[][] series = new Series[rfa.length / 10][10];
			for (int i = 0; i < rfa.length; i++) {
				targetdir = base + metDir + "/" + network + "/"
						+ instances.get(i) + "/";
				String path = targetdir + sampling.get(i) + "/"
						+ rfa[i].getNodes() + "/data/";

				Config.overwrite("MAIN_DATA_FOLDER", path);

				series[(int) Math.floor(i / 10)][i % 10] = Series.generate(
						rfa[i], metrics.toArray(new Metric[0]),
						startIndex.get(i), endIndex.get(i));

				System.out.println(series[(int) Math.floor(i / 10)][i % 10]
						.getFolder());
				System.out.println(Arrays.toString(series[(int) Math
						.floor(i / 10)][i % 10].getRunFolders()));
			}

			File p = new File(plotdir + "plots/");
			File t = new File(plotdir + "temp/");

			if (!p.isDirectory()) {
				p.mkdirs();
			}
			if (!t.isDirectory()) {
				t.mkdirs();
			}

			Config.overwrite("MAIN_PLOT_FOLDER", plotdir + "plots/");
			Config.overwrite("TEMP_FOLDER", plotdir + "temp/");
			Config.overwrite("RUNTIME_PLOTS_GENERATE", "false");
			Config.overwrite("ETC_PLOTS_GENERATE", "false");

			Config.appendToList("GNUPLOT_CONFIG_1", CONFIG1);

			// Config.overwrite("GNUPLOT_OFFSET_X", "100.0");

			// Config.overwrite("GNUPLOT_TERMINAL", "png");
			// Config.overwrite("PLOT_EXTENSION", ".png");
			//
			// Plotting.single(series, metrics.toArray(new Metric[0]),
			// "single/",
			// Type.confidence1, Style.candlesticks); // main path to plots
			// // is set by
			// // Config.overwrite

			Config.overwrite("GNUPLOT_LW", LINEWIDTH);
			Config.overwrite("GNUPLOT_TERMINAL", TERMINAL);
			Config.overwrite("PLOT_EXTENSION", ".pdf");

			Plotting.single(series, metrics.toArray(new Metric[0]), "single/",
					PLOT_TYPE, PLOT_STYLE); // main path to plots
											// is set by
											// Config.overwrite

		}

	}

	/**
	 * 
	 * @param netw
	 *            network ("BA")
	 * @param sam
	 *            sampling algorithms ("BFS", "RANDOMWALK", "UNIFORMSAMPLING")
	 * @param group
	 *            just for naming and sorting
	 * @param is
	 *            network instance (0-4)
	 * @param metrics
	 *            which metrics should be plotted
	 */
	private static void createMultiPlots(String netw, String[] sam,
			String group, String[] is, Set<Metric> metrics, boolean logscale) {
		init();

		double[] scale = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,
				0.9 };

		skipping = "true";
		aggregate = true;

		String network = netw;

		if (is.length == 1) {
			plotdir = base + plotDir + "/" + network + "/" + group + "/"
					+ is[0] + "/"; // TODO

			if (logscale) {
				plotdir += "logscale/";
			}
		} else {
			plotdir = base + plotDir + "/" + network + "/" + group + "/"
					+ is[0] + "-" + is[is.length - 1] + "/"; // TODO
			if (logscale) {
				plotdir += "logscale/";
			}

		}
		File f = new File(plotdir);
		if (!f.isDirectory()) {
			f.mkdir();
		}

		String s = "";
		if ("CL".equalsIgnoreCase(netw)) {
			s = "1000";
		} else {
			s = "10000";
		}

		for (int k = 0; k < is.length; k++) {
			String instance = is[k];
			for (int j = 0; j < sam.length; j++) {
				String sampling = sam[j];
				for (int i = 0; i < 9; i++) {
					fillLists(base + samDir + "/" + s + "/" + network + "/"
							+ scale[i] + "/" + sampling + "/" + instance,
							10000, scale[i], sampling, instance, network, 0, 15);
				}
				fillLists(base + netDir + "/" + s + "/" + network + "/"
						+ instance, 10000, 1.0, "original", instance, network,
						0, 0);
			}
		}

		Collection<Network> rfc = new ArrayList<Network>();
		for (int i = 0; i < dirs.size(); i++) {
			String d = dirs.get(i);
			if (is.length == 1)
				name = getAbbrev(sampling.get(i).trim()) + "-"
						+ scaledown.get(i);
			else
				name = getAbbrev(sampling.get(i).trim()) + "-"
						+ scaledown.get(i) + " (" + instances.get(i) + ")";

			ReadableFolder rf = new ReadableFolder(name, net.get(0), d, suffix,
					null);

			System.out.println("RF: " + d + " - size:" + rf.getFiles().length);

			DescriptionWrapper dwrf = new DescriptionWrapper(rf, name);
			rfc.add(dwrf);
		}

		Network[] rfa = rfc.toArray(new Network[0]);

		if (!aggregate) {
			System.err.println("aggegrate should be true");
		} else {
			Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
			Series[] series = new Series[rfa.length];
			for (int i = 0; i < rfa.length; i++) {
				targetdir = base + metDir + "/" + network + "/"
						+ instances.get(i) + "/";
				String path = targetdir + sampling.get(i) + "/"
						+ rfa[i].getNodes() + "/data/";

				Config.overwrite("MAIN_DATA_FOLDER", path);

				series[i] = Series.generate(rfa[i],
						metrics.toArray(new Metric[0]), startIndex.get(i),
						endIndex.get(i));

			}

			File p = new File(plotdir + "plots/");
			File t = new File(plotdir + "temp/");

			if (!p.isDirectory()) {
				p.mkdirs();
			}
			if (!t.isDirectory()) {
				t.mkdirs();
			}

			Config.overwrite("MAIN_PLOT_FOLDER", plotdir + "plots/");
			Config.overwrite("TEMP_FOLDER", plotdir + "temp/");
			Config.overwrite("RUNTIME_PLOTS_GENERATE", "false");
			Config.overwrite("ETC_PLOTS_GENERATE", "false");

			// Config.overwrite("GNUPLOT_OFFSET_Y", "0.2");
			// Config.overwrite("GNUPLOT_OFFSET_X", "100.0");

			Config.overwrite("GNUPLOT_LW", LINEWIDTH);

			Config.appendToList("GNUPLOT_CONFIG_1", CONFIG1);

			Config.overwrite("GNUPLOT_PRINT_ERRORS", "true");

			Config.overwrite("GNUPLOT_TERMINAL", TERMINAL);
			Config.overwrite("PLOT_EXTENSION", ".pdf");

			Plotting.multi(series, metrics.toArray(new Metric[0]), "multi/",
					PLOT_TYPE, PLOT_STYLE); // main path to plots
											// is set by
		} // Config.overwrite

	}

	/**
	 * 
	 */
	private static void init() {
		startIndex = new LinkedList<Integer>();
		endIndex = new LinkedList<Integer>();
		dirs = new LinkedList<String>();
		size = new LinkedList<Integer>();
		scaledown = new LinkedList<Double>();
		sampling = new LinkedList<String>();
		instances = new LinkedList<String>();
		net = new LinkedList<String>();
	}

	private static void printHelp() {
		System.out.println("Wrong parameter settings!");
	}

	private static String getAbbrev(String sa) {
		if ("UNIFORMSAMPLING".equalsIgnoreCase(sa))
			return "US";
		if ("RANDOMWALK".equalsIgnoreCase(sa))
			return "RW";
		if ("RANDOMWALK_DEGREECORRECTION".equalsIgnoreCase(sa))
			return "RWDC";
		if ("RANDOMWALK_MULTIPLE".equalsIgnoreCase(sa))
			return "RWM";
		if ("FRONTIERSAMPLING".equalsIgnoreCase(sa))
			return "FS";
		if ("RANDOMSTROLL".equalsIgnoreCase(sa))
			return "RS";
		if ("RANDOMSTROLL_DEGREECORRECTION".equalsIgnoreCase(sa))
			return "RSDC";
		if ("RANDOMJUMP".equalsIgnoreCase(sa))
			return "RJ";
		if ("BFS".equalsIgnoreCase(sa))
			return "BFS";
		if ("SNOWBALLSAMPLING".equalsIgnoreCase(sa))
			return "SS";
		if ("RESPONDENTDRIVENSAMPLING".equalsIgnoreCase(sa))
			return "RDS";
		if ("FORESTFIRE".equalsIgnoreCase(sa))
			return "FF";
		if ("DFS".equalsIgnoreCase(sa))
			return "DFS";
		if ("original".equalsIgnoreCase(sa))
			return "O";

		return "";
	}

}
