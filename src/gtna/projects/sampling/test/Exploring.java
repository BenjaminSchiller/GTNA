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
 * Exploring.java
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
package gtna.projects.sampling.test;

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.Assortativity;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.centrality.BetweennessCentrality;
import gtna.metrics.centrality.PageRank;
import gtna.metrics.util.ErrorComparison;
import gtna.metrics.util.TopKCorrelation;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.PFP;
import gtna.networks.model.PositiveFeedbackPreference;
import gtna.networks.model.Regular;
import gtna.networks.model.ZhouMondragon;
import gtna.networks.util.EmptyNetwork;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data.Type;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;
import gtna.util.Config;
import gtna.util.Timer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Tim
 * 
 */

public class Exploring {
	public static void main(String[] args) throws IOException {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		 Config.overwrite("GNUPLOT_PRINT_ERRORS", "false");
		 
		 
		 Runtime.getRuntime().exec(new String[]{"rm", "-rf",  "data/"});
		 Runtime.getRuntime().exec(new String[]{"rm", "-rf",  "plots/"});


		int times = 1; // how many generations?

		

		
		
		Network nw = new ErdosRenyi(50, 3, false, null);
//		Network nw = new ErdosRenyi(2000, 50, false, null);
		Network nw2 = new ErdosRenyi(100, 10, false, null);
		

		
		Metric[] metrics = new Metric[] {
				new BetweennessCentrality(),

		};
		
		Metric[] metrics2 = new Metric[] {

				new BetweennessCentrality(),

				};

		
		
		
		Graph g = nw.generate();
		
		(new GtnaGraphWriter()).write(g, "/Users/Tim/Documents/Projekte/gtna/source/graphs/test.gtna");
	
		Network n = new ReadableFile("test", "/Users/Tim/Documents/Projekte/gtna/source/graphs/", "/Users/Tim/Documents/Projekte/gtna/source/graphs/test.gtna", null);
		
		Series[] s = Series.generate(new Network[]{n}, metrics, times);
		
		
		
		
		/* SAMPLING */

		SamplingAlgorithm a = SamplingAlgorithm.RANDOMWALK;
		double sc = 0.9;
		Transformation sa = SamplingAlgorithmFactory.getInstanceOf(a, sc, true,
				1, null);
		
		Graph gsampled = sa.transform(g);
		
		(new GtnaGraphWriter()).writeWithProperties(g, "/Users/Tim/Documents/Projekte/gtna/source/graphs/test.gtna");
		gsampled = (new ExtractSampledSubgraph()).transform(gsampled);
		
		(new GtnaGraphWriter()).write(gsampled, "/Users/Tim/Documents/Projekte/gtna/source/graphs/sampled.gtna");
		
		Network n2 = new ReadableFile("sampled", "/Users/Tim/Documents/Projekte/gtna/source/graphs/", "/Users/Tim/Documents/Projekte/gtna/source/graphs/sampled.gtna", null);
		
		
		
		Series[] s2 = Series.generate(new Network[]{n2}, metrics2, times);
		
		
		
		Metric[] compM = {
				new TopKCorrelation(new BetweennessCentrality(), s, s2, 
						TopKCorrelation.Type.SAMPLE, TopKCorrelation.Mode.RUNWITHRUN)
		};
		
		Series[] sComp = Series.generate(new Network[] {new EmptyNetwork(nw, "TEST")}, compM, times);
//		Series[] s = Series.get(n, metrics);

		
		
		Style st = Style.linespoint; // Style.candlesticks;
		Type ty = Type.average; // Type.confidence2;
		
		
//		Config.appendToList("GNUPLOT_CONFIG_1", "set logscale x");
		Config.appendToList("GNUPLOT_CONFIG_2", "set boxwidth 0.1");

		Config.overwrite("GNUPLOT_LW", "1");
		
		Plotting.single(sComp, compM, "s/" + "topk" + "/", ty, st);
		Plotting.multi(sComp, compM, "m/" + "topk" + "/", ty, st);

		Plotting.single(s, metrics, "s/original" + "/", ty, st);
		Plotting.multi(s, metrics, "m/original" + "/", ty, st);
		Plotting.single(s2, metrics2, "s/sampled" + "/", ty, st);
		Plotting.multi(s2, metrics2, "m/sampled" + "/", ty, st);
		
		
		Runtime.getRuntime().exec(new String[]{"open",  "plots/"});

}

	/**
	 * @param g
	 * @param dir
	 * @param filename
	 * @param times
	 * @param t
	 */
	private static void plot(Graph g, String dir, String filename, int times,
			Object t) {
		// TODO Auto-generated method stub
		
	}

	public static void plot(Network nw, String dir, String filename, int times,
			Transformation[] t) {
		Transformation t_rpid = new RandomPlaneIDSpaceSimple(1, 100, 100, true);
		Transformation t_rrid = new RandomRingIDSpaceSimple(true);
		Transformation t_crid = new ConsecutiveRingIDSpace(true);

		Transformation t_nid = t_crid;

		for (int i = 0; i < times; i++) {
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

			File d = new File(dir);
			if (!d.exists() || !d.isDirectory()) {
				d.mkdirs();
			}
			d = null;
			
			String graphFilename = dir+filename;

			Graph g = nw.generate();

			for (Transformation ti : t) {
				g = ti.transform(g);
			}

			g = t_nid.transform(g);

			IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");

			// new GtnaGraphWriter().writeWithProperties(g,
			// graphFilename+".txt");

			System.out.println("Plotting > " + g.getNodeCount() + " nodes");
			gephi.plot(g, ids, graphFilename + ".pdf");

			System.out.println(filename);
		}
	}
}
