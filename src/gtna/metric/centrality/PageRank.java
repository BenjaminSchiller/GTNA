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
package gtna.metric.centrality;

import java.util.Arrays;
import java.util.HashMap;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;


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



    /**
     * @param key
     */
    public PageRank(double alpha) {
	super("PAGERANK");
	this.alpha = alpha;
    }


    /* (non-Javadoc)
     * @see gtna.metrics.Metric#computeData(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
     */
    @Override
    public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see gtna.metrics.Metric#writeData(java.lang.String)
     */
    @Override
    public boolean writeData(String folder) {
	// TODO Auto-generated method stub
	return false;
    }

    /* (non-Javadoc)
     * @see gtna.metrics.Metric#getSingles()
     */
    @Override
    public Single[] getSingles() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see gtna.metrics.Metric#applicable(gtna.graph.Graph, gtna.networks.Network, java.util.HashMap)
     */
    @Override
    public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
	// TODO Auto-generated method stub
	return false;
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
		    nch[i] = 1/nod;			// set Hij = 1/li
		}
		H[ni] = nch;
	    } else { // fill A as no outgoing links are present
		double[] nca = A[ni];
		Arrays.fill(nca, 1/g.getNodeCount());
		A[ni] = nca;
	    }
	}
	
	for(int i = 0; i < g.getNodeCount(); i++) {
	    for(int j = 0; j < g.getNodeCount(); j++) {
		S[i][j] = H[i][j] + A[i][j];
	    }
	}
	
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
