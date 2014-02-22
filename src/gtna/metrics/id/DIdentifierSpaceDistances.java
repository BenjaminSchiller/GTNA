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
 * DistanceDistribution.java
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
 */
package gtna.metrics.id;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.id.DoubleIdentifier;
import gtna.id.DoubleIdentifierSpace;
import gtna.id.DoublePartition;
import gtna.id.Partition;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;
import gtna.util.Statistics;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class DIdentifierSpaceDistances extends Metric {
	private int bins;

	private double[][] edgesDistanceDistribution;

	private double[][] edgesDistanceDistributionCdf;

	public DIdentifierSpaceDistances(int bins) {
		super("D_IDENTIFIER_SPACE_DISTANCES",
				new Parameter[] { new DoubleParameter("BINS", bins) });
		this.bins = bins;

		this.edgesDistanceDistribution = new double[][] { new double[] { -1, -1 } };
		this.edgesDistanceDistributionCdf = new double[][] { new double[] { -1,
				-1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		DoubleIdentifierSpace ids = (DoubleIdentifierSpace) g.getProperty("ID_SPACE_0");
		Partition[] partitions = ids.getPartitions();
		Edges edges = g.getEdges();

		double maxDist = ids.getMaxDistance();

		double[] edgeDistances = this.computeEdgeDistances(edges, partitions,
				maxDist);
		this.edgesDistanceDistribution = Statistics.binnedDistribution(
				edgeDistances, 0, 1, this.bins);
		this.edgesDistanceDistributionCdf = Statistics
				.binnedCdf(this.edgesDistanceDistribution);

	}

	private double[] computeEdgeDistances(Edges edges, Partition[] partitions,
			double maxDist) {
		double[] dist = new double[edges.getEdges().size()];
		int index = 0;
		for (Edge edge : edges.getEdges()) {
			dist[index++] = ((DoublePartition) partitions[edge.getSrc()])
					.distance((DoubleIdentifier) partitions[edge.getDst()]
							.getRepresentativeIdentifier())
					/ maxDist;
		}
		return dist;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.edgesDistanceDistribution,
				"D_IDENTIFIER_SPACE_DISTANCES_EDGES_DISTANCE_DISTRIBUTION",
				folder);
		success &= DataWriter.writeWithoutIndex(
				this.edgesDistanceDistributionCdf,
				"D_IDENTIFIER_SPACE_DISTANCES_EDGES_DISTANCE_DISTRIBUTION_CDF",
				folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[] {};
	}
	
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] {};
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {};
	}
	
	@Override
	public boolean readData(String folder) {
		/* 2D Values */
		
		this.edgesDistanceDistribution = read2DValues(folder, "D_IDENTIFIER_SPACE_DISTANCES_EDGES_DISTANCE_DISTRIBUTION");
		this.edgesDistanceDistributionCdf = read2DValues(folder, "D_IDENTIFIER_SPACE_DISTANCES_EDGES_DISTANCE_DISTRIBUTION_CDF");
		
		return true;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ID_SPACE_0")
				&& (g.getProperty("ID_SPACE_0") instanceof DoubleIdentifierSpace);
	}

}
