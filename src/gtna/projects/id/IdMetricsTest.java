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
 * IdMetricsTest.java
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
package gtna.projects.id;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.id.DIdentifierSpaceDistanceProducts;
import gtna.metrics.id.DIdentifierSpaceDistances;
import gtna.metrics.id.PlaneIdentifierSpaceVisualization;
import gtna.metrics.id.RingIdentifierSpaceSuccessorHopDistances;
import gtna.metrics.id.RingIdentifierSpaceVisualzation;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CirclePlacementModel;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.GridPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.models.CirclePlacementModel.DistributionType;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.communities.CommunityDetectionLPA;
import gtna.transformation.embedding.communities.CommunityEmbedding;
import gtna.transformation.embedding.communities.SimpleCommunityEmbedding1;
import gtna.transformation.embedding.communities.SimpleCommunityEmbedding2;
import gtna.transformation.embedding.communities.partitioner.community.EqualSizeCommunityPartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.EqualSizeIdSpacePartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.RelativeSizeIdSpacePartitioner;
import gtna.transformation.embedding.communities.sorter.community.RandomCommunitySorter;
import gtna.transformation.embedding.communities.sorter.node.RandomNodeSorter;
import gtna.transformation.id.RandomRingIDSpace;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.util.Nothing;
import gtna.transformation.util.RemoveGraphProperty;
import gtna.util.Config;
import gtna.util.Stats;
import gtna.util.parameter.IntParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author benni
 * 
 */
public class IdMetricsTest {
	public static void main(String[] args) {
		Stats stats = new Stats();

		Config.overwrite("MAIN_DATA_FOLDER", "./data/id-metrics-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/id-metrics-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "false");
		Config.overwrite("GNUPLOT_PRINT_ERRORS", "false");
		Config.overwrite("GNUPLOT_LW", "5");

		IdMetricsTest.testPlane();
		// IdMetricsTest.testRing();

		if (true) {
			return;
		}

		boolean GET = false;
		int TIMES = 1;
		int nodes = 1000;

		Metric idsd = new DIdentifierSpaceDistances(100);
		Metric idsv1 = new RingIdentifierSpaceVisualzation(100);
		Metric idsv2 = new RingIdentifierSpaceVisualzation(500);
		Metric idsv3 = new RingIdentifierSpaceVisualzation();
		Metric pidsv = new PlaneIdentifierSpaceVisualization();
		Metric ridsshd = new RingIdentifierSpaceSuccessorHopDistances();
		Metric sp = new ShortestPaths();
		Metric didsdp = new DIdentifierSpaceDistanceProducts(1000);
		Metric[] metrics = new Metric[] { idsd, idsv1, idsv2, idsv3, pidsv,
				pidsv };

		Transformation[] rand1 = new Transformation[] {
				new RemoveGraphProperty(), new RandomRingIDSpaceSimple() };
		Transformation[] rand2 = new Transformation[] {
				new RemoveGraphProperty(), new RandomRingIDSpace() };
		Transformation[] lmc = new Transformation[] {
				new RemoveGraphProperty(), new RandomRingIDSpaceSimple(),
				new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0) };
		Transformation[] comm1 = new Transformation[] {
				new RemoveGraphProperty(), new CommunityDetectionLPA(10),
				new SimpleCommunityEmbedding1() };
		Transformation[] comm2 = new Transformation[] {
				new RemoveGraphProperty(), new CommunityDetectionLPA(10),
				new SimpleCommunityEmbedding2() };
		Transformation[][] t = new Transformation[][] { rand1, rand2, lmc,
				comm1, comm2 };

		Transformation[] t1 = new Transformation[] { new Nothing(
				new IntParameter("", 1)) };
		Transformation[] t2 = new Transformation[] { new Nothing(
				new IntParameter("", 2)) };
		Transformation[] t3 = new Transformation[] { new Nothing(
				new IntParameter("", 3)) };
		Transformation[] t4 = new Transformation[] { new Nothing(
				new IntParameter("", 4)) };
		// t = new Transformation[][] { t1, t2, t3, t4 };

		Map<Transformation[], String> names = new HashMap<Transformation[], String>();
		names.put(rand1, "rand1");
		names.put(rand2, "rand2");
		names.put(lmc, "LMC");
		names.put(comm1, "comm1");
		names.put(comm2, "comm2");
		names.put(t1, "T1");
		names.put(t2, "T2");
		names.put(t3, "T3");
		names.put(t4, "T4");

		Network[] nw = new Network[t.length];
		for (int i = 0; i < t.length; i++) {
			Network nw_ = null;
			// nw_ = new Complete(100, t[i]);
			nw_ = new ErdosRenyi(2000, 20, true, t[i]);
			// nw_ = IdMetricsTest.nwCC(nodes, t[i]);
			nw[i] = new DescriptionWrapper(nw_, names.get(t[i]));
		}

		Series[] s = GET ? Series.get(nw, metrics) : Series.generate(nw,
				metrics, TIMES);
		Plotting.multi(s, metrics, "multi/");

		stats.end();
	}

