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
 * BetweennessCentrality.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Tim;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2014-02-03 : readData, getNodeValueLists, getDistributions (Tim Grube)
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
import gtna.util.parameter.Parameter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * The betweenness centrality is calculated using the fast algorithm of Ulrik
 * Brandes, published in "A Faster Algorithm for Betweenness Centrality" (2001)
 * 
 * @author Tim
 * 
 */
public class BetweennessCentrality extends Metric {

	private double[] cbs;
	private NodeValueList betweennessCentrality;
	private Distribution binnedBetwennessCentrality;
	private int edges;
	private int nodes;
	private double bcMed;
	private double bcAvg;
	private double bcMin;
	private double bcMax;
	
	
	private double binSize = 0.01;
	private int spSum=0;

	/**
	 * @param key
	 */
	public BetweennessCentrality() {
		super("BETWEENNESS_CENTRALITY");
	}
	
	/**
	 * 
	 * @param binsize - size of a bin
	 */
	public BetweennessCentrality(double binsize) {
		super("BETWEENNESS_CENTRALITY");
		this.binSize = binsize;
	}

	/**
	 * @param key
	 * @param parameters
	 */
	public BetweennessCentrality(String key, Parameter[] parameters) {
		super(key, parameters);

	} // TODO Remove?

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#computeData(gtna.graph.Graph,
	 * gtna.networks.Network, java.util.HashMap)
	 */
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		this.calculateBC(g);

		double[][] binned = new double[][]{{}};
		
		if(this.spSum!=0){
		// normalization for binned distribution
		double[] cb = new double[cbs.length]; 
		for (int i = 0; i < cbs.length; i++) {
			cb[i] = (double) cbs[i]/(double) this.spSum;
		}
		
		binned = gtna.util.Statistics.binnedDistribution(cb, 0d, 1d, 100);
		}
		
		betweennessCentrality = new NodeValueList("BETWEENNESS_CENTRALITY_NVL", cbs);
		binnedBetwennessCentrality = new Distribution("BETWEENNESS_CENTRALITY_DISTRIBUTION", binned);
		this.nodes = g.getNodes().length;
		this.edges = g.getEdges().size();

		this.bcMax = getMax(cbs);
		this.bcMin = getMin(cbs);
		this.bcAvg = getAvg(cbs);
		this.bcMed = getMed(cbs);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#writeData(java.lang.String)
	 */
	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithIndex(this.betweennessCentrality.getValues(),
				"BETWEENNESS_CENTRALITY_NVL", folder);
		
		// binned Distribution
		success &= DataWriter.writeWithoutIndex(binnedBetwennessCentrality.getDistribution(),"BETWEENNESS_CENTRALITY_DISTRIBUTION", folder);
		
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.metrics.Metric#getSingles()
	 */
	@Override
	public Single[] getSingles() {
		Single nodes = new Single("BETWEENNESS_CENTRALITY_NODES", this.nodes);
		Single edges = new Single("BETWEENNESS_CENTRALITY_EDGES", this.edges);

		Single bcMin = new Single("BETWEENNESS_CENTRALITY_MIN", this.bcMin);
		Single bcMed = new Single("BETWEENNESS_CENTRALITY_MED", this.bcMed);
		Single bcAvg = new Single("BETWEENNESS_CENTRALITY_AVG", this.bcAvg);
		Single bcMax = new Single("BETWEENNESS_CENTRALITY_MAX", this.bcMax);

		return new Single[] { nodes, edges, bcMin, bcMed, bcAvg, bcMax };

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

	/**
	 * Calculates the betweenness centrality array with the Brandes Algorithm
	 * 
	 * see: Algorithm 1: Betweenness centrality in unweighted graphs (A Faster
	 * Algorithm for Betweenness Centrality, Brandes, 2001)
	 * 
	 * @param g
	 */
	private void calculateBC(Graph g) {
		cbs = new double[g.getNodeCount()];
		spSum = 0;
		Arrays.fill(cbs, 0); // initialize with 0 as the nodes are initially
								// included in 0 shortest paths

		Node[] V = g.getNodes();

		for (Node s : V) {
			// stage 1: local init
			ArrayDeque<Node> S = new ArrayDeque<Node>();
			ArrayDeque<Node> Q = new ArrayDeque<Node>();
			HashMap<Node, List<Node>> P = new HashMap<Node, List<Node>>();
			HashMap<Node, Integer> sigma = new HashMap<Node, Integer>();
			HashMap<Node, Integer> distance = new HashMap<Node, Integer>();
			for(Node v : V){
				P.put(v, new ArrayList<Node>());
				if(v.getIndex() == s.getIndex()){
					sigma.put(v, 1);
					distance.put(v, Integer.MAX_VALUE);
				} else {
					sigma.put(v, 0);
					distance.put(v, Integer.MAX_VALUE);
				}
			}
			Q.offer(s);
			
			// stage 2: BFS traversal
			while(!Q.isEmpty()){
				Node v = Q.pollFirst();
				S.push(v);
				
				for(int wIndex : v.getOutgoingEdges()){
					Node w = g.getNode(wIndex);
					
					// w found for the first time:
					if(distance.get(w) == Integer.MAX_VALUE){
						Q.offer(w);
						distance.put(w, distance.get(v)+1);
					}
					
					// new/additional shortest path to w
					if(distance.get(w) == distance.get(v)+1){
						sigma.put(w, sigma.get(w) + sigma.get(v));
						P.get(w).add(v);
					}
				}
			}
			
			// stage 3: dependency accumulation
			HashMap<Node, Double> delta = new HashMap<Node, Double>();
			for(Node v : V){
				delta.put(v, 0d);
			}
			while(!S.isEmpty()){
				Node w = S.pop();
				for(Node v : P.get(w)){
					delta.put(v, delta.get(v) + sigma.get(v)/sigma.get(w)*(1+delta.get(w)));
				}
				if(w.getIndex() != s.getIndex()){
					cbs[w.getIndex()] = cbs[w.getIndex()] + delta.get(w);
				}
			}
			spSum += sumSps(sigma);
			
		}

	}

	/**
	 * @param sigma
	 * @return
	 */
	private int sumSps(HashMap<Node, Integer> sigma) {
		int s = 0;
		
		for(Entry<Node, Integer> e : sigma.entrySet()){
			s+=e.getValue();
		}
		
		return s-2; // -1 since sigma(root)=1 but this is not a shortest path in our sense of shortest path counting
	}

	/**
	 * @param v
	 * @return
	 */
	private Node[] getNeighborNodes(Node v, Graph g) {
		Collection<Node> nv = new ArrayList<Node>();

		int[] oe = v.getOutgoingEdges();

		for (int n : oe) {
			nv.add(g.getNode(n));
		}

		return nv.toArray(new Node[0]);
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
				if("BETWEENNESS_CENTRALITY_NODES".equals(single[0])){
					this.nodes = (int) Math.round(Double.valueOf(single[1]));
				} else if("BETWEENNESS_CENTRALITY_EDGES".equals(single[0])){
					this.edges = (int) Math.round(Double.valueOf(single[1]));
				} else if("BETWEENNESS_CENTRALITY_MIN".equals(single[0])){
					this.bcMin = Double.valueOf(single[1]);
				} else if("BETWEENNESS_CENTRALITY_MED".equals(single[0])){
					this.bcMed = Double.valueOf(single[1]);
				} else if("BETWEENNESS_CENTRALITY_AVG".equals(single[0])){
					this.bcAvg = Double.valueOf(single[1]);
				} else if("BETWEENNESS_CENTRALITY_MAX".equals(single[0])){
					this.bcMax = Double.valueOf(single[1]);
				} 
			}
		}
		
		
		/* Node Value List */
		
		betweennessCentrality = new NodeValueList("BETWEENNESS_CENTRALITY_NVL", readDistribution(folder, "BETWEENNESS_CENTRALITY_NVL"));
		
		
		return true;
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getDistributions()
	 */
	@Override
	public Distribution[] getDistributions() {
		return new Distribution[]{binnedBetwennessCentrality};
	}

	/* (non-Javadoc)
	 * @see gtna.metrics.Metric#getNodeValueLists()
	 */
	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {betweennessCentrality};
	}

}
