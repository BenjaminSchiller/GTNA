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
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metric.centrality.PageRank;
=======
>>>>>>> optimizing the PR metric
=======
import gtna.io.graphWriter.GtnaGraphWriter;
>>>>>>> fixes:
import gtna.metrics.Metric;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.metrics.basic.Assortativity;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
<<<<<<< HEAD
<<<<<<< HEAD
import gtna.networks.Network;
<<<<<<< HEAD
import gtna.networks.model.ZhouMondragon;
=======
import gtna.metrics.centrality.BetweennessCentrality;
=======
import gtna.metrics.sampling.SamplingBias;
>>>>>>> SamplingBias properties
import gtna.networks.Network;
<<<<<<< HEAD
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.Regular;
>>>>>>> bugfix bc (1)
=======
=======
import gtna.metrics.sampling.SamplingModularity;
>>>>>>> Sampling Modularity implemented
import gtna.networks.Network;
<<<<<<< HEAD
import gtna.networks.model.ErdosRenyi;
>>>>>>> calculate prVector
=======
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.WattsStrogatz;
>>>>>>> fully functional implementation of the PageRank metric. ToDo: normalization of PageRank (prVector)
=======
import gtna.networks.model.Regular;
>>>>>>> normalization of the pr-vector
=======
import gtna.networks.model.BarabasiAlbert;
<<<<<<< HEAD
>>>>>>> small improvements using sampling bias
=======
import gtna.networks.model.ErdosRenyi;
>>>>>>> Sampling Modularity implemented
import gtna.plot.Plotting;
=======
=======
import gtna.metrics.basic.Assortativity;
import gtna.metrics.basic.ClusteringCoefficient;
>>>>>>> plotting for example plots
import gtna.metrics.basic.DegreeDistribution;
=======
>>>>>>> added relative eccentricity / effective diameter
import gtna.metrics.basic.ShortestPaths;
=======
import gtna.metrics.basic.DegreeDistribution;
>>>>>>> sorting the PR
import gtna.metrics.centrality.BetweennessCentrality;
=======
import gtna.metrics.sampling.SamplingBias;
>>>>>>> Sampling Bias without CDF as its useless for this metric
=======
>>>>>>> removed finalize method and functionality to insert the neighbor set into the sample directly as it would result in wrong subgraphs
import gtna.networks.Network;
<<<<<<< HEAD
import gtna.networks.model.GeneralizedCondonAndKarp;
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> testing different configurations for the generalized community model
=======
import gtna.networks.model.Regular;
>>>>>>> small fixes to allow tests of the new sampling model
=======
=======
import gtna.networks.model.WattsStrogatz;
import gtna.networks.model.ZhouMondragon;
>>>>>>> optimizing the PR metric
import gtna.plot.Plotting;
>>>>>>> plotting for example plots
=======
import gtna.networks.model.Regular;
import gtna.networks.model.WattsStrogatz;
import gtna.networks.model.ZhouMondragon;
>>>>>>> removed finalize method and functionality to insert the neighbor set into the sample directly as it would result in wrong subgraphs
=======
import gtna.networks.model.ErdosRenyi;
=======
import gtna.metrics.Metric;
import gtna.metrics.centrality.BetweennessCentrality;
import gtna.networks.Network;
import gtna.networks.model.Regular;
>>>>>>> plotting
import gtna.plot.Plotting;
>>>>>>> added relative eccentricity / effective diameter
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.transformation.sampling.subgraph.ExtractSampledSubgraph;
import gtna.util.Config;
import gtna.util.Stats;

import java.io.File;
import java.util.Arrays;
=======
=======
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
>>>>>>> - refactoring
import gtna.metrics.Metric;
import gtna.metrics.basic.Assortativity;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.CondonAndKarp;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.Regular;
import gtna.networks.model.WattsStrogatz;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.id.ConsecutiveRingIDSpace;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.RandomRingIDSpace;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.id.node.NodeIds;
import gtna.util.Config;
>>>>>>> exploring gtna functionality

/**
 * @author Tim
 * 
 */

