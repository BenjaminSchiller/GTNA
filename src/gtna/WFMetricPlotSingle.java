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
public class WFMetricPlotSingle {
    private static String suffix;
    private static String name;
    private static int startIndex;
    private static int endIndex;

    private static LinkedList<String> dirs = new LinkedList<String>();
    private static String orgdir = "";
    private static String dataDir;
    private static String plotDir;

    private static Collection<ReadableFolder> rf0 = new ArrayList<ReadableFolder>();
    private static Collection<ReadableFolder> rf1 = new ArrayList<ReadableFolder>();
    private static Collection<ReadableFolder> rf2 = new ArrayList<ReadableFolder>();
    private static Collection<ReadableFolder> rf3 = new ArrayList<ReadableFolder>();
    private static Collection<ReadableFolder> rf4 = new ArrayList<ReadableFolder>();
    private static Collection<ReadableFolder> rfO = new ArrayList<ReadableFolder>();

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {

	Set<Metric> metrics = new HashSet<Metric>();

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
	    } else if (s.startsWith("plotdir=")) {
		plotDir = s.substring(8);
		File f = new File(plotDir);
		if (!f.isDirectory()) {
		    f.mkdir();
		}
	    } else if (s.startsWith("datadir=")) {
		dataDir = s.substring(8);
		File f = new File(dataDir);
		if (!f.isDirectory()) {
		    f.mkdir();
		}
	    }
	    // readable folder?
	    else if (s.startsWith("loaddir=")) {
		dirs.add(s.substring(8));
	    }
	    // readable folder?
	    else if (s.startsWith("origdir=")) {
		orgdir = s.substring(8);
	    }
	}

	Config.overwrite("MAIN_PLOT_FOLDER",
		plotDir);

	for (String dir : dirs) {
	    for (int i = 0; i < 5; i++) {
		for (int j = 1; j <= 9; j++) {
		    String di = dir + "/0." + j + "/" + i + "/";
		    if (new File(di).isDirectory()) {
			
			ReadableFolder rf = new ReadableFolder(name + "-" + i,
				name, di, suffix, null);
			System.out.println("RF: " + di + " \t with (" + rf.getFiles().length + ") networks");
			if (rf != null) {
			    switch (i) {
			    case 0:
				rf0.add(rf);
				break;
			    case 1:
				rf1.add(rf);
				break;
			    case 2:
				rf2.add(rf);
				break;
			    case 3:
				rf3.add(rf);
				break;
			    case 4:
				rf4.add(rf);
				break;
			    }
			}

		    } else {
			System.out.println("No RF: " + di);
		    }
		}
	    }
	}

	if (!orgdir.equalsIgnoreCase("")) {
	    for(int j = 1; j <= 9; j++) {
		String od = orgdir + "/" +  new Integer((int) (10000 * Double.parseDouble("0." + j)));
		if(new File(od).isDirectory()) {
		    
		    ReadableFolder rfo = new ReadableFolder(name, name, od, suffix, null);
		    System.out.println("RF: " + od + " \t with (" + rfo.getFiles().length + ") networks");
		    rfO.add(rfo);
		} else {
		    System.out.println("NO RF: " + od);
		    System.exit(0);
		}
		
	    }
	}

	Network[] rfa0 = rf0.toArray(new ReadableFolder[0]);
	Network[] rfa1 = rf1.toArray(new ReadableFolder[0]);
	Network[] rfa2 = rf2.toArray(new ReadableFolder[0]);
	Network[] rfa3 = rf3.toArray(new ReadableFolder[0]);
	Network[] rfa4 = rf4.toArray(new ReadableFolder[0]);
	Network[] rfaO = rfO.toArray(new ReadableFolder[0]);

	Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

	Series[][] s = new Series[6][9];

	for (int j = 0; j < 9; j++) {
	    Config.overwrite("MAIN_DATA_FOLDER", dataDir + "0" + "/data/");
	    s[0][j] = Series.generate(rfa0[j], metrics.toArray(new Metric[0]),
		    startIndex, endIndex);
	}
	for (int j = 0; j < 9; j++) {
	    Config.overwrite("MAIN_DATA_FOLDER", dataDir + "1" + "/data/");
	    s[1][j] = Series.generate(rfa1[j], metrics.toArray(new Metric[0]),
		    startIndex, endIndex);
	}
	for (int j = 0; j < 9; j++) {
	    Config.overwrite("MAIN_DATA_FOLDER", dataDir + "2" + "/data/");
	    s[2][j] = Series.generate(rfa2[j], metrics.toArray(new Metric[0]),
		    startIndex, endIndex);
	}
	for (int j = 0; j < 9; j++) {
	    Config.overwrite("MAIN_DATA_FOLDER", dataDir + "3" + "/data/");
	    s[3][j] = Series.generate(rfa3[j], metrics.toArray(new Metric[0]),
		    startIndex, endIndex);
	}
	for (int j = 0; j < 9; j++) {
	    Config.overwrite("MAIN_DATA_FOLDER", dataDir + "4" + "/data/");
	    s[4][j] = Series.generate(rfa4[j], metrics.toArray(new Metric[0]),
		    startIndex, endIndex);
	}
	if (rfaO != null) {
	    Config.overwrite("MAIN_DATA_FOLDER", dataDir + "data/");
	    for (int j = 0; j < 9; j++) {
		s[5][j] = Series.generate(rfaO[j], metrics.toArray(new Metric[0]),
			startIndex, endIndex);
	    }
	}
	
	
	Plotting.single(s, metrics.toArray(new Metric[0]), "/single/",
		Type.confidence1, Style.candlesticks); // main path to plots
	

    }

}
