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
 * Exploring.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.ClusteringCoefficient;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.basic.ShortestPaths;
import gtna.metrics.connectivity.RichClubConnectivity;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.CondonAndKarp;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.WattsStrogatz;
import gtna.plot.Gnuplot.Style;
import gtna.plot.Plotting;
import gtna.plot.data.Data.Type;
import gtna.transformation.Transformation;
import gtna.transformation.edges.Bidirectional;
import gtna.util.Config;

/**
 * @author Tim
 * 
 */

public class Exploring {
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		// Config.overwrite("GNUPLOT_PRINT_ERRORS", "true");

		Transformation t = new Bidirectional();

		Network nw1 = new ErdosRenyi(200, 10, false, new Transformation[]{t});
		Network nw2 = new BarabasiAlbert(200, 10, new Transformation[]{t});
		Network nw3 = new WattsStrogatz(200, 6, 0.2, new Transformation[]{t});
		/*
		 * CC -> 19500msec to 20500msec 
		 */
		Network nw4 = new CondonAndKarp(200, 3, 0.4, 0.05, new Transformation[]{t});

		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new ShortestPaths(), new ClusteringCoefficient()};
		// .generate for new networks
		// .get for loading networks
//		Series s1 = Series.get(nw1, metrics/*, 5*/);
//		Series s2 = Series.get(nw2, metrics/*, 5*/);
//		Series s3 = Series.get(nw3, metrics/*, 5*/);
//		Series s4 = Series.get(nw4, metrics/*, 5*/);

		Series s1 = Series.generate(nw1, metrics, 5);
		Series s2 = Series.generate(nw2, metrics, 5);
		Series s3 = Series.generate(nw3, metrics, 5);
		Series s4 = Series.generate(nw4, metrics, 5);

		
		Series[] s = new Series[] { s1, s2, s3, s4 };

		if (!Plotting.single(s, metrics, "example-s/")) {
//			System.err.println("Failed plotting single values");
		}
//		Plotting.multi(s, metrics, "example-m/", Type.confidence1,
//				Style.candlesticks);
		 if(Plotting.multi(s, metrics, "example-m/")){
//		 System.err.println("Failed plotting multi values");
		 }
	}
}
