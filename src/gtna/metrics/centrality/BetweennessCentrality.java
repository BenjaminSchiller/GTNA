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
 *
 */
package gtna.metrics.centrality;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.metrics.Metric;
import gtna.networks.Network;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * 
 * The betweenness centrality is calculated using the fast algorithm of Ulrik Brandes, published in
 * "A Faster Algorithm for Betweenness Centrality" (2001)
 * 
 * @author Tim
 *
 */
public class BetweennessCentrality extends Metric {

	/**
	 * @param key
	 */
	public BetweennessCentrality() {
		super("BETWEENNESS_CENTRALITY");
	}

	/**
	 * @param key
	 * @param parameters
	 */
	public BetweennessCentrality(String key, Parameter[] parameters) {
		super(key, parameters);
		// TODO Auto-generated constructor stub
	} // TODO Remove?

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
	
	
	/**
	 * Calculates the betweenness centrality array with the Brandes Algorithm
	 * 
	 * see: Algorithm 1: Betweenness centrality in unweighted graphs (A Faster Algorithm for Betweenness Centrality, Brandes, 2001)
	 * @param g
	 */
	private void calculateBC(Graph g){
		int[] cb = new int[g.getNodeCount()];
		Arrays.fill(cb, 0); // initialize with 0 as the nodes are initially included in 0 shortest paths
		
		Node[] V = g.getNodes();
		
		for(Node s : V){
			Stack<Node> S = new Stack<Node>();
			Map<Node, List<Node>> P = new HashMap<Node, List<Node>>();
			int[] sigma_t = new int[V.length]; Arrays.fill(sigma_t, 0); sigma_t[s.getIndex()] = 1;
			int[] distance_t = new int[V.length]; Arrays.fill(distance_t, -1); distance_t[s.getIndex()] = 0;
			
			Queue<Node> Q = new LinkedList<Node>();
			Q.offer(s);
			
			while(!Q.isEmpty()){
				Node v = Q.poll();
				S.push(v);
				Node[] vNeighbors = getNeighborNodes(v, g);
				for(Node w : vNeighbors){
					// w found first time?
					if( distance_t[w.getIndex()] < 0){
						Q.offer(w);
						distance_t[w.getIndex()] = distance_t[v.getIndex()]+1;
					}
					// shortest path to w via v?
					if(distance_t[w.getIndex()] == (distance_t[v.getIndex()]+1)){
						sigma_t[w.getIndex()] = sigma_t[w.getIndex()] + sigma_t[v.getIndex()];
						List<Node> pw = P.get(w);
						if(pw == null)
							pw = new LinkedList<Node>();
						
						pw.add(v);
						P.put(w, pw);
					}
				}
			}
			
			int[] delta_v = new int[V.length]; Arrays.fill(delta_v, 0);
			// S returns nodes in order of non-increasing distance from s
			while(!S.isEmpty()){
				Node w = S.pop();
				for(Node v : P.get(w)){
					delta_v[v.getIndex()] = delta_v[v.getIndex()] + (sigma_t[v.getIndex()] / sigma_t[w.getIndex()] * (1 + delta_v[w.getIndex()]));
					
					if(w.getIndex() != s.getIndex()){
						cb[w.getIndex()] = cb[w.getIndex()] + delta_v[w.getIndex()];
					}
				}
			}
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	/**
	 * @param v
	 * @return
	 */
	private Node[] getNeighborNodes(Node v, Graph g) {
		Collection<Node> nv = new ArrayList<Node>();
		
		int[] oe = v.getOutgoingEdges();
		
		for(int n : oe){
			nv.add(g.getNode(n));
		}
		
		
		return nv.toArray(new Node[0]);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
