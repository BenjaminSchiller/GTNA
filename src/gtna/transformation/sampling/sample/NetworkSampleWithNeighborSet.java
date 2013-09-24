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
 * NetworkSampleWithNeighborSet.java
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
package gtna.transformation.sampling.sample;

import gtna.graph.Graph;
import gtna.graph.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Tim
 *
 */
public class NetworkSampleWithNeighborSet extends NetworkSample {

	/**
	 * 
	 */
	public NetworkSampleWithNeighborSet() {
		super("extended_by_neighborset");
	}
	
	public NetworkSampleWithNeighborSet(String algorithm, double scaledown, int dimension,
			boolean revisiting) {
		super("extended_by_neighborset", algorithm, scaledown, dimension, revisiting);
	}
	
	
	@Override
	public void finalize(Graph g){
		Set<Integer> sampledNodes = this.sampleNodeMapping.keySet();
		Set<Integer> sampleNeighborSet = new HashSet<Integer>();
		
		for(Integer ni : sampledNodes){
			Node n = g.getNode(ni);
			int[] neighborSet = n.getOutgoingEdges();
			
			for(int nsi : neighborSet){
				if(!sampledNodes.contains(nsi)){
					sampleNeighborSet.add(nsi);
				}
			}
		}
		
		Collection<Node> neighborSet = collectNodesToIds(sampleNeighborSet, g); 
		addNodeToSample(neighborSet, this.getNumberOfRounds());
		
	}

	/**
	 * @param sampleNeighborSet
	 * @return
	 */
	private Collection<Node> collectNodesToIds(Set<Integer> sampleNeighborSet, Graph g) {
		Collection<Node> ns = new ArrayList<Node>();
		
		for(Integer i : sampleNeighborSet){
			ns.add(g.getNode(i));
		}
		
		return ns;
	}
	
	public NetworkSample cleanInstance() {
		return new NetworkSampleWithNeighborSet(this.algorithm, this.scaledown, this.dimension, this.revisiting);
		
	}

}
