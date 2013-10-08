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
import gtna.metrics.sampling.SamplingBias;
import gtna.metrics.sampling.SamplingModularity;
import gtna.metrics.sampling.SamplingRevisitFrequency;
import gtna.networks.Network;
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
import java.util.Set;

/**
 * @author Tim
 * 
 */
public class WFSamplingMetricCalc {

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
	private static boolean samplingModularity;
	private static boolean samplingBias;
	private static boolean samplingRevisitFrequency;
	private static int instances;

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

			if (s.equalsIgnoreCase("SB")) {		// has to be computed in an extra run as it needs the original graph with sampling properties
				samplingBias = true;
			    
			}else if (s.equalsIgnoreCase("SM")) {		// has to be computed in an extra run as it needs the original graph with sampling properties
				samplingModularity = true;
			    
			}else if (s.equalsIgnoreCase("SRF")) {		// has to be computed in an extra run as it needs the original graph with sampling properties
				samplingRevisitFrequency = true;
			    
			}
			else if (s.startsWith("suffix=")) {
				suffix = s.substring(7);
				if(suffix.isEmpty()){
					suffix = "";
				}
			} else if (s.startsWith("name=")) {
				name = s.substring(5);
			}else if (s.startsWith("instances=")) {
				instances = Integer.parseInt(s.substring(10));
				
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
			
		
			}else {
			    	System.out.print("WRONG PARAMETER: >> " + s);
				printHelp();
				System.exit(0);
			}
		}

		Config.overwrite("MAIN_DATA_FOLDER", targetdir + "data/");
		Config.overwrite("MAIN_PLOT_FOLDER", targetdir + "plots/");
		
		if(samplingBias)
		    metrics.add(new SamplingBias());
		
		if(samplingModularity) {
			metrics.add(new SamplingModularity());
		   
		}
		    
		
		if(samplingRevisitFrequency) {
		    for(int i = 0; i < instances; i++) {
			metrics.add(new SamplingRevisitFrequency(i));
		    }
		}
		
//	    ReadableFile rf = new ReadableFile(name, dir, "RANDOMWALK (Nodes = 1000) (1000)", null);
		
		ArrayList<ReadableFolder> rfc = new ArrayList<ReadableFolder>();
		for(String d : dirs){
			ReadableFolder rf = new ReadableFolder(name, "", d, "", null);
			rfc.add(rf);
		}
		
		ReadableFolder[] rfa = rfc.toArray(new ReadableFolder[0]);
		if(!aggregate){	
//			Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
//						
//			Series[] series = new Series[rfa.length];
//			
//			for(int i = 0; i < rfa.length; i++) {
//			    series[i] = Series.generate(rfa[i], metrics.toArray(new Metric[0]), startIndex, endIndex);
//			}
		} else {
			Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
			int t = endIndex-startIndex;
			System.out.println("S: " + startIndex + " E: " + endIndex + " E-S: " + t);
			Series[] series = new Series[rfa.length];
			for(int i = 0; i < rfa.length; i++)
				series[i] = Series.generate(rfa[i], metrics.toArray(new Metric[0]), 1);
			
			Plotting.single(series, metrics.toArray(new Metric[0]), "/single/");  // main path to plots is set by Config.overwrite
			Plotting.multi(series, metrics.toArray(new Metric[0]), "/multi/"); // main path to plots is set by Config.overwrite
		
		}

	}

	private static void printHelp() {
		System.out
				.println("Wrong parameter settings!");
	}

}
