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
 * Simulation.java
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
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.EigenvectorCentralityNodeSorter;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.graph.sorting.CentralityNodeSorter.CentralityMode;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.metrics.BiconnectedComponent;
import gtna.metrics.EffectiveDiameter;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.connectivity.RichClubConnectivity;
import gtna.metrics.fragmentation.Fragmentation.Resolution;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.networks.util.ReadableFolder;
import gtna.plot.Plotting;
import gtna.util.Config;

/**
 * @author truong
 * 
 */
public class Simulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("MAIN_DATA_FOLDER", "./data/DegreeDistribution/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/DegreeDistribution/");
		String n = args[0];
		String[] ss = new String[] { n + "2007", n + "2008", n + "2009",
				n + "2010", n + "2011", n + "2012" };
		for (String s : ss) {
			System.out.println("=====" + s + "=====");
			Network nw = new ReadableFolder(s, s, "./graphs/" + s + "/", "",
					null);
			Metric metric = new DegreeDistribution();
			Metric[] metrics = new Metric[] { metric };
			Series series = Series.generate(nw, metrics, 100);
			Plotting.multi(series, metrics, s + "/");
		}
	}
}
