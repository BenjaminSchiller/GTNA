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
package gtna;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.Assortativity;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.centrality.BetweennessCentrality;
import gtna.metrics.centrality.PageRank;
import gtna.networks.Network;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plotting;
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
public class WFMetricCalc {

	private static String dir;
	private static String suffix;
	private static String name;
	private static int startIndex;
	private static int endIndex;
	private static String targetdir;
	private static boolean aggregate = false;
	private static LinkedList<String> dirs = new LinkedList<String>();
	private static String orgdir = "";
	private static Network rfo;

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

			if (s.equalsIgnoreCase("DD")) {
				metrics.add(new DegreeDistribution());
			} else if (s.equalsIgnoreCase("CC")) {
				metrics.add(new ClusteringCoefficient());
			} else if (s.equalsIgnoreCase("HP")) {
				metrics.add(new ShortestPaths());
			} else if (s.equalsIgnoreCase("DIAM")) {
				metrics.add(new ShortestPaths());
			} else if (s.equalsIgnoreCase("ECC")) {
				metrics.add(new ShortestPaths());
			} else if (s.equalsIgnoreCase("BC")) {
				metrics.add(new BetweennessCentrality());
			} else if (s.equalsIgnoreCase("PR")) {
				metrics.add(new PageRank());
			} else if (s.equalsIgnoreCase("ASS")) {
				metrics.add(new Assortativity());
			} else if (s.startsWith("suffix=")) {
				suffix = s.substring(7);
			} else if (s.startsWith("name=")) {
				name = s.substring(5);
			} else if (s.startsWith("seq=")) {
				String seq = s.substring(4);
				String[] se = seq.split("-");
				startIndex = Integer.parseInt(se[0]);
				endIndex = Integer.parseInt(se[1]);
			} else if (s.startsWith("targetdir=")) {
				targetdir = s.substring(10);
				File f = new File(targetdir);
				if (!f.isDirectory()) {
					f.mkdir();
				}
			} else if (s.startsWith("aggregate=")) {
				if(s.equals("aggregate=true")){
					aggregate = true;
				} else {
					aggregate = false;
				}
			}
			// readable folder?
			else if (s.startsWith("loaddir=")) {
				dirs.add(s.substring(8));
			} 
			// readable folder?
			else if (s.startsWith("origdir=")) {
				orgdir = s.substring(8);
			}else {
				printHelp();
				System.exit(0);
			}
		}

		Config.overwrite("MAIN_DATA_FOLDER", targetdir + "data/");
		Config.overwrite("MAIN_PLOT_FOLDER", targetdir + "plots/");
		
		
		Collection<ReadableFolder> rfc = new ArrayList<ReadableFolder>();
		for(String dir : dirs.toArray(new String[0])) {
		    ReadableFolder rf = new ReadableFolder(name, name, dir, suffix, null);
		    
		 // current index is 0!
//			if (startIndex > 0) {
//				for (int i = 0; i < startIndex; i++) {
//					rf.incIndex();
//				}
//			}
		    
		    rfc.add(rf);
		    
		}
		
		if(!orgdir.equalsIgnoreCase("")) {
		    rfo = new ReadableFolder(name, name, orgdir, suffix, null);
		}
//		
		
//		
		Network[] rfa = rfc.toArray(new ReadableFolder[0]);
		
		if(!aggregate){	
			Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
						
			Series[] series = new Series[rfa.length];
			
			for(int i = 0; i < rfa.length; i++) {
			    series[i] = Series.generate(rfa[i], metrics.toArray(new Metric[0]), startIndex, endIndex);
			}	
		
			if(rfo != null) {
			    Series so = Series.generate(rfo, metrics.toArray(new Metric[0]), 1);
			    Series[] ser = new Series[series.length+1];
			    for(int i = 0; i < series.length; i++) {
				ser[i] = series[i];
			    }
			    ser[ser.length-1] = so;
			}
		} else {
			Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
			int t = endIndex-startIndex;
			System.out.println("S: " + startIndex + " E: " + endIndex + " E-S: " + t);
			Series[] series = new Series[rfa.length];
			for(int i = 0; i < rfa.length; i++) {
			    series[i] = Series.generate(rfa[i], metrics.toArray(new Metric[0]), startIndex, endIndex);
			}	
			if(rfo != null) {
			    Series so = Series.generate(rfo, metrics.toArray(new Metric[0]), 1);
			    Series[] ser = new Series[series.length+1];
			    for(int i = 0; i < series.length; i++) {
				ser[i] = series[i];
			    }
			    ser[ser.length-1] = so;
			    series = ser;
			}
			Plotting.single(series, metrics.toArray(new Metric[0]), "/single/");  // main path to plots is set by Config.overwrite

			Plotting.multi(series, metrics.toArray(new Metric[0]), "/multi/"); // main path to plots is set by Config.overwrite
		
		}

	}

	private static void printHelp() {
		System.out
				.println("Wrong parameter settings!");
	}

}
