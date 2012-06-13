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
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.AverageShortestPathLength;
import gtna.metrics.AverageShortestPathLength.Resolution;
import gtna.metrics.BiconnectedComponent;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
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
		Test2.biconnected();
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
		Metric m = new BiconnectedComponent(new RandomNodeSorter(), 0.5);
		Metric[] metrics = new Metric[] { m };

		Series[] series = Series.generate(networks, metrics, 1);

		Plotting.multi(series, metrics, "./BICONNECTED/");
	}

}
