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
import java.util.SortedSet;

import gtna.graph.Graph;
import gtna.graph.Node;

/**
 * @author Flipp
 *
 */
public class RWCommunity {
	private ArrayList<Node> vertices;
	private double gamma;
	private HashMap<Integer, Boolean> c_buffer = new HashMap<Integer, Boolean>();
	private Graph g;
	private HashMap<Node, Double> gammas;
	
	
	public RWCommunity(Graph g){
		this.g = g;
		vertices = new ArrayList<Node>();
		gammas = new HashMap<Node, Double>();
	}

	/**
	 * @return
	 */
	public Node[] getNodes() {
		return vertices.toArray(new Node[vertices.size()]);
	}

	/**
	 * 
	 */
	public void sortVertices() {
		Node temp;
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
		for(Node akt : vertices){
			t += getGamma(akt);
		}
		
		gamma = t / ((double) vertices.size());
		
		return gamma;
	}

	/**
	 * @param aktNode
	 */
	public double calculateGamma(Node aktNode) {
		double in = 0;
		for(int akt : aktNode.getOutgoingEdges()){
			if(contains(akt))
				in++;
			
		}
		if(in == 0)
			return 2 * aktNode.getDegree();
		return (2.0 * (aktNode.getDegree())) / ( in * (in));
	}

	/**
	 * @param akt
	 * @return
	 */
	private boolean contains(int akt) {
		return contains(g.getNode(akt));
	}

	/**
	 * @param aktNode
	 * @return
	 */
	public boolean contains(Node aktNode) {
		boolean in = false;
		for(Node akt : vertices)
			if(akt.equals(aktNode))
				in = true;
		
		return in;
	}

	/**
	 * @param aktNode
	 */
	public void add(Node aktNode) {
		vertices.add(aktNode);
		c_buffer.put(aktNode.getIndex(), true);
	}

	/**
	 * @param akt
	 * @return
	 */
	public double getGamma(Node akt) {
		
		double erg = calculateGamma(akt);
		gammas.put(akt, erg);
		return erg;
	}

	/**
	 * @param akt
	 */
	public void remove(Node akt) {
		c_buffer.remove(akt.getIndex());
		vertices.remove(akt);
	}
	
	public boolean equals(RWCommunity c){
		boolean eq = true;
		for(Node akt : vertices){
			if(!c.contains(akt.getIndex()))
				eq = false;
		}
		
		return eq;
	}

}
