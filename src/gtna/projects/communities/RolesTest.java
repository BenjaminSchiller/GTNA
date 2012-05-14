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
 * Roles.java
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
import gtna.networks.Network;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.models.RandomPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CDDeltaQ;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.GuimeraRolesTransformation;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.util.Config;
import gtna.util.Stats;

/**
 * @author benni
 * 
 */
public class RolesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Metric rc1 = new RolesCorrelation(Role.RoleType.GUIMERA);
		// Metric rc2 = new RolesCorrelation(Role.RoleType.WSN);
		RolesTest.rolesTest();
	}

	public static void rolesTest() {
		Stats stats = new Stats();

		Config.overwrite("MAIN_DATA_FOLDER", "D:/data/roles-test/");
		Config.overwrite("MAIN_PLOT_FOLDER", "D:/plots/roles-test/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "false");

		Metric dd = new DegreeDistribution();
		Metric communities = new Communities();
		Metric roles1 = new Roles(Role.RoleType.GUIMERA);
		Metric roles2 = new Roles(Role.RoleType.WSN);
		Metric[] metrics = new Metric[] { dd, roles2 };

		Transformation lpa = new CDLPA(50);
		Transformation dq = new CDDeltaQ();
		Transformation r1 = new GuimeraRolesTransformation();
		Transformation r2 = new WsnRolesTransformation();
		Transformation[] t = new Transformation[] { lpa, r2 };

		int times = 1;

		Network[] nw = RolesTest.nwCCs(new int[] { 1000 }, t);

		// for (Network NW : nw) {
		// for (int i = 0; i < 3; i++) {
		// Graph g = NW.generate();
		// Gephi gephi = new Gephi();
		// gephi.plot(g, (IdentifierSpace) g.getProperty("ID_SPACE_0"),
		// "./plots/" + NW.getFolderName() + "-" + i + ".png");
		// }
		// }

		Series[] s = Series.generate(nw, metrics, times);

		Plotting.multi(s, metrics, "multi/");
		Plotting.single(s, metrics, "singles/");

		stats.end();
	}

	public static void rolesStability() {
		Config.overwrite("MAIN_DATA_FOLDER", "./data/roles/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/roles/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Config.overwrite("SERIES_GRAPH_WRITE", "false");

		Metric dd = new DegreeDistribution();
		Metric communities = new Communities();
		Metric roles = new Roles(Role.RoleType.GUIMERA);
		Metric roles2 = new Roles(Role.RoleType.WSN);
		Metric[] metrics = new Metric[] { dd, communities, roles, roles2 };

		Transformation lpa = new CDLPA(3);
		Transformation dq = new CDDeltaQ();
		Transformation t_roles = new GuimeraRolesTransformation();
		Transformation t_roles2 = new WsnRolesTransformation();
		Transformation[] t = new Transformation[] { lpa };

		int[] N = new int[] { 3000, 4000, 5000 };
		double[] Z = new double[] { 0.5, 0.75, 1.0, 1.25, 1.5 };
		int times = 10;

		for (int n : N) {
			for (double z : Z) {
				Network[] nw1 = RolesTest.nwCCs(n, z, true, t);
				Network[] nw2 = RolesTest.nwRs(n, z, true, t);

				Series[] s1 = Series.generate(nw1, metrics, times);
				Series[] s2 = Series.generate(nw2, metrics, times);
				Plotting.multi(s1, metrics, "CC-" + n + "-" + z + "-multi/");
				Plotting.multi(s2, metrics, "R-" + n + "-" + z + "-multi/");
				// Plotting.single(s1, metrics, "CC-" + n + "-" + z +
				// "-single/");
				// Plotting.single(s2, metrics, "R-" + n + "-" + z +
				// "-single/");
			}
		}
		// for series in $(ls roles/); do cp
		// roles/${series}/ROLES2/roles2-distribution.pdf temp/${series}.pdf;
		// done
	}

	private static final double xyAll = 40000;
	private static final double xyHotspots = 40000;
	private static final double xyNodes = 40000;
	private static final double boxHotspots = 30000;
	private static final double devHotspots = 5000;
	
	private static final double devNodes = 1000;
	private static final double radius = 1983;

	private static final Partitioner partitioner = new SimplePartitioner();

	private static final NodeConnector connector = new UDGConnector(radius);

	private static Network[] nwCCs(int nodes, double z, boolean withHubs,
			Transformation[] t) {
		Transformation[] roles = RolesTest.getRoles(z, withHubs);
		Network[] nw = new Network[roles.length];
		for (int i = 0; i < roles.length; i++) {
			nw[i] = new DescriptionWrapper(RolesTest.nwCC(nodes,
					RolesTest.add(t, roles[i])), "CC - " + nodes + " - "
					+ roles[i].getDescriptionShort());
		}
		return nw;
	}

	private static Network[] nwRs(int nodes, double z, boolean withHubs,
			Transformation[] t) {
		Transformation[] roles = RolesTest.getRoles(z, withHubs);
		Network[] nw = new Network[roles.length];
		for (int i = 0; i < roles.length; i++) {
			nw[i] = new DescriptionWrapper(RolesTest.nwR(nodes,
					RolesTest.add(t, roles[i])), "R - " + nodes + " - "
					+ roles[i].getDescriptionShort());
		}
		return nw;
	}

	private static Network[] nwCCs(int[] nodes, Transformation[] t) {
		Network[] nw = new Network[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw[i] = RolesTest.nwCC(nodes[i], t);
		}
		return nw;
	}

	private static Network[] nwRs(int[] nodes, Transformation[] t) {
		Network[] nw = new Network[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw[i] = RolesTest.nwR(nodes[i], t);
		}
		return nw;
	}

	private static Network nwCC(int nodes, Transformation[] t) {
		PlacementModel p1 = new CommunityPlacementModel(xyHotspots, xyHotspots, true);
		PlacementModel p2 = new CommunityPlacementModel(xyNodes, xyNodes, true);
		Network nw = new PlacementModelContainer(nodes, nodes / 100, xyAll,
				xyAll, boxHotspots, boxHotspots, p1, p2, partitioner, connector, t);
		// return new DescriptionWrapper(nw, "Communities " + nodes);
		return nw;
	}

	private static Network nwR(int nodes, Transformation[] t) {
		PlacementModel p1 = new RandomPlacementModel(xyAll, xyAll, true);
		PlacementModel p2 = new RandomPlacementModel(xyAll, xyAll, true);
		Network nw = new PlacementModelContainer(nodes, 1, xyAll, xyAll, boxHotspots, boxHotspots, p1,
				p2, partitioner, connector, t);
		// return new DescriptionWrapper(nw, "Random" + nodes);
		return nw;
	}

	private static Transformation[] getRoles(double z, boolean withHubs) {
		int[][] nac = new int[][] { new int[] { 1, 2 }, new int[] { 1, 3 },
				new int[] { 1, 4 }, new int[] { 1, 5 }, new int[] { 2, 3 },
				new int[] { 2, 4 }, new int[] { 2, 5 }, new int[] { 3, 4 },
				new int[] { 3, 5 }, new int[] { 4, 5 } };
		Transformation[] roles = new Transformation[nac.length];
		for (int i = 0; i < nac.length; i++) {
			roles[i] = new WsnRolesTransformation(z, nac[i][0], nac[i][1],
					withHubs);
		}
		return roles;
	}

	private static Transformation[] add(Transformation[] t1, Transformation t) {
		if (t1 == null) {
			t1 = new Transformation[0];
		}
		Transformation[] t2 = new Transformation[t1.length + 1];
		for (int i = 0; i < t1.length; i++) {
			t2[i] = t1[i];
		}
		t2[t1.length] = t;
		return t2;
	}
}
