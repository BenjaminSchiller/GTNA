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
 * CommunityEmbedding1.java
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
 * Assigns IDs at equal distances to nodes, grouped by their community.
 * 
 * @author benni
 * 
 */
public class SimpleCommunityEmbedding1 extends SimpleCommunityEmbedding {

	public SimpleCommunityEmbedding1() {
		super("COMMUNITY_EMBEDDING_1");
	}

	@Override
	public Graph transform(Graph g) {
		RingPartitionSimple[] partitions = new RingPartitionSimple[g.getNodes().length];
		RingIdentifierSpaceSimple idSpace = new RingIdentifierSpaceSimple(
				partitions, this.wrapAround);
		CommunityList cl = (CommunityList) g.getProperty("COMMUNITIES_0");

		int index = 0;
		double step = 1.0 / (double) g.getNodes().length;
		for (Community c : cl.getCommunities()) {
			for (int node : c.getNodes()) {
				double pos = (double) index * step;
				partitions[node] = new RingPartitionSimple(new RingIdentifier(
						pos, this.wrapAround));
				index++;
			}
		}

		g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		return g;
	}

}
