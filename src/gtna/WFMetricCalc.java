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
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plotting;
import gtna.util.Config;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Tim
 * 
 */
public class WFMetricCalc {

    private static Collection<Metric> metrics = new ArrayList<Metric>();
    private static String dir;
    private static String suffix;
    private static String name;
    private static int startIndex;
    private static int endIndex;
    private static String targetdir;

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
	Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

	if (args.length == 1) {
	    if (args[0].equalsIgnoreCase("help")) {
		printHelp();
		System.exit(0);
	    }
	}

	for (String s : args) {

	    if (s.equalsIgnoreCase("DD")) {
		metrics.add(new DegreeDistribution());
	    } else if (s.equalsIgnoreCase("CC")) {
		metrics.add(new ClusteringCoefficient());
	    } else if (s.equalsIgnoreCase("HP")) {
		System.err
			.println("HopPlot is currentliy not supported. (not implemented now)");
		// metrics.add(new HopPlot());
	    } else if (s.equalsIgnoreCase("DIAM")) {
		metrics.add(new ShortestPaths());
	    } else if (s.equalsIgnoreCase("ECC")) {
		System.err
			.println("ECC is currentliy not supported. (not implemented now)");
		// metrics.add(new Eccentricity()); // Check BSc. Thesis
		// implementation!
	    } else if (s.equalsIgnoreCase("BC")) {
		System.err
			.println("BetweennessCentrality is currentliy not supported. (not implemented now)");
		// metrics.add(new BetweennessCentrality());
	    } else if (s.equalsIgnoreCase("PR")) {
		System.err
			.println("PageRank is currentliy not supported. (not implemented now)");
		// metrics.add(new PageRankDistribution());
	    } else if (s.equalsIgnoreCase("ASS")) {
		System.err
			.println("Assortativity is currentliy not supported. (not implemented now)");
		// metrics.add(new Assortativity());
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
	    }
	    // readable folder?
	    else if (s.startsWith("loaddir=")) {
		dir = s.substring(8);
	    } else {
		printHelp();
		System.exit(0);
	    }
	}
	
	Config.overwrite("MAIN_DATA_FOLDER", targetdir + "data/");
	Config.overwrite("MAIN_PLOT_FOLDER", targetdir + "plots/");

	ReadableFolder rf = new ReadableFolder(name, dir, dir, suffix, null);
	// current index is 0!
	if (startIndex > 0) {
	    for (int i = 0; i < startIndex; i++) {
		rf.incIndex();
	    }
	}

	Series series = Series.generate(rf, metrics.toArray(new Metric[0]),
		endIndex - startIndex);

	Plotting.single(series, metrics.toArray(new Metric[0]), targetdir
		+ "/single/");

	Plotting.multi(series, metrics.toArray(new Metric[0]), targetdir
		+ "/multi/");

    }

    private static void printHelp() {
	System.out
		.println("Usage:"
			+ "sampling=<samplingalgorithm> dimension=<how many walker> scaledown=<sample size in percentage> revisiting=<true/false> randomSeed=<dd.mm.yyyy>"
			+ "loadDir=<directory with prepared networks>"
			+ "network=<synthetic network> size=<number of nodes> p1=<network_probability1> p2=<network_probability2> bidirectional=<true/false>");
    }

}