public class Exploring {
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Config.overwrite("GNUPLOT_PRINT_ERRORS", "true");
<<<<<<< HEAD
<<<<<<< HEAD
		
		
<<<<<<< HEAD
		boolean get = false; // get or generate
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		int times = 2;		// how many generations?
		boolean b = false; // bidirectional
		
		

<<<<<<< HEAD
		boolean r = true;
		
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		Network nw0 = new ZhouMondragon(1000, 0.25, null);
=======
		Network nw0 = new ZhouMondragon(10000, 0.25, 2, null);
>>>>>>> debugged rich-club generator.
		
=======
		Network nw0 = new BarabasiAlbert(10000, 5, null);
>>>>>>> debugged calculation of the assortativity coefficient
		
<<<<<<< HEAD
		Network[] n = new Network[] {nw0};
=======
		boolean r = false;
=======
		int times = 1;		// how many generations?
=======
		int times = 5;		// how many generations?
>>>>>>> optimizing the PR metric
=======
		int times = 2;		// how many generations?
>>>>>>> optimized BC
=======
		int times = 1;		// how many generations?
>>>>>>> removed finalize method and functionality to insert the neighbor set into the sample directly as it would result in wrong subgraphs
		boolean b = false; // bidirectional?
		boolean r = false; // ring?
>>>>>>> Sampling Modularity implemented
		
		SamplingAlgorithm a = SamplingAlgorithm.BFS;
=======
//		boolean get = false; // get or generate
		int times = 1;		// how many generations?
//		boolean b = false; // bidirectional?
//		boolean r = false; // ring?
//		
<<<<<<< HEAD
<<<<<<< HEAD
		SamplingAlgorithm a = SamplingAlgorithm.UNIFORMSAMPLING;
>>>>>>> fixes:
		double sc = 0.2;
		
		Transformation sa = SamplingAlgorithmFactory.getInstanceOf(a, sc, true, 1, null);
		Transformation sa2 = SamplingAlgorithmFactory.getInstanceOf(a, sc, true, 1, null);
		Transformation[] t = new Transformation[3];
		
		
		
