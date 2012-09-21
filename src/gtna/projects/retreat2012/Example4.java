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
 * Example3.java
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
package gtna.projects.retreat2012;

import gtna.data.Series;
import gtna.id.data.LruDataStore;
import gtna.metrics.Metric;
import gtna.metrics.MetricDescriptionWrapper;
import gtna.metrics.routing.DataStorageMetric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.routing.routingTable.CcnRouting;
import gtna.routing.routingTable.RoutingTableRouting;
import gtna.routing.selection.source.ConsecutiveSourceSelection;
import gtna.routing.selection.target.DataStorageRandomTargetSelection;
import gtna.transformation.Transformation;
import gtna.transformation.id.node.NodeIds;
import gtna.transformation.id.node.NodeIdsDataStorage;
import gtna.transformation.id.node.NodeIdsRoutingTable;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class Example4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "data/retreat2012-example4/");
		Config.overwrite("MAIN_PLOT_FOLDER", "plots/retreat2012-example4/");

		Config.overwrite("SERIES_GRAPH_WRITE", "true");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");

		Config.overwrite("ROUTING_ROUTES_PER_NODE", "5");

		int times = 2;

		int nodes = 500;
		int degree = 10;

		Transformation t1 = new NodeIds();
		Transformation t2 = new NodeIdsRoutingTable();
		Transformation t3 = new NodeIdsDataStorage(new LruDataStore(0, 10), 1);

		Transformation[] t = new Transformation[] { t1, t2, t3 };

		Network er = new ErdosRenyi(nodes, degree, true, t);
		Network nw = new DescriptionWrapper(er, "ER");

		Routing r1 = new Routing(new RoutingTableRouting(),
				new ConsecutiveSourceSelection(),
				new DataStorageRandomTargetSelection());
		Routing r2 = new Routing(new CcnRouting(),
				new ConsecutiveSourceSelection(),
				new DataStorageRandomTargetSelection());
		DataStorageMetric ds1 = new DataStorageMetric();
		DataStorageMetric ds2 = new DataStorageMetric();

		Metric m1 = new MetricDescriptionWrapper(r1, "IP");
		Metric m2 = new MetricDescriptionWrapper(ds1, "DS before", 1);
		Metric m3 = new MetricDescriptionWrapper(r2, "CCN");
		Metric m4 = new MetricDescriptionWrapper(ds2, "DS after", 2);

		Metric[] metrics = new Metric[] { m1, m2, m3, m4 };

		Series s = Series.generate(nw, metrics, times);

		Plotting.multi(s, metrics, "multi/");
	}

}
