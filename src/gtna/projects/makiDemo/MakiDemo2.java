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
import gtna.drawing.Gephi;
import gtna.drawing.NodeColors;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.IdentifierSpace;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.communities.Communities;
import gtna.metrics.communities.Roles;
import gtna.networks.Network;
import gtna.networks.etc.GpsNetwork;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.ReadableFile;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.CommunityColors;
import gtna.transformation.communities.NodeColor;
import gtna.transformation.communities.RoleColors;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.util.Config;
import gtna.util.Execute;

import java.awt.Color;

/**
 * @author benni
 * 
 */
public class MakiDemo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/maki/demo2/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/maki/demo2/");

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("PLOT_EXTENSION", ".png");
		Config.overwrite("GNUPLOT_TERMINAL", "png");

		Config.overwrite("GEPHI_RING_RADIUS", "100");
		Config.overwrite("GEPHI_DRAW_CURVED_EDGES", "false");
		Config.overwrite("GEPHI_EDGE_SCALE", "0.5");
		Config.overwrite("GEPHI_NODE_BORDER_WIDTH", "0.1");
		Config.overwrite("GEPHI_NODE_SIZE", "8");

		int times = 1;
		boolean generate = false;

		Metric dd = new DegreeDistribution();
		Metric cc = new ClusteringCoefficient();
		Metric sp = new ShortestPaths();

		Metric c = new Communities();
		Metric r = new Roles(Role.RoleType.WSN);

		Metric[] metrics = new Metric[] { c };

		// Field size
		double width = 1000;
		double height = 1000;
		// Number of nodes
		int[] nodes = new int[] { 800, 900, 1000 };

		Transformation t1 = new CDLPA(50);
		Transformation t2 = new WsnRolesTransformation();
		Transformation t3 = new NodeColor(Color.gray);
		Transformation t4 = new CommunityColors();
		Transformation t5 = new RoleColors(Role.RoleType.WSN);

		Transformation[] t = new Transformation[] { t1, t2, t3, t4, t5 };

		// Network nw1 = placement(800, 16, 500, 500, 60, t);
		// Series s1 = Series.generate(nw1, metrics, times);
		// Plotting.multi(s1, metrics, "placement/");
		// plot(nw1, Config.get("MAIN_PLOT_FOLDER") + "placement");

		double[] radius = new double[] { 50, 60, 70 };
		String[] map = new String[] { "darmstadt-all", "manhattan", "google" };
		String[] name = new String[] { "Darmstadt", "Manhattan", "Google" };

		for (int i = 1; i < name.length; i++) {
			for (int j = 0; j < radius.length; j++) {
				Network nw = wifi("resources/_wifi/source/" + map[i] + ".txt",
						map[i] + "-" + radius[j], name[i], radius[j], t);
				plot(nw, Config.get("MAIN_PLOT_FOLDER") + "wifi-" + map[i],
						radius[j]);
			}
		}

	}

	private static Network placement(int nodes, int hotspots, double width,
			double height, double radius, Transformation[] transformations) {
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

		return new ReadableFile("Hotspot Placement", "hotspot-placement",
				filename, transformations);
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

		return new ReadableFile("WiFi @ " + name, folder, filename,
				transformations);
	}

	@SuppressWarnings("rawtypes")
	private static void plot(Network nw, String name, double radius) {
		Gephi ge = new Gephi();

		Graph g = nw.generate();
		IdentifierSpace idSpace = (IdentifierSpace) g.getProperty("ID_SPACE_0");

		GraphProperty[] colors = g.getProperties("NODE_COLORS");
		String[] cNames = new String[] { "none", "communities", "roles",
				"misc1", "misc2", "misc3" };
		for (int i = 0; i < colors.length; i++) {
			String filename1 = name + "-" + i + "-" + cNames[i] + "-" + radius
					+ ".pdf";
			String filename2 = name + "-" + i + "-" + cNames[i] + "-" + radius
					+ ".jpg";

			System.out.println(nw.getFolder());

			System.out.println("plotting to " + filename1);
			ge.plot(g, idSpace, (NodeColors) colors[i], filename1);

			System.out.println("converting to " + filename2);
			Execute.exec("/usr/local/bin/convert " + filename1 + " "
					+ filename2);
			Execute.exec("rm " + filename1);
			System.out.println();
		}

	}
}
