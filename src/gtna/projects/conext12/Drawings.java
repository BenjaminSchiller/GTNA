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
 * Drawings.java
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
package gtna.projects.conext12;

import gtna.drawing.Gephi;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.CommunityColors;
import gtna.util.Config;
import gtna.util.Execute;

import java.io.File;

/**
 * @author benni
 * 
 */
public class Drawings {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/placement-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/placement-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("SERIES_GRAPH_WRITE", "false");

		Config.overwrite("GEPHI_RING_RADIUS", "500");
		Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
		Config.overwrite("GEPHI_EDGE_SCALE", "0.1");
		Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "10");
		Config.overwrite("GEPHI_NODE_SIZE", "100");

		int times = 5;

		plotHotspot(2000, 20, 10000, 1000, times);
		plotHotspot(2000, 20, 10000, 2000, times);
		plotHotspot(2000, 20, 10000, 3000, times);
		plotHotspot(2000, 20, 10000, 4000, times);

		plotHotspot(2000, 20, 5000, 1000, times);
		plotHotspot(2000, 20, 5000, 2000, times);
		plotHotspot(2000, 20, 5000, 3000, times);
		plotHotspot(2000, 20, 5000, 4000, times);

		plotRandom(2000, times);
	}

	private static void plotRandom(int nodes, int times) {
		Network nw = Drawings.nwRandom(nodes);
		String name = "random-" + nodes;
		plot(nw, name, times);
	}

	private static void plotHotspot(int nodes, int hotspots,
			double devHotspots, double devNodes, int times) {
		Network nw = Drawings.nwCC(nodes, hotspots, devHotspots, devNodes);
		String name = "hotspot-" + nodes + "_" + hotspots + "_" + devHotspots
				+ "_" + devNodes;
		plot(nw, name, times);
	}

	private static void plot(Network nw, String name, int times) {
		for (int i = 0; i < times; i++) {
			String filename = "plots/drawings/" + name + "-" + i;
			String filename1 = filename + ".pdf";
			String filename2 = filename + ".jpg";

			if ((new File(filename2)).exists()) {
				continue;
			}

			Graph g = nw.generate();

			Transformation t1 = new CDLPA(50);
			Transformation t2 = new CommunityColors();

			g = t1.transform(g);
			g = t2.transform(g);

			Gephi gephi = new Gephi();
			System.out.println(filename1);
			gephi.plot(g, (IdentifierSpace) g.getProperty("ID_SPACE_0"),
					filename1);
			Execute.exec("/usr/local/bin/convert " + filename1 + " "
					+ filename2);
			Execute.exec("rm " + filename1);
			System.out.println("=> " + filename2);
		}
	}

	private static final double xyNodes = 40000;
	private static final double xyHotspots = 30000;
	// private static final double devHotspots = 5000;
	// private static final double devNodes = 2000;
	private static final double radius = 1983;

	private static final Partitioner partitioner = new SimplePartitioner();

	private static final NodeConnector connector = new UDGConnector(radius);

	private static Network nwCC(int nodes, int hotspots, double devHotspots,
			double devNodes) {
		PlacementModel p1 = new CommunityPlacementModel(devHotspots,
				devHotspots, false);
		PlacementModel p2 = new CommunityPlacementModel(devNodes, devNodes,
				false);
		Network nw = new PlacementModelContainer(nodes, hotspots, xyNodes,
				xyNodes, xyHotspots, xyHotspots, p1, p2, partitioner,
				connector, null);
		return new DescriptionWrapper(nw, "Communities " + nodes);
	}

	private static Network nwRandom(int nodes) {
		PlacementModel p1 = new RandomPlacementModel(xyNodes, xyNodes, false);
		PlacementModel p2 = new RandomPlacementModel(xyNodes, xyNodes, false);
		return new PlacementModelContainer(nodes, 1, xyNodes, xyNodes, xyNodes,
				xyNodes, p1, p2, partitioner, connector, null);
	}

}
