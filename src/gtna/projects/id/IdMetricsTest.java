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
import gtna.metrics.id.DIdentifierSpaceDistanceProducts;
import gtna.metrics.id.DIdentifierSpaceDistances;
import gtna.metrics.id.PlaneIdentifierSpaceDistancesToCenter;
import gtna.metrics.id.PlaneIdentifierSpaceVisualization;
import gtna.metrics.id.RingIdentifierSpaceSuccessorDistances;
import gtna.metrics.id.RingIdentifierSpaceSuccessorHopDistances;
import gtna.metrics.id.RingIdentifierSpaceVisualzation;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CirclePlacementModel;
import gtna.networks.model.placementmodels.models.CirclePlacementModel.DistributionType;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.GridPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.embedding.communities.CommunityEmbedding;
import gtna.transformation.embedding.communities.partitioner.community.EqualSizeCommunityPartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.EqualSizeIdSpacePartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.RelativeSizeIdSpacePartitioner;
import gtna.transformation.embedding.communities.sorter.community.RandomCommunitySorter;
import gtna.transformation.embedding.communities.sorter.node.RandomNodeSorter;
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
		IdMetricsTest.testRing();

		stats.end();
	}

	public static void testRing() {
		Metric idsd = new DIdentifierSpaceDistances(100);
		Metric didsdp = new DIdentifierSpaceDistanceProducts(1000);
		Metric idsv1 = new RingIdentifierSpaceVisualzation(100);
		Metric idsv2 = new RingIdentifierSpaceVisualzation(500);
		Metric idsv3 = new RingIdentifierSpaceVisualzation();
		Metric ridsshd = new RingIdentifierSpaceSuccessorHopDistances();
		Metric ridssd = new RingIdentifierSpaceSuccessorDistances(100);
		Metric[] metrics = new Metric[] { idsd, didsdp, idsv1, idsv2, idsv3,
				ridsshd, ridssd };

		Transformation[] t1 = new Transformation[] { new RemoveGraphProperty(),
				new RandomRingIDSpaceSimple(true) };
		Transformation[] t2 = new Transformation[] { new RemoveGraphProperty(),
				new RandomRingIDSpaceSimple(true),
				new LMC(1000, LMC.MODE_UNRESTRICTED, 0, LMC.DELTA_1_N, 0) };
		Transformation[] t3 = new Transformation[] {
				new RemoveGraphProperty(),
				new CDLPA(50),
				new CommunityEmbedding(new RandomCommunitySorter(),
						new EqualSizeIdSpacePartitioner(0.0),
						new RandomNodeSorter(),
						new EqualSizeCommunityPartitioner(), true) };
		Transformation[] t4 = new Transformation[] {
				new RemoveGraphProperty(),
				new CDLPA(50),
				new CommunityEmbedding(new RandomCommunitySorter(),
						new RelativeSizeIdSpacePartitioner(0.0),
						new RandomNodeSorter(),
						new EqualSizeCommunityPartitioner(), true) };

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
		Metric idsd = new DIdentifierSpaceDistances(100);
		Metric didsdp = new DIdentifierSpaceDistanceProducts(1000);
		Metric pidsd2c = new PlaneIdentifierSpaceDistancesToCenter(100);
		Metric pidsv = new PlaneIdentifierSpaceVisualization();
		Metric[] metrics = new Metric[] { idsd, didsdp, pidsd2c, pidsv };

		Transformation[] t1 = new Transformation[] { new Nothing(
				new IntParameter("INDEX", 1)) };
		Transformation[] t2 = new Transformation[] { new Nothing(
				new IntParameter("INDEX", 2)) };
		Transformation[] t3 = new Transformation[] { new Nothing(
				new IntParameter("INDEX", 3)) };

		Map<Transformation[], String> names = new HashMap<Transformation[], String>();
		names.put(t1, "Communities");
		names.put(t2, "Random");
		names.put(t3, "Grid-Circle");

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
