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
 * MaxRolePercRole.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.communities;

import gtna.communities.Roles.Role;

/**
 * @author Flipp
 * 
 */
public class MaxRolePercRole implements Comparable<MaxRolePercRole> {
	double perc;
	Role maxRole;
	int node;

	/**
	 * @param is
	 * @param countRoles
	 */
	public MaxRolePercRole(int[] is, int countRoles, int node) {
		double max = 0;
		this.node = node;
		for (int i = 0; i < is.length; i++) {
			if (is[i] > max) {
				maxRole = Role.values()[i];
				max = is[i];
			}
		}

		perc = (max / (double) countRoles);
	}

	/**
	 * @return
	 */
	public Role getRole() {
		return maxRole;
	}

	/**
	 * @return
	 */
	public double getValue() {
		return perc;
	}

	public int compareTo(MaxRolePercRole arg0) {
		if (perc < arg0.getValue())
			return -1;
		else if (perc == arg0.getValue()) {
			if (getNode() < arg0.getNode())
				return -1;
			else if (getNode() == arg0.getNode())
				return 0;

			return 1;
		}

		return 1;
	}

	/**
	 * @return
	 */
	private int getNode() {
		return node;
	}

}
