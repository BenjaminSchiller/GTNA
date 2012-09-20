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
 * CommunityEmbeddingsTest.java
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

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.io.graphReader.GtnaV1GraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.communities.Communities;
import gtna.metrics.id.DIdentifierSpaceDistances;
import gtna.metrics.routing.Routing;
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
import gtna.networks.util.ReadableFile;
import gtna.plot.Data.Type;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedy.GreedyBacktracking;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.attackableEmbedding.swapping.Swapping;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.embedding.communities.CommunityEmbedding;
import gtna.transformation.embedding.communities.SimpleCommunityEmbedding1;
import gtna.transformation.embedding.communities.SimpleCommunityEmbedding2;
import gtna.transformation.embedding.communities.partitioner.community.EqualSizeCommunityPartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.EqualSizeIdSpacePartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.RelativeSizeIdSpacePartitioner;
import gtna.transformation.embedding.communities.sorter.community.NeighborsByEdgesCommunitySorter;
import gtna.transformation.embedding.communities.sorter.community.OriginalCommunitySorter;
import gtna.transformation.embedding.communities.sorter.node.OriginalNodeSorter;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.partition.StrongConnectivityPartition;
import gtna.util.Config;
import gtna.util.Stats;

import java.util.HashMap;
import java.util.Map;

/**
 * @author benni
 * 
 */
public class CommunityEmbeddingsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (false) {

			String filename1 = "./resources/WOT-1-BD/2005-02-25.wot.txt";
			String filename2 = "./resources/2005-02-25.wot.txt";

			Graph g = new GtnaV1GraphReader().read(filename1);
			new GtnaGraphWriter().write(g, filename2);

