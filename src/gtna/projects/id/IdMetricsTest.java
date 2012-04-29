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
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.communities.CommunityDetectionLPA;
import gtna.transformation.embedding.communities.SimpleCommunityEmbedding1;
import gtna.transformation.embedding.communities.SimpleCommunityEmbedding2;
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
		Metric[] metrics = new Metric[] { idsd, idsv1, idsv2, idsv3 };
		metrics = new Metric[] { didsdp };

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