	public static void testRing() {
		Metric idsd = new DIdentifierSpaceDistances(100);
		Metric idsv1 = new RingIdentifierSpaceVisualzation(100);
		Metric idsv2 = new RingIdentifierSpaceVisualzation(500);
		Metric idsv3 = new RingIdentifierSpaceVisualzation();
		Metric ridsshd = new RingIdentifierSpaceSuccessorHopDistances();
		Metric didsdp = new DIdentifierSpaceDistanceProducts(1000);
		Metric[] metrics = new Metric[] { idsd, idsv1, idsv2, idsv3, ridsshd,
				didsdp };

		Transformation[] t1 = new Transformation[] { new RemoveGraphProperty(),
				new RandomRingIDSpaceSimple() };
		Transformation[] t2 = new Transformation[] { new RemoveGraphProperty(),
				new RandomRingIDSpaceSimple(),
				new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0) };
		Transformation[] t3 = new Transformation[] {
				new RemoveGraphProperty(),
				new CommunityDetectionLPA(50),
				new CommunityEmbedding(new RandomCommunitySorter(),
						new EqualSizeIdSpacePartitioner(0.0),
						new RandomNodeSorter(),
						new EqualSizeCommunityPartitioner(), 1.0, true) };
		Transformation[] t4 = new Transformation[] {
				new RemoveGraphProperty(),
				new CommunityDetectionLPA(50),
				new CommunityEmbedding(new RandomCommunitySorter(),
						new RelativeSizeIdSpacePartitioner(0.0),
						new RandomNodeSorter(),
						new EqualSizeCommunityPartitioner(), 1.0, true) };

		Map<Transformation[], String> names = new HashMap<Transformation[], String>();
		names.put(t1, "random");
		names.put(t2, "lmc");
		names.put(t3, "ce equal");
		names.put(t4, "ce relative");

		int nodes = 1000;

		Network nw1 = IdMetricsTest.nwCC(nodes, t1, names);
		Network nw2 = IdMetricsTest.nwCC(nodes, t2, names);
		Network nw3 = IdMetricsTest.nwCC(nodes, t3, names);
		Network nw4 = IdMetricsTest.nwCC(nodes, t4, names);

		Network[] nw = new Network[] { nw1, nw2, nw3, nw4 };

		Series[] s = Series.generate(nw, metrics, 1);

		Plotting.multi(s, metrics, "ring/");
	}

	public static void testPlane() {
		Metric pidsv = new PlaneIdentifierSpaceVisualization();
		Metric[] metrics = new Metric[] { pidsv };

		Transformation[] t1 = new Transformation[] { new Nothing(
				new IntParameter("INDEX", 1)) };
		Transformation[] t2 = new Transformation[] { new Nothing(
				new IntParameter("INDEX", 2)) };
		Transformation[] t3 = new Transformation[] { new Nothing(
				new IntParameter("INDEX", 3)) };

		Map<Transformation[], String> names = new HashMap<Transformation[], String>();
		names.put(t1, "nw1");
		names.put(t2, "nw2");
		names.put(t3, "nw3");

		int nodes = 2000;

		Network nw1 = IdMetricsTest.nwCC(nodes, t1, names);
		Network nw2 = IdMetricsTest.nwR(nodes, t2, names);
		Network nw3 = IdMetricsTest.nwGC(nodes, t3, names);

		Network[] nw = new Network[] { nw1, nw2, nw3 };

		Series[] s = Series.generate(nw, metrics, 1);

		Plotting.multi(s, metrics, "plane/");
	}

	private static final double xyNodes = 40000;
	private static final double xyHotspots = 30000;
	private static final double devHotspots = 10000;
	private static final double devNodes = 2000;
	private static final double radius = 1983;

	private static final Partitioner partitioner = new SimplePartitioner();

	private static final NodeConnector connector = new UDGConnector(radius);

	public static Network nwCC(int nodes, Transformation[] t,
			Map<Transformation[], String> names) {
		PlacementModel p1 = new CommunityPlacementModel(devHotspots,
				devHotspots, false);
		PlacementModel p2 = new CommunityPlacementModel(devNodes, devNodes,
				false);
		Network nw = new PlacementModelContainer(nodes, nodes / 100, xyNodes,
				xyNodes, xyHotspots, xyHotspots, p1, p2, partitioner,
				connector, t);
		return new DescriptionWrapper(nw, names.get(t));
	}

	public static Network nwR(int nodes, Transformation[] t,
			Map<Transformation[], String> names) {
		PlacementModel p1 = new RandomPlacementModel(xyNodes, xyNodes, true);
		PlacementModel p2 = new RandomPlacementModel(xyNodes, xyNodes, false);
		Network nw = new PlacementModelContainer(nodes, 1, xyNodes, xyNodes,
				xyNodes, xyNodes, p1, p2, partitioner, connector, t);
		return new DescriptionWrapper(nw, names.get(t));
	}

	public static Network nwGC(int nodes, Transformation[] t,
			Map<Transformation[], String> names) {
		PlacementModel p1 = new GridPlacementModel(xyNodes / 1.5,
				xyNodes / 1.5, 5, 5, false);
		PlacementModel p2 = new CirclePlacementModel(radius * 1.2,
				DistributionType.FIXED, DistributionType.FIXED, false);
		Network nw = new PlacementModelContainer(nodes, 23, xyNodes, xyNodes,
				xyNodes, xyNodes, p1, p2, partitioner, connector, t);
		return new DescriptionWrapper(nw, names.get(t));
	}
}
