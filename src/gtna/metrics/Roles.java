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
package gtna.metrics;

import gtna.communities.Roles.Role;
import gtna.data.Value;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.Timer;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class Roles extends Metric {
	private Distribution distribution;

	private Timer runtime;

	public Roles() {
		super("ROLES");
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if (!g.hasProperty("ROLES_0")) {
			this.runtime = new Timer();
			this.distribution = new Distribution(new double[] { 0.0 });
			this.runtime.end();
			return;
		}
		this.runtime = new Timer();
		gtna.communities.Roles roles = (gtna.communities.Roles) g
				.getProperty("ROLES_0");
		double[] r = new double[Role.values().length];
		for (Role role : Role.values()) {
			r[gtna.communities.Roles.toIndex(role) - 1] = (double) roles
					.getNodesOfRole(role).size() / (double) g.getNodes().length;
		}
		this.distribution = new Distribution(r);
		this.runtime.end();
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
	public Value[] getValues() {
		Value runtime = new Value("ROLES_RUNTIME", this.runtime.getRuntime());
		return new Value[] { runtime };
	}

}
