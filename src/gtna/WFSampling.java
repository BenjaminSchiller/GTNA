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

import gtna.graph.Graph;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.util.ReadableFolder;
import gtna.transformation.Transformation;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ColorSampledSubgraph;
import gtna.transformation.sampling.subgraph.ColoredHeatmapSampledSubgraph;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Tim
 * 
 */
public class WFSampling {

	private static Transformation subgraph;
	private static SamplingAlgorithm samplingAlgorithm;
	private static double scaledown;
	private static boolean rev;
	private static int dim;
	private static long seed;
	private static String dir;
	private static int startIndex;
	private static int endIndex;
	private static String srcdir;
	private static String suffix;
	private static String targetdir;

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		
		
	    if (args.length == 0
			|| (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
		    printHelp();
		    System.exit(0);
		}

		for (String s : args) {
		    matchArgument(s);
		}
		if(!initialized()) {
		    System.out.println("You have to set all necessary params first!");
		}
		
		Transformation[] samplingTransformation = new Transformation[2];
		samplingTransformation[0] = instantiateSamplingTransformation(samplingAlgorithm, scaledown, dim, rev, seed);
		if(subgraph != null) {
		    samplingTransformation[1] = subgraph;
		}
		
		ReadableFolder rf = new ReadableFolder(samplingAlgorithm.name(), dir, srcdir, suffix, samplingTransformation); 
		// current index is 0!
		if(startIndex > 0) {
		    for(int i = 0; i < startIndex; i++) {
			rf.incIndex();
		    }
		}
		
		Graph g;
		for(int j = startIndex; j < endIndex; j++) {
		    g = rf.generate();
		    if(g!=null) {
		    g = samplingTransformation[0].transform(g); // sample graph
		    g = samplingTransformation[1].transform(g);	// subgraph generation/coloring
		    
		    writeGraphToFile(g, targetdir+g.getName());
		    }
		}

	}

	/**
	 * @return
	 */
	private static boolean initialized() {
	    // TODO Auto-generated method stub
	    return true;
	}

	private static void printHelp() {
		System.out.println("Usage:"
				+ "sampling=<samplingalgorithm> dimension=<how many walker> scaledown=<sample size in percentage> revisiting=<true/false> randomSeed=<dd.mm.yyyy>"
				+ "loadDir=<directory with prepared networks>");
	}

	private static Transformation instantiateSamplingTransformation(
			SamplingAlgorithm alg, double sd, int dimension,
			boolean revisiting, Long randomSeed) {
		Transformation samplingalgorithm = SamplingAlgorithmFactory
				.getInstanceOf(alg, sd, revisiting, dimension,
						randomSeed);
		return samplingalgorithm;
	}
	
	
	/**
	     * @param s
	     * @throws ParseException
	     */
	    private static void matchArgument(String s) throws ParseException {

		// parse network generation details
		if (s.startsWith("sampling=")) {
		    String sn = s.substring(9);
		    samplingAlgorithm = matchSamplingAlgorithm(sn);
		} else if (s.startsWith("scaledown=")) {
		    scaledown = Double.parseDouble(s.substring(10));
		} else if (s.startsWith("rev=")) {
		    if(s.equalsIgnoreCase("rev=true")) {
			rev = true;
		    }else {
			rev = false;
		    }
		} else if (s.startsWith("dim=")) {
		    dim = Integer.parseInt(s.substring(4));
		}else if (s.startsWith("suffix=")) {
		    suffix = s.substring(7);
		}  
		else if (s.startsWith("subgraph=")) {
		    String sg = s.substring(9);
		    subgraph = matchSubgraph(sg);
		}  else if(s.startsWith("randomSeed=")){
			String seedDate = s.substring(11);
			seedDate.matches("[0-3][0-9]\\.[0-1][0-9]\\.[0-9][0-9][0-9][0-9]");
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Date seeddate =  df.parse(seedDate);
			seed = seeddate.getTime();
		} 
		else if (s.startsWith("dir=")) {
		    dir = s.substring(4);
		    File f = new File(dir);
		    if (!f.isDirectory()) {
			f.mkdir();
		    }
		} else if (s.startsWith("srcdir=")) {
		    srcdir = s.substring(7);
		    File f = new File(srcdir);
		    if (!f.isDirectory()) {
			f.mkdir();
		    }
		}else if (s.startsWith("targetdir=")) {
		    targetdir = s.substring(10);
		    File f = new File(targetdir);
		    if (!f.isDirectory()) {
			f.mkdir();
		    }
		} else if (s.startsWith("seq=")) {
		    String seq = s.substring(4);
		    String[] se = seq.split("-");
		    startIndex = Integer.parseInt(se[0]);
		    endIndex = Integer.parseInt(se[1]);
		} 
		
	}

	    
	private static String writeGraphToFile(Graph g, String filename) {
		new GtnaGraphWriter().writeWithProperties(g, filename);
		return filename;
	}
	    
	/**
	 * @param sn
	 * @return
	 */
	private static SamplingAlgorithm matchSamplingAlgorithm(String sn) {
	   for(SamplingAlgorithm sa : SamplingAlgorithm.values()) {
	       if(sn.equalsIgnoreCase(sa.name())) {
		   return sa;
	       }
	   }
	   
	   throw new IllegalArgumentException("Sampling Algorithm unknown, please choose one of: " + Arrays.toString(SamplingAlgorithm.values()));    
	}

	/**
	 * @param sg
	 * @return
	 */
	private static Transformation matchSubgraph(String sg) {
	    Transformation subgraph;
		if (sg.equalsIgnoreCase("subgraph")) {
		    subgraph = new ExtractSampledSubgraph();
		} else if (sg.equalsIgnoreCase("coloring")) {
		    subgraph = new ColorSampledSubgraph();
		} else if (sg.equalsIgnoreCase("heatmap")) {
		    subgraph = new ColoredHeatmapSampledSubgraph();
		} else {
			throw new IllegalArgumentException(
					"subgraph must be one of {subgraph, coloring, heatmap}");
		}
		
		return subgraph;
	} 

}
