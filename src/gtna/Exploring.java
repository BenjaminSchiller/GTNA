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

import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.GeneralizedCondonAndKarp;
import gtna.networks.model.Regular;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ColoredHeatmapSampledSubgraph;
import gtna.util.Config;

import java.util.Arrays;

/**
 * @author Tim
 * 
 */

public class Exploring {
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Config.overwrite("GNUPLOT_PRINT_ERRORS", "true");
		
		
		boolean get = false; // get or generate
		int times = 1;		// how many generations?
		boolean b = false; // bidirectional?
		boolean r = false; // ring?
		
		SamplingAlgorithm a = SamplingAlgorithm.BFS;
		double sc = 0.8;
		
		Transformation sa = SamplingAlgorithmFactory.getInstanceOf(a, sc, false, 1, new Long(0), true);
		Transformation sa2 = SamplingAlgorithmFactory.getInstanceOf(a, sc, false, 1, new Long(0), false);
		Transformation[] t = new Transformation[3];
		
		
		
		Arrays.fill(t, sa);
		t[0] = sa;
		t[1] = sa2;
		t[2] = new ColoredHeatmapSampledSubgraph();
		
		
//		Network nw1 = new Regular(30, 10, true, false, null);
		Network nw1 = new Regular(20, 2, true, false, t);
		
		Network[] nw = new Network[2];
		Arrays.fill(nw, nw1);
		Network nw0 = new GeneralizedCondonAndKarp(nw, 0.00005, t);
		
		Network[] n = new Network[] {nw0 /*, nw1*/};
		
		Metric[] metrics = new Metric[] { 
				new DegreeDistribution()
				};
		
//		Series[] s = get ? Series.get(n, metrics) : Series.generate(n, metrics, times);
//		Series[] s = Series.generate(n, metrics, times);
//
//		Plotting.single(s, metrics, "example-s/");
//
//		Plotting.multi(s, metrics, "example-m/");
////		 
		 for(Network i : n){
			 System.out.println("Plotting network - " + i.getKey() + " @ " + i.getNodes() + " nodes");
			 plot(i, "./plots/network-plot/n-"+i.getKey() + "-" + i.getNodes(), times, t);
		 }
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
	
			new GtnaGraphWriter().writeWithProperties(g, graphFilename+".txt");
			
			gephi.plot(g, ids, graphFilename+".pdf");
			
			System.out.println(filename);
		}
	}
}
