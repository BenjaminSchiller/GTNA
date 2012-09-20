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
 * LMCNodeSorter.java
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
package gtna.transformation.embedding.communities.sorter.node;

import gtna.communities.Community;
import gtna.graph.Graph;
import gtna.id.Partition;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.transformation.Transformation;
import gtna.transformation.attackableEmbedding.lmc.LMC;
import gtna.transformation.id.RandomRingIDSpaceSimple;
import gtna.util.GraphUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author benni
 * 
 */
public class LmcNodeSorter extends NodeSorter {

	public LmcNodeSorter() {
		super("NODE_SORTER_LMC");
	}

	@Override
	public int[] sort(Graph g, Community[] communities, Community community) {

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

		NodePosition[] positions = new NodePosition[community.getNodes().length];
		index = 0;
		for (int node : community.getNodes()) {
			double pos = ((RingIdentifier) partitions[indexMap.get(node)]
					.getRepresentativeIdentifier()).getPosition();
			positions[index++] = new NodePosition(node, pos);
		}

		Arrays.sort(positions);

		int[] sorted = new int[positions.length];
		for (int i = 0; i < positions.length; i++) {
			sorted[i] = positions[i].index;
		}

		return sorted;
	}

	private class NodePosition implements Comparable<NodePosition> {
		private int index;

		private double pos;

		private NodePosition(int index, double pos) {
			this.index = index;
			this.pos = pos;
		}

		@Override
		public int compareTo(NodePosition o) {
			double dist = this.pos - o.pos;
			if (dist < 0) {
				return -1;
			} else if (dist > 0) {
				return 1;
			} else {
				return 0;
			}
		}

		public String toString() {
			return this.pos + " - " + this.index;
		}
	}

}
