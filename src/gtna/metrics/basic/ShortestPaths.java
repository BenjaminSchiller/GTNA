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
 */
package gtna.metrics.basic;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
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

<<<<<<< HEAD
<<<<<<< HEAD
    private Distribution hopPlot;
=======
	private Distribution hopPlot;
=======
    private Distribution hopPlot;
>>>>>>> added relative eccentricity / effective diameter

    private double ecc90;

<<<<<<< HEAD
	public ShortestPaths() {
		super("SHORTEST_PATHS");
	}
>>>>>>> Sampling Bias without CDF as its useless for this metric

    private double ecc90;

    private double eccRelative90;

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
		this.computeShortestPathLengthDistribution(SPL));
	this.shortestPathLengthDistributionAbsolute = new Distribution(
		this.computeShortestPathLengthDistributionAbsolute(SPL, graph));
	this.connectivity = (double) Util.sum(SPL)
		/ (double) ((double) graph.getNodes().length * (double) (graph
			.getNodes().length - 1));

<<<<<<< HEAD
	this.hopPlot = new Distribution(computeHP(SPL, graph));

	this.ecc90 = calculateEccentricity(this.shortestPathLengthDistributionAbsolute.getCdf(), graph, 0.90);
	this.eccRelative90 = calculateEccentricity(this.shortestPathLengthDistribution.getCdf(), graph, 0.90);

