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
 * MaxRolePercRole2.java
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

import gtna.communities.Roles2.Role2;

/**
 * @author Flipp
 * 
 */
public class MaxRolePercRole2 implements Comparable<MaxRolePercRole2> {
	double perc;
	Role2 maxRole;
	int node;

	/**
	 * @param is
	 * @param countRoles
	 * @param i2
	 */
	public MaxRolePercRole2(int[] is, int countRoles, int i2) {
		double max = 0;
		node = i2;
		for (int i = 0; i < is.length; i++) {
			if (is[i] > max) {
				maxRole = Role2.values()[i];
				max = is[i];
			}
		}

		perc = max / (double) countRoles;
	}

	/**
	 * @return
	 */
	public Role2 getRole() {
		return maxRole;
	}

	/**
	 * @return
	 */
	public double getValue() {
		return perc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(MaxRolePercRole2 arg0) {
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
