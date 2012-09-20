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
 * LmcCommunityPartitioner.java
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
import gtna.id.Partition;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.util.GraphUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author benni
 * 
 */
public class LmcCommunityPartitioner extends CommunityPartitioner {

	public LmcCommunityPartitioner() {
		super("COMMUNITY_PARTITIONER_LMC");
	}

	@Override
	public Map<Integer, Double> getLocations(Graph g, Community community,
			int[] nodes, double start, double end) {

		Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

		int index = 0;
		for (int node : community.getNodes()) {
			indexMap.put(node, index++);
		}

		Graph subgraph = GraphUtils.subgraph(g, indexMap);
		Transformation rids = new RandomRingIDSpaceSimple(true);
		Transformation lmc = new LMC(1000, LMC.MODE_UNRESTRICTED, 0,
				LMC.DELTA_1_N, 0);

		subgraph = rids.transform(subgraph);
		subgraph = lmc.transform(subgraph);

		RingIdentifierSpaceSimple ids = (RingIdentifierSpaceSimple) subgraph
				.getProperty("ID_SPACE_0");
		Partition[] partitions = ids.getPartitions();

		Map<Integer, Double> locations = new HashMap<Integer, Double>();
		double size = end - start;
		for (int node : community.getNodes()) {
			double pos = ((RingIdentifier) partitions[indexMap.get(node)]
					.getRepresentativeIdentifier()).getPosition();
			locations.put(node, start + pos * size);
		}

		return locations;
	}

}
