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
package gtna.metrics.communities;

import gtna.communities.Role;
import gtna.communities.Role.RoleType;
import gtna.communities.RolesGraphProperty;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class Roles extends Metric {
	private RoleType type;

	private Distribution distribution;

	public Roles(RoleType type) {
		super("ROLES", new Parameter[] { new StringParameter("TYPE",
				type.toString()) });
		this.type = type;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ROLES_" + this.type.toString() + "_0");
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if (!g.hasProperty("ROLES_" + this.type.toString() + "_0")) {
			this.distribution = new Distribution(new double[] { 0.0 });
			return;
		}
		RolesGraphProperty roles = (RolesGraphProperty) g.getProperty("ROLES_"
				+ this.type.toString() + "_0");
		Role[] types = roles.getRoles()[0].getRoleTypes();
		double[] r = new double[types.length];
		for (Role role : types) {
			r[role.toIndex()] = (double) roles.getNodes(role).length
					/ (double) g.getNodes().length;
		}
		this.distribution = new Distribution(r);
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.distribution.getDistribution(), "ROLES_DISTRIBUTION",
				folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[0];
	}

}
