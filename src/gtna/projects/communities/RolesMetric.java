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
 * RolesMetric.java
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
package gtna.projects.communities;

import gtna.communities.Role;
import gtna.data.Series;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.communities.Communities;
import gtna.metrics.communities.Roles;
import gtna.metrics.placement.Coverage;
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.plot.Gnuplot.Style;
import gtna.plot.data.Data.Type;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDDeltaQ;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.GuimeraRolesTransformation;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Config;

/**
 * @author benni
 * 
 */
public class RolesMetric {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/roles/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/roles/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "true");
		Config.overwrite("SERIES_GRAPH_WRITE", "false");

		Metric dd = new DegreeDistribution();
		Metric cov = new Coverage();
		Metric communities = new Communities();
		Metric roles = new Roles(Role.RoleType.GUIMERA);
		Metric[] metrics = new Metric[] { communities };

		int times = 10;
		boolean generate = false;

		Transformation wcc = new WeakConnectivityPartition();
		Transformation gcc = new LargestWeaklyConnectedComponent();
		Transformation dq = new CDDeltaQ();
		Transformation lpa = new CDLPA(50);
		Transformation r = new GuimeraRolesTransformation();
		Transformation r2 = new WsnRolesTransformation();

		Transformation[] t1 = new Transformation[] { dq, dq, dq, dq, dq, dq, r,
				r2 };
		Transformation[] t2 = new Transformation[] { lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa, lpa,
				lpa, lpa, lpa, lpa, lpa, lpa, r, r2 };
		t1 = new Transformation[] { lpa };
		t2 = new Transformation[] { dq };

		// Network nw1 = RolesMetric.communityCommunity(1000, t1);
		// Network nw2 = RolesMetric.communityCommunity(1000, t2);

		int[] nodes = new int[] { 1000, 2000, 3000, 4000 };
		Network[] nw1 = new Network[nodes.length];
		Network[] nw2 = new Network[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw1[i] = RolesMetric.communityCommunity(nodes[i], t1);
			nw2[i] = RolesMetric.communityCommunity(nodes[i], t2);
		}

		Network[][] nw = new Network[][] { nw1, nw2 };
		Series[][] s = generate ? Series.generate(nw, metrics, times) : Series
				.get(nw, metrics);

		Plotting.single(s, metrics, "single/", Type.average, Style.linespoint);
		// Plotting.singleBy(s, metrics, "single-edges/", dd, "EDGES");
		Plotting.multi(s, metrics, "multi/", Type.average, Style.linespoint);
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
}
