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
import gtna.util.Config;

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
public class PlotsMulti {

	private static String suffix = ".gtna";
	private static String name;
	private static String targetdir;

	private static LinkedList<String> dirs = new LinkedList<String>();
	private static ArrayList<Integer> scaledown = new ArrayList<Integer>();
	private static String sampling = "samplingalgorithm";
	private static String net;
	// private static ArrayList<Integer> sizes = new ArrayList<Integer>();
	private static String plotdir;

	private static Set<Metric> metrics = new HashSet<Metric>();
	private static int times;

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {

		matchArguments(args);

		Collection<Network> rfc = new ArrayList<Network>();
		for (int i = 0; i < dirs.size(); i++) {
			// add scaled networks
			for (int j = 0; j < scaledown.size(); j++) {
				String d = dirs.get(i);

				String p = d + net + "/" + scaledown.get(j) + "/" + sampling
						+ "/";
				
				System.out.println("Reading directory: " + p);
				File dir = new File(p);
				File[] fs = dir.listFiles();
				if (fs.length > 0) {
					name = fs[0].getName().substring(0,
							fs[0].getName().lastIndexOf("-"));
				}

				// System.out.println("Recognized name: " + name); // TODO
				// REMOVE

				ReadableFolder rf = new ReadableFolder(name, name, p, suffix,
						null);

				// System.out.println("RF: " + d + " - size:" +
				// rf.getFiles().length); // TODO
				// REMOVE

				DescriptionWrapper dwrf = new DescriptionWrapper(rf, name + "-"
						+ scaledown.get(j) + "%");
				rfc.add(dwrf);
			}
			// add original network
			String d = dirs.get(i);

			ReadableFile rf = new ReadableFile(net, net, d + net + suffix, null);
			DescriptionWrapper dwrf = new DescriptionWrapper(rf, name + "-"
					+ "100%");
			rfc.add(dwrf);
		}

		Network[] rfa = rfc.toArray(new Network[0]);

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		

		Series[] series = new Series[rfa.length];
		for (int i = 0; i < rfa.length; i++) {
			String path;
			if (i >= scaledown.size()) {
				path = targetdir + "original" + "/" + 100 + "/data";
			} else {
				path = targetdir + sampling + "/" + scaledown.get(i) + "/data";
			}

			Config.overwrite("MAIN_DATA_FOLDER", path);

			series[i] = Series.generate(rfa[i], metrics.toArray(new Metric[0]),
					times);

			System.out.println(series[i].getFolder());
			System.out.println(Arrays.toString(series[i].getRunFolders()));
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

		
//		Config.appendToList("GNUPLOT_CONFIG_1", "set logscale x");

		Type plotType = Type.average; // Type.confidence1;
		Style plotStyle = Style.linespoint; // Style.candlesticks;
		

		Plotting.multi(series, metrics.toArray(new Metric[0]), "multi/",
				plotType, plotStyle); // main path to plots
										// is set by
										// Config.overwrite

	}

	/**
	 * @param args
	 */
	private static void matchArguments(String[] args) {
		for (String s : args) {
			// System.out.println(s);
			// parse network generation details
			if (s.startsWith("sampling=")) {
				sampling = s.substring(9);
				// parse network generation details
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
