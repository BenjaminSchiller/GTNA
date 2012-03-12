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
package gtna.projects;

import gtna.data.Series;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.graph.sorting.Roles2NodeSorter;
import gtna.metrics.Communities;
import gtna.metrics.DegreeDistribution;
import gtna.metrics.Metric;
import gtna.metrics.Roles;
import gtna.metrics.Roles2;
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
import gtna.plot.Plot;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CommunityColors;
import gtna.transformation.communities.CommunityDetectionDeltaQ;
import gtna.transformation.communities.CommunityDetectionLPA;
import gtna.transformation.communities.Roles2Generation;
import gtna.transformation.communities.RolesGeneration;
import gtna.transformation.partition.GiantConnectedComponent;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/metrics-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/metrics-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		int times = 5;
		int nodes = 4000;

		Transformation t_lpa = new CommunityDetectionLPA();
		Transformation t_dq = new CommunityDetectionDeltaQ();
		Transformation t_cc = new CommunityColors();
		Transformation t_r = new RolesGeneration();
		Transformation t_r2 = new Roles2Generation(true);
		Transformation t_gcc = new GiantConnectedComponent();

		Transformation[] t1 = new Transformation[] { t_gcc, t_lpa, t_r, t_r2 };
		Transformation[] t2 = new Transformation[] { t_gcc, t_dq, t_r, t_r2 };
		Network nw1 = new DescriptionWrapper(Test.communityNew(nodes, t1),
				"LPA - " + nodes);
		Network nw2 = new DescriptionWrapper(Test.communityNew(nodes, t2),
				"DeltaQ - " + nodes);
		Network[] nw = new Network[] { nw1, nw2 };

		Metric[] metrics = new Metric[] {
				new DegreeDistribution(),
				new WeakFragmentation(new DegreeNodeSorter(
						NodeSorter.NodeSorterMode.DESC),
						Fragmentation.Resolution.PERCENT),
				new WeakFragmentation(new RandomNodeSorter(),
						Fragmentation.Resolution.PERCENT),
				new WeakFragmentation(new Roles2NodeSorter(
						Roles2NodeSorter.HS_S_HB_B_HC_C),
						Fragmentation.Resolution.PERCENT),
				new WeakFragmentation(new Roles2NodeSorter(
						Roles2NodeSorter.HS_HB_S_B_HC_C),
						Fragmentation.Resolution.PERCENT),
				new WeakFragmentation(new Roles2NodeSorter(
						Roles2NodeSorter.HS_HB_HC_S_B_C),
						Fragmentation.Resolution.PERCENT), new Roles(), new Roles2(), new Communities() };
		Series[] s = Series.generate(nw, metrics, times);
		Plot.multiAvg(s, "multi/", metrics);
	}

	private static Network communityNew(int nodes, Transformation[] t) {
		PlacementModel p1 = new CommunityPlacementModel(20, 20, 0.5, false);
		PlacementModel p2 = new CommunityPlacementModel(20, 20, 0.2, false);
		Partitioner partitioner = new SimplePartitioner();
		NodeConnector connector = new UDGConnector(1);
		return new PlacementModelContainer(nodes, nodes / 100, 40, 40, p1, p2,
				partitioner, connector, null, t);
	}
}
