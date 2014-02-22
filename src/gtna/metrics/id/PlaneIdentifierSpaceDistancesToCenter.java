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
 * PlaneIdentifierSpaceDistancesToCenter.java
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
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
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
public class PlaneIdentifierSpaceDistancesToCenter extends Metric {

	private int bins;

	private double[][] distanceToCenterDistribution;

	private double[][] distanceToCenterDistributionCdf;

	public PlaneIdentifierSpaceDistancesToCenter(int bins) {
		super("PLANE_IDENTIFIER_SPACE_DISTANCES_TO_CENTER",
				new Parameter[] { new IntParameter("BINS", bins) });
		this.bins = bins;

		this.distanceToCenterDistribution = new double[][] { new double[] { -1,
				-1 } };
		this.distanceToCenterDistributionCdf = new double[][] { new double[] {
				-1, -1 } };
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");
		PlaneIdentifierSpaceSimple plane = (PlaneIdentifierSpaceSimple) ids;
		Partition[] partitions = plane.getPartitions();

		PlaneIdentifier center = new PlaneIdentifier(plane.getxModulus() / 2.0,
				plane.getyModulus() / 2.0, plane.getxModulus(),
				plane.getyModulus(), plane.isWrapAround());
		double maxDist = Math.sqrt(Math.pow(plane.getxModulus() / 2.0, 2.0)
				+ Math.pow(plane.getyModulus() / 2.0, 2.0));

		double[] dist = this.computeCenterDistances(g.getNodes(), partitions,
				center, maxDist);

		this.distanceToCenterDistribution = Statistics.binnedDistribution(dist,
				0, 1, this.bins);
		this.distanceToCenterDistributionCdf = Statistics
				.binnedCdf(this.distanceToCenterDistribution);

	}

	private double[] computeCenterDistances(Node[] nodes,
			Partition[] partitions, PlaneIdentifier center, double maxDist) {
		double[] dist = new double[nodes.length];

		for (Node node : nodes) {
			PlanePartitionSimple p = (PlanePartitionSimple) partitions[node
					.getIndex()];
			dist[node.getIndex()] = p.distance(center) / maxDist;
		}

		return dist;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(
				this.distanceToCenterDistribution,
				"PLANE_IDENTIFIER_SPACE_DISTANCES_TO_CENTER_"
						+ "DISTANCE_TO_CENTER_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(
				this.distanceToCenterDistributionCdf,
				"PLANE_IDENTIFIER_SPACE_DISTANCES_TO_CENTER_"
						+ "DISTANCE_TO_CENTER_DISTRIBUTION_CDF", folder);
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
		
		this.distanceToCenterDistribution = read2DValues(folder, "PLANE_IDENTIFIER_SPACE_DISTANCES_TO_CENTER_"
						+ "DISTANCE_TO_CENTER_DISTRIBUTION");
		this.distanceToCenterDistributionCdf = read2DValues(folder, "PLANE_IDENTIFIER_SPACE_DISTANCES_TO_CENTER_"
				+ "DISTANCE_TO_CENTER_DISTRIBUTION_CDF");
		
		return true;
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ID_SPACE_0")
				&& g.getProperty("ID_SPACE_0") instanceof PlaneIdentifierSpaceSimple;
	}

	

}
