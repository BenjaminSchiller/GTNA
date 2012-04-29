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
 * DIdentifierSpaceDistanceProducts.java
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
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.DIdentifierSpace;
import gtna.id.DPartition;
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
public class DIdentifierSpaceDistanceProducts extends Metric {
	private int bins;

	private double[][] neighborDistanceProductDistribution;

	private double[][] neighborDistanceProductDistributionCdf;

	private double[][] neighborDistanceProductSqrtDistribution;

	private double[][] neighborDistanceProductSqrtDistributionCdf;

	public DIdentifierSpaceDistanceProducts(int bins) {
		super("D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS",
				new Parameter[] { new DoubleParameter("BINS", bins) });
		this.bins = bins;

		this.neighborDistanceProductDistribution = new double[][] { new double[] {
				-1, -1 } };
		this.neighborDistanceProductDistributionCdf = new double[][] { new double[] {
				-1, -1 } };
		this.neighborDistanceProductSqrtDistribution = new double[][] { new double[] {
				-1, -1 } };
		this.neighborDistanceProductSqrtDistributionCdf = new double[][] { new double[] {
				-1, -1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		DIdentifierSpace ids = (DIdentifierSpace) g.getProperty("ID_SPACE_0");
		Partition<Double>[] partitions = ids.getPartitions();

		double maxDist = ids.getMaxDistance();
		double step = 1.0 / (double) this.bins;

		double[] prod = this.computeNeighborDistanceProducts(g.getNodes(),
				partitions, maxDist);
		double[][] prodBinned = Statistics.binning(prod, 0, 1, step);

		this.neighborDistanceProductDistribution = new double[this.bins][2];
		this.neighborDistanceProductDistribution = new double[this.bins][2];
		for (int i = 0; i < this.bins; i++) {
			this.neighborDistanceProductDistribution[i][0] = (double) i * step;
			this.neighborDistanceProductDistribution[i][1] = prodBinned[i].length;
		}
		ArrayUtils.divide(this.neighborDistanceProductDistribution, 1,
				g.getNodes().length);

		this.neighborDistanceProductDistributionCdf = new double[this.bins][2];
		this.neighborDistanceProductDistributionCdf[0][0] = this.neighborDistanceProductDistribution[0][0];
		this.neighborDistanceProductDistributionCdf[0][1] = this.neighborDistanceProductDistribution[0][1];
		for (int i = 1; i < this.neighborDistanceProductDistributionCdf.length; i++) {
			this.neighborDistanceProductDistributionCdf[i][0] = this.neighborDistanceProductDistribution[i][0];
			this.neighborDistanceProductDistributionCdf[i][1] = this.neighborDistanceProductDistributionCdf[i - 1][1]
					+ this.neighborDistanceProductDistribution[i][1];
		}

		double[] prodSqrt = prod.clone();
		for (int i = 0; i < prodSqrt.length; i++) {
			prodSqrt[i] = Math.sqrt(prodSqrt[i]);
		}
		double[][] prodSqrtBinned = Statistics.binning(prodSqrt, 0, 1, step);

		this.neighborDistanceProductSqrtDistribution = new double[this.bins][2];
		this.neighborDistanceProductSqrtDistribution = new double[this.bins][2];
		for (int i = 0; i < this.bins; i++) {
			this.neighborDistanceProductSqrtDistribution[i][0] = (double) i
					* step;
			this.neighborDistanceProductSqrtDistribution[i][1] = prodSqrtBinned[i].length
					/ (double) g.getNodes().length;
		}

		this.neighborDistanceProductSqrtDistributionCdf = new double[this.bins][2];
		this.neighborDistanceProductSqrtDistributionCdf[0][0] = this.neighborDistanceProductSqrtDistribution[0][0];
		this.neighborDistanceProductSqrtDistributionCdf[0][1] = this.neighborDistanceProductSqrtDistribution[0][1];
		for (int i = 1; i < this.neighborDistanceProductSqrtDistributionCdf.length; i++) {
			this.neighborDistanceProductSqrtDistributionCdf[i][0] = this.neighborDistanceProductSqrtDistribution[i][0];
			this.neighborDistanceProductSqrtDistributionCdf[i][1] = this.neighborDistanceProductSqrtDistributionCdf[i - 1][1]
					+ this.neighborDistanceProductSqrtDistribution[i][1];
		}
	}

	private double[] computeNeighborDistanceProducts(Node[] nodes,
			Partition<Double>[] partitions, double maxDist) {
		double[] prod = new double[nodes.length];

		for (Node node : nodes) {
			DPartition p = (DPartition) partitions[node.getIndex()];
			if (node.getOutDegree() > 0) {
				prod[node.getIndex()] = 1;
			}
			for (int out : node.getOutgoingEdges()) {
				prod[node.getIndex()] *= p.distance(partitions[out]
						.getRepresentativeID()) / maxDist;
			}
			if (node.getOutDegree() > 0) {
				prod[node.getIndex()] /= (double) node.getOutDegree();
			}
		}

		return prod;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter
				.writeWithoutIndex(
						this.neighborDistanceProductDistribution,
						"D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_NEIGHBORS_DISTANCE_PRODUCT_DISTRIBUTION",
						folder);
		success &= DataWriter
				.writeWithoutIndex(
						this.neighborDistanceProductDistributionCdf,
						"D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_NEIGHBORS_DISTANCE_PRODUCT_DISTRIBUTION_CDF",
						folder);
		success &= DataWriter
				.writeWithoutIndex(
						this.neighborDistanceProductSqrtDistribution,
						"D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_NEIGHBORS_DISTANCE_PRODUCT_SQRT_DISTRIBUTION",
						folder);
		success &= DataWriter
				.writeWithoutIndex(
						this.neighborDistanceProductSqrtDistributionCdf,
						"D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_NEIGHBORS_DISTANCE_PRODUCT_SQRT_DISTRIBUTION_CDF",
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