		Arrays.fill(t, sa);
		t[0] = sa;
		t[1] = sa2;
		t[t.length-1] = new ExtractSampledSubgraph();
		
=======
//		SamplingAlgorithm a = SamplingAlgorithm.RANDOMWALK;
//		double sc = 0.2;
=======
		SamplingAlgorithm a = SamplingAlgorithm.RANDOMWALK;
		double sc = 0.2;
>>>>>>> plotting
//		
		Transformation sa = SamplingAlgorithmFactory.getInstanceOf(a, sc, true, 1, null);
//		Transformation sa2 = SamplingAlgorithmFactory.getInstanceOf(a, sc, true, 1, null);
		Transformation[] t = new Transformation[2];
//		
//		
//		
		Arrays.fill(t, sa);
		t[0] = sa;
//		t[1] = sa2;
		t[t.length-1] = new ExtractSampledSubgraph();
//		
>>>>>>> added relative eccentricity / effective diameter
		
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		Network nw0 = new ErdosRenyi(1000, 25, false, t);
		Network nw1 = new ErdosRenyi(500, 25, false, t);
		Network nw2 = new Regular(300, 4, r, b, null);
		Network nw3 = new Regular(400, 4, r, b, null);
		Network nw4 = new Regular(500, 4, r, b, null);
		Network nw5 = new Regular(600, 4, r, b, null);
		Network nw6 = new Regular(700, 4, r, b, null);
		Network nw7 = new Regular(800, 4, r, b, null);
		Network nw8 = new Regular(900, 4, r, b, null);
		Network nw9 = new Regular(1000, 4, r, b, null);
>>>>>>> debugging bc (2)
		
<<<<<<< HEAD
		Metric[] metrics = new Metric[] { 
<<<<<<< HEAD
				new Assortativity()
=======
		Network[] n = new Network[] {nw0 /*, nw1, nw2, nw3, nw4, nw5, nw6, nw7, nw8, nw9*/};
		
		Metric[] metrics = new Metric[] { 
				new BetweennessCentrality()
>>>>>>> bugfix bc (1)
=======
		Network nw0 = new ErdosRenyi(10000, 5, false, null);
=======
		Network nw0 = new ErdosRenyi(20, 5, false, null);
>>>>>>> calculate prVector
=======
		Network nw0 = new ErdosRenyi(200, 5, false, null);
>>>>>>> debugging plotting of page rank metric
		Network nw1 = new ErdosRenyi(20000, 5, false, null);
		Network nw2 = new ErdosRenyi(50000, 5, false, null);
		Network nw3 = new ErdosRenyi(100000, 5, false, null);
=======
		Network nw0 = new WattsStrogatz(4000, 5, 0.001, null);
		Network nw1 = new WattsStrogatz(4000, 5, 0.003, null);
		Network nw2 = new WattsStrogatz(4000, 5, 0.006, null);
		Network nw3 = new WattsStrogatz(4000, 5, 0.009, null);
>>>>>>> fully functional implementation of the PageRank metric. ToDo: normalization of PageRank (prVector)
=======
		Network nw0 = new Regular(400, 5, true, false, null);
		Network nw1 = new Regular(400, 5, false, false, null);
		Network nw2 = new Regular(4000, 15, true, false, null);
		Network nw3 = new Regular(4000, 20, true, false, null);
>>>>>>> normalization of the pr-vector
		
		Network[] n = new Network[] {nw0, nw1};
		
		Metric[] metrics = new Metric[] { 
<<<<<<< HEAD
				new Assortativity(0)
				, new ClusteringCoefficient()
>>>>>>> complete implementation: Assortativity coefficient (Newman, 2002, Assortative Mixing in Networks)
=======
			new PageRank()
>>>>>>> calculate prVector
=======
		Network[] n = new Network[] {nw0, nw1 /*, nw2, nw3, nw4, nw5, nw6, nw7, nw8, nw9*/};
=======
		Network nw0 = new BarabasiAlbert(10000, 50, t);
=======
		Network nw0 = new BarabasiAlbert(100, 2, t);
>>>>>>> Sampling Modularity implemented
		Network nw1 = new BarabasiAlbert(10000, 100, t);
		Network nw2 = new BarabasiAlbert(10000, 150, t);
		Network nw3 = new BarabasiAlbert(10000, 200, t);
		
		Network[] n = new Network[] {nw0 /*, nw1, nw2, nw3*/};
>>>>>>> small improvements using sampling bias
		
		Metric[] metrics = new Metric[] { 
<<<<<<< HEAD
				new SamplingBias()
>>>>>>> SamplingBias properties
=======
				new SamplingModularity()
>>>>>>> Sampling Modularity implemented
=======
//		Network nw1 = new Regular(30, 10, true, false, null);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		Network nw3 = new Regular(100, 10, true, false, null);
		Network nw1 = new WattsStrogatz(1000, 10, 0.1, null);
		Network nw2 = new WattsStrogatz(1000, 10, 0.01, null);
		
		Network[] ws1 = ZhouMondragon.get(50, new double[] {0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6}, 7, null);
		
		Network[] nw = new Network[2];
		Arrays.fill(nw, nw1);
		Network nw0 = new GeneralizedCondonAndKarp(nw, 0.00005, t);
		
		Network[] n = new Network[] {nw3};
//		Network[] n = ws1;
		
		DegreeDistribution m = new DegreeDistribution();
		m.computeData(nw3.generate(), nw3, null);
		
		
		Metric[] metrics = new Metric[] { 
<<<<<<< HEAD
<<<<<<< HEAD
				new DegreeDistribution()
>>>>>>> testing different configurations for the generalized community model
=======
				new DegreeDistribution(),
				new ClusteringCoefficient(),
				new ShortestPaths(),
				new BetweennessCentrality(),
				new Assortativity(),
				new SamplingBias(),
				new PageRank(),
				new SamplingModularity()
>>>>>>> plotting for example plots
=======
//				new DegreeDistribution(),
//				new ClusteringCoefficient(),
				new ShortestPaths()
=======
		Network nw3 = new Regular(1000, 10, true, false, null);
=======
		Network nw3 = new ErdosRenyi(1000, 6.0, true, null);
>>>>>>> added relative eccentricity / effective diameter
=======
		Network nw3 = new Regular(1000, 5, true, false, t);
>>>>>>> plotting
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
<<<<<<< HEAD
//		Metric[] metrics = new Metric[] { 
////				new DegreeDistribution(),
////				new ClusteringCoefficient(),
//				new ShortestPaths()
>>>>>>> fixes:
=======
		Metric[] metrics = new Metric[] { 
//				new DegreeDistribution(),
//				new ClusteringCoefficient(),
<<<<<<< HEAD
				new ShortestPaths()
>>>>>>> added relative eccentricity / effective diameter
=======
				new BetweennessCentrality()
>>>>>>> plotting
//				new BetweennessCentrality(),
//				new Assortativity(),
//				new SamplingBias()
//				new PageRank(),
//				new SamplingModularity(),
//				new DegreeDistributionComparator(m),
//				new SamplingRevisitFrequency()
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> optimizing the PR metric
				};
=======
				new DegreeDistribution() };
>>>>>>> debugged rich-club generator.
		
<<<<<<< HEAD
		
		Series[] s = get ? Series.get(n, metrics) : Series.generate(n, metrics, times);
=======
//				};
>>>>>>> fixes:
=======
				};
>>>>>>> added relative eccentricity / effective diameter
		
=======
//		Series[] s = get ? Series.get(n, metrics) : Series.generate(n, metrics, times);
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
		Series[] s = Series.generate(n, metrics, times);

>>>>>>> SamplingBias properties
		Plotting.single(s, metrics, "example-s/");
//
		Plotting.multi(s, metrics, "example-m/");
<<<<<<< HEAD
<<<<<<< HEAD
//		
		
=======
		 
<<<<<<< HEAD
<<<<<<< HEAD
		/*
>>>>>>> complete implementation: Assortativity coefficient (Newman, 2002, Assortative Mixing in Networks)
=======
=======
>>>>>>> removed finalize method and functionality to insert the neighbor set into the sample directly as it would result in wrong subgraphs
//		Series[] s = Series.generate(n, metrics, times);
//
//		Plotting.single(s, metrics, "example-s/");
//
//		Plotting.multi(s, metrics, "example-m/");
<<<<<<< HEAD
////		 
>>>>>>> testing different configurations for the generalized community model
		 for(Network i : n){
			 System.out.println("Plotting network - " + i.getKey() + " @ " + i.getNodes() + " nodes");
			 plot(i, "./plots/network-plot/n-"+i.getKey() + "-" + i.getNodes(), times, t);
		 }
<<<<<<< HEAD
		 */
		
=======
//		 for(Network i : n){
=======
//		 f#or(Network i : n){
>>>>>>> small improvements using sampling bias
=======
//		 
//		 for(Network i : n){
>>>>>>> Sampling Modularity implemented
//			 System.out.println("Plotting network - " + i.getKey() + " @ " + i.getNodes() + " nodes");
//			 plot(i, "./plots/network-plot/n-"+i.getKey() + "-" + i.getNodes(), times);
//		 }
>>>>>>> SamplingBias properties
=======
>>>>>>> testing different configurations for the generalized community model
=======
=======
>>>>>>> added relative eccentricity / effective diameter
		Series[] s = Series.generate(n, metrics, times);

		Plotting.single(s, metrics, "example-s/");

		Plotting.multi(s, metrics, "example-m/");
<<<<<<< HEAD
		
//		 for(Network i : n){
//			 System.out.println("Plotting network - " + i.getKey() + " @ " + i.getNodes() + " nodes");
//			 plot(i, "./plots/network-plot/n-"+i.getDescription(), times, new Transformation[] {});
//		 }
>>>>>>> plotting for example plots
=======
		
<<<<<<< HEAD
		 for(Network i : n){
			 System.out.println("Plotting network - " + i.getKey() + " @ " + i.getNodes() + " nodes");
			 plot(i, "./plots/network-plot/"+i.getDescriptionShort(), times, t);
		 }
>>>>>>> removed finalize method and functionality to insert the neighbor set into the sample directly as it would result in wrong subgraphs
=======
		Graph g = nw3.generate();
		g = t[0].transform(g);
		g = t[1].transform(g);
=======
>>>>>>> added relative eccentricity / effective diameter
		
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
>>>>>>> fixes:
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
=======
=======
		
		
		boolean get = false; // get or generate
		int times = 1;		// how many generations?
		boolean b = false; // bidirectional
		
<<<<<<< HEAD
<<<<<<< HEAD
//		Transformation t = new Bidirectional();
>>>>>>> - refactoring

//		Network nw0 = new Regular(10, 2, true, false, null);
//		Network nw1 = new ErdosRenyi(10, 3, false, null);
//		Network nw2 = new BarabasiAlbert(10, 2, null);
//		Network nw3 = new WattsStrogatz(10, 2, 0.2, null);
//		Network nw4 = new CondonAndKarp(10, 2, 0.4, 0.05, null);
//		Network nw5 = new Regular(100, 5, true, false, null);
//		Network nw6 = new ErdosRenyi(100, 10, false, null);
//		Network nw7 = new BarabasiAlbert(100, 10, null);
//		Network nw8 = new WattsStrogatz(100, 6, 0.2, null);
//		Network nw9 = new CondonAndKarp(100, 4, 0.4, 0.05, null);
=======
>>>>>>> changed probabilities for adding a new edge. probability gets very (far to) small in later iterations as the destination degree is nearly stable whereas the network degree is growing fast!
		
=======
//		Transformation t = new Bidirectional();
>>>>>>> debugged calculation of the assortativity coefficient

//		Network nw0 = new Regular(10, 2, true, false, null);
//		Network nw1 = new ErdosRenyi(10, 3, false, null);
//		Network nw2 = new BarabasiAlbert(10, 2, null);
//		Network nw3 = new WattsStrogatz(10, 2, 0.2, null);
//		Network nw4 = new CondonAndKarp(10, 2, 0.4, 0.05, null);
//		Network nw5 = new Regular(100, 5, true, false, null);
//		Network nw6 = new ErdosRenyi(100, 10, false, null);
//		Network nw7 = new BarabasiAlbert(100, 10, null);
//		Network nw8 = new WattsStrogatz(100, 6, 0.2, null);
//		Network nw9 = new CondonAndKarp(100, 4, 0.4, 0.05, null);
		

		boolean r = true;
		
		Network nw0 = new BarabasiAlbert(10000, 5, null);
		
		Network[] n = new Network[] {nw0};
		
		Metric[] metrics = new Metric[] { 
				new Assortativity()
				};
		
		
		Series[] s = get ? Series.get(n, metrics) : Series.generate(n, metrics, times);

<<<<<<< HEAD
		if (!Plotting.single(s, metrics, "example-s/")) {
//			System.err.println("Failed plotting single values");
		}
//		Plotting.multi(s, metrics, "example-m/", Type.confidence1,
//				Style.candlesticks);
		 if(Plotting.multi(s, metrics, "example-m/")){
//		 System.err.println("Failed plotting multi values");
		 }
<<<<<<< HEAD
>>>>>>> exploring gtna functionality
=======
=======
		Plotting.single(s, metrics, "example-s/");

		Plotting.multi(s, metrics, "example-m/");
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> Plotting-s/-m
		 
=======
		
		
>>>>>>> first attempts creating rd-networks
=======
		 
>>>>>>> debugged calculation of the assortativity coefficient
		 for(Network i : n){
			 System.out.println("Plotting network - " + i.getKey() + " @ " + i.getNodes() + " nodes");
			 plot(i, "./plots/network-plot/n-"+i.getKey() + "-" + i.getNodes(), times);
		 }
	}
	
	
	public static void plot(Network nw, String filename, int times) {
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
			
			g = t_nid.transform(g);
	
			IdentifierSpace ids = (IdentifierSpace) g
					.getProperty("ID_SPACE_0");
	
			new GtnaGraphWriter().writeWithProperties(g, graphFilename+".txt");
			
			gephi.plot(g, ids, graphFilename+".pdf");
			
			System.out.println(filename);
		}
>>>>>>> - refactoring
	}
}
