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
 * Communities.java
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

import gtna.data.Value;
import gtna.graph.Graph;
import gtna.io.DataWriter;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.Timer;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class Communities extends MetricImpl implements Metric {
	private Distribution communitySize;

	private Timer runtime;

	public Communities() {
		super("COMMUNITIES");
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if (!g.hasProperty("COMMUNITIES_0")) {
			this.runtime = new Timer();
			this.communitySize = new Distribution(new double[] { 0.0 });
			this.runtime.end();
		}
		this.runtime = new Timer();
		gtna.communities.Communities communities = (gtna.communities.Communities) g
				.getProperty("COMMUNITIES_0");
		double[] c = new double[communities.getCommunities().length];
		for (int i = 0; i < c.length; i++) {
			c[i] = (double) communities.getCommunities()[i].getNodes().length
					/ (double) g.getNodes().length;
		}
		Arrays.sort(c);
		for (int i = 0; i < c.length / 2; i++) {
			double temp = c[i];
			c[i] = c[c.length - i - 1];
			c[c.length - i - 1] = temp;
		}
		this.communitySize = new Distribution(c);
		this.runtime.end();
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.communitySize.getDistribution(), "COMMUNITIES_SIZE",
				folder);
		success &= DataWriter.writeWithIndex(this.communitySize.getCdf(),
				"COMMUNITIES_SIZE_CDF", folder);
		return success;
	}

	@Override
	public Value[] getValues() {
		return new Value[] {};
	}

}
