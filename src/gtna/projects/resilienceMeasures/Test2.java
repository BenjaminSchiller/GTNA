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
import gtna.graph.sorting.CentralityNodeSorter;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.CentralityNodeSorter.CentralityMode;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.algorithms.GraphSPall;
import gtna.graph.sorting.algorithms.GraphSPallFloyd;
import gtna.metrics.AverageShortestPathLength;
import gtna.metrics.AverageShortestPathLength.Resolution;
import gtna.metrics.BiconnectedComponent;
import gtna.metrics.ApproxEffectiveDiameter;
import gtna.metrics.ExactEffectiveDiameter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.GLP;
import gtna.networks.model.PFP;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;

/**
 * @author truong
 * 
 */
public class Test2 {
	public static void main(String[] args) {
		Test2.ASPLTest();
	}

	public static void readFileTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Config.overwrite("GNUPLOT_PATH",
		// "C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		String graphFile = "./caida2007.gtna";
		Network nw1 = new ReadableFile("CAIDA", "CAIDA", graphFile, null);
		int N = 12160;

		Network nw2 = new PFP(N, 20, 0.4, 0.021, null);
		Network[] networks = new Network[] { nw1, nw2 };

		Metric metric = new BiconnectedComponent(new DegreeNodeSorter(
				NodeSorterMode.DESC));
		Metric[] metrics = new Metric[] { metric };

		Series[] s = Series.generate(networks, metrics, 20);
		Plotting.multi(s, metrics, "compare/");
	}

	public static void effectiveDiameter() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		Network nw = new PFP(10000, 10, 0.4, 0.021, null);
		Network[] networks = new Network[] { nw };

		NodeSorter sorter = new DegreeNodeSorter(NodeSorterMode.DESC);
		Metric m1 = new ApproxEffectiveDiameter(128, 7, sorter);
		Metric m2 = new ExactEffectiveDiameter(sorter);
		Metric[] metrics = new Metric[] { m1 };

		Series[] s = Series.generate(networks, metrics, 1);
		Plotting.multi(s, metrics, "effectiveDiameter/");

	}

	public static void allPairsTest() {
		Network nw = new PFP(100, 10, 0.4, 0.021, null);
		Graph g = nw.generate();
		GraphSPallFloyd allpairs = new GraphSPallFloyd(g);
		System.out.println("" + allpairs.dist(1, 90));
		System.out.println("" + allpairs.dist(90, 1));
	}

	public static void ASPLTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Network nw = new PFP(20000, 10, 0.4, 0.021, null);
		Network[] networks = new Network[] { nw };
		NodeSorter sorter = new DegreeNodeSorter(NodeSorterMode.DESC);
		Metric m = new AverageShortestPathLength(sorter, Resolution.PERCENT);
		Metric[] metrics = new Metric[] { m };

		Series[] s = Series.generate(networks, metrics, 1);
		Plotting.multi(s, metrics, "ASPL/");
	}
}