//	System.out.println("\nSPR: " + Arrays.toString(shortestPathLengthDistribution.getCdf()));
//	System.out.println("SPA: " + Arrays.toString(shortestPathLengthDistributionAbsolute.getCdf()));
//	System.out.println("HP: " + Arrays.toString(hopPlot.getCdf()));
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
    private double calculateEccentricity(double[] spCdf, Graph graph, double q) {
	for (int i = 0; i < spCdf.length; i++) {
	    if (spCdf[i] >= q) {
		return i;
	    }
=======
	@Override
	public void computeData(Graph graph, Network nw,
			HashMap<String, Metric> metrics) {
		this.localCharacteristicPathLength = new double[graph.getNodes().length];
		long[] SPL = this.computeShortestPathLengths(graph.getNodes());
		this.shortestPathLengthDistribution = new Distribution(
				this.computeShortestPathLengthDistribution(SPL));
		this.shortestPathLengthDistributionAbsolute = new Distribution(
				this.computeShortestPathLengthDistributionAbsolute(SPL, graph));
		this.connectivity = (double) Util.sum(SPL)
				/ (double) ((double) graph.getNodes().length * (double) (graph
						.getNodes().length - 1));
		
		this.hopPlot = new Distribution(computeHP(SPL, graph));
		
		this.ecc90 = calculateEccentricity(hopPlot.getCdf(), graph, 0.90);
		
		
	}
=======
    private double eccRelative90;

    public ShortestPaths() {
	super("SHORTEST_PATHS");
    }

    @Override
    public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
	return true;
    }
>>>>>>> added relative eccentricity / effective diameter

    @Override
    public void computeData(Graph graph, Network nw,
	    HashMap<String, Metric> metrics) {
	this.localCharacteristicPathLength = new double[graph.getNodes().length];
	long[] SPL = this.computeShortestPathLengths(graph.getNodes());
	this.shortestPathLengthDistribution = new Distribution(
		this.computeShortestPathLengthDistribution(SPL));
	this.shortestPathLengthDistributionAbsolute = new Distribution(
		this.computeShortestPathLengthDistributionAbsolute(SPL, graph));
	this.connectivity = (double) Util.sum(SPL)
		/ (double) ((double) graph.getNodes().length * (double) (graph
			.getNodes().length - 1));

<<<<<<< HEAD
	/**
	 * @param sPL
	 * @return
	 */
	private double[] computeHP(long[] SPL, Graph g) {
		double[] hp = new double[SPL.length];
		for(int i = 0; i < hp.length; i++){
			hp[i] = (double) SPL[i] / (double) g.getNodeCount();
		}
		
		return hp;
>>>>>>> Sampling Bias without CDF as its useless for this metric
=======
	this.hopPlot = new Distribution(computeHP(SPL, graph));

	this.ecc90 = calculateEccentricity(this.shortestPathLengthDistributionAbsolute.getCdf(), graph, 0.90);
	this.eccRelative90 = calculateEccentricity(this.shortestPathLengthDistribution.getCdf(), graph, 0.90);

	System.out.println("\nSPR: " + Arrays.toString(shortestPathLengthDistribution.getCdf()));
	System.out.println("SPA: " + Arrays.toString(shortestPathLengthDistributionAbsolute.getCdf()));
	System.out.println("HP: " + Arrays.toString(hopPlot.getCdf()));
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
    private double calculateEccentricity(double[] spCdf, Graph graph, double q) {
	for (int i = 0; i < spCdf.length; i++) {
	    if (spCdf[i] >= q) {
		return i;
	    }
>>>>>>> added relative eccentricity / effective diameter
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

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> added relative eccentricity / effective diameter
    private long[] inc(long[] values, int index) {
	try {
	    values[index]++;
	    return values;
	} catch (ArrayIndexOutOfBoundsException e) {
	    long[] valuesNew = new long[index + 1];
	    System.arraycopy(values, 0, valuesNew, 0, values.length);
	    valuesNew[index] = 1;
	    return valuesNew;
<<<<<<< HEAD
=======
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(
				this.hopPlot.getDistribution(),
				"SHORTEST_PATHS_HOP_PLOT", folder);
		success &= DataWriter.writeWithIndex(
				this.hopPlot.getCdf(),
				"SHORTEST_PATHS_HOP_PLOT_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.shortestPathLengthDistribution.getDistribution(),
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION", folder);
		success &= DataWriter.writeWithIndex(
				this.shortestPathLengthDistribution.getCdf(),
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_CDF", folder);
		success &= DataWriter.writeWithIndex(
				this.shortestPathLengthDistributionAbsolute.getDistribution(),
				"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE",
				folder);
		success &= DataWriter
				.writeWithIndex(
						this.shortestPathLengthDistributionAbsolute.getCdf(),
						"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE_CDF",
						folder);
		return success;
>>>>>>> Sampling Bias without CDF as its useless for this metric
	}
    }

<<<<<<< HEAD
=======
	}
    }

>>>>>>> added relative eccentricity / effective diameter
    @Override
    public boolean writeData(String folder) {
	boolean success = true;
	success &= DataWriter.writeWithIndex(this.hopPlot.getDistribution(),
		"SHORTEST_PATHS_HOP_PLOT", folder);
	success &= DataWriter.writeWithIndex(this.hopPlot.getCdf(),
		"SHORTEST_PATHS_HOP_PLOT_CDF", folder);
	success &= DataWriter.writeWithIndex(
		this.shortestPathLengthDistribution.getDistribution(),
		"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION", folder);
	success &= DataWriter.writeWithIndex(
		this.shortestPathLengthDistribution.getCdf(),
		"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_CDF", folder);
	success &= DataWriter.writeWithIndex(
		this.shortestPathLengthDistributionAbsolute.getDistribution(),
		"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE",
		folder);
	success &= DataWriter
		.writeWithIndex(
			this.shortestPathLengthDistributionAbsolute.getCdf(),
			"SHORTEST_PATHS_SHORTEST_PATH_LENGTH_DISTRIBUTION_ABSOLUTE_CDF",
			folder);
	return success;
    }
<<<<<<< HEAD
=======
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
		Single ecc = new Single("SHORTEST_PATHS_EFFECTIVE_DIAMETER_ECC_90QUANTIL",
				this.ecc90);
		return new Single[] { averageShortestPathLength,
				medianShortestPathLength, maximumShortestPathLength,
				connectivity, ecc };
	}
>>>>>>> Sampling Bias without CDF as its useless for this metric
=======
>>>>>>> added relative eccentricity / effective diameter

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
	Single eccRel = new Single(
		"SHORTEST_PATHS_RELATIVE_EFFECTIVE_DIAMETER_ECC_90QUANTIL", this.eccRelative90);
	return new Single[] { averageShortestPathLength,
		medianShortestPathLength, maximumShortestPathLength,
		connectivity, ecc, eccRel };
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
}
