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
package gtna.projects.etc;

import gtna.communities.Role;
import gtna.data.Series;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.graph.sorting.WsnRolesNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.communities.Communities;
import gtna.metrics.communities.Roles;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Data.Type;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.communities.CDDeltaQ;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.CommunityColors;
import gtna.transformation.communities.GuimeraRolesTransformation;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Config;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterList;

/**
 * @author benni
 * 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/quick_example/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/quick_example/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "true");

		Metric dd = new DegreeDistribution();
		Metric sp = new ShortestPaths();
		Metric wf_d = new WeakFragmentation(new DegreeNodeSorter(
				NodeSorterMode.DESC), Resolution.PERCENT);
		Metric wf_r = new WeakFragmentation(new RandomNodeSorter(),
				Resolution.PERCENT);
		Metric[] metrics = new Metric[] { dd, sp, wf_d, wf_r };

		int times = 5;
		boolean generate = true;

		// double[] d = new double[] { 10, 15, 20, 25, 30, 35, 40, 45, 50 };
		// Transformation[] t = new Transformation[] {
		// new WeakConnectivityPartition(), new GiantConnectedComponent() };
		// Network[] nw1 = ErdosRenyi.get(100, d, true, t);
		// Network[] nw2 = ErdosRenyi.get(200, d, true, t);
		// Network[] nw3 = ErdosRenyi.get(300, d, true, t);
		// Network[] nw4 = ErdosRenyi.get(400, d, true, t);
		// Network[][] nw = new Network[][] { nw1, nw2, nw3, nw4 };

		Transformation[] t1 = new Transformation[] {
				new WeakConnectivityPartition(),
				new LargestWeaklyConnectedComponent(),
				new LMC(100, "mode", 1.1, "deltaMode", 1001) };
		Transformation[] t2 = new Transformation[] {
				new WeakConnectivityPartition(),
				new LargestWeaklyConnectedComponent(),
				new LMC(100, "mode", 1.1, "deltaMode", 1002) };
		Transformation[] t3 = new Transformation[] {
				new WeakConnectivityPartition(),
				new LargestWeaklyConnectedComponent(),
				new LMC(100, "mode", 1.1, "deltaMode", 1003) };

		Network nw11 = new ErdosRenyi(100, 10, true, t1);
		Network nw12 = new ErdosRenyi(100, 10, true, t2);
		Network nw13 = new ErdosRenyi(100, 10, true, t3);
		Network nw21 = new ErdosRenyi(200, 10, true, t1);
		Network nw22 = new ErdosRenyi(200, 10, true, t2);
		Network nw23 = new ErdosRenyi(200, 10, true, t3);
		Network[] nw1 = new Network[] { nw11, nw12, nw13 };
		Network[] nw2 = new Network[] { nw21, nw22, nw23 };
		Network[][] nw = new Network[][] { nw1, nw2 };

		Series[][] s = generate ? Series.generate(nw, metrics, times) : Series
				.get(nw, metrics);
		Series[] s1 = Series.get(nw1, metrics);

		Plotting.single(s, metrics, "single/", Type.average, Style.linespoint);
		// Plotting.singleBy(s, metrics, "single-edges/", dd, "EDGES");
		// Plotting.multi(s1, metrics, "multi/", Type.confidence1,
		// Style.candlesticks);
	}

	private static void series() {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/testing/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Metric[] metrics = new Metric[] {
				new DegreeDistribution(),
				new ShortestPaths(),
				new WeakFragmentation(new RandomNodeSorter(),
						Fragmentation.Resolution.PERCENT),
				new WeakFragmentation(new DegreeNodeSorter(
						NodeSorter.NodeSorterMode.DESC),
						Fragmentation.Resolution.PERCENT) };
		int times = 10;
		Network nw = new ErdosRenyi(200, 10, true, new Transformation[] {
				new WeakConnectivityPartition(),
				new LargestWeaklyConnectedComponent(),
				new RandomRingIDSpaceSimple(true),
				new RandomRingIDSpaceSimple(true),
				new RandomRingIDSpaceSimple(true) });
		Series s = Series.generate(nw, metrics, times);
	}

	private static void diff() {
		Network nw1 = new ErdosRenyi(1000, 10, true, null);
		Network nw2 = new ErdosRenyi(5000, 50, true, null);
		System.out.println(nw1.getDescription());
		System.out.println(nw1.getDescriptionLong());
		System.out.println(nw1.getDescriptionShort());
		System.out.println(nw1.getFolder());
		System.out.println("");
		System.out.println(nw2.getDescription());
		System.out.println(nw2.getDescriptionLong());
		System.out.println(nw2.getDescriptionShort());
		System.out.println(nw2.getFolder());
		System.out.println("");
		System.out.println(nw1.getDiffDescription(nw2));
		System.out.println(nw1.getDiffDescriptionLong(nw2));
		System.out.println(nw1.getDiffDescriptionShort(nw2));
		System.out.println("");
	}

	private static void lists() {
		String[] names = new String[] { "CLUSTERING_COEFFICIENT",
				"COMMUNITIES", "COVERAGE", "DEGREE_DISTRIBUTION",
				"EDGE_CROSSINGS", "RICH_CLUB_CONNECTIVITY", "ROLES", "ROLES2",
				"ROUTING", "SHORTEST_PATHS", "STRONG_CONNECTIVITY",
				"WEAK_CONNECTIVITY" };
		names = new String[] { "COMPLETE", "RING", "STAR", "GPS_NETWORK",
				"BARABASI_ALBERT", "COMMUNITIES_NETWORK",
				"COMMUNITIES_NETWORK_2", "COMMUNITIES_NETWORK_3", "DE_BRUIJN",
				"ERDOS_RENYI", "GILBERT", "GN", "GNC", "GNR", "WATTS_STROGATZ" };

		for (String name : names) {
			ParameterList list = new ParameterList(name);
			System.out.println(list.getDescription());
			System.out.println(list.getDescriptionLong());
			System.out.println(list.getDescriptionShort());
			System.out.println(list.getFolder());
			System.out.println("");
		}

		ParameterList list = new ParameterList("CLUSTERING_COEFFICIENT");
		list = new ParameterList("ERDOS_RENYI", new Parameter[] {
				new IntParameter("AVERAGE_DEGREE", 4),
				new BooleanParameter("BIDIRECTIONAL", true) });
		list = new ParameterList("GREEDY", new Parameter[] { new IntParameter(
				"TTL", 12) });
		list = new ParameterList("UNDIRECTED_MOTIFS_4");
		System.out.println(list.getDescription());
		System.out.println(list.getDescriptionLong());
		System.out.println(list.getDescriptionShort());
		System.out.println(list.getFolder());
	}

	private static void metrics() {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/metrics-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/metrics-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		int times = 5;
		int nodes = 4000;

		Transformation t_lpa = new CDLPA(50);
		Transformation t_dq = new CDDeltaQ();
		Transformation t_cc = new CommunityColors();
		Transformation t_r = new GuimeraRolesTransformation();
		Transformation t_r2 = new WsnRolesTransformation();
		Transformation t_gcc = new LargestWeaklyConnectedComponent();

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
				new WeakFragmentation(new WsnRolesNodeSorter(
						WsnRolesNodeSorter.HS_S_HB_B_HC_C),
						Fragmentation.Resolution.PERCENT),
				new WeakFragmentation(new WsnRolesNodeSorter(
						WsnRolesNodeSorter.HS_HB_S_B_HC_C),
						Fragmentation.Resolution.PERCENT),
				new WeakFragmentation(new WsnRolesNodeSorter(
						WsnRolesNodeSorter.HS_HB_HC_S_B_C),
						Fragmentation.Resolution.PERCENT),
				new Roles(Role.RoleType.GUIMERA), new Roles(Role.RoleType.WSN),
				new Communities() };
		Series[] s = Series.generate(nw, metrics, times);
		// PlotOld.multiAvg(s, "multi/", metrics);
	}

	private static Network communityNew(int nodes, Transformation[] t) {
		PlacementModel p1 = new CommunityPlacementModel(0.5 * 20, 0.5 * 20,
				false);
		PlacementModel p2 = new CommunityPlacementModel(0.2 * 20, 0.2 * 20,
				false);
		Partitioner partitioner = new SimplePartitioner();
		NodeConnector connector = new UDGConnector(1);
		return new PlacementModelContainer(nodes, nodes / 100, 40, 40, 40, 40,
				p1, p2, partitioner, connector, t);
	}
}
