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
 * CcnRouting.java
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
package gtna.projects.ccnRouting;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.id.data.LruDataStore;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.Metric;
import gtna.metrics.MetricDescriptionWrapper;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.centrality.BetweennessCentrality;
import gtna.metrics.centrality.TopK;
import gtna.metrics.routing.DataStorageMetric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.routing.routingTable.RoutingTableRouting;
import gtna.routing.selection.source.ConsecutiveSourceSelection;
import gtna.routing.selection.source.RandomSourceSelection;
import gtna.routing.selection.source.SourceSelection;
import gtna.routing.selection.target.DataStorageZipfTargetSelection;
import gtna.routing.selection.target.TargetSelection;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.transformation.id.node.NodeIds;
import gtna.transformation.id.node.NodeIdsDataStorage;
import gtna.transformation.id.node.NodeIdsRoutingTable;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class CcnRoutingSetup {
	// main folder where results are stored and graph can be found...
	public static final String folder = "/Users/benni/Downloads/ccn/";
	public static final String dataFolder = folder + "data/";
	public static final String plotFolder = folder + "plots/";
	public static final String plotFolderConf = folder + "plots-conf/";
	public static final String tempFolder = folder + "temp/";
	// where to find the graph(s)
	public static final String graphFolder = folder + "graphs/";

	// name of the input graph to read (GTNA format!)
	public static final String graphName = "ws-100-5";
	public static final String graphFilename = graphName + ".gtna";
	public static final String graphFilenameTransformed = "transformed--"
			+ graphFilename;

	public static final boolean skip = false;
	// total repititions of the whole simulation
	public static final int times = 100;

	// how many routing requests to send per node
	public static final int routesPerNode = 5;
	// iterations of the CCN routing (routerPerNode for each turn)
	public static final int turns = 2;

	// data items per node
	public static final int itemsPerNode = 100;
	// cache size at each node
	public static final int cacheSize = 10;
	// alpha parameter of the zipf popularity distribution
	public static final double alpha = 0.9;

	// random selectin of start node
	public static final SourceSelection ssRandom = new RandomSourceSelection();
	// round robin selection of sources (each node exactly routerPerNode times
	public static final SourceSelection ssConsecutive = new ConsecutiveSourceSelection();
	public static final SourceSelection ss = ssConsecutive;
	public static final TargetSelection ts = new DataStorageZipfTargetSelection(
			alpha);

	public static void main(String[] args) {

		Config.overwrite("MAIN_DATA_FOLDER", dataFolder);
		Config.overwrite("MAIN_PLOT_FOLDER", plotFolder);
		Config.overwrite("TEMP_FOLDER", tempFolder);

		Config.overwrite("PLOT_EXTENSION", ".png");
		Config.overwrite("GNUPLOT_TERMINAL", "png");

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "" + skip);
		Config.overwrite("ROUTING_ROUTES_PER_NODE", "" + routesPerNode);

		Transformation t0, t1, t2, t3, t4;
		t0 = new Bidirectional();
		t1 = new LargestWeaklyConnectedComponent();
		t2 = new NodeIds();
		t3 = new NodeIdsRoutingTable();
		t4 = new NodeIdsDataStorage(new LruDataStore(0, cacheSize),
				itemsPerNode);
		Transformation[] t = new Transformation[] { t0, t1, t2, t3, t4 };

		Network rf = new ReadableFile(graphName, graphName, graphFolder
				+ graphFilename, t);
		Network nw = new DescriptionWrapper(rf, graphName);

		Metric[] topk = new Metric[7];
		topk[0] = new MetricDescriptionWrapper(new TopK(
				TopK.Type.BETWEENNESS_CENTRALITY, TopK.Type.DEGREE_CENTRALITY),
				"B/D");
		topk[1] = new MetricDescriptionWrapper(new TopK(
				TopK.Type.BETWEENNESS_CENTRALITY,
				TopK.Type.ROUTING_BETWEENNESS, "ROUTING_" + turns), "B/R (CCN)");
		topk[2] = new MetricDescriptionWrapper(new TopK(
				TopK.Type.DEGREE_CENTRALITY, TopK.Type.ROUTING_BETWEENNESS,
				"ROUTING_" + turns), "D/R (CCN)");
		topk[3] = new MetricDescriptionWrapper(new TopK(
				TopK.Type.BETWEENNESS_CENTRALITY,
				TopK.Type.ROUTING_BETWEENNESS, "ROUTING_0"),
				"B/R (GREEDY before)");
		topk[4] = new MetricDescriptionWrapper(new TopK(
				TopK.Type.DEGREE_CENTRALITY, TopK.Type.ROUTING_BETWEENNESS,
				"ROUTING_0"), "D/R (GREEDY before)");
		topk[5] = new MetricDescriptionWrapper(new TopK(
				TopK.Type.BETWEENNESS_CENTRALITY,
				TopK.Type.ROUTING_BETWEENNESS, "ROUTING_" + (turns + 1)),
				"B/R (GREEDY after)");
		topk[6] = new MetricDescriptionWrapper(new TopK(
				TopK.Type.DEGREE_CENTRALITY, TopK.Type.ROUTING_BETWEENNESS,
				"ROUTING_" + (turns + 1)), "D/R (GREEDY after)");

		Metric[] metrics = new Metric[4 + turns * 2 + 2 + topk.length];
		metrics[0] = new DegreeDistribution();
		metrics[1] = new BetweennessCentrality();
		metrics[2] = new MetricDescriptionWrapper(new Routing(
				new RoutingTableRouting(), ss, ts), "GREEDY-before", 0);
		metrics[3] = new MetricDescriptionWrapper(new DataStorageMetric(),
				"GREEDY-before", 0);

		for (int i = 0; i < turns; i++) {
			metrics[4 + i * 2] = new MetricDescriptionWrapper(new Routing(
					new RoutingTableRouting(), ss, ts), "NDN-" + (i + 1), i + 1);
			metrics[4 + i * 2 + 1] = new MetricDescriptionWrapper(
					new DataStorageMetric(), "NDN-" + (i + 1), i + 1);
		}

		metrics[4 + turns * 2] = new MetricDescriptionWrapper(new Routing(
				new RoutingTableRouting(), ss, ts), "GREEDY-after", 1);
		metrics[4 + turns * 2 + 1] = new MetricDescriptionWrapper(
				new DataStorageMetric(), "GREEDY-after", turns + 1);

		for (int i = 0; i < topk.length; i++) {
			metrics[4 + turns * 2 + 2 + i] = topk[i];
		}

		Series s = Series.generate(nw, metrics, times);
		Plotting.multi(s, metrics, "multi/");
		Config.overwrite("MAIN_PLOT_FOLDER", plotFolderConf);
		Plotting.multi(s, metrics, "multi/", Type.confidence1,
				Style.candlesticks);
	}

	public static void generateProperties() {
		Transformation t0, t1, t2, t3, t4;
		t0 = new Bidirectional();
		t1 = new LargestWeaklyConnectedComponent();
		t2 = new NodeIds();
		t3 = new NodeIdsRoutingTable();
		// cache size: 10 files for all
		// files: 100 per host
		t4 = new NodeIdsDataStorage(new LruDataStore(0, cacheSize),
				itemsPerNode);

		Transformation[] t = new Transformation[] { t0, t1, t2, t3, t4 };

		Network rf = new ReadableFile(graphName, graphName, graphFolder
				+ graphFilename, t);
		Graph g = rf.generate();
		g = t0.transform(g);
		g = t1.transform(g);
		g = t2.transform(g);
		g = t3.transform(g);

		GraphWriter w = new GtnaGraphWriter();
		w.writeWithProperties(g, graphFolder + graphFilenameTransformed);

	}
}
