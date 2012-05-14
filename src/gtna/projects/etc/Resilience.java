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
 * Resilience.java
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
package gtna.projects.etc;

import gtna.communities.Role;
import gtna.communities.Role.RoleType;
import gtna.communities.RoleList;
import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.graph.sorting.DegreeNodeSorter;
import gtna.graph.sorting.GuimeraRolesNodeSorter;
import gtna.graph.sorting.NodeSorter;
import gtna.graph.sorting.WsnRolesNodeSorter;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.communities.Communities;
import gtna.metrics.communities.Roles;
import gtna.networks.Network;
import gtna.networks.model.ErdosRenyi;
import gtna.networks.model.placementmodels.NodeConnector;
import gtna.networks.model.placementmodels.Partitioner;
import gtna.networks.model.placementmodels.PlacementModel;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.UDGConnector;
import gtna.networks.model.placementmodels.models.CommunityPlacementModel;
import gtna.networks.model.placementmodels.partitioners.SimplePartitioner;
import gtna.networks.util.DescriptionWrapper;
import gtna.plot.Plotting;
import gtna.transformation.Transformation;
import gtna.transformation.communities.CommunityColors;
import gtna.transformation.communities.CDDeltaQ;
import gtna.transformation.communities.CDLPA;
import gtna.transformation.communities.GuimeraRolesTransformation;
import gtna.transformation.communities.WsnRolesTransformation;
import gtna.transformation.partition.LargestWeaklyConnectedComponent;
import gtna.transformation.partition.WeakConnectivityPartition;
import gtna.util.Config;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class Resilience {

	public static void main(String[] args) {
		Transformation t_lpa = new CDLPA(50);
		Transformation t_dq = new CDDeltaQ();
		Transformation t_cc = new CommunityColors();
		Transformation t_r = new GuimeraRolesTransformation();
		Transformation t_r2 = new WsnRolesTransformation();
		Transformation t_gcc = new LargestWeaklyConnectedComponent();
		Transformation t_wcp = new WeakConnectivityPartition();

		int[] nodes = new int[] { 1000, 2000, 3000, 4000 };

		int times = 1;

		Transformation[] t1 = new Transformation[] { t_wcp, t_gcc, t_lpa, t_r,
				t_r2 };
		Transformation[] t2 = new Transformation[] { t_wcp, t_gcc, t_dq, t_r,
				t_r2 };
		Network nw1 = new DescriptionWrapper(Resilience.communityNew(2000, t1),
				"LPA");
		Network nw2 = new DescriptionWrapper(Resilience.communityNew(2000, t2),
				"DeltaQ");
		Network[] nw = new Network[] { nw1, nw2 };

		Network[] nw_lpa = new Network[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw_lpa[i] = new DescriptionWrapper(Resilience.communityNew(
					nodes[i], t1), "LPA - " + nodes[i]);
		}
		Network[] nw_dq = new Network[nodes.length];
		for (int i = 0; i < nodes.length; i++) {
			nw_dq[i] = new DescriptionWrapper(Resilience.communityNew(nodes[i],
					t2), "DeltaQ - " + nodes[i]);
		}

		Config.overwrite("MAIN_DATA_FOLDER", "./data/roles/");
		Config.overwrite("MAIN_PLOT_FOLDER", "./plots/roles/");
		Config.overwrite("SKIP_EXISTING_DATA_FOLDERS", "false");
		Metric[] metrics = new Metric[] { new Roles(Role.RoleType.GUIMERA),
				new Roles(RoleType.WSN), new Communities(),
				new DegreeDistribution() };

		Series[] s_lpa = Series.generate(nw_lpa, metrics, times);
		Series[] s_dq = Series.generate(nw_dq, metrics, times);

		Plotting.multi(s_lpa, metrics, "lpa/");
		Plotting.multi(s_dq, metrics, "deltaQ/");
		Plotting.single(new Series[][] { s_lpa, s_dq }, metrics, "singles/");
	}

	private static void sorting() {
		if (false) {
			Network nw = new ErdosRenyi(20, 5, false, null);
			Graph g = nw.generate();
			NodeSorter sorter = new DegreeNodeSorter(
					NodeSorter.NodeSorterMode.ASC);
			System.out.println(sorter.getKey() + ":");
			Random rand = new Random();
			Resilience.print(sorter.sort(g, rand), g);
			System.out.println();
			for (int i = 0; i < 30; i++) {
				System.out.println(Resilience.toString(sorter.sort(g, rand)));
			}
			return;
		}

		Network nw = Resilience.communityNew(2000, null);
		Transformation t_lpa = new CDLPA(50);
		Transformation t_cc = new CommunityColors();
		Transformation t_r = new GuimeraRolesTransformation();
		Transformation t_r2 = new WsnRolesTransformation();
		Graph g = nw.generate();
		g = t_lpa.transform(g);
		g = t_cc.transform(g);
		g = t_r.transform(g);
		g = t_r2.transform(g);

		Random rand = new Random();
		NodeSorter sorter = new WsnRolesNodeSorter(
				WsnRolesNodeSorter.HS_HB_HC_S_B_C);
		sorter = new GuimeraRolesNodeSorter(
				GuimeraRolesNodeSorter.GH_CH_PH_SC_P_UP_KN);

		System.out.println(sorter.getKey() + ":");
		Resilience.print(sorter.sort(g, rand), g);
		System.out.println();
		// for (int i = 0; i < 30; i++) {
		// System.out.println(Resilience.toString(sorter.sort(g, rand)));
		// }
	}

	private static Network communityNew(int nodes, Transformation[] t) {
		PlacementModel p1 = new CommunityPlacementModel(0.5 * 20, 0.5 * 20,
				false);
		PlacementModel p2 = new CommunityPlacementModel(0.2 * 20, 0.2 * 20,
				false);
		Partitioner partitioner = new SimplePartitioner();
		NodeConnector connector = new UDGConnector(1);
		return new PlacementModelContainer(nodes, nodes / 100, 40, 40, 40, 40,
				p1, p2, partitioner, connector, t);
	}

	private static void print(Node[] nodes, Graph g) {
		// Roles2 roles = (Roles2) g.getProperty("ROLES2_0");
		RoleList roles = (RoleList) g.getProperty("ROLES_0");
		for (int i = 0; i < nodes.length; i++) {
			if (i > 0
					&& roles.getRole(nodes[i].getIndex()) == roles
							.getRole(nodes[i - 1].getIndex())) {
				continue;
			}
			System.out.println("  " + nodes[i] + " @ "
					+ roles.getRole(nodes[i].getIndex()));
			// System.out.println("  " + nodes[i]);
		}
	}

	private static String toString(Node[] nodes) {
		StringBuffer buff = new StringBuffer();
		for (Node n : nodes) {
			buff.append(n.getIndex() + " ");
		}
		return buff.toString();
	}

}
