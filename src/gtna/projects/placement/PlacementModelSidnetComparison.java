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
 * PlacementModelSidnetComparisson.java
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
package gtna.projects.placement;

import gtna.communities.Role;
import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.communities.Communities;
import gtna.metrics.communities.Roles;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Data.Type;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CommunityDetectionLPA;
import gtna.transformation.communities.GuimeraRolesTransformation;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.transformation.util.RemoveGraphProperty;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class PlacementModelSidnetComparison {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Stats stats = new Stats();

		Config.overwrite("MAIN_DATA_FOLDER",
				"./data/placement-sidnet-comparison/");
		Config.overwrite("MAIN_PLOT_FOLDER",
				"./plots/placement-sidnet-comparison/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "false");

		int[] N = new int[] { 1000, 2000, 3000 };
		int times = 100;
		boolean get = false;
		Config.overwrite("GNUPLOT_OFFSET_X", "0.2");
		Config.overwrite("GNUPLOT_LW", "1");

		for (int nodes : N) {
			Transformation[] t1 = new Transformation[] {
					new CommunityDetectionLPA(20),
					new GuimeraRolesTransformation(),
					new WsnRolesTransformation() };
			Transformation[] t2 = new Transformation[] {
					new GuimeraRolesTransformation(),
					new WsnRolesTransformation() };
			Transformation[] t3 = new Transformation[] {
					new RemoveGraphProperty(
							RemoveGraphProperty.RemoveType.SELECTED,
							new String[] { "COMMUNITIES_0" }),
					new CommunityDetectionLPA(20),
					new GuimeraRolesTransformation(),
					new WsnRolesTransformation() };

			Network nw1 = new DescriptionWrapper(
					PlacementModelSidnetComparison.nwCC(nodes, t1), "PM-"
							+ nodes);
			Network nw2 = new ReadableFolder("SIDnet-" + nodes
					+ " (SIDnet LPA)", "sidnet", "./resources/sidnet/" + nodes
					+ "/", ".txt", t2);
			Network nw3 = new ReadableFolder("SIDnet-" + nodes + " (GTNA LPA)",
					"sidnet", "./resources/sidnet/" + nodes + "/", ".txt", t3);

			Network[] nw = new Network[] { nw1, nw2, nw3 };

			Metric dd = new DegreeDistribution();
			Metric sp = new ShortestPaths();
			Metric c = new Communities();
			Metric r1 = new Roles(Role.RoleType.GUIMERA);
			Metric r2 = new Roles(Role.RoleType.WSN);
			Metric[] metrics = new Metric[] { dd, sp, c, r1, r2 };

			Series[] s = get ? Series.get(nw, metrics) : Series.generate(nw,
					metrics, times);
			Plotting.multi(s, metrics, "multi-" + nodes + "/",
					Type.confidence1, Style.candlesticks);
		}

		stats.end();
	}

	private static final double xyNodes = 40000;
	private static final double xyHotspots = 30000;
	private static final double devHotspots = 5000;
	private static final double devNodes = 2000;
	private static final double radius = 1983;

	private static final Partitioner partitioner = new SimplePartitioner();

	private static final NodeConnector connector = new UDGConnector(radius);

	public static Network nwCC(int nodes, Transformation[] t) {
		PlacementModel p1 = new CommunityPlacementModel(devHotspots,
				devHotspots, false);
		PlacementModel p2 = new CommunityPlacementModel(devNodes, devNodes,
				false);
		Network nw = new PlacementModelContainer(nodes, nodes / 100, xyNodes,
				xyNodes, xyHotspots, xyHotspots, p1, p2, partitioner,
				connector, t);
		return nw;
	}

}
