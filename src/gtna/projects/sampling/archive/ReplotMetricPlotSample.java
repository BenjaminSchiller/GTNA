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
import gtna.metrics.sampling.SamplingBias;
import gtna.metrics.sampling.SamplingModularity;
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
public class ReplotMetricPlotSample {

	private static String base = "/Users/Tim/Documents/UNI/MasterThesis/sync/metric_sync/";
	private static String samDir = "samplemetrics-samples";
//	private static String netDir = "networks";
	private static String metDir = "sampling-metrics";
	private static String ploDir = "plots5";

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

	private static String suffix = "";
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
		String[] n = new String[] { "ER", "BA", "WS", "CK", "REG"};
		String[] uniform = new String[] { "UNIFORMSAMPLING" };
		String[] random = new String[] { "RANDOMWALK",
				"RANDOMWALK_DEGREECORRECTION", "RANDOMWALK_MULTIPLE",
				"FRONTIERSAMPLING", "RANDOMSTROLL",
				"RANDOMSTROLL_DEGREECORRECTION", "RANDOMJUMP" };
		String[] bfs = new String[] { "BFS", "SNOWBALLSAMPLING",
				"RESPONDENTDRIVENSAMPLING", "FORESTFIRE" };
		String[] dfs = new String[] { "DFS" };
		
		Set<Metric> metrics = new HashSet<Metric>();
		Metric sb = new SamplingBias();
		Metric sm = new SamplingModularity();
		
		metrics.add(sb);
		metrics.add(sm);
		
//		String network = n[0];
		String[] instance = new String[]{"0"};
		double[] scale = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9 };

//		createPlots(network, dfs, "dfs", instance, scale, metrics);
//		createPlots(network, new String[] {"BFS"}, "bfs", instance, scale, metrics);
//		createPlots(network, uniform, "uniform", instance, scale, metrics);
//		createPlots(network, new String[] {"RANDOMWALK"}, "rw", instance, scale, metrics);
		
		ArrayList<String> samplingalgorithms = new ArrayList<String>();
		samplingalgorithms.addAll(Arrays.asList(bfs));
		samplingalgorithms.addAll(Arrays.asList(dfs));
		samplingalgorithms.addAll(Arrays.asList(random));
		samplingalgorithms.addAll(Arrays.asList(uniform));
		
		for(String network : n){
			for(int i = 0; i < scale.length; i++){
				createPlots(network, samplingalgorithms.toArray(new String[0]), "sample", instance, new double[] {scale[i]}, metrics);
			}
		}
	}

	private static void createPlots(String netw, String[] sam, String group, String[] is, double[] sc, Set<Metric> metrics) {
		init();
		

		double[] scale = sc;

		skipping = "true";
		aggregate = true;

		String network = netw;
		
		if(sc.length==1)
			plotdir = base + ploDir + "/" + network + "/" + group + "/" + sc[0] + "/"; // TODO
		else
			plotdir = base + ploDir + "/" + network + "/" + group + "/" + sc[0] + "-" + sc[sc.length-1] + "/"; // TODO
		File f = new File(plotdir);
		if (!f.isDirectory()) {
			f.mkdir();
		}

		
		
		for (int k = 0; k < is.length; k++){
			String instance = is[k];
		for (int j = 0; j < sam.length; j++) {
			String sampling1 = sam[j];
			for (int i = 0; i < scale.length; i++) {
				fillLists(base + samDir + "/10000/" + network + "/" + scale[i]
						+ "/" + sampling1 + "/" + instance + "/original-with-props", 10000, scale[i],
						sampling1, instance, network, 0, 0);
			}
		}
		}
		
		Collection<Network> rfc = new ArrayList<Network>();
		for (int i = 0; i < dirs.size(); i++) {
			String d = dirs.get(i);
			if(is.length == 1)
				name = getAbbrev(sampling.get(i).trim());
			else
				name = getAbbrev(sampling.get(i).trim()) + " (" + instances.get(i) + ")";
			
			ReadableFolder rf = new ReadableFolder(name, net.get(0), d, suffix,
					null);

			System.out.println("RF: " + d + " - size:" + rf.getFiles().length);

			DescriptionWrapper dwrf = new DescriptionWrapper(rf, name);
			
			if(rf.getFiles().length > 0)
				rfc.add(dwrf);
		}

		Network[] rfa = rfc.toArray(new Network[0]);

		if (!aggregate) {
			System.err.println("aggegrate should be true");
		} else {
			Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
			Series[] series = new Series[rfa.length];
			for (int i = 0; i < rfa.length; i++) {
				targetdir = base + metDir + "/" + network + "/" + scaledown.get(i) + "/";
				String path = targetdir + sampling.get(i) + "/"
						+ rfa[i].getNodes() + "/data/";

				Config.overwrite("MAIN_DATA_FOLDER", path);

				series[i] = Series.generate(
						rfa[i], metrics.toArray(new Metric[0]),
						startIndex.get(i), endIndex.get(i));

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

			Config.overwrite("GNUPLOT_TERMINAL", "png");
			Config.overwrite("PLOT_EXTENSION", ".png");

			Plotting.multi(series, metrics.toArray(new Metric[0]), "multi/",
					Type.confidence1, Style.candlesticks); // main path to plots
															// is set by
															// Config.overwrite

			Config.overwrite("GNUPLOT_TERMINAL", "pdf");
			Config.overwrite("PLOT_EXTENSION", ".pdf");

			Plotting.multi(series, metrics.toArray(new Metric[0]), "multi/",
					Type.confidence1, Style.candlesticks); // main path to plots
															// is set by
															// Config.overwrite

		}
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
