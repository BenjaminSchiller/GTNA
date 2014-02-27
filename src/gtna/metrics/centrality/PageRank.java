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
 * PageRank.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim Grube;
 * Contributors:    -;
 *
 * Changes since 2013-07-17
 * ---------------------------------------
 * 2014-02-03 : readData, getNodeValueList(), getDistributions() (Tim Grube)
 *
 */
package gtna.metrics.centrality;

import gtna.data.NodeValueList;
import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.io.DataReader;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.Distribution;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Tim
 * 
 */
public class PageRank extends Metric {
	
	private NodeValueList pageRank;
	
	private static final int ITERATIONS = 50;
	private int bins = 20;

	private double alpha = 0.85; // initialized with the Brin/Page proposed
									// value

	
	
	private double[][] H; // hyperlink matrix
	private double[][] A;
	private double[][] S;

	private double[][] ONE;

	private double[][] G;

	private double[] prVector;

	private double nodes;

	private double edges;

	private double prMax;

	private double prMin;

	private double prMed;

	private double prAvg;

	
	
	public PageRank(double alpha, int bins){
		super("PAGERANK_DISTRIBUTION");
		this.alpha = alpha;
		this.bins = bins;
	}
	
	public PageRank(int bins){
		super("PAGERANK_DISTRIBUTION");
		this.bins = bins;
	}
	
	/**
	 * @param key
	 */
	public PageRank(double alpha) {
		super("PAGERANK_DISTRIBUTION");
		this.alpha = alpha;
	}

	public PageRank() {
		this(0.85); // usage of the standard value
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		calculateBaseMatrices(g);

		prVector = initVector(G.length);

		double[] prOld = initVector(G.length);
		for (int j = 0; j < ITERATIONS; j++) {
			prOld = prVector;
			prVector = calculateMatrixVectorProduct(G, prVector);
			prVector = normalizeVector(prVector);
		}
//		Arrays.sort(prVector);
		pageRank = new NodeValueList("PAGERANK_DISTRIBUTION_PAGERANK_DISTRIBUTION", prVector);
		nodes = g.getNodeCount();
		edges = g.getEdges().size();

		this.prMax = getMax(prVector);
		this.prMin = getMin(prVector);
		this.prMed = getMed(prVector);
		this.prAvg = getAvg(prVector);

	}

	/**
	 * @param vector
	 * @return
	 */
	private double[] normalizeVector(double[] vector) {
		double[] normalized = new double[vector.length];
		double sumNorm = 0;
		for (double d : vector) {
			sumNorm += Math.abs(d);
		}

		for (int i = 0; i < vector.length; i++) {
			normalized[i] = vector[i] / sumNorm;
		}

		return normalized;
	}

	/**
	 * @param prVector2
	 * @param prOld
	 * @return
	 */
	private double[] diffVector(double[] prVector2, double[] prOld) {
		double[] d = new double[prVector2.length];

		for (int i = 0; i < d.length; i++) {
			d[i] = prVector2[i] - prOld[i];
		}

		return d;
	}

	/**
	 * @param length
	 * @return
	 */
	private double[] initVector(int length) {
		double[] t = new double[length];
		Arrays.fill(t, length);
		return t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.pageRank.getValues(),
				"PAGERANK_DISTRIBUTION_PAGERANK_NVL", folder);
		success &= DataWriter.writeWithoutIndex(this.pageRank.toDistribution(bins).getDistribution(),
				"PAGERANK_DISTRIBUTION_PAGERANK_DISTRIBUTION", folder);
		return success;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single nodes = new Single("PAGERANK_DISTRIBUTION_NODES", this.nodes);
		Single edges = new Single("PAGERANK_DISTRIBUTION_EDGES", this.edges);

		Single prMin = new Single("PAGERANK_DISTRIBUTION_MIN", this.prMin);
		Single prMed = new Single("PAGERANK_DISTRIBUTION_MED", this.prMed);
		Single prAvg = new Single("PAGERANK_DISTRIBUTION_AVG", this.prAvg);
		Single prMax = new Single("PAGERANK_DISTRIBUTION_MAX", this.prMax);

