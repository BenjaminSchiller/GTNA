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
 *
 */
package gtna.metrics.id;

import gtna.data.Single;
import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.DIdentifierSpace;
import gtna.id.Partition;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.ArrayUtils;
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

	private double[][] nodesDistanceDistribution;

	private double[][] nodesDistanceDistributionCdf;

	private double[][] edgesDistanceDistribution;

	private double[][] edgesDistanceDistributionCdf;

	private static boolean computeNodeDistances = false;

	public DIdentifierSpaceDistances(int bins) {
		super("D_IDENTIFIER_SPACE_DISTANCES",
				new Parameter[] { new DoubleParameter("BINS", bins) });
		this.bins = bins;

		this.nodesDistanceDistribution = new double[][] { new double[] { -1, -1 } };
		this.nodesDistanceDistributionCdf = new double[][] { new double[] { -1,
				-1 } };
		this.edgesDistanceDistribution = new double[][] { new double[] { -1, -1 } };
		this.edgesDistanceDistributionCdf = new double[][] { new double[] { -1,
				-1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		DIdentifierSpace ids = (DIdentifierSpace) g.getProperty("ID_SPACE_0");
		Partition<Double>[] partitions = ids.getPartitions();
		Edges edges = g.getEdges();

		double maxDist = ids.getMaxDistance();
		double step = maxDist / (double) this.bins;

		if (DIdentifierSpaceDistances.computeNodeDistances) {
			double[][] nodeDistancesBinned = this.computeNodeDistancesBinned(
					g.getNodes(), partitions, maxDist, step, this.bins);
			this.nodesDistanceDistribution = new double[this.bins][2];
			for (int i = 0; i < this.bins; i++) {
				this.nodesDistanceDistribution[i][0] = (double) i * step;
				this.nodesDistanceDistribution[i][1] = nodeDistancesBinned[i].length;
			}
			ArrayUtils.divide(this.nodesDistanceDistribution, 1,
					nodeDistancesBinned.length);

			this.nodesDistanceDistributionCdf = new double[this.nodesDistanceDistribution.length][2];
			this.nodesDistanceDistributionCdf[0][0] = this.nodesDistanceDistribution[0][0];
			this.nodesDistanceDistributionCdf[0][1] = this.nodesDistanceDistribution[0][1];
			for (int i = 1; i < this.nodesDistanceDistributionCdf.length; i++) {
				this.nodesDistanceDistributionCdf[i][0] = this.nodesDistanceDistribution[i][0];
				this.nodesDistanceDistributionCdf[i][1] = this.nodesDistanceDistributionCdf[i - 1][1]
						+ this.nodesDistanceDistribution[i][1];
			}
		} else {
			this.nodesDistanceDistribution = new double[][] { new double[] { 0,
					0 } };
			this.nodesDistanceDistributionCdf = new double[][] { new double[] {
					0, 0 } };
		}

		double[] edgeDistances = this.computeEdgeDistances(edges, partitions);
		double[][] edgeDistancesBinned = Statistics.binning(edgeDistances, 0,
				maxDist, step);
		this.edgesDistanceDistribution = new double[this.bins][2];
		for (int i = 0; i < this.bins; i++) {
			this.edgesDistanceDistribution[i][0] = (double) i * step;
			this.edgesDistanceDistribution[i][1] = edgeDistancesBinned[i].length;
		}
		ArrayUtils.divide(this.edgesDistanceDistribution, 1,
				edgeDistances.length);

		this.edgesDistanceDistributionCdf = new double[this.edgesDistanceDistribution.length][2];
		this.edgesDistanceDistributionCdf[0][0] = this.edgesDistanceDistribution[0][0];
		this.edgesDistanceDistributionCdf[0][1] = this.edgesDistanceDistribution[0][1];
		for (int i = 1; i < this.edgesDistanceDistributionCdf.length; i++) {
			this.edgesDistanceDistributionCdf[i][0] = this.edgesDistanceDistribution[i][0];
			this.edgesDistanceDistributionCdf[i][1] = this.edgesDistanceDistributionCdf[i - 1][1]
					+ this.edgesDistanceDistribution[i][1];
		}
	}

	private double[][] computeNodeDistancesBinned(Node[] nodes,
			Partition<Double>[] partitions, double maxDist, double step,
			int bins) {
		double[][] binned = new double[bins][2];
		for (Node src : nodes) {
			int index = 0;
			double[] dist = new double[nodes.length - 1];
			for (Node dst : nodes) {
				if (src.getIndex() == dst.getIndex()) {
					continue;
				}
				dist[index++] = partitions[src.getIndex()]
						.distance(partitions[dst.getIndex()]
								.getRepresentativeID());
			}
			double[][] binned1 = Statistics.binning(dist, 0, maxDist, bins);
			for (int i = 0; i < binned1.length; i++) {
				binned[i][0] = binned1[i][0];
				binned[i][1] += binned1[i][1];
			}
		}
		return binned;
	}

	private double[] computeEdgeDistances(Edges edges,
			Partition<Double>[] partitions) {
		double[] dist = new double[edges.getEdges().size()];
		int index = 0;
		for (Edge edge : edges.getEdges()) {
			dist[index++] = partitions[edge.getSrc()].distance(partitions[edge
					.getDst()].getRepresentativeID());
		}
		return dist;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.nodesDistanceDistribution,
				"D_IDENTIFIER_SPACE_DISTANCES_NODES_DISTANCE_DISTRIBUTION",
				folder);
		success &= DataWriter.writeWithoutIndex(
				this.nodesDistanceDistributionCdf,
				"D_IDENTIFIER_SPACE_DISTANCES_NODES_DISTANCE_DISTRIBUTION_CDF",
				folder);
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
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ID_SPACE_0")
				&& (g.getProperty("ID_SPACE_0") instanceof DIdentifierSpace);
	}

}
