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
 * MakiDemo1.java
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
package gtna.projects.makiDemo;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.util.Config;
import gtna.util.Execute;

/**
 * @author benni
 * 
 */
public class MakiDemo1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/maki/demo1/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/maki/demo1/");

		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("PLOT_EXTENSION", ".png");
		Config.overwrite("GNUPLOT_TERMINAL", "png");

		int times = 1;
		boolean generate = false;

		Metric dd = new DegreeDistribution();
		Metric cc = new ClusteringCoefficient();
		Metric sp = new ShortestPaths();

		Metric[] metrics = new Metric[] { dd, sp };

		int[] nodes = new int[] { 1000, 2000, 3000, 4000, 5000 };
		double[] degree = new double[] { 4, 8, 12, 16, 20, 24 };
		boolean b = false;
		Transformation[] t = null;

		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < degree.length; j++) {
				Network nw = new ErdosRenyi(nodes[i], degree[j], b, t);
				if (generate) {
					Series.generate(nw, metrics, times);
				}
			}
		}

		Network nw1 = new ErdosRenyi(nodes[0], degree[0], b, t);

		Network[] nw2 = ErdosRenyi.get(nodes[0], degree, b, t);

		Network[][] nw3 = new Network[nodes.length][];
		for (int i = 0; i < nodes.length; i++) {
			nw3[i] = ErdosRenyi.get(nodes[i], degree, false, t);
		}

		Network[][] nw4 = new Network[degree.length][];
		for (int i = 0; i < degree.length; i++) {
			nw4[i] = ErdosRenyi.get(nodes, degree[i], b, t);
		}

		Series s1 = Series.get(nw1, metrics);
		Series[] s2 = Series.get(nw2, metrics);
		Series[][] s3 = Series.get(nw3, metrics);
		Series[][] s4 = Series.get(nw4, metrics);

		Plotting.multi(s1, metrics, "multi-1/");
		Plotting.multi(s2, metrics, "multi-2/");
		Plotting.single(s3, metrics, "single-3/");
		Plotting.single(s4, metrics, "single-4/");
		
		Execute.exec("open plots/maki/demo1/");
	}

}
