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
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.metrics.centrality;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
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

    private double alpha = 0.85; // initialized with the Brin/Page proposed value

    private double[][] H;	// hyperlink matrix
    private double[][] A;
    private double[][] S;

    private double[][] ONE;

    private double[][] G;

    private double[] prVector;

    private Distribution pr;

    private double nodes;

    private double edges;



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


    /* (non-Javadoc)
     * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
     */
    @Override
    public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
	calculateBaseMatrices(g);
	
	prVector = initVector(G.length);
	
	double[] prOld = initVector(G.length);
	for(int j = 0; j < 500; j++) {
	    prOld = prVector;
	    prVector = calculateMatrixVectorProduct(G, prVector);
	    prVector = normalizeVector(prVector);
	}
	Arrays.sort(prVector);
	pr = new Distribution(prVector);
	nodes = g.getNodeCount();
	edges = g.getEdges().size();

    }

    /**
	 * @param vector
	 * @return
	 */
	private double[] normalizeVector(double[] vector) {
		double[] normalized = new double[vector.length];
		double sumNorm = 0;
		for(double d : vector){
			sumNorm += Math.abs(d);
		}
		
		for(int i = 0; i < vector.length; i++){
			normalized[i] = vector[i]/sumNorm;
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
	
	for(int i = 0; i < d.length; i++) {
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
	Arrays.fill(t, 0.0);
	
	t[0] = 1000.0;
	
	return t;
    }


    /* (non-Javadoc)
     * @see gtna.metrics.Metric#writeData(java.lang.String)
     */
    @Override
    public boolean writeData(String folder) {
	boolean success = true;
	success &= DataWriter.writeWithIndex(
		this.pr.getDistribution(),
		"PAGERANK_DISTRIBUTION_PAGERANK_DISTRIBUTION", folder);
	success &= DataWriter.writeWithIndex(
		this.pr.getCdf(),
		"PAGERANK_DISTRIBUTION_PAGERANK_DISTRIBUTION_CDF", folder);
	return success;

    }

    /* (non-Javadoc)
     * @see gtna.metrics.Metric#getSingles()
     */
    @Override
    public Single[] getSingles() {
	Single nodes = new Single("PAGERANK_DISTRIBUTION_NODES", this.nodes);
	Single edges = new Single("PAGERANK_DISTRIBUTION_EDGES", this.edges);

	Single prMin = new Single("PAGERANK_DISTRIBUTION_MIN",
			this.pr.getMin());
	Single prMed = new Single("PAGERANK_DISTRIBUTION_MED",
			this.pr.getMedian());
	Single prAvg = new Single("PAGERANK_DISTRIBUTION_AVG",
			this.pr.getAverage());
	Single prMax = new Single("PAGERANK_DISTRIBUTION_MAX",
			this.pr.getMax());
	
	return new Single[] { nodes, edges, prMin, prMed, prAvg, prMax };

    }

    /* (non-Javadoc)
     * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
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
	for(Node n : nodes) {
	    int ni = n.getIndex();
	    if(n.getOutDegree() > 0) { // fill H as some outgoing links are present
		double[] nch = H[ni];
		int nod = n.getOutDegree();
		for(int i : n.getOutgoingEdges()) {
		    nch[i] = (double)1/(double)nod;			// set Hij = 1/li
		}
		H[ni] = nch;
	    } else { // fill A as no outgoing links are present
		double[] nca = A[ni];
		Arrays.fill(nca, 1/g.getNodeCount());
		A[ni] = nca;
	    }
	}
	
	S = addMatrices(H, A);
	double[][] alphaS = multiplyMatrix(S, alpha); 
	double[][] jumpM = multiplyMatrix(ONE, 1/g.getNodeCount());
	jumpM = multiplyMatrix(ONE, 1-alpha);
	G = addMatrices(alphaS, jumpM);
	
    }
    
    private double[] calculateMatrixVectorProduct(double[][] m, double[] v) {
	if(m.length != v.length) {
	    throw new IllegalArgumentException("m/v dimensions are incompatible for the Matrix-Vector Multiplication");
	}
	
	double[] r = new double[v.length];
	Arrays.fill(r, 0.0);
	
	for(int i = 0; i < v.length; i++) {
	    for(int j = 0; j < v.length; j++) {
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
	for(int i = 0; i < s1.length; i++) {
	    for(int j = 0; j < s1[0].length; j++) {
		s1[i][j] = s1[i][j] * alpha2;
	    }
	}
	
	return s1;
    }


    private double[][] addMatrices(double[][] s1, double[][] s2) {
	double[][] t = new double[s1.length][s1[0].length];
	for(int i = 0; i < s1.length; i++) {
	    for(int j = 0; j < s1[0].length; j++) {
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
	for(int i = 0; i < M.length; i++) {
	   double[] mi = M[i];
	   Arrays.fill(mi, initValue);
	   M[i] = mi;
	}
    }

}
