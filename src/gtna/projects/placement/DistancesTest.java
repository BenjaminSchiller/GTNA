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
 * DistanceDistribution.java
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
package gtna.projects.placement;

import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.id.DIdentifierSpaceDistances;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data.Type;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class DistancesTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/dist/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/dist/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "false");
		// Config.overwrite("GNUPLOT_TERMINAL", "png");
		// Config.overwrite("PLOT_EXTENSION", ".png");

		Metric dd = new DegreeDistribution();
		Metric dist = new DIdentifierSpaceDistances(1);
		Metric[] metrics = new Metric[] { dd, dist };

		int times = 5;
		boolean generate = true;

		Transformation[] t = new Transformation[] {};

		int[] nodes = new int[] { 1000, 2000, 3000 };
		Network[] nw1 = new Network[nodes.length];
		Network[] nw2 = new Network[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw1[i] = DistancesTest.communityCommunity(nodes[i], t);
			nw2[i] = DistancesTest.random(nodes[i], t);
		}

		Network[][] nw = new Network[][] { nw1, nw2 };
		Series[][] s = generate ? Series.generate(nw, metrics, times) : Series
				.get(nw, metrics);

		Plotting.single(s, metrics, "single/", Type.average, Style.linespoint);
		// Plotting.singleBy(s, metrics, "single-edges/", dd, "EDGES");
		Plotting.multi(s, metrics, "multi/", Type.average, Style.dots);
	}

	private static final Partitioner partitioner = new SimplePartitioner();

	private static final NodeConnector connector = new UDGConnector(120);

	private static final double xy = 2000;

	private static Network communityCommunity(int nodes, Transformation[] t) {
		PlacementModel p1 = new CommunityPlacementModel(0.4 * xy, 0.4 * xy, true);
		PlacementModel p2 = new CommunityPlacementModel(0.1 * xy, 0.1 * xy, true);
		return new PlacementModelContainer(nodes, 10, xy, xy, xy, xy, p1, p2,
				partitioner, connector, t);
	}

	private static Network random(int nodes, Transformation[] t) {
		PlacementModel p1 = new RandomPlacementModel(xy, xy, true);
		PlacementModel p2 = new RandomPlacementModel(xy, xy, true);
		return new PlacementModelContainer(nodes, 1, xy, xy, xy, xy, p1, p2,
				partitioner, connector, t);
	}
}
