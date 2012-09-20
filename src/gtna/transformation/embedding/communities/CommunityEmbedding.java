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
 * CommunityEmbedding.java
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
import gtna.graph.Node;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.transformation.Transformation;
import gtna.transformation.embedding.communities.partitioner.community.CommunityPartitioner;
import gtna.transformation.embedding.communities.partitioner.idSpace.IdSpacePartitioner;
import gtna.transformation.embedding.communities.sorter.community.CommunitySorter;
import gtna.transformation.embedding.communities.sorter.node.NodeSorter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterListParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author benni
 * 
 */
public class CommunityEmbedding extends Transformation {

	protected CommunitySorter communitySorter;

	protected IdSpacePartitioner idSpacePartitioner;

	protected NodeSorter nodeSorter;

	protected CommunityPartitioner communityPartitioner;

	protected boolean wrapAround;

	/**
	 * @param key
	 */
	public CommunityEmbedding(CommunitySorter communitySorter,
			IdSpacePartitioner idSpacePartitioner, NodeSorter nodeSorter,
			CommunityPartitioner communityPartitioner, boolean wrapAround) {
		super("COMMUNITY_EMBEDDING",
				new Parameter[] {
						new ParameterListParameter("COMMUNITY_SORTER",
								communitySorter),
						new ParameterListParameter("ID_SPACE_PARTITIONER",
								idSpacePartitioner),
						new ParameterListParameter("NODE_SORTER", nodeSorter),
						new ParameterListParameter("COMMUNITY_PARTITIONER",
								communityPartitioner) });
		this.communitySorter = communitySorter;
		this.nodeSorter = nodeSorter;
		this.idSpacePartitioner = idSpacePartitioner;
		this.communityPartitioner = communityPartitioner;
		this.wrapAround = wrapAround;
	}

	@Override
	public Graph transform(Graph g) {
		CommunityList communityList = (CommunityList) g
				.getProperty("COMMUNITIES_0");

		RingPartitionSimple[] partitions = new RingPartitionSimple[g.getNodes().length];
		RingIdentifierSpaceSimple ids = new RingIdentifierSpaceSimple(
				partitions, this.wrapAround);

		// sort communities
		Community[] communities = this.communitySorter.sort(g,
				communityList.getCommunities());

		// compute intervals for communities
		double[][] intervals = this.idSpacePartitioner.getIntervals(ids, g,
				communities);

		// sort nodes (per community)
		int[][] nodes = new int[communities.length][];
		for (int i = 0; i < communities.length; i++) {
			nodes[i] = this.nodeSorter.sort(g, communities, communities[i]);
		}

		// determine position of each node (per community)
		Map<Integer, Double> pos = new HashMap<Integer, Double>();
		for (int i = 0; i < communities.length; i++) {
			double start = intervals[i][0];
			double end = intervals[i][1];
			pos.putAll(this.communityPartitioner.getLocations(g,
					communities[i], nodes[i], start, end));
		}

		for (Node node : g.getNodes()) {
			partitions[node.getIndex()] = new RingPartitionSimple(
					new RingIdentifier(pos.get(node.getIndex()),
							this.wrapAround));
		}

		g.addProperty(g.getNextKey("ID_SPACE"), ids);
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("COMMUNITIES_0");
	}

}
