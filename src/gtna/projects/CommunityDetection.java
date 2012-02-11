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
 * CommunityDetection.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects;

import gtna.communities.Communities;
import gtna.data.Series;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.GraphReader;
import gtna.io.GraphWriter;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CirclePlacementModel;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Gephi;
import gtna.plot.Plot;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.greedy.Greedy;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CommunityDetectionDeltaQ;
import gtna.transformation.communities.CommunityDetectionLPA;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class CommunityDetection {
	public static void main(String[] args) {
		Stats stats = new Stats();
		// CommunityDetection.plot(3);
		// CommunityDetection.metrics(10);
		stats.end();
	}

	private static void metrics(int times) {
		int hs = 1;
		PlacementModel p1 = new CirclePlacementModel(1500, 1500, 300,
				CirclePlacementModel.DistributionType.FIXED,
				CirclePlacementModel.DistributionType.FIXED);
		PlacementModel p2 = new CirclePlacementModel(200, 200, 80,
				CirclePlacementModel.DistributionType.FIXED,
				CirclePlacementModel.DistributionType.FIXED);
		p2 = new RandomPlacementModel(250, 250, true);
		p2 = new CommunityPlacementModel(250, 250, 0.4, false);
		p1 = new RandomPlacementModel(1000, 1000, false);
		p2 = new RandomPlacementModel(1000, 1000, false);
		Partitioner p = new SimplePartitioner();
		NodeConnector c = new UDGConnector(100);
		RoutingAlgorithm r = new Greedy();

		hs = 1;
		p1 = new RandomPlacementModel(40000, 40000, false);
		p2 = new RandomPlacementModel(40000, 40000, false);
		c = new UDGConnector(1983);

		Transformation t1 = new CommunityDetectionLPA();
		Transformation t2 = new CommunityDetectionDeltaQ();
		Transformation[] t_lpa = new Transformation[] { t1 };
		Transformation[] t_dq = new Transformation[] { t2 };
		Network[][] nw = new Network[2][10];
		for (int i = 0; i < nw[0].length; i++) {
			int n = (i + 1) * 1000;
			nw[0][i] = new DescriptionWrapper(new PlacementModelContainer(n,
					hs, p1, p2, p, c, r, t_lpa), "Random LPA");
			nw[1][i] = new DescriptionWrapper(new PlacementModelContainer(n,
					hs, p1, p2, p, c, r, t_dq), "Random DeltaQ");
		}

		Config.overwrite("METRICS", "COMMUNITIES");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/CD2/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/CD2/");

		Series[][] s = Series.get(nw);
		// Plot.multiConf(s, "multi/");
		Plot.singlesConf(s, "singles/");
	}

	// private static void plot(int times) {
	// int n = 500;
	// int hs = 10;
	// PlacementModel p1 = new RandomPlacementModel(40000, 40000, false);
	// PlacementModel p2 = new RandomPlacementModel(40000, 40000, false);
	//
	// // p1 = new CommunityPlacementModel(40000, 40000, 0.8, false);
	// // p2 = new CommunityPlacementModel(40000, 40000, 0.1, false);
	// // p1 = new RandomPlacementModel(10000, 10000, true);
	// // p2 = new RandomPlacementModel(1, 1, true);
	// p1 = new CirclePlacementModel(10000, 10000, 4000,
	// CirclePlacementModel.DistributionType.FIXED,
	// CirclePlacementModel.DistributionType.FIXED);
	// p2 = new CirclePlacementModel(2000, 2000, 800,
	// CirclePlacementModel.DistributionType.FIXED,
	// CirclePlacementModel.DistributionType.FIXED);
	// Partitioner p = new SimplePartitioner();
	// NodeConnector c = new UDGConnector(1500);
	// RoutingAlgorithm r = null;
	// Network nw = new PlacementModelContainer(n, hs, p1, p2, p, c, null,
	// null);
	// Transformation t1_dq = new CommunityDetectionDeltaQ();
	// Transformation t1_lpa = new CommunityDetectionLPA();
	// Transformation t2 = new CommunityColors();
	// Transformation[] tt1 = new Transformation[] { t1_dq, t2 };
	// Transformation[] tt2 = new Transformation[] { t1_lpa, t2 };
	// for (int i = 0; i < times; i++) {
	// Gephi gephi = new Gephi();
	// Config.overwrite("GEPHI_RING_RADIUS", "100");
	// Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "1");
	// Config.overwrite("GEPHI_EDGE_SCALE", "0.1");
	// Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
	// Config.overwrite("GEPHI_NODE_SIZE", "50");
	//
	// Graph g1 = nw.generate();
	// GraphWriter.writeWithProperties(g1, "./plots/CD/graphs/test.txt");
	// for (Transformation tt : tt1) {
	// g1 = tt.transform(g1);
	// }
	// Graph g2 = GraphReader
	// .readWithProperties("./plots/CD/graphs/test.txt");
	// for (Transformation tt : tt2) {
	// g2 = tt.transform(g2);
	// }
	//
	// Communities C1 = (Communities) g1.getProperty("COMMUNITIES_0");
	// Communities C2 = (Communities) g2.getProperty("COMMUNITIES_0");
	// System.out.println(C1.getCommunities().length + " / "
	// + C2.getCommunities().length);
	// gephi.plot(g1, (IdentifierSpace) g1.getProperty("ID_SPACE_0"),
	// "./plots/CD/graphs/test-" + i + "-dq.pdf");
	// gephi.plot(g2, (IdentifierSpace) g2.getProperty("ID_SPACE_0"),
	// "./plots/CD/graphs/test-" + i + "-lpa.pdf");
	// GraphWriter.writeWithProperties(g1, "./data/CD/graphs/test.txt");
	// }
	// }
}
