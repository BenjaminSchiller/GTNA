/*
 * ===========================================================
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
 * GTNA.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 */
package gtna;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.comexport.ComExporter;
import gtna.networks.Network;
import gtna.networks.canonical.Ring;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CirclePlacementModel;
import gtna.networks.model.placementmodels.models.GridPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.plot.Gephi;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CommunityDetectionDeltaQ;
import gtna.transformation.communities.CommunityDetectionLPA;
import gtna.transformation.communities.CommunityDetectionLPAExtended;
import gtna.util.Config;
import gtna.util.Stats;

public class GTNA {

	public static void main(String[] args) throws Exception {
		// starts a basic statistic to keep track of the computations duration
		// and used memory
		Stats stats = new Stats();
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("METRICS", "DEGREE_DISTRIBUTION");
		
//		Network n = new Ring(100, new Greedy(), new Transformation[]{ new CommunityDetectionLPAExtended() });
//		public PlacementModelContainer(int nodes, int hotspots, double width,
//				double height, PlacementModel hotspotPlacer,
//				PlacementModel nodePlacer, Partitioner partitioner,
//				NodeConnector nodeConnector, RoutingAlgorithm r, Transformation[] t) {
		Network n = new PlacementModelContainer(625, 25, 1250, 1250, new GridPlacementModel(1000, 1000, 5, 5, false), new GridPlacementModel(50, 50, 5, 5, false), new SimplePartitioner(), new UDGConnector(10), new Greedy(), new Transformation[]{new CommunityDetectionDeltaQ()} );
		Graph g = n.generate();
		Transformation t = new CommunityDetectionDeltaQ();
		g = t.transform(g);
		Series.generate(n, 1);
		Gephi g2 = new Gephi();
		System.out.println(g.getProperty("COMMUNITIES_0"));
		g2.Plot(g, (IdentifierSpace) g.getProperty("ID_SPACE_0"), "test.pdf");

		
		// overwrites the standard data output folder
//		Config.overwrite("MAIN_DATA_FOLDER", "./data/example1/");
		// overwrites the standard plot output folder
//		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/example1/");
		// overwrites the set and order of metrics to compute
		// here, only the clustering coefficient and degree didstribution
		// metrics are computed


		// // CAN network with 100 nodes, d=3, and r=2
		// // neither a transformation nor a routing algorithm are given
		// Network can = new CAN(100, 3, 2, null, null);
		//
		// // CAN networks with different numbers of nodes
		// Network[] cans = CAN.get(new int[] { 100, 200, 300 }, 2, 4, null,
		// null);
		//
		// // generates the graph of a possible instance
		// // the properties of this graph are determined by the configuration
		// Graph g = can.generate();
		//
		// // generates a series for the specified network
		// // the series represents the average of 12 independent runs
		// // i.e. can.generate() is called 12 times to generate different
		// // instances
		// // then, all metric specified in METRICS are computed for these
		// graphs
		// Series s = Series.generate(can, 12);
		//
		// // generates an array of series for the specified networks
		// Series[] s2 = Series.generate(cans, 5);
		//
		// // plots the average values for all multi-scalar metrics computed for
		// // the single CAN network
		// Plot.multiAvg(s, "can-avg/");
		// // plots the confidence intervals for all multi-scalar metrics
		// computed
		// // for the single CAN network
		// Plot.multiConf(s, "can-conf/");
		// // plots averages, confidence intervals and generates a txt file to
		// // easily display all plots in a text document
		// Plot.allMulti(s, "can/");
		//
		// // plots all multi-scalar metrics for the list of CAN networks
		// Plot.allMulti(s2, "cans-multi/");
		// // plots all single-scalar metrics for the list of CAN networks
		// // the network size is recognized as the difference in their
		// // configuration and used as the value on the x-axis
		// Plot.allSingle(s2, "can-singles/");

		// outputs the collected statistics
		stats.end();
	}
}
