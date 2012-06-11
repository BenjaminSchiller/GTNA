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
 * Caida.java
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
import gtna.graph.sorting.CentralityNodeSorter;
import gtna.graph.sorting.CentralityNodeSorter.CentralityMode;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.EigenvectorCentralityNodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.AverageShortestPathLength;
import gtna.metrics.BiconnectedComponent;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;

/**
 * @author truong
 * 
 */
public class Caida {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/Fragmentation/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/Fragmentation/");
		String caidaFile = "./" + args[0] + ".gtna";
		String folder = "" + args[1] + "/";
		Network nw = new ReadableFile("CAIDA", "CAIDA", caidaFile, null);
		Network[] networks = new Network[] { nw };
		Metric metric = new WeakFragmentation(new CentralityNodeSorter(
				CentralityMode.CLOSENESS, NodeSorterMode.DESC),
				Resolution.SINGLE);
		Metric[] metrics = new Metric[] { metric };

		Series[] s = Series.generate(networks, metrics, 1);
		Plotting.multi(s, metrics, folder);
	}
}
