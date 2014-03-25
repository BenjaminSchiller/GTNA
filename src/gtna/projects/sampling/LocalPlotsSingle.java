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
package gtna.projects.sampling;

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
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.util.ArrayUtils;
import gtna.util.Config;
import gtna.util.parameter.IntParameter;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class LocalPlotsSingle {

	private static String suffix = ".gtna";
	private static String name;
	private static String targetdir;

	private static LinkedList<String> dirs = new LinkedList<String>();
	private static ArrayList<Integer> scaledown = new ArrayList<Integer>();
	private static ArrayList<String> sampling = new ArrayList<String>();
	private static String net;
	// private static ArrayList<Integer> sizes = new ArrayList<Integer>();
	private static String plotdir;

	private static Set<Metric> metrics = new HashSet<Metric>();
	private static int times;
	private static boolean withOriginal = false;
	private static boolean ex;

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		Config.appendToList("GNUPLOT_CONFIG_1", "unset logscale x");
		metrics.add(new ShortestPaths());
		metrics.add(new ClusteringCoefficient());
		metrics.add(new Assortativity());
		metrics.add(new DegreeDistribution());
		
		times=10;
		scaledown.add(1);scaledown.add(2);scaledown.add(3);scaledown.add(4);scaledown.add(5);scaledown.add(6);
		scaledown.add(7);scaledown.add(8);scaledown.add(9);scaledown.add(10);scaledown.add(15);
		
		String[] networks = {"ca-GrQc", "cit-HepPh", "p2p-Gnutella31"};
		
		String b = "/Users/Tim/Documents/Projekte/sampling/rm/";
		
		String pd=b+"mplots/";
		String td=b+"mmetrics/";
		
		withOriginal=true;
		
		dirs.add(b+"mgraphs/");
		for(String n : networks){
			net = n;
			sampling.clear();
			for(SamplingAlgorithm sa : SamplingAlgorithm.values()){
				sampling.add(sa.name());
			}
			targetdir = td + net + "/" + "data/";
			plotdir = pd + net + "/single/";
				
				
			try{
				plot();
			}catch (RuntimeException e){}
			
		}
	}

	/**
	 * 
	 */
	private static void plot() {
		Collection<Network> rfc = new ArrayList<Network>();
		for (int i = 0; i < dirs.size(); i++) {
			// add scaled networks
			for (String sam : sampling) {
				for (int j = 0; j < scaledown.size(); j++) {
					int scale = scaledown.get(j);
					String d = dirs.get(i);

					String p = d + net + "/" + scale + "/"
							+ sam + "/";

					
					File dir = new File(p);
					File[] fs = dir.listFiles();
					if (fs.length > 0) {
						name = fs[0].getName().substring(0,
								fs[0].getName().lastIndexOf("-"));
					}


					ReadableFolder rf = new ReadableFolder(name, name, p,
							suffix, null);

					 System.out.println("RF: " + p + " - size:" +
					 rf.getFiles().length); // TODO
//					 REMOVE

					DescriptionWrapper dwrf = new DescriptionWrapper(rf, sam.toLowerCase(), new IntParameter("SIZE", scale));
					rfc.add(dwrf);
				}
				if(withOriginal){
					// add original network
					String d = dirs.get(i);

					ReadableFile rf = new ReadableFile(net, net, d + net + suffix,
							null);
					DescriptionWrapper dwrf = new DescriptionWrapper(rf, sam.toLowerCase(), new IntParameter("SIZE", 30));
					rfc.add(dwrf);
				}
				
			}
			
		}

		Network[] rfa = rfc.toArray(new Network[0]);

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true"); 

		
		int first = sampling.size();
		int second = (withOriginal) ? scaledown.size()+1 : scaledown.size();
		
		Series[][] series = new Series[first][second];
		System.out.println("Initialized array: (" + first + "x" + second + ")");
		for (int i = 0; i < rfa.length; i++) {
			String path;
			if (withOriginal && i%second == second-1) { // TODO
				path = targetdir + "original" + "/" + 100 + "/data";
			} else {
				String s = getSamplingAlgorithm(rfa[i].getDescription());
				path = targetdir + s + "/" + scaledown.get(i%second) + "/data";
			}
			
			// TODO
			 System.out.println("Setting MAIN_DATA_FOLDER=" + path); 
			// remove
			Config.overwrite("MAIN_DATA_FOLDER", path);

			int a1 = (int)Math.floor((double)i/(double)second);
			int a2 = i%second;
			System.out.println(">>> \n\nTrying to add data at: " + a1 + "x" + a2 + "\n >>> and limits are: " + series.length+1 + "x" + series[a1].length);
			try{
			series[a1][a2] = Series.get(rfa[i], metrics.toArray(new Metric[0]));
			}catch (Exception e){
				
				ex = true;
			}
		}

		if(ex)
			return;
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
		
		Config.overwrite("GNUPLOT_TERMINAL", "pdf dashed");
		
		Config.overwrite("GNUPLOT_LW", "3");

		Type plotType_average = Type.average; // Type.confidence1;
		Style plotStyle_linespoint = Style.linespoint; // Style.candlesticks;
		
		Type plotType_conf1 = Type.confidence2; // Type.confidence1;
		Style plotStyle_candle = Style.candlesticks; // Style.candlesticks;
		
		
		Config.overwrite("GNUPLOT_CONFIG_1", "unset logscale x");
		
		
		Plotting.single(series, metrics.toArray(new Metric[0]), "single/",
				plotType_average, plotStyle_linespoint); // main path to plots
										// is set by
										// Config.overwrite
		
		Plotting.single(series, metrics.toArray(new Metric[0]), "single-conf/",
				plotType_conf1, plotStyle_candle); // main path to plots
										// is set by
										// Config.overwrite
		
		
		Plotting.single(series, metrics.toArray(new Metric[0]), "single/",
				plotType_average, plotStyle_linespoint); // main path to plots
										// is set by
										// Config.overwrite
		
		Config.overwrite("GNUPLOT_CONFIG_1", "set logscale x");

		

		Plotting.single(series, metrics.toArray(new Metric[0]), "single-log/",
				plotType_average, plotStyle_linespoint); // main path to plots
										// is set by
										// Config.overwrite
		Plotting.single(series, metrics.toArray(new Metric[0]), "single-log-conf/",
				plotType_conf1, plotStyle_candle); // main path to plots
										// is set by
										// Config.overwrite
	}

	/**
	 * @param description
	 * @return
	 */
	private static String getSamplingAlgorithm(String description) {
		System.out.println("Trying to match algorithm: " + description);
		for(SamplingAlgorithm sa : SamplingAlgorithm.values()){
			if(description.contains(sa.toString().toLowerCase()) || description.contains(sa.toString().toUpperCase()))
				return sa.toString().toLowerCase();
		}
		
		System.exit(1);
		return "not-recognized";
	}

	/**
	 * @param args
	 */
	private static void matchArguments(String[] args) {
		for (String s : args) {
			// System.out.println(s);
			// parse network generation details
			if (s.startsWith("sampling=")) {
				sampling.addAll(matchSampling(s.substring(9)));
				// parse network generation details
			} else if (s.startsWith("withOriginal=")) {
				withOriginal = (s.equalsIgnoreCase("withOriginal=true")) ? true : false;
			} else if (s.startsWith("network=")) {
				net = s.substring(8);
			} else if (s.startsWith("scaledown=")) {
				scaledown.addAll(matchScaledowns(s.substring(10)));
			} else if (s.startsWith("times=")) {
				times = Integer.parseInt(s.substring(6));
				// }else if (s.startsWith("size=")) {
				// sizes.add(Integer.parseInt(s.substring(5)));
			} else if (s.startsWith("metrics=")) {
				metrics.addAll(matchMetrics(s.substring(8)));
			} else if (s.startsWith("suffix=")) {
				suffix = s.substring(7);
			} else if (s.startsWith("dstDir=")) {
				targetdir = s.substring(7);
				File f = new File(targetdir);
				if (!f.isDirectory()) {
					f.mkdir();
				}
			} else if (s.startsWith("plotDir=")) {
				plotdir = s.substring(8);
				File f = new File(plotdir);
				if (!f.isDirectory()) {
					f.mkdir();
				}
			}
			// readable folder?
			else if (s.startsWith("loaddir=")) {
				dirs.add(s.substring(8));
			} else {
				System.err.println("Parameter not recognized: " + s);
				System.exit(1);
			}
		}
	}

	/**
	 * @param substring
	 * @return
	 */
	private static Collection<? extends String> matchSampling(String s) {
		ArrayList<String> recognized = new ArrayList<String>();
		for (String m : s.split(",")) {
			for (SamplingAlgorithm sa : SamplingAlgorithm.values()) {
				if (sa.toString().equalsIgnoreCase(m)) {
					recognized.add(sa.toString().toLowerCase());
				}
			}
		}

		return recognized;
	}

	/**
	 * @param substring
	 * @return
	 */
	private static Collection<? extends Integer> matchScaledowns(
			String substring) {

		LinkedList<Integer> sds = new LinkedList<Integer>();
		for (String sd : substring.split(",")) {
			sds.offer(Integer.parseInt(sd));
		}

		return sds;
	}

	/**
	 * @param s
	 * @return
	 */
	private static Collection<? extends Metric> matchMetrics(String s) {
		LinkedList<Metric> recognized = new LinkedList<Metric>();

		for (String m : s.split(",")) {
			Metric i;
			switch (m) {
			case "CC":
				i = new ClusteringCoefficient();
				break;
			case "DD":
				i = new DegreeDistribution();
				break;
			case "SP":
				i = new ShortestPaths();
				break;
			case "AS":
				i = new Assortativity();
				break;
			case "BC":
				i = new BetweennessCentrality();
				break;
			case "PR":
				i = new PageRank();
				break;
			default:
				i = null;
			}

			if (i != null) {
				recognized.offer(i);
			}
		}

		return recognized;
	}
}
