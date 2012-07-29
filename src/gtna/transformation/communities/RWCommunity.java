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

import java.util.HashMap;

import gtna.communities.CBufferingCommunity;
import gtna.graph.Graph;
import gtna.graph.Node;


public class RWCommunity extends CBufferingCommunity {
	private double gamma;
	private Graph g;
	private HashMap<Integer, Double> gammas = new HashMap<Integer, Double>();
	
	
	public RWCommunity(int index, Graph g){
		super(index);
		this.g = g;
	}

	public void sortVertices() {
		Integer temp;
		for(int i = nodes.size(); i > 1; i--){
			for(int j = 0; j < i-1; j++){
				if(getGamma(nodes.get(j)) < getGamma(nodes.get(j+1))){
					temp = nodes.get(j);
					nodes.set(j, nodes.get(j+1));
					nodes.set(j+1, temp);
				}
					
			}
		}
		
			
	}

	public double computeGamma() {
		double t = 0;
		for(int akt : nodes){
			t += getGamma(akt);
		}
		
		gamma = t / ((double) nodes.size());
		
		return gamma;
	}


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


	public double getGamma(int node) {
		
		double erg = calculateGamma(node);
		gammas.put(node, erg);
		return erg;
	}

	public boolean equals(RWCommunity c){
		boolean eq = true;
		for(int akt : nodes){
			if(!c.contains(akt))
				eq = false;
		}
		
		return eq;
	}

}
