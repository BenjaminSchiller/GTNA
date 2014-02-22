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
 * RingIdentifierSpaceSuccessorDistances.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-05: readData, getNodeValueList, getDistributions (Tim Grube)
 *
 */
package gtna.metrics.id;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.DoubleIdentifier;
import gtna.id.DoublePartition;
import gtna.id.Partition;
import gtna.id.ring.RingIdentifierSpace;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.Statistics;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class RingIdentifierSpaceSuccessorDistances extends Metric {

	private int bins;

	private double[][] successorDistanceDistribution;

	private double[][] successorDistanceDistributionCdf;

	public RingIdentifierSpaceSuccessorDistances(int bins) {
		super("RING_IDENTIFIER_SPACE_SUCCESSOR_DISTANCES",
				new Parameter[] { new IntParameter("BINS", bins) });
		this.bins = bins;

		this.successorDistanceDistribution = new double[][] { new double[] {
				-1, -1 } };
		this.successorDistanceDistributionCdf = new double[][] { new double[] {
				-1, -1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		RingIdentifierSpace ids = (RingIdentifierSpace) g
				.getProperty("ID_SPACE_0");
		Partition[] partitions = ids.getPartitions();
		double maxDist = ids.getMaxDistance();

		int[] nodesSorted = SuccessorComparator.getNodesSorted(partitions);

		double[] dist = this.computeSuccessorDistances(g.getNodes(),
				nodesSorted, partitions, maxDist);
		this.successorDistanceDistribution = Statistics.binnedDistribution(
				dist, 0, 1, this.bins);
		this.successorDistanceDistributionCdf = Statistics
				.binnedCdf(this.successorDistanceDistribution);

	}

	private double[] computeSuccessorDistances(Node[] nodes, int[] nodesSorted,
			Partition[] partitions, double maxDist) {
		double[] dist = new double[nodes.length];

		for (int i = 0; i < nodesSorted.length; i++) {
			int n = nodesSorted[i];
			int succ = nodesSorted[(i + 1) % nodesSorted.length];
			dist[i] = ((DoublePartition) partitions[n])
					.distance((DoubleIdentifier) partitions[succ]
							.getRepresentativeIdentifier())
					/ maxDist;
		}

		return dist;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(
				this.successorDistanceDistribution,
				"RING_IDENTIFIER_SPACE_SUCCESSOR_DISTANCES_"
						+ "SUCCESSOR_DISTANCE_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(
				this.successorDistanceDistributionCdf,
				"RING_IDENTIFIER_SPACE_SUCCESSOR_DISTANCES_"
						+ "SUCCESSOR_DISTANCE_CDF", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[0];
	}
	
	@Override
	public Distribution[] getDistributions(){
		return new Distribution[0];
	}

	@Override
	public NodeValueList[] getNodeValueLists(){
		return new NodeValueList[0];
	}

	@Override
	public boolean readData(String folder) {

		
		/* 2D values */
		
		this.successorDistanceDistribution = read2DValues(folder, "RING_IDENTIFIER_SPACE_SUCCESSOR_DISTANCES_"
				+ "SUCCESSOR_DISTANCE_DISTRIBUTION");
		this.successorDistanceDistributionCdf = read2DValues(folder, "RING_IDENTIFIER_SPACE_SUCCESSOR_DISTANCES_"
						+ "SUCCESSOR_DISTANCE_CDF");
		
				
		return true;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ID_SPACE_0")
				&& g.getProperty("ID_SPACE_0") instanceof RingIdentifierSpace;
	}

}
