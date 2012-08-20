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
 * MakiDemo3.java
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
package gtna.projects.makiDemo;

import gtna.communities.CommunityList;
import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDDeltaQ;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.CommunityColors;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class Immi2 {

	public static void main(String[] args) {
		Stats stats = new Stats();

		boolean deltaQ = true;
		boolean lpa = true;
		boolean random = false;
		boolean community = false;
		boolean communityNew = true;
		int[] Nodes = new int[] { 2000 };
		int times = 1;
		for (int nodes : Nodes) {
			if (random) {
				Network nw = random(nodes);
				plot(nw, "./plots/LPA/random-" + nodes, times, false, lpa);
				plot(nw, "./plots/DELTA_Q/random-" + nodes, times, deltaQ,
						false);
			}
			if (community) {
				Network nw = community(nodes);
				plot(nw, "./plots/LPA/community-" + nodes, times, false, lpa);
				plot(nw, "./plots/DELTA_Q/community-" + nodes, times, deltaQ,
						false);
			}
			if (communityNew) {
				Network nw = communityNew(nodes);
				plot(nw, "./plots/LPA/communityNew-" + nodes, times, false, lpa);
				plot(nw, "./plots/DELTA_Q/communityNew-" + nodes, times,
						deltaQ, false);
			}
		}

		stats.end();
	}

	private static double width = 40000;

	private static double height = 40000;

	private static Partitioner partitioner = new SimplePartitioner();

	private static NodeConnector connector = new UDGConnector(1983);

	private static Network random(int nodes) {
		PlacementModel p1 = new RandomPlacementModel(width, height, false);
		PlacementModel p2 = new RandomPlacementModel(1, 1, true);
		return new PlacementModelContainer(nodes, nodes, width, height, 30000,
				30000, p1, p2, partitioner, connector, null);
	}

	private static Network community(int nodes) {
		PlacementModel p1 = new CommunityPlacementModel(0.5 * width,
				0.5 * height, false);
		PlacementModel p2 = new CommunityPlacementModel(0.2 * width,
				0.2 * height, false);
		return new PlacementModelContainer(nodes, nodes / 100, width, height,
				30000, 30000, p1, p2, partitioner, connector, null);
	}

	private static Network communityNew(int nodes) {
		PlacementModel p1 = new CommunityPlacementModel(0.5 * width,
				0.5 * height, false);
		PlacementModel p2 = new CommunityPlacementModel(0.2 * width,
				0.2 * height, false);
		return new PlacementModelContainer(nodes, nodes / 100, 2 * width,
				2 * height, 60000, 60000, p1, p2, partitioner, connector, null);
	}

	public static void plot(Network nw, String filename, int times,
			boolean deltaQ, boolean lpa) {
		if (!deltaQ && !lpa) {
			return;
		}
		Transformation t_dq = new CDDeltaQ();
		Transformation t_lpa = new CDLPA(50);
		Transformation t_cc = new CommunityColors();
		for (int i = 0; i < times; i++) {
			Gephi gephi = new Gephi();
			Config.overwrite("GEPHI_RING_RADIUS", "1");
			Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "1");
			Config.overwrite("GEPHI_EDGE_SCALE", "0.01");
			Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
			Config.overwrite("GEPHI_NODE_SIZE", "1.5");

			String graphFilename = "/tmp/graph.txt";
			String plotFilenameDQ = filename + "_DQ_" + i + ".pdf";
			String plotFilenameLPA = filename + "_LPA_" + i + ".pdf";

			Graph g = nw.generate();
			new GtnaGraphWriter().writeWithProperties(g, graphFilename);

			System.out.println(filename);
			// if (deltaQ) {
			// g = new GtnaGraphReader().readWithProperties(graphFilename);
			// g = t_dq.transform(g);
			// g = t_cc.transform(g);
			// CommunityList c = (CommunityList) g
			// .getProperty("COMMUNITIES_0");
			// IdentifierSpace ids = (IdentifierSpace) g
			// .getProperty("ID_SPACE_0");
			// System.out.println("deltaQ: " + c.getCommunities().length);
			// gephi.plot(g, ids, plotFilenameDQ);
			// }
			if (lpa) {
				// g = new GtnaGraphReader().readWithProperties(graphFilename);
				g = t_lpa.transform(g);
				g = t_cc.transform(g);
				CommunityList c = (CommunityList) g
						.getProperty("COMMUNITIES_0");
				IdentifierSpace ids = (IdentifierSpace) g
						.getProperty("ID_SPACE_0");
				System.out.println("LPA: " + c.getCommunities().length);
				gephi.plot(g, ids, plotFilenameLPA);
				Immi1
						.exec("C:/Program Files (x86)/Boxoft PDF to JPG (freeware)/pdftojpg "
								+ plotFilenameLPA
								+ " "
								+ "C:/Users/ISCased/workspace/github/GTNA/plots/LPA");
			}
		}
	}

}
