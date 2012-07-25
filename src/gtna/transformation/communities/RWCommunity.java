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
 * Community.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.communities;

import java.util.ArrayList;
import java.util.HashMap;

import gtna.communities.Community;
import gtna.graph.Graph;
import gtna.graph.Node;

/**
 * @author Flipp
 *
 */
public class RWCommunity extends Community {
	private ArrayList<Integer> vertices;
	private double gamma;
	private HashMap<Integer, Boolean> c_buffer = new HashMap<Integer, Boolean>();
	private Graph g;
	private HashMap<Integer, Double> gammas;
	
	
	public RWCommunity(int index, Graph g){
		super(index);
		this.g = g;
		vertices = new ArrayList<Integer>();
		gammas = new HashMap<Integer, Double>();
	}

	/**
	 * @return
	 */
	public int[] getNodes() {
		// need to convert by hand because Integer[] != int[]
		int[] ret = new int[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			ret[i] = vertices.get(i);
		return ret;
	}

	/**
	 * 
	 */
	public void sortVertices() {
		Integer temp;
		for(int i = vertices.size(); i > 1; i--){
			for(int j = 0; j < i-1; j++){
				if(getGamma(vertices.get(j)) < getGamma(vertices.get(j+1))){
					temp = vertices.get(j);
					vertices.set(j, vertices.get(j+1));
					vertices.set(j+1, temp);
				}
					
			}
		}
		
			
	}

	/**
	 * 
	 */
	public double computeGamma() {
		double t = 0;
		for(int akt : vertices){
			t += getGamma(akt);
		}
		
		gamma = t / ((double) vertices.size());
		
		return gamma;
	}

	/**
	 * @param aktNode
	 */
	public double calculateGamma(int node) {
		double in = 0;
		Node aktNode = g.getNode(node);
		for(int akt : aktNode.getOutgoingEdges()){
			if(contains(akt))
				in++;
			
		}
		if(in == 0)
			return 2 * aktNode.getDegree();
		return (2.0 * (aktNode.getDegree())) / ( in * (in));
	}

	/**
	 * @param aktNode
	 * @return
	 */
	public boolean contains(int node) {
		return(c_buffer.containsKey(node));
		
	}

	/**
	 * @param aktNode
	 */
	public void add(int aktNode) {
		vertices.add(aktNode);
		c_buffer.put(aktNode, true);
	}

	/**
	 * @param akt
	 * @return
	 */
	public double getGamma(int node) {
		
		double erg = calculateGamma(node);
		gammas.put(node, erg);
		return erg;
	}

	/**
	 * @param akt
	 */
	public void remove(Integer akt) {
		c_buffer.remove(akt);
		vertices.remove(akt);
	}
	
	public boolean equals(RWCommunity c){
		boolean eq = true;
		for(int akt : vertices){
			if(!c.contains(akt))
				eq = false;
		}
		
		return eq;
	}

}
