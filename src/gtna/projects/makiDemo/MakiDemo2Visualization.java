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
 * MakiDemo2.java
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

import gtna.communities.Role;
import gtna.communities.Role.RoleType;
import gtna.data.Series;
import gtna.drawing.Gephi;
import gtna.drawing.NodeColors;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.communities.Communities;
import gtna.metrics.communities.Roles;
import gtna.networks.Network;
import gtna.networks.etc.GpsNetwork;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.CommunityColors;
import gtna.transformation.communities.NodeColor;
import gtna.transformation.communities.RoleColors;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.util.Config;
import gtna.util.Execute;
import gtna.util.Stats;

import java.awt.Color;

/**
 * @author benni 17 sec
 */
public class MakiDemo2Visualization extends MakiDemo {

	protected static int times = 1;

	// 3 sec
	protected static boolean generate = true;

	// 4 sec
	protected static boolean plot = true;

	// 27 / 9 sec
	protected static boolean draw = true;

	protected static boolean open = true;

	/**
	 * In this demo, GPS traces of WiFi access Points (Darmstadt, Manhattan, and
	 * Google) are read from source files and transformed into graphs with added
	 * location information. Also, a graph based on our Hotspot Placement model
	 * is generated. Using transformations, information about the community
	 * structure of the network is added. Based on this information, roles are
	 * assigned to nodes. Metrics for the degree, community, and role
	 * distribution are computed and plotted. Finally, visualizations of the
	 * networks are generated using the graph drawing capabilities.
	 */
	public static void main(String[] args) throws InterruptedException {
		Stats stats = new Stats();

		/**
		 * Config
		 */
		config(2, false, "pdf", 3);
		config();

		/**
		 * Metrics
		 */
		Metric degreeDistribution = new DegreeDistribution();
		Metric communities = new Communities();
		Metric roles = new Roles(RoleType.WSN);

		Metric[] metrics = new Metric[] { degreeDistribution, communities,
				roles };

		/**
		 * Transformations
		 */
		Transformation t1 = new CDLPA(50);
		Transformation t2 = new WsnRolesTransformation();
		Transformation t3 = new NodeColor(Color.gray);
		Transformation t4 = new CommunityColors();
		Transformation t5 = new RoleColors(Role.RoleType.WSN);

		Transformation[] t = new Transformation[] { t1, t2, t3, t4, t5 };

		/**
		 * Creating network instances
		 */
		Network hotspotPlacement = hotspotPlacement(800, 16, 500, 500, 60, t);
		Network darmstadt = wifi("resources/darmstadt_all.txt", "darmstadt",
				"Darmstadt", 50, t);
		Network manhattan = wifi("resources/manhattan.txt", "manhattan",
				"Manhattan", 50, t);
		Network google = wifi("resources/google.txt", "google", "Google", 50, t);

		Network[] nw = new Network[] { hotspotPlacement, darmstadt, manhattan,
				google };

		/**
		 * Performing computations / simulations
		 */
		if (generate) {
			Series.generate(nw, metrics, times);
		}

		/**
		 * Getting the results
		 */
		Series[] s = Series.get(nw, metrics);

		/**
		 * Plotting the results
		 */
		if (plot) {
			Plotting.multi(s, metrics, "multi/");
		}

		/**
		 * Graph drawing
		 */
		for (Network network : nw) {
			if (draw) {
				draw(network, network.getName(), 50);
			}
		}

		/**
		 * Opening files
		 */
		if (open) {
			open(new String[] { Config.get("MAIN_DATA_FOLDER"),
					Config.get("MAIN_PLOT_FOLDER") });
			Thread.sleep(1000);

			openPlots(new String[] { "multi/ROLES-WSN/max",
					"multi/COMMUNITIES/adjacent-communities",
					"multi/COMMUNITIES/community-size-fraction",
					"multi/DEGREE_DISTRIBUTION/dd-inDegreeDistribution-cdf" });

			String p = Config.get("MAIN_PLOT_FOLDER");
			open(new String[] { p + "HotspotPlacement-2-roles-50.0.jpg",
					p + "HotspotPlacement-1-communities-50.0.jpg",
					p + "HotspotPlacement-0-none-50.0.jpg",
					p + "Darmstadt-2-roles-50.0.jpg",
					p + "Darmstadt-1-communities-50.0.jpg",
					p + "Darmstadt-0-none-50.0.jpg",
					p + "Google-2-roles-50.0.jpg",
					p + "Google-1-communities-50.0.jpg",
					p + "Google-0-none-50.0.jpg",
					p + "Manhattan-2-roles-50.0.jpg",
					p + "Manhattan-1-communities-50.0.jpg",
					p + "Manhattan-0-none-50.0.jpg" });
		}

		stats.end();
	}

