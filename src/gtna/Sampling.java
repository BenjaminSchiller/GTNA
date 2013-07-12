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
 * Sampling.java
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
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.CondonAndKarp;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.WattsStrogatz;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.id.RandomPlaneIDSpaceSimple;
import gtna.transformation.id.node.NodeIds;
import gtna.transformation.sampling.SamplingAlgorithmFactory;
import gtna.transformation.sampling.SamplingAlgorithmFactory.SamplingAlgorithm;
import gtna.util.Config;

/**
 * @author Tim
 *
 */
public class Sampling {

    /**
     * @param args
     */
    public static void main(String[] args) {
	Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

	boolean get = false; // get or generate
	int times = 1;		// how many generations?
	
	// Sampling parameter
	double scaledown = 0.1;
	int dimension = 1;
	boolean revisiting = false;

	Transformation uniformSampling = 
		SamplingAlgorithmFactory.getInstanceOf(SamplingAlgorithm.UNIFORMSAMPLING,
			scaledown, revisiting, dimension);

	Transformation[] t = new Transformation[] {uniformSampling};
	Network nw1 = new ErdosRenyi(10000, 12, false, t); 
//	Network nw2 = new BarabasiAlbert(10, 2, null);
//	Network nw3 = new WattsStrogatz(10, 2, 0.2, null);
//	Network nw4 = new CondonAndKarp(10, 2, 0.4, 0.05, null);
//	Network nw5 = new ErdosRenyi(100, 10, false, null);
//	Network nw6 = new BarabasiAlbert(100, 10, null);
//	Network nw7 = new WattsStrogatz(100, 6, 0.2, null);
//	Network nw8 = new CondonAndKarp(100, 4, 0.4, 0.05, null);
	
	Network[] n = new Network[] {nw1 /*, nw2, nw3, nw4, nw5, nw6, nw7, nw8*/};
	
	Metric[] metrics = new Metric[] { 
			new DegreeDistribution(),
			new ShortestPaths(), 
			new ClusteringCoefficient()
			};
	
	
	Series[] s = get ? Series.get(n, metrics) : Series.generate(n, metrics, times);

	
	Plotting.single(s, metrics, "example-s/");
	Plotting.multi(s, metrics, "example-m/");
	 for(Network i : n){
		 System.out.println("Plotting network - " + i.getKey() + " @ " + i.getNodes() + " nodes");
		 plot(i, "./plots/network-plot/n-"+i.getKey() + "-" + i.getNodes(), times);
	 }
}


@SuppressWarnings("javadoc")
public static void plot(Network nw, String filename, int times) {
	Transformation t_rpid = new RandomPlaneIDSpaceSimple(1, 100, 100, true);
	Transformation t_nid = new NodeIds();
	
	
	for (int i = 0; i < times; i++) {
		Gephi gephi = new Gephi();
		Config.overwrite("GEPHI_RING_RADIUS", "1");
		Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.01");
		Config.overwrite("GEPHI_EDGE_SCALE", "0.001");
		Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "true");
		Config.overwrite("GEPHI_NODE_SIZE", "0.1");

		String graphFilename = filename;
		
		Graph g = nw.generate();
		
		g = t_rpid.transform(g);

		IdentifierSpace ids = (IdentifierSpace) g
				.getProperty("ID_SPACE_0");

		new GtnaGraphWriter().writeWithProperties(g, graphFilename+".txt");
		
		gephi.plot(g, ids, graphFilename+".pdf");
		
		System.out.println(filename);
	}
}

}
