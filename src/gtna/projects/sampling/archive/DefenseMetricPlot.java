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
public class DefenseMetricPlot {

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
	private static final String LINEWIDTH = "3";
	
	
	
	
	
	private static String base = "./";
	private static String samDir = "samples";
	private static String netDir = "networks";
	private static String metDir = "metrics";
	private static String plotDir = "defense-plots";

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

		/*
		 * plots for ER
		 */
		String[] samER1 = new String[] { "BFS", "DFS", "RANDOMWALK",
				"UNIFORMSAMPLING" };
		String samER1group = "combined";
		Set<Metric> metricsER1 = new HashSet<Metric>();
		metricsER1.add(new ShortestPaths());
		createSinglePlots("ER", samER1, samER1group, new String[] { "0" },
				metricsER1);
		
		String[] samERM1 = new String[]{"RANDOMWALK"};
		String samERM1group = "rw";
		Set<Metric> metricsERM = new HashSet<Metric>();
		metricsERM.add(new ShortestPaths());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"RANDOMWALK_DEGREECORRECTION"};
		samERM1group = "rwdc";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new ShortestPaths());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"RESPONDENTDRIVENSAMPLING"};
		samERM1group = "rds";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new ShortestPaths());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"DFS"};
		samERM1group = "dfs";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new ShortestPaths());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		
		samERM1 = new String[]{"BFS"};
		samERM1group = "bfs";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"RESPONDENTDRIVENSAMPLING"};
		samERM1group = "rds";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"SNOWBALLSAMPLING"};
		samERM1group = "ss";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"RANDOMWALK"};
		samERM1group = "rw";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"RANDOMWALK_DEGREECORRECTION"};
		samERM1group = "rwdc";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"FRONTIERSAMPLING"};
		samERM1group = "fs";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"RANDOMSTROLL"};
		samERM1group = "rs";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"RANDOMSTROLL_DEGREECORRECTION"};
		samERM1group = "rsdc";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		samERM1 = new String[]{"UNIFORMSAMPLING"};
		samERM1group = "us";
		metricsERM = new HashSet<Metric>();
		metricsERM.add(new DegreeDistribution());
		createMultiPlots("ER", samERM1, samERM1group, new String[]{"0"}, metricsERM);
		
		/*
		 * plots for WS
		 */
		String[] samWS1 = new String[] { "BFS", "DFS", "RANDOMWALK",
				"UNIFORMSAMPLING" };
		String samWS1group = "combined";
		Set<Metric> metricsWS1 = new HashSet<Metric>();
		metricsWS1.add(new ClusteringCoefficient());
		createSinglePlots("WS", samWS1, samWS1group, new String[] { "0" },
				metricsWS1);
		String[] samWS2 = random;
		String samWS2group = "rw-class";
		Set<Metric> metricsWS2 = new HashSet<Metric>();
		metricsWS2.add(new ClusteringCoefficient());
		createSinglePlots("WS", samWS2, samWS2group, new String[] { "0" },
				metricsWS2);
		String[] samWS3 = new String[] { "UNIFORMSAMPLING" };
		String samWS3group = "us";
		Set<Metric> metricsWS3 = new HashSet<Metric>();
		metricsWS3.add(new ShortestPaths());
		createSinglePlots("WS", samWS3, samWS3group, new String[] { "0" },
				metricsWS3);
		String[] samWS4 = random;
		String samWS4group = "rw-class";
		Set<Metric> metricsWS4 = new HashSet<Metric>();
		metricsWS4.add(new ShortestPaths());
		createSinglePlots("WS", samWS4, samWS4group, new String[] { "0" },
				metricsWS4);
		String[] samWS5 = new String[] { "UNIFORMSAMPLING", "RANDOMWALK",
				"RANDOMSTROLL" };
		String samWS5group = "rw-us-rs";
		Set<Metric> metricsWS5 = new HashSet<Metric>();
		metricsWS5.add(new ShortestPaths());
		createSinglePlots("WS", samWS5, samWS5group, new String[] { "0" },
				metricsWS5);
		
		
		String[] samWSM1 = new String[]{"RANDOMWALK"};
		String samWSM1group = "rw";
		HashSet<Metric> metricsWSM = new HashSet<Metric>();
		metricsWSM.add(new DegreeDistribution());
		createMultiPlots("WS", samWSM1, samWSM1group, new String[]{"0"}, metricsWSM);
		samWSM1 = new String[]{"RANDOMWALK_DEGREECORRECTION"};
		samWSM1group = "rwdc";
		metricsWSM = new HashSet<Metric>();
		metricsWSM.add(new DegreeDistribution());
		createMultiPlots("WS", samWSM1, samWSM1group, new String[]{"0"}, metricsWSM);
		
		/*
		 * plots for CK
		 */
		String[] samCKM1 = new String[]{"RANDOMWALK"};
		String samCKM1group = "rw";
		HashSet<Metric> metricsCKM = new HashSet<Metric>();
		metricsCKM.add(new DegreeDistribution());
		createMultiPlots("CK", samCKM1, samCKM1group, new String[]{"0"}, metricsCKM);
		samCKM1 = new String[]{"RANDOMWALK_DEGREECORRECTION"};
		samCKM1group = "rwdc";
		metricsCKM = new HashSet<Metric>();
		metricsCKM.add(new DegreeDistribution());
		createMultiPlots("CK", samCKM1, samCKM1group, new String[]{"0"}, metricsCKM);
		

		/*
		 * plots for BA
		 */
		String[] samBA1 = new String[] { "BFS", "DFS", "RANDOMWALK",
				"UNIFORMSAMPLING" };
		String samBA1group = "combined";
		Set<Metric> metricsBA1 = new HashSet<Metric>();
		metricsBA1.add(new ClusteringCoefficient());
		createSinglePlots("BA", samBA1, samBA1group, new String[] { "0" },
				metricsBA1);
		String[] samBA2 = random;
		String samBA2group = "rw-class";
		Set<Metric> metricsBA2 = new HashSet<Metric>();
		metricsBA2.add(new ClusteringCoefficient());
		createSinglePlots("BA", samBA2, samBA2group, new String[] { "0" },
				metricsBA2);
		String[] samBA3 = new String[] { "BFS", "DFS", "RANDOMWALK",
				"UNIFORMSAMPLING" };
		String samBA3group = "combined";
		Set<Metric> metricsBA3 = new HashSet<Metric>();
		metricsBA3.add(new Assortativity());
		createSinglePlots("BA", samBA3, samBA3group, new String[] { "0" },
				metricsBA3);
		String[] samBA4 = random;
		String samBA4group = "rw-class";
		Set<Metric> metricsBA4 = new HashSet<Metric>();
		metricsBA4.add(new Assortativity());
		createSinglePlots("BA", samBA4, samBA4group, new String[] { "0" },
				metricsBA4);
		String[] samBA5 = new String[] { "BFS", "DFS", "RANDOMWALK",
				"UNIFORMSAMPLING" };
		String samBA5group = "combined";
		Set<Metric> metricsBA5 = new HashSet<Metric>();
		metricsBA5.add(new ShortestPaths());
		createSinglePlots("BA", samBA5, samBA5group, new String[] { "0" },
				metricsBA5);
		String[] samBA6 = new String[] { "UNIFORMSAMPLING", "RANDOMWALK",
		"RANDOMSTROLL" };
		String samBA6group = "rw-us-rs";
		Set<Metric> metricsBA6 = new HashSet<Metric>();
		metricsWS5.add(new ShortestPaths());
		createSinglePlots("BA", samBA6, samBA6group, new String[] { "0" },
		metricsWS5);
		
		
		String[] samBAM1 = new String[]{"RANDOMWALK"};
		String samBAM1group = "rw";
		Set<Metric> metricsBAM = new HashSet<Metric>();
		metricsBAM.add(new ShortestPaths());
		createMultiPlots("BA", samBAM1, samBAM1group, new String[]{"0"}, metricsBAM);
		samBAM1 = new String[]{"RANDOMWALK_DEGREECORRECTION"};
		samBAM1group = "rwdc";
		metricsBAM = new HashSet<Metric>();
		metricsBAM.add(new ShortestPaths());
		createMultiPlots("BA", samBAM1, samBAM1group, new String[]{"0"}, metricsBAM);
		samBAM1 = new String[]{"FRONTIERSAMPLING"};
		samBAM1group = "fs";
		metricsBAM = new HashSet<Metric>();
		metricsBAM.add(new ShortestPaths());
		createMultiPlots("BA", samBAM1, samBAM1group, new String[]{"0"}, metricsBAM);
		samBAM1 = new String[]{"RANDOMWALK_MULTIPLE"};
		samBAM1group = "rwm";
		metricsBAM = new HashSet<Metric>();
		metricsBAM.add(new ShortestPaths());
		createMultiPlots("BA", samBAM1, samBAM1group, new String[]{"0"}, metricsBAM);
		samBAM1 = new String[]{"RANDOMSTROLL"};
		samBAM1group = "rs";
		metricsBAM = new HashSet<Metric>();
		metricsBAM.add(new ShortestPaths());
		createMultiPlots("BA", samBAM1, samBAM1group, new String[]{"0"}, metricsBAM);
		samBAM1 = new String[]{"UNIFORMSAMPLING"};
		samBAM1group = "us";
		metricsBAM = new HashSet<Metric>();
		metricsBAM.add(new ShortestPaths());
		createMultiPlots("BA", samBAM1, samBAM1group, new String[]{"0"}, metricsBAM);
		

		/*
		 * plots for REG
		 */
		String[] samREG1 = new String[] { "BFS", "DFS", "RANDOMWALK",
				"UNIFORMSAMPLING" };
		String samREG1group = "combined";
		Set<Metric> metricsREG1 = new HashSet<Metric>();
		metricsREG1.add(new ClusteringCoefficient());
		createSinglePlots("REG", samREG1, samREG1group, new String[] { "0" },
				metricsREG1);
		String[] samREG3 = new String[] { "BFS", "DFS", "RANDOMWALK",
				"UNIFORMSAMPLING" };
		String samREG3group = "combined";
		Set<Metric> metricsREG3 = new HashSet<Metric>();
		metricsREG3.add(new Assortativity());
		createSinglePlots("REG", samREG3, samREG3group, new String[] { "0" },
				metricsREG3);
		
		String[] samREGM1 = new String[]{"RANDOMWALK"};
		String samREGM1group = "rw";
		HashSet<Metric> metricsREGM = new HashSet<Metric>();
		metricsREGM.add(new DegreeDistribution());
		createMultiPlots("REG", samREGM1, samREGM1group, new String[]{"0"}, metricsREGM);
		samREGM1 = new String[]{"BFS"};
		samREGM1group = "bfs";
		metricsREGM = new HashSet<Metric>();
		metricsREGM.add(new DegreeDistribution());
		createMultiPlots("REG", samREGM1, samREGM1group, new String[]{"0"}, metricsREGM);
		samREGM1 = new String[]{"UNIFORMSAMPLING"};
		samREGM1group = "us";
		metricsREGM = new HashSet<Metric>();
		metricsREGM.add(new DegreeDistribution());
		createMultiPlots("REG", samREGM1, samREGM1group, new String[]{"0"}, metricsREGM);

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
			String group, String[] is, Set<Metric> metrics) {
		init();

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

//			Config.overwrite("GNUPLOT_OFFSET_X", "100.0");

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
			String group, String[] is, Set<Metric> metrics) {
		init();

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

			File p = new File(plotdir + "plots/" );
			File t = new File(plotdir + "temp/" );

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

//			Config.overwrite("GNUPLOT_OFFSET_Y", "0.2");
			// Config.overwrite("GNUPLOT_OFFSET_X", "100.0");
			
			Config.overwrite("GNUPLOT_LW", LINEWIDTH);
			
			Config.appendToList("GNUPLOT_CONFIG_1", CONFIG1);

			Config.overwrite("GNUPLOT_PRINT_ERRORS", "true");

//			Config.overwrite("GNUPLOT_TERMINAL", "png");
//			Config.overwrite("PLOT_EXTENSION", ".png");
//
//			Plotting.multi(series, metrics.toArray(new Metric[0]), "multi/",
//					Type.confidence1, Style.candlesticks); // main path to plots
//															// is set by
//															// Config.overwrite

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
