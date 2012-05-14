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
 * PlottingTest.java
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
package gtna.projects.plotting;

import gtna.data.Series;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.NodeSorter.NodeSorterMode;
import gtna.graph.sorting.RandomNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.fragmentation.Fragmentation;
import gtna.metrics.fragmentation.WeakFragmentation;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class PlottingTest {
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/plotting-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/plotting-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		boolean get = true;
		int times = 1;

		NodeSorter s1 = new DegreeNodeSorter(NodeSorterMode.DESC);
		NodeSorter s2 = new RandomNodeSorter();

		Metric dd = new DegreeDistribution();
		Metric wf1 = new WeakFragmentation(s1, Fragmentation.Resolution.PERCENT);
		Metric wf2 = new WeakFragmentation(s2, Fragmentation.Resolution.PERCENT);

		Metric[] metrics = new Metric[] { dd, wf1, wf2 };

		Transformation[] t = new Transformation[] {
				new CDLPA(50), new WsnRolesTransformation() };

		Network nw1 = new DescriptionWrapper(new ErdosRenyi(1000, 10, true, t),
				"ER 1000");
		Network nw2 = new DescriptionWrapper(new ErdosRenyi(2000, 10, true, t),
				"ER 2000");

		Network[] nw = new Network[] { nw1, nw2 };

		Series[] s = get ? Series.get(nw, metrics) : Series.generate(nw,
				metrics, times);

		Plotting.multi(s, metrics, "multi/");
		Plotting.single(s, metrics, "single/");
	}
}
