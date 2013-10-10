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
package gtna;

import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;
import gtna.util.Config;

import java.io.File;
import java.util.Arrays;

/**
 * @author Tim
 * 
 */

public class Exploring {
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Config.overwrite("GNUPLOT_PRINT_ERRORS", "true");
		
		
//		boolean get = false; // get or generate
		int times = 1;		// how many generations?
//		boolean b = false; // bidirectional?
//		boolean r = false; // ring?
//		
//		SamplingAlgorithm a = SamplingAlgorithm.RANDOMWALK;
//		double sc = 0.2;
//		
//		Transformation sa = SamplingAlgorithmFactory.getInstanceOf(a, sc, true, 1, null);
//		Transformation sa2 = SamplingAlgorithmFactory.getInstanceOf(a, sc, true, 1, null);
//		Transformation[] t = new Transformation[3];
//		
//		
//		
//		Arrays.fill(t, sa);
//		t[0] = sa;
//		t[1] = sa2;
//		t[t.length-1] = new ExtractSampledSubgraph();
//		
		
//		Network nw1 = new Regular(30, 10, true, false, null);
		Network nw3 = new ErdosRenyi(1000, 6.0, true, null);
//		Network nw1 = new WattsStrogatz(1000, 10, 0.1, null);
//		Network nw2 = new WattsStrogatz(1000, 10, 0.01, null);
//		
//		Network[] ws1 = ZhouMondragon.get(50, new double[] {0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6}, 7, null);
//		
//		Network[] nw = new Network[2];
//		Arrays.fill(nw, nw1);
//		Network nw0 = new GeneralizedCondonAndKarp(nw, 0.00005, t);
//		
		Network[] n = new Network[] {nw3};
////		Network[] n = ws1;
//		
//		DegreeDistribution m = new DegreeDistribution();
//		m.computeData(nw3.generate(), nw3, null);
//		
//		
		Metric[] metrics = new Metric[] { 
//				new DegreeDistribution(),
//				new ClusteringCoefficient(),
				new ShortestPaths()
//				new BetweennessCentrality(),
//				new Assortativity(),
//				new SamplingBias()
//				new PageRank(),
//				new SamplingModularity(),
//				new DegreeDistributionComparator(m),
//				new SamplingRevisitFrequency()
				};
		
//		Series[] s = get ? Series.get(n, metrics) : Series.generate(n, metrics, times);
		Series[] s = Series.generate(n, metrics, times);

		Plotting.single(s, metrics, "example-s/");

		Plotting.multi(s, metrics, "example-m/");
		
//		Graph g = nw3.generate();
//		g = t[0].transform(g);
//		g = t[1].transform(g);
//		
//		new GtnaGraphWriter().writeWithProperties(g, "./plots/network-plot/" + "base.txt");
//		for(int si = 0; si < 2; si++) {
//		    ((ExtractSampledSubgraph) t[2]).setIndex(si);
//		    Graph gi = t[2].transform(g);
//		    
//		    new GtnaGraphWriter().write(gi, "./plots/network-plot/" + "sample_" +si + ".txt");
//		    plotGraph(gi, "./plots/network-plot/", gi.toString() + "_"
//			+ si);
//		}	
		 
		
		
//		for(Network i : n){
//			 System.out.println("Plotting network - " + i.getKey() + " @ " + i.getNodes() + " nodes");
//			 plot(i, "./plots/network-plot/"+i.getDescriptionShort(), times, t);
//		 }
	}
	
	
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
	
	
	public static void plot(Network nw, String filename, int times, Transformation[] t) {
		Transformation t_rpid = new RandomPlaneIDSpaceSimple(1, 100, 100, true);
		Transformation t_rrid = new RandomRingIDSpaceSimple(true);
		Transformation t_crid = new ConsecutiveRingIDSpace(true);
		
		Transformation t_nid = t_crid;
		
		
		for (int i = 0; i < times; i++) {
			Gephi gephi = new Gephi();
			if(t_nid == t_rpid){
				Config.overwrite("GEPHI_RING_RADIUS", "1");
				Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.01");
				Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
				Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
				Config.overwrite("GEPHI_NODE_SIZE", "0.1");
			}else 
				if(t_nid == t_rrid || t_nid == t_crid){
					Config.overwrite("GEPHI_RING_RADIUS", "50");
					Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.01");
					Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
					Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
					Config.overwrite("GEPHI_NODE_SIZE", "0.001");	
			}

			String graphFilename = filename;
			
			Graph g = nw.generate();
			
			for(Transformation ti : t){
				g = ti.transform(g);
			}
			
			g = t_nid.transform(g);
	
			IdentifierSpace ids = (IdentifierSpace) g
					.getProperty("ID_SPACE_0");
	
//			new GtnaGraphWriter().writeWithProperties(g, graphFilename+".txt");
			
			System.out.println("Plotting > " + g.getNodeCount() + " nodes");
			gephi.plot(g, ids, graphFilename+".pdf");
			
			System.out.println(filename);
		}
	}
}
