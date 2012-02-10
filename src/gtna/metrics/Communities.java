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

import gtna.communities.Community;
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

	private double modularity;

	public Communities() {
		super("COMMUNITIES");
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		if (!g.hasProperty("COMMUNITIES_0")) {
			this.runtime = new Timer();
			this.communitySize = new Distribution(new double[] { 0.0 });
			this.runtime.end();
			return;
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
		modularity = this.calculateModularity(g, communities);
		this.runtime.end();

	}

	private double calculateModularity(Graph g,
			gtna.communities.Communities communities) {
		double E = 0;
		for (Community c : communities.getCommunities()) {
			for (int aktNode : c.getNodes()) {
				E += g.getNode(aktNode).getOutDegree();
			}
		}
		double Q = 0;
		for (Community c : communities.getCommunities()) {
			double IC = 0;
			double OC = 0;
			for (int src : c.getNodes()) {
				for (int dst : g.getNode(src).getOutgoingEdges()) {
					if (communities.getCommunityOfNode(dst) == c) {
						IC++;
					} else {
						OC++;
					}
				}
			}
			Q += IC / E - Math.pow((2 * IC + OC) / (2 * E), 2);
		}
		return Q;
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
		Value mod = new Value("COMMUNITIES_MODULARITY", modularity);
		Value com = new Value("COMMUNITIES_COMMUNITIES",
				this.communitySize.getDistribution().length);
		Value size_min = new Value("COMMUNITIES_COMMUNITY_SIZE_MIN",
				this.communitySize.getMin());
		Value size_med = new Value("COMMUNITIES_COMMUNITY_SIZE_MED",
				this.communitySize.getMedian());
		Value size_avg = new Value("COMMUNITIES_COMMUNITY_SIZE_AVG",
				this.communitySize.getAverage());
		Value size_max = new Value("COMMUNITIES_COMMUNITY_SIZE_MAX",
				this.communitySize.getMax());
		Value rt = new Value("COMMUNITIES_RUNTIME", this.runtime.getRuntime());
		return new Value[] { mod, com, size_min, size_med, size_avg, size_max, rt };
	}

}