		return new Single[] { nodes, edges, prMin, prMed, prAvg, prMax };

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#applicable(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return true;
	}

	private void calculateBaseMatrices(Graph g) {
		H = new double[g.getNodeCount()][g.getNodeCount()];
		A = new double[g.getNodeCount()][g.getNodeCount()];
		S = new double[g.getNodeCount()][g.getNodeCount()];
		G = new double[g.getNodeCount()][g.getNodeCount()];
		ONE = new double[g.getNodeCount()][g.getNodeCount()];

		initializeMatrix(H, 0);
		initializeMatrix(A, 0);
		initializeMatrix(S, 0);
		initializeMatrix(G, 0);
		initializeMatrix(ONE, 1);

		Node[] nodes = g.getNodes();
		for (Node n : nodes) {
			int ni = n.getIndex();
			if (n.getOutDegree() > 0) { // fill H as some outgoing links are
										// present
				double[] nch = H[ni];
				int nod = n.getOutDegree();
				for (int i : n.getOutgoingEdges()) {
					nch[i] = (double) 1 / (double) nod; // set Hij = 1/li
				}
				H[ni] = nch;
			} else { // fill A as no outgoing links are present
				double[] nca = A[ni];
				Arrays.fill(nca, 1 / g.getNodeCount());
				A[ni] = nca;
			}
		}

		S = addMatrices(H, A);
		double[][] alphaS = multiplyMatrix(S, alpha);
		double[][] jumpM = multiplyMatrix(ONE, 1 / g.getNodeCount());
		jumpM = multiplyMatrix(ONE, 1 - alpha);
		G = addMatrices(alphaS, jumpM);

	}

	private double[] calculateMatrixVectorProduct(double[][] m, double[] v) {
		if (m.length != v.length) {
			throw new IllegalArgumentException(
					"m/v dimensions are incompatible for the Matrix-Vector Multiplication");
		}

		double[] r = new double[v.length];
		Arrays.fill(r, 0.0);

		for (int i = 0; i < v.length; i++) {
			for (int j = 0; j < v.length; j++) {
				r[i] = r[i] + m[i][j] * v[j];
			}
		}

		return r;
	}

	/**
	 * @param s2
	 * @param alpha2
	 * @return
	 */
	private double[][] multiplyMatrix(double[][] s1, double alpha2) {
		for (int i = 0; i < s1.length; i++) {
			for (int j = 0; j < s1[0].length; j++) {
				s1[i][j] = s1[i][j] * alpha2;
			}
		}

		return s1;
	}

	private double[][] addMatrices(double[][] s1, double[][] s2) {
		double[][] t = new double[s1.length][s1[0].length];
		for (int i = 0; i < s1.length; i++) {
			for (int j = 0; j < s1[0].length; j++) {
				t[i][j] = s1[i][j] + s2[i][j];
			}
		}

		return t;
	}

	/**
	 * @param a2
	 * @param i
	 */
	private void initializeMatrix(double[][] M, int initValue) {
		for (int i = 0; i < M.length; i++) {
			double[] mi = M[i];
			Arrays.fill(mi, initValue);
			M[i] = mi;
		}
	}

	private double getMax(double[] dis) {
		double max = 0;

		for (double d : dis) {
			max = Math.max(max, d);
		}

		return max;
	}

	private double getMin(double[] dis) {
		double min = Double.MAX_VALUE;

		for (double d : dis) {
			min = Math.min(min, d);
		}

		return min;
	}

	private double getAvg(double[] dis) {
		double sum = 0;

		for (double d : dis) {
			sum += d;
		}

		return sum / dis.length;
	}

	private double getMed(double[] dis) {
		double[] s = dis;
		double median;
		Arrays.sort(s);

		if (s.length % 2 != 0) {
			// odd number of entries
			median = dis[(int) Math.floor(dis.length / 2)];
		} else {
			// even number of entries
			double umed = dis[(int) dis.length / 2 - 1];
			double omed = dis[(int) dis.length / 2];

			median = umed + (omed - umed) / 2;
		}

		return median;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#readData(java.lang.String)
	 */
	@Override
	public boolean readData(String folder) {
		/* SINGLES */
		String[][] singles = DataReader.readSingleValues(folder + "_singles.txt");
		
		for(String[] single : singles){
			if(single.length == 2){
				if("PAGERANK_DISTRIBUTION_NODES".equals(single[0])){
					this.nodes = (int) Math.round(Double.valueOf(single[1]));
				} else if("PAGERANK_DISTRIBUTION_EDGES".equals(single[0])){
					this.edges = (int) Math.round(Double.valueOf(single[1]));
				} else if("PAGERANK_DISTRIBUTION_MIN".equals(single[0])){
					this.prMin = Double.valueOf(single[1]);
				} else if("PAGERANK_DISTRIBUTION_MED".equals(single[0])){
					this.prMed = Double.valueOf(single[1]);
				} else if("PAGERANK_DISTRIBUTION_AVG".equals(single[0])){
					this.prAvg = Double.valueOf(single[1]);
				} else if("PAGERANK_DISTRIBUTION_MAX".equals(single[0])){
					this.prMax = Double.valueOf(single[1]);
				} 
			}
		}
		
		
		/* NodeValue Lists */
		
		pageRank = new NodeValueList("PAGERANK_DISTRIBUTION_PAGERANK_NVL", readDistribution(folder, "PAGERANK_DISTRIBUTION_PAGERANK_NVL"));
		
		
		return true;
	}

	@Override
	public Distribution[] getDistributions() {
		return new Distribution[0];
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {pageRank};
	}

}
