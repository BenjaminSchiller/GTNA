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
import gtna.util.Distribution;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class IdentifierSpaceDistances extends Metric {
	private double bucketSize;

	private Distribution allDistribution;

	private Distribution edgesDistribution;

	public IdentifierSpaceDistances(double bucketSize) {
		super(
				"IDENTIFIER_SPACE_DISTANCES",
				new Parameter[] { new DoubleParameter("BUCKET_SIZE", bucketSize) });
		this.bucketSize = bucketSize;
	}

	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		DIdentifierSpace ids = (DIdentifierSpace) g.getProperty("ID_SPACE_0");
		Partition<Double>[] partitions = ids.getPartitions();
		Edges edges = g.getEdges();

		double[] edgeDistances = this
				.computeEdgeDistances(g, partitions, edges);
		this.divide(edgeDistances, edges.size());
		this.edgesDistribution = new Distribution(edgeDistances);

		double[] allDistances = this.computeAllDistances(g, partitions);
		this.divide(allDistances, g.getNodes().length
				* (g.getNodes().length - 1));
		this.allDistribution = new Distribution(allDistances);

	}

	private double[] addValue(double[] values, double value) {
		int bucket = this.getBucket(value);
		try {
			values[bucket]++;
			return values;
		} catch (IndexOutOfBoundsException e) {
			double[] newValues = new double[bucket + 1];
			System.arraycopy(values, 0, newValues, 0, values.length);
			newValues[bucket] = 1;
			return newValues;
		}
	}

	private int getBucket(double value) {
		return (int) Math.ceil(value / this.bucketSize);
	}

	private void divide(double[] values, double divisor) {
		for (int i = 0; i < values.length; i++) {
			values[i] /= divisor;
		}
	}

	private double[] computeAllDistances(Graph g, Partition<Double>[] partitions) {
		double[] values = new double[1];
		for (Node n1 : g.getNodes()) {
			for (Node n2 : g.getNodes()) {
				if (n1.getIndex() == n2.getIndex()) {
					continue;
				}
				Partition<Double> p1 = partitions[n1.getIndex()];
				Partition<Double> p2 = partitions[n2.getIndex()];
				double dist = p1.distance(p2.getRepresentativeID());
				values = this.addValue(values, dist);
			}
		}
		return values;
	}

	private double[] computeEdgeDistances(Graph g,
			Partition<Double>[] partitions, Edges edges) {
		double[] values = new double[1];
		for (Edge edge : edges.getEdges()) {
			Partition<Double> p1 = partitions[edge.getSrc()];
			Partition<Double> p2 = partitions[edge.getDst()];
			double dist = p1.distance(p2.getRepresentativeID());
			values = this.addValue(values, dist);
		}
		return values;
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.allDistribution.getDistribution(),
				"IDENTIFIER_SPACE_DISTANCES_DISTANCE_DISTRIBUTION_ALL", folder);
		success &= DataWriter.writeWithIndex(this.allDistribution.getCdf(),
				"IDENTIFIER_SPACE_DISTANCES_DISTANCE_DISTRIBUTION_ALL_CDF",
				folder);
		success &= DataWriter.writeWithIndex(
				this.edgesDistribution.getDistribution(),
				"IDENTIFIER_SPACE_DISTANCES_DISTANCE_DISTRIBUTION_EDGES",
				folder);
		success &= DataWriter.writeWithIndex(this.edgesDistribution.getCdf(),
				"IDENTIFIER_SPACE_DISTANCES_DISTANCE_DISTRIBUTION_EDGES_CDF",
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
