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
 * IdentifierSpaceHopDistances.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-05: readData, getDistributions, getNodeValueLists (Tim Grube)
 *
 */
package gtna.metrics.id;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.Partition;
import gtna.id.ring.RingIdentifierSpace;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.ArrayUtils;
import gtna.util.Distribution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author benni
 * 
 */
public class RingIdentifierSpaceSuccessorHopDistances extends Metric {
	private Distribution successorHopDistanceDistribution;

	public RingIdentifierSpaceSuccessorHopDistances() {
		super("RING_IDENTIFIER_SPACE_SUCCESSOR_HOP_DISTANCES");

		this.successorHopDistanceDistribution = new Distribution("RING_IDENTIFIER_SPACE_SUCCESSOR_HOP_DISTANCES_DISTRIBUTION", 
				new double[] { -1 });
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		RingIdentifierSpace ids = (RingIdentifierSpace) g
				.getProperty("ID_SPACE_0");
		Partition[] partitions = ids.getPartitions();

		int[] nodesSorted = SuccessorComparator.getNodesSorted(partitions);

		this.successorHopDistanceDistribution = this
				.computeSuccessorHopDistanceDistribution(g.getNodes(),
						nodesSorted);
	}

	private int getSuccessor(int node, int[] nodesSorted) {
		return nodesSorted[(node + 1) % nodesSorted.length];
	}

	private int getHopDistance(Node[] nodes, int src, int dst) {
		int[] hop = ArrayUtils.initIntArray(nodes.length, -1);
		hop[src] = 0;
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(src);
		while (!queue.isEmpty()) {
			int current = queue.poll();
			for (int out : nodes[current].getOutgoingEdges()) {
				if (hop[out] >= 0) {
					continue;
				}
				hop[out] = hop[current] + 1;
				queue.add(out);
				if (out == dst) {
					return hop[out];
				}
			}
		}
		return 0;
	}

	private int[] getSuccessorHopDistances(Node[] nodes, int[] nodesSorted) {
		int[] successorHopDistance = new int[nodes.length];
		for (int i = 0; i < nodesSorted.length; i++) {
			int node = nodesSorted[i];
			int succ = this.getSuccessor(i, nodesSorted);
			successorHopDistance[node] = this.getHopDistance(nodes, node, succ);
		}
		return successorHopDistance;
	}

	private Distribution computeSuccessorHopDistanceDistribution(Node[] nodes,
			int[] nodesSorted) {
		int[] successorHopDistances = this.getSuccessorHopDistances(nodes,
				nodesSorted);
		int max = ArrayUtils.getMaxInt(successorHopDistances);
		double[] distr = new double[max + 1];
		for (int v : successorHopDistances) {
			distr[v]++;
		}

		ArrayUtils.divide(distr, nodes.length);
		return new Distribution("RING_IDENTIFIER_SPACE_SUCCESSOR_HOP_DISTANCES_DISTRIBUTION", distr);
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(
				this.successorHopDistanceDistribution.getDistribution(),
				"RING_IDENTIFIER_SPACE_SUCCESSOR_HOP_DISTANCES_DISTRIBUTION",
				folder);
		success &= DataWriter.writeWithoutIndex(
				this.successorHopDistanceDistribution.getCdf(),
				"RING_IDENTIFIER_SPACE_SUCCESSOR_HOP_DISTANCES_CDF", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[0];
	}
	
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[]{successorHopDistanceDistribution};
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[0];
	}
	
	@Override
	public boolean readData(String folder) {
		
		/* Distribution */
		
		this.successorHopDistanceDistribution = new Distribution("RING_IDENTIFIER_SPACE_SUCCESSOR_HOP_DISTANCES_DISTRIBUTION", readDistribution(folder, "RING_IDENTIFIER_SPACE_SUCCESSOR_HOP_DISTANCES_DISTRIBUTION"));
		
		
		return true;
	}
	
	

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ID_SPACE_0")
				&& g.getProperty("ID_SPACE_0") instanceof RingIdentifierSpace;
	}

	

}
