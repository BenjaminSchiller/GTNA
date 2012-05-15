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
import gtna.metrics.EffectiveDiameter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.PFP1;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.util.Config;

/**
 * @author truong
 * 
 */
public class Test2 {
	public static void main(String[] args) {
		Test2.effectiveDiameter();
	}

	public static void readFileTest() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		String graphFile = ".\\caida2007.gtna";
		Network nw1 = new ReadableFile("CAIDA", "CAIDA", graphFile, null);
		Network[] networks = new Network[] { nw1 };

		Metric m = new DegreeDistribution();
		Metric[] metrics = new Metric[] { m };

		Series[] s = Series.generate(networks, metrics, 3);
		Plotting.multi(s, metrics, "readFileTest/");
	}

	public static void effectiveDiameter() {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("GNUPLOT_PATH",
				"C:\\Program Files (x86)\\gnuplot\\bin\\gnuplot.exe");

		Network nw = new PFP1(1000, 10, 0.4, 0.021, null);
		Network[] networks = new Network[] { nw };

		Metric m = new EffectiveDiameter(128, 7, new DegreeNodeSorter(
				NodeSorterMode.DESC), true);
		Metric[] metrics = new Metric[] { m };

		Series[] s = Series.generate(networks, metrics, 1);
		Plotting.multi(s, metrics, "effectiveDiameter/");
	}
}
