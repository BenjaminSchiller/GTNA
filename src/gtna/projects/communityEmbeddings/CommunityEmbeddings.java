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
 * CommunityEmbeddings.java
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
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.communities.Communities;
import gtna.metrics.id.DIdentifierSpaceDistanceProducts;
import gtna.metrics.id.DIdentifierSpaceDistances;
import gtna.metrics.id.RingIdentifierSpaceSuccessorHopDistances;
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
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plotting;
import gtna.plot.Data.Type;
import gtna.plot.Gnuplot.Style;
import gtna.routing.greedy.Greedy;
import gtna.routing.greedy.GreedyBacktracking;
import gtna.routing.greedyVariations.DepthFirstGreedy;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.attackableEmbedding.swapping.Swapping;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.embedding.communities.CommunityEmbedding;
import gtna.transformation.embedding.communities.partitioner.community.LmcCommunityPartitioner;
import gtna.transformation.embedding.communities.partitioner.community.RandomCommunityPartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.RandomIdSpacePartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.RelativeSizeIdSpacePartitioner;
import gtna.transformation.embedding.communities.sorter.community.NeighborsByEdgesCommunitySorter;
import gtna.transformation.embedding.communities.sorter.community.RandomCommunitySorter;
import gtna.transformation.embedding.communities.sorter.node.LmcNodeSorter;
import gtna.transformation.embedding.communities.sorter.node.RandomNodeSorter;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.util.RemoveGraphProperty;
import gtna.util.Config;
import gtna.util.Stats;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author benni
 * 
 */
public class CommunityEmbeddings {

	public static final int times = 50;

	public static final int ttl = 100;

	public static final boolean get = true;

	public static final boolean skip = true;

