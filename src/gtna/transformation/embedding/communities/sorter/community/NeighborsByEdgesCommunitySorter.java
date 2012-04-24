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
 * NeighborsByEdgesCommunitySorter.java
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
package gtna.transformation.embedding.communities.sorter.community;

import gtna.communities.Community;
import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * @author benni
 * 
 */
public class NeighborsByEdgesCommunitySorter extends CommunitySorter {

	public NeighborsByEdgesCommunitySorter() {
		super("COMMUNITY_SORTER_NEIGHBORS_BY_EDGES");
	}

	@Override
	public Community[] sort(Graph g, Community[] communities) {
		Set<Community> set = new HashSet<Community>();
		for (Community c : communities) {
			set.add(c);
		}

		Community largest = this.getLargestCommunity(set);
		set.remove(largest);

		Community[] sorted = new Community[communities.length];
		sorted[0] = largest;
		for (int i = 1; i < communities.length; i++) {
			sorted[i] = this.getCommunityWithMostEdges(g, sorted[i - 1], set);
			set.remove(sorted[i]);
		}

		return sorted;
	}

	protected Community getLargestCommunity(Set<Community> set) {
		Community max = null;
		for (Community c : set) {
			if (max == null || max.getNodes().length < c.getNodes().length) {
				max = c;
			}
		}
		return max;
	}

	protected Community getCommunityWithMostEdges(Graph g, Community compare,
			Set<Community> set) {
		Community max = null;
		int edges = 0;
		for (Community c : set) {
			if (max == null || this.getEdges(g, compare, c) > edges) {
				max = c;
			}
		}
		return max;
	}

	protected int getEdges(Graph g, Community c1, Community c2) {
		Set<Integer> n2 = new HashSet<Integer>();
		for (int node : c2.getNodes()) {
			n2.add(node);
		}

		int edges = 0;

		for (int nodeIndex : c1.getNodes()) {
			Node node = g.getNode(nodeIndex);

			for (int out : node.getOutgoingEdges()) {
				if (n2.contains(out)) {
					edges++;
				}
			}

			for (int in : node.getIncomingEdges()) {
				if (n2.contains(in)) {
					edges++;
				}
			}
		}

		return edges;
	}

}
