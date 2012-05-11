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
 * RolesGraphProperty.java
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
package gtna.communities;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author benni
 * 
 */
public class RoleList implements GraphProperty {
	private Role[] roles;

	public RoleList(Role[] roles) {
		this.roles = roles;
	}

	public RoleList(ArrayList<Role> roles) {
		this.roles = new Role[roles.size()];
		for (int i = 0; i < roles.size(); i++) {
			this.roles[i] = roles.get(i);
		}
	}

	public Role getRole(int index) {
		return this.roles[index];
	}

	public Role[] getRoles() {
		return this.roles;
	}

	public int[] getNodes(Role role) {
		Set<Integer> set = new HashSet<Integer>();
		for (int index = 0; index < this.roles.length; index++) {
			if (this.roles[index].equals(role)) {
				set.add(index);
			}
		}
		int[] nodes = new int[set.size()];
		int index = 0;
		for (int node : set) {
			nodes[index++] = node;
		}
		return nodes;
	}

	@Override
	public boolean write(String filename, String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub

	}

}
