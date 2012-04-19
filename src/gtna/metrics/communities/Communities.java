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
package gtna.metrics.communities;

import gtna.communities.Community;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.ArrayUtils;
import gtna.util.Distribution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author benni
 * 
 */
public class Communities extends Metric {
	private double[] communitySize;

	private double[] communitySizeFraction;

	private Distribution sizeDistribution;

	private Distribution adjacentCommunities;

	private double modularity;

	private double communityCount;

	public Communities() {
		super("COMMUNITIES");
		this.communitySize = new double[] { -1 };
		this.sizeDistribution = new Distribution(new double[] { -1 });
		this.adjacentCommunities = new Distribution(new double[] { -1 });
		this.modularity = -1;
		this.communityCount = -1;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("COMMUNITIES_0");
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		gtna.communities.Communities communities = (gtna.communities.Communities) g
				.getProperty("COMMUNITIES_0");

		this.communitySize = this.computeCommunitySize(communities);
		this.communitySizeFraction = this.communitySize.clone();
		ArrayUtils.divide(this.communitySizeFraction, g.getNodes().length);

		this.sizeDistribution = this.computeSizeDistribution(communities);

		this.adjacentCommunities = this.computeAdjacentCommunities(g,
				communities);

		this.modularity = this.calculateModularity(g, communities);
		this.communityCount = communities.getCommunities().length;
	}

	/**
	 * Computes the list of community sizes ordered descending.
	 * 
	 * @param communities
	 *            communityList
	 * @return list of community sizes, ordered descending
	 */
	private double[] computeCommunitySize(
			gtna.communities.Communities communities) {
		double[] size = new double[communities.getCommunities().length];
		for (int i = 0; i < communities.getCommunities().length; i++) {
			size[i] = communities.getCommunities()[i].getNodes().length;
		}
		Arrays.sort(size);
		ArrayUtils.reverse(size);
		return size;
	}

	/**
	 * Computes the distribution of community sizes.
	 * 
	 * @param communities
	 *            communityList
	 * @return distribution of community sizes
	 */
	private Distribution computeSizeDistribution(
			gtna.communities.Communities communities) {
		int max = 0;
		for (Community c : communities.getCommunities()) {
			max = Math.max(max, c.getNodes().length);
		}

		double[] sd = new double[max + 1];
		for (Community c : communities.getCommunities()) {
			sd[c.getNodes().length]++;
		}

		for (int i = 0; i < sd.length; i++) {
			sd[i] /= communities.getCommunities().length;
		}

		return new Distribution(sd);
	}

	/**
	 * Computes the distribution of adjacent communities in a given graph for a
	 * given communityList.
	 * 
	 * @param g
	 *            graph
	 * @param communities
	 *            communityList
	 * @return distribution of adjacent communities
	 */
	private Distribution computeAdjacentCommunities(Graph g,
			gtna.communities.Communities communities) {
		int[] nac = new int[g.getNodes().length];
		int max = 0;
		for (Node node : g.getNodes()) {
			nac[node.getIndex()] = this.getAdjacentCommunities(node, g,
					communities);
			max = Math.max(max, nac[node.getIndex()]);
		}
		double[] ac = new double[max + 1];
		for (int i = 0; i < nac.length; i++) {
			ac[nac[i]]++;
		}
		for (int i = 0; i < ac.length; i++) {
			ac[i] = ac[i] / (double) g.getNodes().length;
		}
		return new Distribution(ac);
	}

	/**
	 * Computes the number of communities adjacent to the given node, i.e., the
	 * number of communities reachable through the node's outgoing edges.
	 * Adjacent communities includes the node's own communits, hence
	 * adjacentComunities >= 1.
	 * 
	 * @param node
	 *            node index
	 * @param g
	 *            graph
	 * @param communities
	 *            communityList
	 * @return number of communiities adjacent to the given node
	 */
	private int getAdjacentCommunities(Node node, Graph g,
			gtna.communities.Communities communities) {
		Set<Integer> ac = new HashSet<Integer>();
		ac.add(communities.getCommunityOfNode(node.getIndex()).getIndex());
		for (int out : node.getOutgoingEdges()) {
			ac.add(communities.getCommunityOfNode(out).getIndex());
		}
		return ac.size();
	}

	/**
	 * Computes the modularity of the given CommunityList
	 * 
	 * @param g
	 *            graph
	 * @param communities
	 *            communityList
	 * @return modularity of the given CommunityList
	 */
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

		success &= DataWriter.writeWithIndex(this.communitySize,
				"COMMUNITIES_COMMUNITY_SIZE", folder);
		success &= DataWriter.writeWithIndex(this.communitySizeFraction,
				"COMMUNITIES_COMMUNITY_SIZE_FRACTION", folder);

		success &= DataWriter.writeWithIndex(
				this.sizeDistribution.getDistribution(),
				"COMMUNITIES_SIZE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(this.sizeDistribution.getCdf(),
				"COMMUNITIES_SIZE_DISTRIBUTION_CDF", folder);

		success &= DataWriter.writeWithIndex(
				this.adjacentCommunities.getDistribution(),
				"COMMUNITIES_ADJACENT_COMMUNITIES", folder);
		success &= DataWriter.writeWithIndex(this.adjacentCommunities.getCdf(),
				"COMMUNITIES_ADJACENT_COMMUNITIES_CDF", folder);

		return success;
	}

	@Override
	public Single[] getSingles() {
		Single mod = new Single("COMMUNITIES_MODULARITY", modularity);
		Single com = new Single("COMMUNITIES_COMMUNITY_COUNT",
				this.communityCount);

		Single size_min = new Single("COMMUNITIES_COMMUNITY_SIZE_MIN",
				this.sizeDistribution.getMin());
		Single size_med = new Single("COMMUNITIES_COMMUNITY_SIZE_MED",
				this.sizeDistribution.getMedian());
		Single size_avg = new Single("COMMUNITIES_COMMUNITY_SIZE_AVG",
				this.sizeDistribution.getAverage());
		Single size_max = new Single("COMMUNITIES_COMMUNITY_SIZE_MAX",
				this.sizeDistribution.getMax());

		Single adjacent_min = new Single(
				"COMMUNITIES_ADJACENT_COMMUNITIES_MIN",
				this.adjacentCommunities.getMin());
		Single adjacent_med = new Single(
				"COMMUNITIES_ADJACENT_COMMUNITIES_MED",
				this.adjacentCommunities.getMedian());
		Single adjacent_avg = new Single(
				"COMMUNITIES_ADJACENT_COMMUNITIES_AVG",
				this.adjacentCommunities.getAverage());
		Single adjacent_max = new Single(
				"COMMUNITIES_ADJACENT_COMMUNITIES_MAX",
				this.adjacentCommunities.getMax());

		return new Single[] { mod, com, size_min, size_med, size_avg, size_max,
				adjacent_min, adjacent_med, adjacent_avg, adjacent_max };
	}

}
