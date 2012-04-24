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
 * EqualSizeCommunityPartitioner.java
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
package gtna.transformation.embedding.communities.partitioner.community;

import gtna.communities.Community;
import gtna.graph.Graph;

import java.util.HashMap;
import java.util.Map;

/**
 * @author benni
 * 
 */
public class EqualSizeCommunityPartitioner extends CommunityPartitioner {

	public EqualSizeCommunityPartitioner() {
		super("COMMUNITY_PARTITIONER_EQUAL_SIZE");
	}

	@Override
	public Map<Integer, Double> getLocations(Graph g, Community community,
			int[] nodes, double start, double end) {
		Map<Integer, Double> locations = new HashMap<Integer, Double>();

		double step = (end - start) / (double) community.getNodes().length;
		double loc = start;
		for (int node : nodes) {
			locations.put(node, loc);
			loc += step;
		}

		return locations;
	}

}
