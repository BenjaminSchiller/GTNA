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
 * TopKTest.java
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
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.centrality.BetweennessCentrality;
import gtna.metrics.centrality.TopK;
import gtna.metrics.centrality.TopK.Type;
import gtna.networks.Network;
import gtna.networks.model.BarabasiAlbert;
import gtna.networks.model.ErdosRenyi;
import gtna.plot.Plotting;
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class TopKTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		int nodes = 500;
		int edges = 10;
		Network nw1 = new ErdosRenyi(nodes, edges, true, null);
		Network nw2 = new BarabasiAlbert(nodes, edges, null);
		Network[] nw = new Network[] { nw1, nw2 };
		Metric[] metrics = new Metric[] { new DegreeDistribution(),
				new BetweennessCentrality(),
				new TopK(Type.BETWEENNESS_CENTRALITY, Type.DEGREE_CENTRALITY, "ROUTING") };

		Series[] s = Series.generate(nw, metrics, 50);
		Plotting.multi(s, metrics, "multi/");
		Plotting.multi(s, metrics, "multi-confg/", Data.Type.confidence1,
				Style.candlesticks);
	}

}