	private static Network hotspotPlacement(int nodes, int hotspots,
			double width, double height, double radius,
			Transformation[] transformations) {
		PlacementModel p1 = new CommunityPlacementModel(0.5 * width,
				0.5 * height, false);
		PlacementModel p2 = new CommunityPlacementModel(0.2 * width,
				0.2 * height, false);
		Network nw = new PlacementModelContainer(nodes, hotspots, 2 * width,
				2 * height, width * 4, height * 4, p1, p2,
				new SimplePartitioner(), new UDGConnector(radius),
				transformations);
		Graph g = nw.generate();
		if (transformations != null) {
			for (Transformation t : transformations) {
				g = t.transform(g);
			}
		}
		String filename = Config.get("MAIN_DATA_FOLDER") + "placement.gtna";
		(new GtnaGraphWriter()).writeWithProperties(g, filename);

		return new DescriptionWrapper(new ReadableFile("HotspotPlacement",
				"hotspot-placement", filename, transformations),
				"Hotspot Placement");
	}

	private static Network wifi(String src, String folder, String name,
			double radius, Transformation[] transformations) {
		Network nw = new GpsNetwork(src, name, radius, null);

		Graph g = nw.generate();
		if (transformations != null) {
			for (Transformation t : transformations) {
				g = t.transform(g);
			}
		}

		String filename = Config.get("MAIN_DATA_FOLDER") + folder + ".gtna";
		(new GtnaGraphWriter()).writeWithProperties(g, filename);

		return new DescriptionWrapper(new ReadableFile(name, folder, filename,
				transformations), name);
	}

	@SuppressWarnings("rawtypes")
	private static void draw(Network nw, String name, double radius) {
		Gephi ge = new Gephi();

		Graph g = nw.generate();
		IdentifierSpace idSpace = (IdentifierSpace) g.getProperty("ID_SPACE_0");

		GraphProperty[] colors = g.getProperties("NODE_COLORS");
		String[] cNames = new String[] { "none", "communities", "roles",
				"misc1", "misc2", "misc3" };
		for (int i = 0; i < colors.length; i++) {
			String filename1 = Config.get("MAIN_PLOT_FOLDER") + name + "-" + i
					+ "-" + cNames[i] + "-" + radius + ".pdf";
			String filename2 = Config.get("MAIN_PLOT_FOLDER") + name + "-" + i
					+ "-" + cNames[i] + "-" + radius + ".jpg";

			System.out.println("\nDrawing '" + nw.getDescription() + "' to "
					+ filename2);

			ge.plot(g, idSpace, (NodeColors) colors[i], filename1);

			Execute.exec("/opt/local/bin/convert " + filename1 + " "
					+ filename2);
			Execute.exec("rm " + filename1);
			// if (i >= 0) {
			// break;
			// }
		}
	}

	protected static void config() {
		Config.overwrite("GEPHI_RING_RADIUS", "100");
		Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
		Config.overwrite("GEPHI_EDGE_SCALE", "0.5");
		Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.1");
		Config.overwrite("GEPHI_NODE_SIZE", "6");
	}
}
