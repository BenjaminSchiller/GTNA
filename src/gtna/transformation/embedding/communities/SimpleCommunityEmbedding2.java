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
 * CommunityEmbedding2.java
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
package gtna.transformation.embedding.communities;

import gtna.communities.Community;
import gtna.communities.CommunityList;
import gtna.graph.Graph;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;

/**
 * Assigns equal-sized partitions of the ID space to each community. In each
 * community, IDs are assigned at equal distance.
 * 
 * @author benni
 * 
 */
public class SimpleCommunityEmbedding2 extends SimpleCommunityEmbedding {

	/**
	 * @param key
	 */
	public SimpleCommunityEmbedding2() {
		super("COMMUNITY_EMBEDDING_2");
	}

	@Override
	public Graph transform(Graph g) {
		RingPartitionSimple[] partitions = new RingPartitionSimple[g.getNodes().length];
		RingIdentifierSpaceSimple idSpace = new RingIdentifierSpaceSimple(
				partitions, this.wrapAround);
		CommunityList cl = (CommunityList) g.getProperty("COMMUNITIES_0");

		int index0 = 0;
		double step0 = 1.0 / (double) cl.getCommunities().length;
		for (Community c : cl.getCommunities()) {
			double start0 = (double) index0 * step0;
			double end0 = (double) (index0 + 1) * step0;

			int index1 = 0;
			double step1 = (end0 - start0) / (double) c.getNodes().length;
			for (int node : c.getNodes()) {
				double pos = start0 + ((double) index1 * step1);
				partitions[node] = new RingPartitionSimple(new RingIdentifier(
						pos, this.wrapAround));

				index1++;
			}

			index0++;
		}

		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		return g;
	}

}
