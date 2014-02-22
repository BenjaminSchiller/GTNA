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
import gtna.metrics.Metric;
import gtna.metrics.basic.Assortativity;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.centrality.BetweennessCentrality;
import gtna.metrics.util.ErrorComparison;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.PFP;
import gtna.networks.model.PositiveFeedbackPreference;
import gtna.networks.model.Regular;
import gtna.networks.model.ZhouMondragon;
import gtna.networks.util.EmptyNetwork;
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


		int times = 100; // how many generations?

		
		SamplingAlgorithm a = SamplingAlgorithm.RANDOMWALK;
		double sc = 0.2;
		//
		Transformation sa = SamplingAlgorithmFactory.getInstanceOf(a, sc, true,
				1, null);
		// 
		Transformation[] t = new Transformation[0];

		
		
		Network nw = new ErdosRenyi(100, 10, false, null);
		Network nw2 = new ErdosRenyi(100, 50, false, null);
		
		Network[] n = new Network[] { nw };
		Network[] n2 = new Network[] { nw2 };
		
		Metric[] metrics = new Metric[] {
		 new DegreeDistribution(),
		 new ClusteringCoefficient(),
		 new ShortestPaths(),
//		new BetweennessCentrality()
		 new BetweennessCentrality(),
		 new Assortativity()
		// new SamplingBias()
		// new PageRank(),
		// new SamplingModularity(),
		// new DegreeDistributionComparator(m),
		// new SamplingRevisitFrequency()
		};
		
		Metric[] metrics2 = new Metric[] {
				 new DegreeDistribution(),
				 new ClusteringCoefficient(),
				 new ShortestPaths(),
				new BetweennessCentrality(),
				// new BetweennessCentrality(),
				 new Assortativity()
				// new SamplingBias()
				// new PageRank(),
				// new SamplingModularity(),
				// new DegreeDistributionComparator(m),
				// new SamplingRevisitFrequency()
				};

		
		Timer timer = new Timer();
		
		Series[] s = Series.generate(n, metrics, times);
		Series[] s2 = Series.generate(n2, metrics2, 1);
		
		
		
		Metric[] compM = {
				new ErrorComparison(new DegreeDistribution(), s2, s, ErrorComparison.BASEWITHRUN),
//				new ErrorComparison(new ClusteringCoefficient(), s2, s, ErrorComparison.BASEWITHRUN),
//				new ErrorComparison(new ShortestPaths(), s2, s, ErrorComparison.BASEWITHRUN),
//				new ErrorComparison(new Assortativity(), s2, s, ErrorComparison.BASEWITHRUN),
//				new ErrorComparison(new BetweennessCentrality(), s2, s, ErrorComparison.BASEWITHRUN)
		};
		
		Series[] sComp = Series.generate(new Network[] {new EmptyNetwork(nw2, "TEST")}, compM, times);
//		Series[] s = Series.get(n, metrics);
		timer.end();
		System.out.println("Gen: " + timer.getMsec() + "ms");
		
		
		Style st = Style.candlesticks;
		Type ty = Type.confidence2;
		
		
//		Config.appendToList("GNUPLOT_CONFIG_1", "set logscale x");
		Config.appendToList("GNUPLOT_CONFIG_2", "set boxwidth 0.1");

		Config.overwrite("GNUPLOT_LW", "2");
		
		Plotting.single(sComp, compM, "s/" + nw.getNameShort() + "/", ty, st);
		Plotting.multi(sComp, compM, "m/" + nw.getNameShort() + "/", ty, st);
		
		Runtime.getRuntime().exec(new String[]{"open",  "plots/"});

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
