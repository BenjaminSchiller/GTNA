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
import gtna.metrics.routing.DataStorageMetric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.util.DescriptionWrapper;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.routing.routingTable.CcnRouting;
import gtna.routing.routingTable.RoutingTableRouting;
import gtna.routing.selection.source.ConsecutiveSourceSelection;
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
	public static final String folder = "/Users/benni/Downloads/ccn/";
	public static final String dataFolder = folder + "data/";
	public static final String plotFolder = folder + "plots/";
	public static final String tempFolder = folder + "temp/";
	public static final String graphFolder = folder + "graphs/";

	public static final String graphName = "caida";
	public static final String graphFilename = graphName + ".gtna";
	public static final String graphFilenameTransformed = "transformed--"
			+ graphFilename;

	public static final boolean skip = false;
	public static final int times = 5;

	public static final int routesPerNode = 5;
	public static final int turns = 5;

	public static final int itemsPerNode = 100;
	public static final int cacheSize = 10;
	public static final double alpha = 0.9;

	public static final SourceSelection ss = new ConsecutiveSourceSelection();
	public static final TargetSelection ts = new DataStorageZipfTargetSelection(
			alpha);

	public static void main(String[] args) {

		Config.overwrite("MAIN_DATA_FOLDER", dataFolder);
		Config.overwrite("MAIN_PLOT_FOLDER", plotFolder);
		Config.overwrite("TEMP_FOLDER", tempFolder);

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

		Metric[] metrics = new Metric[turns * 2 + 3];
		metrics[0] = new DegreeDistribution();
		metrics[1] = new MetricDescriptionWrapper(new Routing(
				new RoutingTableRouting(), ss, ts), "IP");
		metrics[2] = new MetricDescriptionWrapper(new DataStorageMetric(),
				"DS-0", 0);

		for (int i = 0; i < turns; i++) {
			metrics[3 + i * 2] = new MetricDescriptionWrapper(new Routing(
					new CcnRouting(), ss, ts), "NDN-" + (i + 1), i + 1);
			metrics[3 + i * 2 + 1] = new MetricDescriptionWrapper(
					new DataStorageMetric(), "NDN-" + (i + 1), i + 1);
		}

		Series s = Series.generate(nw, metrics, times);
		Plotting.multi(s, metrics, "multi/");
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
