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

import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.util.ReadableFolder;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ColorSampledSubgraph;
import gtna.transformation.sampling.subgraph.ColoredHeatmapSampledSubgraph;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;
import gtna.util.Config;

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
	private static Double scaledown;
	private static boolean rev = false;
	private static Integer dim;
	private static Long seed;
	private static String dir;
	private static Integer startIndex;
	private static Integer endIndex;
	private static String srcdir;
	private static String suffix = "";
	private static String targetdir;
	private static boolean withNeigborSet;
	private static boolean plotting = false;
	private static int samplingEndIndex;
	private static int samplingStartIndex;

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {

		if (args.length == 0
				|| (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
			printHelp();
			System.exit(1);
		}

		for (String s : args) {
			matchArgument(s);
		}
		if (!initialized()) {
			System.out.println("You have to set all necessary params first!");
			System.exit(1);
		}

		Transformation[] samplingTransformation = new Transformation[2];
		samplingTransformation[0] = instantiateSamplingTransformation(
				samplingAlgorithm, scaledown, dim, rev, seed);
		if (subgraph != null) {
			samplingTransformation[1] = subgraph;
		}

		ReadableFolder rf = new ReadableFolder(samplingAlgorithm.name(), dir,
				srcdir, suffix, null);
		// current index is 0!
		if (startIndex > 0) {
			for (int i = 0; i < startIndex; i++) {
				rf.incIndex();
			}
		}

		Graph g;
		for (int j = startIndex; j <= endIndex; j++) {
			g = rf.generate();
			if (g != null) {
				for (int si = samplingStartIndex; si < samplingEndIndex; si++) {
					Graph gi = samplingTransformation[0].transform(g); // sample
					// graph
					gi = samplingTransformation[1].transform(gi); // subgraph
					// generation/coloring

					writeGraphToFile(gi, targetdir + j + "/", gi.toString()
							+ "_" + si);

					if (plotting)
						plotGraph(gi, targetdir + j + "/" + "plots/",
								gi.toString() + "_" + si);
				}
				writeOriginalGraphWithPropertiesToFile(g, targetdir + j
						+ "/original-with-props/", g.toString());
				if (plotting)
					plotGraph(g, targetdir + j + "/plots/", g.toString());
			}
		}

	}

	/**
	 * @param g
	 * @param string
	 */
	private static void plotGraph(Graph g, String dir, String filename) {

		File d = new File(dir);
		if (!d.exists() || !d.isDirectory()) {
			d.mkdirs();
		}

		filename = dir + filename;

		Transformation t_rpid = new RandomPlaneIDSpaceSimple(1, 100, 100, true);
		Transformation t_rrid = new RandomRingIDSpaceSimple(true);
		Transformation t_crid = new ConsecutiveRingIDSpace(true);

		Transformation t_nid = t_crid;

		Gephi gephi = new Gephi();
		if (t_nid == t_rpid) {
			Config.overwrite("GEPHI_RING_RADIUS", "1");
			Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.01");
			Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
			Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
			Config.overwrite("GEPHI_NODE_SIZE", "0.1");
		} else if (t_nid == t_rrid || t_nid == t_crid) {
			Config.overwrite("GEPHI_RING_RADIUS", "50");
			Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.01");
			Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
			Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
			Config.overwrite("GEPHI_NODE_SIZE", "0.001");
		}

		g = t_nid.transform(g);

		IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");

		// new GtnaGraphWriter().writeWithProperties(g, graphFilename+".txt");
		// // writing is already done

		gephi.plot(g, ids, filename + ".pdf");

	}

	/**
	 * @return
	 */
	private static boolean initialized() {

		if (dim == null || dim < 1 || dir == null || srcdir == null
				|| targetdir == null || scaledown == null) {
			return false;
		}

		return true;

	}

	private static void printHelp() {
		System.out
				.println("Usage:"
						+ "sampling=<samplingalgorithm> dimension=<how many walker> scaledown=<sample size in percentage> revisiting=<true/false> randomSeed=<dd.mm.yyyy>"
						+ "loadDir=<directory with prepared networks>");
	}

	private static Transformation instantiateSamplingTransformation(
			SamplingAlgorithm alg, double sd, int dimension,
			boolean revisiting, Long randomSeed) {
		Transformation samplingalgorithm = SamplingAlgorithmFactory
				.getInstanceOf(alg, sd, revisiting, dimension, randomSeed);

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
			if (s.equalsIgnoreCase("rev=true")) {
				rev = true;
			} else {
				rev = false;
			}
		} else if (s.startsWith("sampleNeighborSet=")) {
			if (s.equalsIgnoreCase("sampleNeighborSet=true")) {
				withNeigborSet = true;
			} else {
				withNeigborSet = false;
			}
		} else if (s.startsWith("dim=")) {
			dim = Integer.parseInt(s.substring(4));
		} else if (s.startsWith("suffix=")) {
			suffix = s.substring(7);
		} else if (s.startsWith("subgraph=")) {
			String sg = s.substring(9);
			subgraph = matchSubgraph(sg);
		} else if (s.startsWith("randomSeed=")) {
			String seedDate = s.substring(11);
			if (!seedDate.isEmpty()) {
				seedDate.matches("[0-3][0-9]\\.[0-1][0-9]\\.[0-9][0-9][0-9][0-9]");
				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				Date seeddate = df.parse(seedDate);
				seed = seeddate.getTime();
			} else {
				seed = null;
			}
		} else if (s.startsWith("dir=")) {
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
		} else if (s.startsWith("targetdir=")) {
			targetdir = s.substring(10);
			File f = new File(targetdir);
			if (!f.isDirectory()) {
				f.mkdir();
			}
		} else if (s.startsWith("srcseq=")) {
			String seq = s.substring(7);
			String[] se = seq.split("-");
			startIndex = Integer.parseInt(se[0]);
			endIndex = Integer.parseInt(se[1]);
		} else if (s.startsWith("seq=")) {
			String seq = s.substring(4);
			String[] se = seq.split("-");
			samplingStartIndex = Integer.parseInt(se[0]);
			samplingEndIndex = Integer.parseInt(se[1]);
		} else if (s.startsWith("plot=")) {
			if (s.equalsIgnoreCase("plot=true")) {
				plotting = true;
			} else {
				plotting = false;
			}
		}

	}

	private static String writeGraphToFile(Graph g, String dir, String filename) {
		File d = new File(dir);
		if (!d.exists() || !d.isDirectory()) {
			d.mkdirs();
		}

		filename = dir + filename;
		// new GtnaGraphWriter().writeWithProperties(g, filename);
		new GtnaGraphWriter().write(g, filename + ".gtna");
		return filename;
	}

	/**
	 * @param g
	 * @param string
	 * @param string2
	 * @return
	 */
	private static String writeOriginalGraphWithPropertiesToFile(Graph g,
			String dir, String filename) {
		File d = new File(dir);
		if (!d.exists() || !d.isDirectory()) {
			d.mkdirs();
		}

		filename = dir + filename;
		new GtnaGraphWriter().writeWithProperties(g, filename);
		// new GtnaGraphWriter().write(g, filename + ".gtna");
		return filename;

	}

	/**
	 * @param sn
	 * @return
	 */
	private static SamplingAlgorithm matchSamplingAlgorithm(String sn) {
		for (SamplingAlgorithm sa : SamplingAlgorithm.values()) {
			if (sn.equalsIgnoreCase(sa.name())) {
				return sa;
			}
		}

		throw new IllegalArgumentException(
				"Sampling Algorithm unknown, please choose one of: "
						+ Arrays.toString(SamplingAlgorithm.values()));
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
