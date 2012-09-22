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
 * NetworkFragmentationCC.java
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
package gtna.projects.resilience;

import gtna.communities.Role;
import gtna.data.Series;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.graph.sorting.WsnRolesNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.communities.Communities;
import gtna.metrics.communities.Roles;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data.Type;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.util.Config;
import gtna.util.parameter.IntParameter;

/**
 * @author benni
 * 
 */
public class NetworkFragmentationCC {

	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/network-fragmentation-cc/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/network-fragmentation-cc/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");

		NodeSorter s1 = new DegreeNodeSorter(NodeSorterMode.DESC);
		NodeSorter s2 = new RandomNodeSorter();
		NodeSorter s3 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.HS_HB_HC_S_B_C);
		NodeSorter s4 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.HS_HB_S_B_HC_C);
		NodeSorter s5 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.HS_S_HB_B_HC_C);
		NodeSorter s6 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.S_B_HS_HB_HC_C);
		NodeSorter s7 = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.S_HS_B_HB_HC_C);

		Metric dd = new DegreeDistribution();
		Metric c = new Communities();
		Metric r = new Roles(Role.RoleType.WSN);
		Metric wf1 = new WeakFragmentation(s1, Fragmentation.Resolution.PERCENT);
		Metric wf2 = new WeakFragmentation(s2, Fragmentation.Resolution.PERCENT);
		Metric wf3 = new WeakFragmentation(s3, Fragmentation.Resolution.PERCENT);
		Metric wf4 = new WeakFragmentation(s4, Fragmentation.Resolution.PERCENT);
		Metric wf5 = new WeakFragmentation(s5, Fragmentation.Resolution.PERCENT);
		Metric wf6 = new WeakFragmentation(s6, Fragmentation.Resolution.PERCENT);
		Metric wf7 = new WeakFragmentation(s7, Fragmentation.Resolution.PERCENT);

		Metric[] metrics = new Metric[] { dd, c, r, wf1, wf2, wf3, wf4, wf5,
				wf6, wf7 };

		Transformation[] t = new Transformation[] {
				new CDLPA(50), new WsnRolesTransformation() };

		Network nw1 = NetworkFragmentationCC.nwCC(1000, t);
		Network nw2 = NetworkFragmentationCC.nwCC(2000, t);
		Network nw3 = NetworkFragmentationCC.nwCC(3000, t);
		Network nw4 = NetworkFragmentationCC.nwCC(4000, t);
		Network nw5 = NetworkFragmentationCC.nwCC(5000, t);

		Network[] nw = new Network[] { nw1, nw2, nw3, nw4, nw5 };

		boolean get = false;
		int times = 50;

		Series[] s = get ? Series.get(nw, metrics) : Series.generate(nw,
				metrics, times);

		Plotting.multi(s, metrics, "multi-avg/");
		Plotting.multi(s, metrics, "multi-conf/", Type.confidence1,
				Style.candlesticks);
		for (Network network : nw) {
			Plotting.multi(Series.get(network, metrics), metrics, "multi-"
					+ network.getNodes() + "-avg/");
			Plotting.multi(Series.get(network, metrics), metrics, "multi-"
					+ network.getNodes() + "-conf/", Type.confidence1,
					Style.candlesticks);
		}
	}

	private static final double xyNodes = 40000;
	private static final double xyHotspots = 30000;
	private static final double devHotspots = 10000;
	private static final double devNodes = 1000;
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
		return new DescriptionWrapper(nw, "Communities " + nodes,
				new IntParameter("NODES", nodes));
	}
}
