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
 * 2014-02-05: readData, getDistributions, getNodeValueLists (Tim Grube)
 */
package gtna.metrics.id;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
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
public class DIdentifierSpaceDistanceProducts extends Metric {
	private int bins;

	private double[][] neighborDistanceProductDistribution;

	private double[][] neighborDistanceProductDistributionCdf;

	private double[][] neighborDistanceProductRootDistribution;

	private double[][] neighborDistanceProductRootDistributionCdf;

	public DIdentifierSpaceDistanceProducts(int bins) {
		super("D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS",
				new Parameter[] { new DoubleParameter("BINS", bins) });
		this.bins = bins;

		this.neighborDistanceProductDistribution = new double[][] { new double[] {
				-1, -1 } };
		this.neighborDistanceProductDistributionCdf = new double[][] { new double[] {
				-1, -1 } };
		this.neighborDistanceProductRootDistribution = new double[][] { new double[] {
				-1, -1 } };
		this.neighborDistanceProductRootDistributionCdf = new double[][] { new double[] {
				-1, -1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		DoubleIdentifierSpace ids = (DoubleIdentifierSpace) g.getProperty("ID_SPACE_0");
		Partition[] partitions = ids.getPartitions();

		double maxDist = ids.getMaxDistance();
		// double step = 1.0 / (double) this.bins;

		double[] prod = this.computeNeighborDistanceProducts(g.getNodes(),
				partitions, maxDist);

		this.neighborDistanceProductDistribution = Statistics
				.binnedDistribution(prod, 0, 1, this.bins);
		this.neighborDistanceProductDistributionCdf = Statistics
				.binnedCdf(this.neighborDistanceProductDistribution);

		double[] prodRoot = prod.clone();
		for (int i = 0; i < prodRoot.length; i++) {
			prodRoot[i] = Math.pow(prodRoot[i],
					1.0 / (double) g.getNodes()[i].getOutDegree());
		}

		this.neighborDistanceProductRootDistribution = Statistics
				.binnedDistribution(prodRoot, 0, 1, this.bins);
		this.neighborDistanceProductRootDistributionCdf = Statistics
				.binnedCdf(this.neighborDistanceProductRootDistribution);
	}

	private double[] computeNeighborDistanceProducts(Node[] nodes,
			Partition[] partitions, double maxDist) {
		double[] prod = new double[nodes.length];

		for (Node node : nodes) {
			DoublePartition p = (DoublePartition) partitions[node.getIndex()];
			if (node.getOutDegree() > 0) {
				prod[node.getIndex()] = 1;
			}
			for (int out : node.getOutgoingEdges()) {
				prod[node.getIndex()] *= ((DoublePartition) p)
						.distance((DoubleIdentifier) partitions[out]
								.getRepresentativeIdentifier())
						/ maxDist;
			}
		}

		return prod;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(
				this.neighborDistanceProductDistribution,
				"D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_"
						+ "NEIGHBORS_DISTANCE_PRODUCT_DISTRIBUTION", folder);
		success &= DataWriter
				.writeWithoutIndex(
						this.neighborDistanceProductDistributionCdf,
						"D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_"
								+ "NEIGHBORS_DISTANCE_PRODUCT_DISTRIBUTION_CDF",
						folder);
		success &= DataWriter.writeWithoutIndex(
				this.neighborDistanceProductRootDistribution,
				"D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_"
						+ "NEIGHBORS_DISTANCE_PRODUCT_ROOT_DISTRIBUTION",
				folder);
		success &= DataWriter.writeWithoutIndex(
				this.neighborDistanceProductRootDistributionCdf,
				"D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_"
						+ "NEIGHBORS_DISTANCE_PRODUCT_ROOT_DISTRIBUTION_CDF",
				folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[] {};
	}
	
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[]{};
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[]{};
	}
	
	@Override
	public boolean readData(String folder) {
		/* 2D Values */
		
		this.neighborDistanceProductDistribution = read2DValues(folder, "D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_"
						+ "NEIGHBORS_DISTANCE_PRODUCT_DISTRIBUTION");
		this.neighborDistanceProductDistributionCdf = read2DValues(folder, "D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_"
								+ "NEIGHBORS_DISTANCE_PRODUCT_DISTRIBUTION_CDF");
		this.neighborDistanceProductRootDistribution = read2DValues(folder, "D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_"
						+ "NEIGHBORS_DISTANCE_PRODUCT_ROOT_DISTRIBUTION");
		this.neighborDistanceProductRootDistributionCdf = read2DValues(folder, "D_IDENTIFIER_SPACE_DISTANCE_PRODUCTS_"
						+ "NEIGHBORS_DISTANCE_PRODUCT_ROOT_DISTRIBUTION_CDF");
		
		return true;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ID_SPACE_0")
				&& (g.getProperty("ID_SPACE_0") instanceof DoubleIdentifierSpace);
	}

}
