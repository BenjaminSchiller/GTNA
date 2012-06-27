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
 * Test2.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: truong;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.projects.resilienceMeasures;

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.io.graphReader.GtnaGraphReader;
import gtna.io.graphWriter.GraphWriter;
import gtna.io.graphWriter.GtnaGraphWriter;
import gtna.metrics.ASPL;
import gtna.metrics.BiconnectedComponent;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.IG;
import gtna.networks.model.PFP;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.trash.AverageShortestPathLength;
import gtna.trash.AverageShortestPathLength.Resolution;
import gtna.util.Config;

/**
 * @author truong
 * 
 */
public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test2.biconnectedCorrectnessTest();
	}

	public static void mixedTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Network nw = new BarabasiAlbert(3000, 4, null);
		Network nw = new ReadableFile("CAIDA", "CAIDA", "./caida2007.gtna",
				null);
		Network[] networks = new Network[] { nw };
		Metric m = new AverageShortestPathLength(new DegreeNodeSorter(
				NodeSorterMode.DESC), Resolution.MIXED, 0.15);
		Metric[] metrics = new Metric[] { m };

		Series[] series = Series.generate(networks, metrics, 1);

		Plotting.multi(series, metrics, "./MIXEDTEST/");
	}

	public static void biconnected() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Network nw = new BarabasiAlbert(10000, 4, null);
		Network nw = new ReadableFile("CAIDA", "CAIDA", "./caida2007.gtna",
				null);
		Network[] networks = new Network[] { nw };
		Metric m = new BiconnectedComponent(new RandomNodeSorter(), 0.4);
		Metric[] metrics = new Metric[] { m };

		Series[] series = Series.generate(networks, metrics, 1);

		Plotting.multi(series, metrics, "./BICONNECTED/");
	}

	public static void createGtnaGraph() {
		Network nw = new BarabasiAlbert(15, 4, null);
		Graph g = nw.generate();
		GraphWriter writer = new GtnaGraphWriter();
		writer.write(g, "graph.gtna");
		System.out.println("Graph was created");
	}

	public static void biconnectedCorrectnessTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Network nw = new IG(1000, 10, 0.4, null);
		Network[] networks = new Network[] { nw };
		Metric m = new BiconnectedComponent(new DegreeNodeSorter(
				NodeSorterMode.DESC), 0.2);
		Metric[] metrics = new Metric[] { m };
		Series[] series = Series.generate(networks, metrics, 1);
		Plotting.multi(series, metrics, "./GRAPH/");
	}

	public static void ASPLtest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Network nw = new IG(1000, 10, 0.4, null);
		Network[] networks = new Network[] { nw };
		Metric m = new ASPL(new DegreeNodeSorter(NodeSorterMode.DESC), 1.0);
		// Metric m = new AverageShortestPathLength(new DegreeNodeSorter(
		// NodeSorterMode.DESC), Resolution.SINGLE, 0);
		Metric[] metrics = new Metric[] { m };
		Series[] series = Series.generate(networks, metrics, 1);
		Plotting.multi(series, metrics, "./GRAPH/");
	}

}