			return;
		}

		if (false) {

			Network nw = new ErdosRenyi(10, 3, true, null);

			Transformation cd = new CDLPA(20);
			Transformation ce1 = new SimpleCommunityEmbedding1();
			Transformation ce2 = new SimpleCommunityEmbedding2();
			Transformation ce_1 = new CommunityEmbedding(
					new OriginalCommunitySorter(),
					new RelativeSizeIdSpacePartitioner(0.0),
					new OriginalNodeSorter(),
					new EqualSizeCommunityPartitioner(), true);
			Transformation ce_2 = new CommunityEmbedding(
					new OriginalCommunitySorter(),
					new EqualSizeIdSpacePartitioner(0.0),
					new OriginalNodeSorter(),
					new EqualSizeCommunityPartitioner(), true);
			Transformation ce_3 = new CommunityEmbedding(
					new NeighborsByEdgesCommunitySorter(),
					new EqualSizeIdSpacePartitioner(0.0),
					new OriginalNodeSorter(),
					new EqualSizeCommunityPartitioner(), true);

			Graph g = nw.generate();

			g = cd.transform(g);
			g = ce_3.transform(g);

			System.out.println(nw.getDescription());

			return;
		}

		Stats stats = new Stats();

		Config.overwrite("MAIN_DATA_FOLDER", "./data/community-embedding/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/community-embedding/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "false");
		Config.overwrite("GNUPLOT_PRINT_ERRORS", "false");

		Metric dd = new DegreeDistribution();
		Metric communities = new Communities();
		Metric routing1 = new Routing(new GreedyBacktracking(100));
		Metric routing2 = new Routing(new Greedy());
		Metric routing3 = new Routing(new DepthFirstGreedy(100));
		Metric idsd = new DIdentifierSpaceDistances(100);
		Metric[] metrics = new Metric[] { dd, communities, idsd };

		Transformation scp = new StrongConnectivityPartition();
		Transformation gcc = new LargestWeaklyConnectedComponent();
		Transformation cd = new CDLPA(20);
		// Transformation cd = new CommunityDetectionDeltaQ();

		Transformation re = new RandomRingIDSpaceSimple(true);
		Transformation lmc = new LMC(1000, LMC.MODE_UNRESTRICTED, 0,
				LMC.DELTA_1_N, 0);
		Transformation sw = new Swapping(1000);

		Transformation ce1 = new SimpleCommunityEmbedding1();
		Transformation ce2 = new SimpleCommunityEmbedding2();
		Transformation ce_1 = new CommunityEmbedding(
				new OriginalCommunitySorter(),
				new RelativeSizeIdSpacePartitioner(0.0),
				new OriginalNodeSorter(), new EqualSizeCommunityPartitioner(),
				true);
		Transformation ce_2 = new CommunityEmbedding(
				new OriginalCommunitySorter(), new EqualSizeIdSpacePartitioner(
						0.0), new OriginalNodeSorter(),
				new EqualSizeCommunityPartitioner(), true);
		Transformation ce_3 = new CommunityEmbedding(
				new NeighborsByEdgesCommunitySorter(),
				new RelativeSizeIdSpacePartitioner(0.0),
				new OriginalNodeSorter(), new EqualSizeCommunityPartitioner(),
				true);
		Transformation ce_4 = new CommunityEmbedding(
				new NeighborsByEdgesCommunitySorter(),
				new EqualSizeIdSpacePartitioner(0.0), new OriginalNodeSorter(),
				new EqualSizeCommunityPartitioner(), true);

		Transformation[] tr = new Transformation[] { scp, gcc, cd, re };
		Transformation[] tlmc = new Transformation[] { scp, gcc, cd, re, lmc };
		Transformation[] tsw = new Transformation[] { scp, gcc, cd, re, sw };
		Transformation[] t1 = new Transformation[] { scp, gcc, cd, ce1 };
		Transformation[] t2 = new Transformation[] { scp, gcc, cd, ce2 };
		Transformation[] t3 = new Transformation[] { scp, gcc, cd, ce_1 };
		Transformation[] t4 = new Transformation[] { scp, gcc, cd, ce_2 };
		Transformation[] t5 = new Transformation[] { scp, gcc, cd, ce_3 };
		Transformation[] t6 = new Transformation[] { scp, gcc, cd, ce_4 };

		Map<Transformation[], String> names = new HashMap<Transformation[], String>();
		names.put(tr, "Random");
		names.put(tlmc, "LMC");
		names.put(tsw, "Swapping");
		names.put(t1, "CE 1  - rel | orig");
		names.put(t3, "CE 1' - rel | orig");
		names.put(t2, "CE 2  - eq  | orig");
		names.put(t4, "CE 2' - eq  | orig");
		names.put(t5, "CE 3' - rel | edge");
		names.put(t6, "CE 4' - eq  | edge");

		String folder = "multi/";
		Transformation[][] t = new Transformation[][] { tr, tlmc, tsw, t1, t3,
				t2, t4, t5, t6 };

		t = new Transformation[][] { tr, tsw, t1, t3 };

		// folder = "rel-orig/";
		// t = new Transformation[][] { t1, t3 };
		// folder = "eq-orig/";
		// t = new Transformation[][] { t2, t4 };
		// folder = "edge/";
		// t = new Transformation[][] { t5, t6 };
		// folder = "rel/";
		// t = new Transformation[][] { t1, t3, t5 };
		// folder = "eq/";
		// t = new Transformation[][] { t2, t4, t6 };

		String spi = "./resources/spi-2011-02.spi.txt";
		String wot = "./resources/2005-02-25.wot.txt";

		boolean GET = true;
		int TIMES = 2;

		int networkType = 3;
		int nodes = 2000;

		Network[] nw = new Network[t.length];
		String name = null;
		for (int i = 0; i < t.length; i++) {
			if (networkType == 1) {
				nw[i] = new ReadableFile("SPI", "spi", spi, t[i]);
				name = "SPI";
			}
			if (networkType == 2) {
				nw[i] = new ReadableFile("WOT", "wot", wot, t[i]);
				name = "WOT";
			}
			if (networkType == 3) {
				nw[i] = CommunityEmbeddingsTest.nwCC(nodes, t[i]);
				name = "CC";
			}
			nw[i] = new DescriptionWrapper(nw[i], name + " - "
					+ names.get(t[i]));
		}

		Series[] s = GET ? Series.get(nw, metrics) : Series.generate(nw,
				metrics, TIMES);

		// Plotting.multi(s, metrics, "multi/");

		Config.overwrite("GNUPLOT_OFFSET_X", "0");
		// Config.overwrite("IDENTIFIER_SPACE_DISTANCES_EDGES_DISTANCE_DISTRIBUTION_PLOT_LOGSCALE",
		// "y");
		Plotting.multi(s, metrics, name + "-" + folder, Type.average,
				Style.linespoint);

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
