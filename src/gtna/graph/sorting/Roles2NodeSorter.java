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
 * RolesNodeSorter.java
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
package gtna.graph.sorting;

import gtna.communities.Roles2;
import gtna.communities.Roles2.Role2;
import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class Roles2NodeSorter extends NodeSorter {
	private Roles2 roles;

	private Role2[] order;

	public static final Role2[] HS_S_HB_B_HC_C = new Role2[] { Role2.HUB_STAR,
			Role2.STAR, Role2.HUB_BRIDGE, Role2.BRIDGE, Role2.HUB_COMMON,
			Role2.COMMON };
	public static final Role2[] HS_HB_HC_S_B_C = new Role2[] { Role2.HUB_STAR,
			Role2.HUB_BRIDGE, Role2.HUB_COMMON, Role2.STAR, Role2.BRIDGE,
			Role2.COMMON };
	public static final Role2[] HS_HB_S_B_HC_C = new Role2[] { Role2.HUB_STAR,
			Role2.HUB_BRIDGE, Role2.STAR, Role2.BRIDGE, Role2.HUB_COMMON,
			Role2.COMMON };

	public Roles2NodeSorter(Role2[] order) {
		super("ROLES2_" + Roles2NodeSorter.toString(order));
		this.order = order;
	}

	private static String toString(Role2[] order) {
		StringBuffer buff = new StringBuffer();
		for (Role2 r : order) {
			if (buff.length() == 0) {
				buff.append(Roles2.toString(r));
			} else {
				buff.append("-" + Roles2.toString(r));
			}
		}
		return buff.toString();
	}

	@Override
	public Node[] sort(Graph g, Random rand) {
		Node[] sorted = this.clone(g.getNodes());
		this.roles = (Roles2) g.getProperty("ROLES2_0");

		Roles2Asc asc = new Roles2Asc(this.order, this.roles);
		Arrays.sort(sorted, asc);
		this.randomize(sorted, rand);

		return sorted;
	}

	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		return this.roles.getRoleOfNode(n1.getIndex()) == this.roles
				.getRoleOfNode(n2.getIndex());
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("ROLES2_0");
	}

	private static class Roles2Asc implements Comparator<Node> {
		private HashMap<Role2, Integer> map;

		private Roles2 roles;

		private Roles2Asc(HashMap<Role2, Integer> map, Roles2 roles) {
			this.map = map;
			this.roles = roles;
		}

		private Roles2Asc(Role2[] order, Roles2 roles) {
			this(Roles2Asc.generateMap(order), roles);
		}

		private static HashMap<Role2, Integer> generateMap(Role2[] order) {
			HashMap<Role2, Integer> map = new HashMap<Role2, Integer>();
			for (int i = 0; i < order.length; i++) {
				map.put(order[i], i);
			}
			return map;
		}

		public int compare(Node n1, Node n2) {
			Role2 r1 = this.roles.getRoleOfNode(n1.getIndex());
			Role2 r2 = this.roles.getRoleOfNode(n2.getIndex());
			return this.map.get(r1) - this.map.get(r2);
		}
	}

}
