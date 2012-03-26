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
import gtna.data.Single;
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
public class Communities extends Metric {
	private Distribution communitySize;

	private Timer runtime;

	private double modularity;

	private double communities;

	public Communities() {
		super("COMMUNITIES");
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
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
		this.modularity = this.calculateModularity(g, communities);
		this.communities = communities.getCommunities().length;
		this.runtime.end();

	}

	private double calculateModularity(Graph g,
			gtna.communities.Communities communities) {
		double E = g.getEdges().size();
		double Q = 0;
		for (Community c : communities.getCommunities()) {
			double IC = 0;
			double OC = 0;
			for (int node : c.getNodes()) {
				for (int out : g.getNode(node).getOutgoingEdges()) {
					if (communities.getCommunityOfNode(out).getIndex() == c
							.getIndex()) {
						IC++;
					} else {
						OC++;
					}
				}
				for (int in : g.getNode(node).getIncomingEdges()) {
					if (communities.getCommunityOfNode(in).getIndex() != c
							.getIndex()) {
						OC++;
					}
				}
			}
			Q += IC / E - Math.pow((IC + OC) / E, 2);
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
	public Single[] getSingles() {
		Single mod = new Single("COMMUNITIES_MODULARITY", modularity);
		Single com = new Single("COMMUNITIES_COMMUNITIES", this.communities);
		Single size_min = new Single("COMMUNITIES_COMMUNITY_SIZE_MIN",
				this.communitySize.getMin());
		Single size_med = new Single("COMMUNITIES_COMMUNITY_SIZE_MED",
				this.communitySize.getMedian());
		Single size_avg = new Single("COMMUNITIES_COMMUNITY_SIZE_AVG",
				this.communitySize.getAverage());
		Single size_max = new Single("COMMUNITIES_COMMUNITY_SIZE_MAX",
				this.communitySize.getMax());
		Single rt = new Single("COMMUNITIES_RUNTIME", this.runtime.getRuntime());
		return new Single[] { mod, com, size_min, size_med, size_avg, size_max,
				rt };
	}

}
