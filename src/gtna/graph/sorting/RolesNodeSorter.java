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

import gtna.communities.Role;
import gtna.communities.RoleList;
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
public class RolesNodeSorter extends NodeSorter {
	protected String key;

	protected Role[] order;

	protected RoleList roles;

	public RolesNodeSorter(String key, Role[] order) {
		super("ROLES_" + key + "_" + RolesNodeSorter.toString(order));
		this.key = key;
		this.order = order;
	}

	private static String toString(Role[] order) {
		StringBuffer buff = new StringBuffer();
		for (Role r : order) {
			if (buff.length() == 0) {
				buff.append(r.getKey());
			} else {
				buff.append("-" + r.getKey());
			}
		}
		return buff.toString();
	}

	@Override
	public Node[] sort(Graph g, Random rand) {
		Node[] sorted = this.clone(g.getNodes());
		this.roles = (RoleList) g.getProperty("ROLES_" + this.key + "_0");

		RolesAsc asc = new RolesAsc(this.order, this.roles);
		Arrays.sort(sorted, asc);
		this.randomize(sorted, rand);

		return sorted;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("ROLES_" + this.key + "_0");
	}

	@Override
	protected boolean isPropertyEqual(Node n1, Node n2) {
		return this.roles.getRole(n1.getIndex()) == this.roles.getRole(n2
				.getIndex());
	}

	private static class RolesAsc implements Comparator<Node> {
		private HashMap<Role, Integer> map;

		private RoleList roles;

		private RolesAsc(HashMap<Role, Integer> map, RoleList roles) {
			this.map = map;
			this.roles = roles;
		}

		private RolesAsc(Role[] order, RoleList roles) {
			this(RolesAsc.generateMap(order), roles);
		}

		private static HashMap<Role, Integer> generateMap(Role[] order) {
			HashMap<Role, Integer> map = new HashMap<Role, Integer>();
			for (int i = 0; i < order.length; i++) {
				map.put(order[i], i);
			}
			return map;
		}

		public int compare(Node n1, Node n2) {
			Role r1 = this.roles.getRole(n1.getIndex());
			Role r2 = this.roles.getRole(n2.getIndex());
			return this.map.get(r1) - this.map.get(r2);
		}
	}
}