	public static void main(String[] args) {
		Stats stats = new Stats();

		Config.overwrite("MAIN_DATA_FOLDER", "./data/community-embedding/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/community-embedding/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skip);
		Config.overwrite("AGGREGATE_ALL_AVAILABLE_RUNS", "true");

		Config.overwrite("GNUPLOT_TERMINAL", "png");
		Config.overwrite("PLOT_EXTENSION", ".png");

		/*
		 * Metrics
		 */

		Metric dd = new DegreeDistribution();
		Metric c = new Communities();

		Metric r1 = new Routing(new Greedy(ttl));
		Metric r2 = new Routing(new GreedyBacktracking(ttl));
		Metric r3 = new Routing(new DepthFirstGreedy(ttl));

		Metric didsd = new DIdentifierSpaceDistances(100);
		Metric didsdp = new DIdentifierSpaceDistanceProducts(1000);
		Metric ridsshd = new RingIdentifierSpaceSuccessorHopDistances();
		// Metric ridsv = new RingIdentifierSpaceVisualzation(100);

		Metric[] metrics = new Metric[] { dd, c, r1, r2, r3, didsd, didsdp,
				ridsshd };

		/*
		 * Transformations
		 */

		// sort communities: random / edges
		// partition id space: random
		// sort nodes: random / lmc
		// partition communities: random

		Transformation rridss = new RandomRingIDSpaceSimple(true);
		Transformation sw = new Swapping(1000);
		Transformation lmc = new LMC(1000, LMC.MODE_UNRESTRICTED, 0,
				LMC.DELTA_1_N, 0);

		Transformation ce1 = new CommunityEmbedding(
				new RandomCommunitySorter(), new RandomIdSpacePartitioner(),
				new RandomNodeSorter(), new RandomCommunityPartitioner(), true);
		Transformation ce2 = new CommunityEmbedding(
				new NeighborsByEdgesCommunitySorter(),
				new RandomIdSpacePartitioner(), new RandomNodeSorter(),
				new RandomCommunityPartitioner(), true);
		Transformation ce3 = new CommunityEmbedding(
				new NeighborsByEdgesCommunitySorter(),
				new RandomIdSpacePartitioner(), new LmcNodeSorter(),
				new RandomCommunityPartitioner(), true);
		Transformation ce4 = new CommunityEmbedding(
				new NeighborsByEdgesCommunitySorter(),
				new RandomIdSpacePartitioner(), new RandomNodeSorter(),
				new LmcCommunityPartitioner(), true);
		Transformation ce5 = new CommunityEmbedding(
				new NeighborsByEdgesCommunitySorter(),
				new RelativeSizeIdSpacePartitioner(0.0),
				new RandomNodeSorter(), new LmcCommunityPartitioner(), true);

		Transformation[] t_r = new Transformation[] { rridss };
		Transformation[] t_sw = new Transformation[] { rridss, sw };
		Transformation[] t_lmc = new Transformation[] { rridss, lmc };

		Transformation[] t_ce1 = new Transformation[] { ce1 };
		Transformation[] t_ce2 = new Transformation[] { ce2 };
		Transformation[] t_ce3 = new Transformation[] { ce3 };
		Transformation[] t_ce4 = new Transformation[] { ce4 };
		Transformation[] t_ce5 = new Transformation[] { ce5 };

		Transformation[][] T = new Transformation[][] { t_r, t_sw, t_lmc,
				t_ce1, t_ce2, t_ce4, t_ce5 };
		T = new Transformation[][] { t_lmc, t_ce4, t_ce5 };

		/*
		 * Transformation -> name mapping
		 */

		Map<Transformation[], String> names = new HashMap<Transformation[], String>();
		names.put(t_r, "Random");
		names.put(t_sw, "Swapping");
		names.put(t_lmc, "LMC");
		names.put(t_ce1, "CE - random | R | R | R");
		names.put(t_ce2, "CE - edges | R | R | R");
		names.put(t_ce3, "CE - edges | R | lmc-order | R");
		names.put(t_ce4, "CE - edges | R | R | lmc-positions");
		names.put(t_ce5, "CE - edges | relative | R | lmc-positions");

		/*
		 * generate graphs
		 */

		CommunityEmbeddings.generateGraphs();

		/*
		 * generate series
		 */

		String[][] graphs = CommunityEmbeddings.graphs();
		for (String[] graph : graphs) {
			System.out.println(graph[0] + ": generating series");
			Network[] nw = new Network[T.length];

			for (int i = 0; i < T.length; i++) {
				nw[i] = new DescriptionWrapper(new ReadableFolder(graph[0]
						+ " - " + names.get(T[i]), graph[0], graph[2], ".gtna",
						T[i]), graph[0] + " - " + names.get(T[i]));
				System.out.println("  => " + nw[i].getDescriptionShort());
				System.out.println("     " + nw[i].getFolderName());
			}

			Series[] s = get ? Series.get(nw, metrics) : Series.generate(nw,
					metrics, times);
			Plotting.multi(s, metrics, graph[0] + "-multi/", Type.confidence1,
					Style.candlesticks);
		}

		stats.end();
	}

	private static String[][] graphs() {
		String coauthor1Name = "COAUTHOR-1";
		String coauthor1SrcFilename = "resources/coauthor/original/coauthor1990A8P0RankDir.graph.gtna";
		String coauthor1DstFolder = "resources/__community-embedding/coauthor/coauthor1990A8P0RankDir.graph.gtna/";
		String[] coauthor1 = new String[] { coauthor1Name,
				coauthor1SrcFilename, coauthor1DstFolder };

		String coauthor2Name = "COAUTHOR-2";
		String coauthor2SrcFilename = "resources/coauthor/original/coauthor1993A8P0RankDir.graph.gtna";
		String coauthor2DstFolder = "resources/__community-embedding/coauthor/coauthor1993A8P0RankDir.graph.gtna/";
		String[] coauthor2 = new String[] { coauthor2Name,
				coauthor2SrcFilename, coauthor2DstFolder };

		String spi1Name = "SPI-1";
		String spi1SrcFilename = "resources/spi/_RLN/0_analyze_buddy_2010.csv.gtna";
		String spi1DstFolder = "resources/__community-embedding/spi/0_analyze_buddy_2010.csv.gtna/";
		String[] spi1 = new String[] { spi1Name, spi1SrcFilename, spi1DstFolder };

		String wot1Name = "WOT-1";
		String wot1SrcFilename = "resources/wot/original/2005-02-25.wot.gtna";
		String wot1DstFolder = "resources/__community-embedding/wot/2005-02-25.wot.gtna/";
		String[] wot1 = new String[] { wot1Name, wot1SrcFilename, wot1DstFolder };

		String cc1Name = "CC-1000";
		String cc1SrcFilename = null;
		String cc1DstFolder = "resources/__community-embedding/cc-1000/";
		String[] cc1 = new String[] { cc1Name, cc1SrcFilename, cc1DstFolder };

		String cc5Name = "CC-5000";
		String cc5SrcFilename = null;
		String cc5DstFolder = "resources/__community-embedding/cc-5000/";
		String[] cc5 = new String[] { cc5Name, cc5SrcFilename, cc5DstFolder };

		String er1Name = "ER-1000";
		String er1SrcFilename = null;
		String er1DstFolder = "resources/__community-embedding/er-1000/";
		String[] er1 = new String[] { er1Name, er1SrcFilename, er1DstFolder };

		String er5Name = "ER-5000";
		String er5SrcFilename = null;
		String er5DstFolder = "resources/__community-embedding/er-5000/";
		String[] er5 = new String[] { er5Name, er5SrcFilename, er5DstFolder };

		return new String[][] { coauthor1, coauthor2, cc1, cc5, er1, er5, spi1,
				wot1 };
		// return new String[][] { coauthor1, coauthor2, cc1, cc5, er1, er5,
		// spi1,
		// wot1 };
	}

	private static void generateGraphs() {
		String[][] graphs = CommunityEmbeddings.graphs();
		for (String[] graph : graphs) {
			System.out.println(graph[0] + ": generating graphs");
			CommunityEmbeddings.generateGraphs(graph[0], graph[1], graph[2]);
		}
	}

	private static void generateGraphs(String name, String srcFilename,
			String dstFolder) {
		new File(dstFolder).mkdirs();

		Transformation rgp = new RemoveGraphProperty();
		Transformation bi = new Bidirectional();
		Transformation lwcc = new LargestWeaklyConnectedComponent();
		Transformation cd = new CDLPA(50);

		for (int i = 0; i < times; i++) {
			String dst = dstFolder + i + ".gtna";
			if ((new File(dst)).exists()) {
				continue;
			}
			System.out.print("  " + i + " => ");

			Graph g = null;
			if (srcFilename != null) {
				g = new GtnaGraphReader().readWithProperties(srcFilename);
			} else if (name.startsWith("CC-")) {
				int nodes = Integer.parseInt(name.replace("CC-", ""));
				Network nw = CommunityEmbeddings.nwCC(nodes, null);
				g = nw.generate();
			} else if (name.startsWith("ER-")) {
				int nodes = Integer.parseInt(name.replace("ER-", ""));
				Network nw = new ErdosRenyi(nodes, 20, true, null);
				g = nw.generate();
			} else {
				System.err.println("unknown graph type '" + name + "'");
			}

			g = rgp.transform(g);
			g = bi.transform(g);
			g = lwcc.transform(g);
			g = cd.transform(g);

			new GtnaGraphWriter().writeWithProperties(g, dst);
			System.out.println(dst);
		}

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
