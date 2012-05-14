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
 * Test.java
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
package gtna.projects.communityEmbeddings;

import gtna.communities.Role;
import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
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
import gtna.networks.util.ReadableList;
import gtna.plot.Data.Type;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.transformation.connectors.UnitDiscGraph;
import gtna.transformation.partition.LargestConnectedComponent;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.partition.StrongConnectivityPartition;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Test {
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/wsn-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/wsn-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		Transformation scp = new StrongConnectivityPartition();
		Transformation gcc = new LargestWeaklyConnectedComponent();

		Transformation[] t1 = new Transformation[] {
				new WsnRolesTransformation(1, 1, 2, true),
				new UnitDiscGraph(1983) };
		Transformation[] t2 = new Transformation[] {
				new CDLPA(20),
				new WsnRolesTransformation(1, 1, 2, true),
				new UnitDiscGraph(1983) };

		int nodes = 1000;
		String[] list = new String[] {
				"/Users/benni/Downloads/WSN/beispiel/" + nodes + "-1-graph.txt",
				"/Users/benni/Downloads/WSN/beispiel/" + nodes + "-2-graph.txt",
				"/Users/benni/Downloads/WSN/beispiel/" + nodes + "-3-graph.txt" };
		Network nw1 = new ReadableList("CC-" + nodes + "", "cc-" + nodes + "",
				list, t1);
		// Network nw2 = new ReadableList("CC-1000", "cc-1000", list, t2);
		Network nw3 = Test.nwCC(nodes, t2);

		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new Communities(), new Roles(Role.RoleType.WSN) };

		Network[] nw = new Network[] { nw1, nw3 };

		boolean GET = false;
		int TIMES = 3;

		Series[] s = GET ? Series.get(nw, metrics) : Series.generate(nw,
				metrics, TIMES);
		Plotting.multi(s, metrics, "multi-1000/", Type.confidence1,
				Style.candlesticks);

		// Graph g = nw3.generate();
		// Gephi gephi = new Gephi();
		// gephi.plot(g, (IdentifierSpace) g.getProperty("ID_SPACE_0"),
		// "./plots/blafasel-1.png");
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
		return new DescriptionWrapper(nw, "Communities " + nodes);
	}
}
