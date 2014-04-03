/*
 * ===========================================================
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
 * ShortestPath.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2013-07-xx : added effective diameter, hop plot (Tim Grube)
 * 2014-02-03 : readData, getDistributions, getNodeValueLists (Tim Grube)
 * 2014-04-03 : removed rel. effective diameter (Tim Grube)
 */
package gtna.metrics.basic;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Config;
import gtna.util.Distribution;
import gtna.util.Util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

// TODO problem with averages for big networks (> 40.000)
public class ShortestPaths extends Metric {
	// TODO add LCPL => binning?!?
	// TODO add distribution of LCPL?!?
	// TODO remove computation of distribution

	private Distribution shortestPathLengthDistribution;

	private Distribution shortestPathLengthDistributionAbsolute;

	private double[] localCharacteristicPathLength;

	private double connectivity;

	private Distribution hopPlot;

	private double ecc90;

	private double percentage;

	public ShortestPaths() {
		super("SHORTEST_PATHS");
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	@Override
	public void computeData(Graph graph, Network nw,
			HashMap<String, Metric> metrics) {
		this.localCharacteristicPathLength = new double[graph.getNodes().length];
		long[] SPL = this.computeShortestPathLengths(graph.getNodes());
		this.shortestPathLengthDistribution = new Distribution(
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION",
				this.computeShortestPathLengthDistribution(SPL));
		this.shortestPathLengthDistributionAbsolute = new Distribution(
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE",
				this.computeShortestPathLengthDistributionAbsolute(SPL, graph));
		this.connectivity = (double) Util.sum(SPL)
				/ (double) ((double) graph.getNodes().length * (double) (graph
						.getNodes().length - 1));

		this.hopPlot = new Distribution("SHORTEST_PATHS_HOP_PLOT", computeHP(
				SPL, graph));

		percentage = Double.parseDouble(Config.get("SHORTEST_PATHS_EFFECTIVE_DIAMETER_PERCENTAGE")) / 100d;
		this.ecc90 = calculateEccentricity(
				this.shortestPathLengthDistribution.getCdf(), graph, percentage);
		
		
		if(percentage != 0.9){
			System.out.println("~ Have you thought about changing the texts for the plotting?");
		}
	}

	/**
	 * @param spCdf
	 *            hopplot cdf
	 * @param graph
	 *            graph
	 * @param q
	 *            reachable quantile
	 * @return
	 */
	private double calculateEccentricity(double[][] spCdf, Graph graph, double q) {
		for (int i = 0; i < spCdf.length; i++) {
			if (spCdf[i][1] >= q) {
				return spCdf[i][0];
			}
		}

		return -1;
	}

	/**
	 * @param sPL
	 * @return
	 */
	private double[] computeHP(long[] SPL, Graph g) {
		double[] hp = new double[SPL.length];
		for (int i = 0; i < hp.length; i++) {
			hp[i] = (double) SPL[i] / (double) g.getNodeCount();
		}

		return hp;
	}

	private double[] computeShortestPathLengthDistribution(long[] SPL) {
		long sum = Util.sum(SPL);
		double[] spld = new double[SPL.length];
		for (int i = 0; i < SPL.length; i++) {
			spld[i] = (double) SPL[i] / sum;
		}
		return spld;
	}

	private double[] computeShortestPathLengthDistributionAbsolute(long[] SPL,
			Graph graph) {
		long sum = (long) graph.getNodes().length
				* (long) (graph.getNodes().length - 1);
		double[] spld = new double[SPL.length];
		for (int i = 0; i < SPL.length; i++) {
			spld[i] = (double) SPL[i] / sum;
		}
		return spld;
	}

	private long[] computeShortestPathLengths(Node[] nodes) {
		long[] SPL = new long[1];
		for (Node n : nodes) {
			SPL = this.computeSPL(nodes, n, SPL);
		}
		return SPL;
	}

	private long[] computeSPL(Node[] nodes, Node start, long[] SPL) {
		Queue<Integer> queue = new LinkedList<Integer>();
		int[] spl = Util.initIntArray(nodes.length, -1);
		long sum = 0;
		int found = 0;
		queue.add(start.getIndex());
		spl[start.getIndex()] = 0;
		while (!queue.isEmpty()) {
			Node current = nodes[queue.poll()];
			for (int outIndex : current.getOutgoingEdges()) {
				if (spl[outIndex] != -1) {
					continue;
				}
				spl[outIndex] = spl[current.getIndex()] + 1;
				queue.add(outIndex);
				found++;
				sum += spl[outIndex];
				SPL = this.inc(SPL, spl[outIndex]);
			}
		}
		this.localCharacteristicPathLength[start.getIndex()] = (double) sum
				/ (double) found;
		return SPL;
	}

	private long[] inc(long[] values, int index) {
		try {
			values[index]++;
			return values;
		} catch (ArrayIndexOutOfBoundsException e) {
			long[] valuesNew = new long[index + 1];
			System.arraycopy(values, 0, valuesNew, 0, values.length);
			valuesNew[index] = 1;
			return valuesNew;
		}
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.hopPlot.getDistribution(),
				"SHORTEST_PATHS_HOP_PLOT", folder);
		success &= DataWriter.writeWithoutIndex(this.hopPlot.getCdf(),
				"SHORTEST_PATHS_HOP_PLOT_CDF", folder);
		success &= DataWriter.writeWithoutIndex(
				this.shortestPathLengthDistribution.getDistribution(),
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION", folder);
		success &= DataWriter.writeWithoutIndex(
				this.shortestPathLengthDistribution.getCdf(),
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithoutIndex(
				this.shortestPathLengthDistributionAbsolute.getDistribution(),
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE",
				folder);
		success &= DataWriter
				.writeWithoutIndex(
						this.shortestPathLengthDistributionAbsolute.getCdf(),
						"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE_CDF",
						folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		Single averageShortestPathLength = new Single(
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_AVG",
				this.shortestPathLengthDistribution.getAverage());
		Single medianShortestPathLength = new Single(
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_MED",
				this.shortestPathLengthDistribution.getMedian());
		Single maximumShortestPathLength = new Single(
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_MAX",
				this.shortestPathLengthDistribution.getMax());
		Single connectivity = new Single("SHORTEST_PATHS_CONNECTIVITY",
				this.connectivity);
		Single ecc = new Single(
				"SHORTEST_PATHS_EFFECTIVE_DIAMETER_ECC_90QUANTIL", this.ecc90);
		return new Single[] { averageShortestPathLength,
				medianShortestPathLength, maximumShortestPathLength,
				connectivity, ecc };
	}

	@Override
	public Distribution[] getDistributions() {
		return new Distribution[] { this.shortestPathLengthDistribution,
				this.shortestPathLengthDistributionAbsolute, this.hopPlot };
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[0];
	}

	/**
	 * @return the shortestPathLengthDistribution
	 */
	public Distribution getShortestPathLengthDistribution() {
		return this.shortestPathLengthDistribution;
	}

	/**
	 * @return the shortestPathLengthDistributionAbsolute
	 */
	public Distribution getShortestPathLengthDistributionAbsolute() {
		return this.shortestPathLengthDistributionAbsolute;
	}

	/**
	 * @return the localCharacteristicPathLength
	 */
	public double[] getLocalCharacteristicPathLength() {
		return this.localCharacteristicPathLength;
	}

	/**
	 * @return the connectivity
	 */
	public double getConnectivity() {
		return this.connectivity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#readData(java.lang.String)
	 */
	@Override
	public boolean readData(String folder) {

		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder
				+ "_singles.txt");

		for (String[] single : singles) {
			if (single.length == 2) {
				if ("SHORTEST_PATHS_CONNECTIVITY".equals(single[0])) {
					this.connectivity = Double.valueOf(single[1]);
				} else if ("SHORTEST_PATHS_EFFECTIVE_DIAMETER_ECC_90QUANTIL"
						.equals(single[0])) {
					this.ecc90 = Double.valueOf(single[1]);
				}
			}
		}

		/* DISTRIBUTIONS */

		shortestPathLengthDistributionAbsolute = new Distribution(
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE",
				readDistribution(folder,
						"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE"));
		shortestPathLengthDistribution = new Distribution(
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION",
				readDistribution(folder,
						"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION"));
		hopPlot = new Distribution("SHORTEST_PATHS_HOP_PLOT", readDistribution(
				folder, "SHORTEST_PATHS_HOP_PLOT"));

		return true;
	}

}
