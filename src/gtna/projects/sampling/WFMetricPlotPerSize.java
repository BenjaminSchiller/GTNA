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
public class WFMetricPlotPerSize {

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
	private static LinkedList<String> net = new LinkedList<String>();
	private static String plotdir;
	private static String skipping="false";

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {

	Set<Metric> metrics = new HashSet<Metric>();

	if (args.length == 1) {
	    if (args[0].equalsIgnoreCase("help")) {
		printHelp();
		System.exit(0);
	    }
	}

	System.out.println("> PARAMS: " + Arrays.toString(args));

	for (String s : args) {
//		System.out.println(s);
		// parse network generation details
		if (s.startsWith("sampling=")) {
			sampling.add(s.substring(9));
			// parse network generation details
		} else if (s.startsWith("network=")) {
			net.add(s.substring(8));
		} else if (s.startsWith("scaledown=")) {
			scaledown.add(Double.parseDouble(s.substring(10)));
		} else if (s.startsWith("size=")) {
			size.add(Integer.parseInt(s.substring(5)));
		} else if (s.equalsIgnoreCase("DD")) {
			metrics.add(new DegreeDistribution());
		} else if (s.equalsIgnoreCase("CC")) {
			metrics.add(new ClusteringCoefficient());
		} else if (s.equalsIgnoreCase("HP") || s.equalsIgnoreCase("DIAM")
				|| s.equalsIgnoreCase("ECC")) {
			metrics.add(new ShortestPaths());
		} else if (s.equalsIgnoreCase("BC")) {
			metrics.add(new BetweennessCentrality());
		} else if (s.equalsIgnoreCase("PR")) {
			metrics.add(new PageRank());
		} else if (s.equalsIgnoreCase("ASS")) {
			metrics.add(new Assortativity());
		} else if (s.startsWith("suffix=")) {
			suffix = s.substring(7);
		} else if (s.startsWith("seq=")) {
			String seq = s.substring(4);
			String[] se = seq.split("-");
			startIndex.add(Integer.parseInt(se[0]));
			endIndex.add(Integer.parseInt(se[1]));
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
		} else if (s.startsWith("aggregate=")) {
			if (s.equals("aggregate=true")) {
				aggregate = true;
			} else {
				aggregate = false;
			}
		}else if (s.startsWith("skip=")) {
			if (s.equals("skip=true")) {
				skipping = "true";
			} else {
				skipping = "false";
			}
		}
		// readable folder?
		else if (s.startsWith("loaddir=")) {
			dirs.add(s.substring(8));
		} else {
			printHelp();
			System.exit(0);
		}
	}

	Config.overwrite("MAIN_PLOT_FOLDER", plotdir + "plots-bySize/");

	Collection<Network> rfc = new ArrayList<Network>();

	String[] dir = dirs.toArray(new String[0]);
	for (int i = 0; i < dir.length; i++) {
		String d = dirs.get(i);
		name = net.get(i) + "-" + sampling.get(i).trim() + "-" + size.get(i) + "-"
				+ scaledown.get(i);
		ReadableFolder rf = new ReadableFolder(name, net.get(0), d, suffix, null);
		
		System.out.println("RF: " + d + " - size:" + rf.getFiles().length);
		
		DescriptionWrapper dwrf = new DescriptionWrapper(rf, name);
		rfc.add(dwrf);

	}
	Network[] rfa = rfc.toArray(new Network[0]);

	if (!aggregate) {
	    System.err
		    .println("This jar-File should be used to replot already calculated metrics. The aggregate property has to be set true");
	} else {
	    Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
	    Series[] series = new Series[rfa.length];
	    Series[][] s = new Series[1][rfa.length];
	    for (int i = 0; i < rfa.length; i++) {
	    	Config.overwrite("MAIN_DATA_FOLDER", targetdir  + sampling.get(i) + "/" + rfa[i].getNodes()
					+ "/data/");
		series[i] = Series.generate(rfa[i],
			metrics.toArray(new Metric[0]), startIndex.get(i), endIndex.get(i));
		s[0][i] = series[i];

	    }
	    
	    
	    Plotting.multi(s, metrics.toArray(new Metric[0]), "/multi/",
		    Type.confidence1, Style.candlesticks); // main path to plots
							   // is set by
							   // Config.overwrite
	    
	    Plotting.single(s, metrics.toArray(new Metric[0]), "/single/",
		    Type.confidence1, Style.candlesticks); // main path to plots
							   // is set by
							   // Config.overwrite
	    
	}

    }

    private static void printHelp() {
	System.out.println("Wrong parameter settings!");
    }

}
