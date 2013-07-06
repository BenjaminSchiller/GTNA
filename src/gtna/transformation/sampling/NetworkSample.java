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
 * NetworkSample.java
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
package gtna.transformation.sampling;

import gtna.graph.Node;
import gtna.util.parameter.Parameter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Tim
 *
 */
public class NetworkSample extends Parameter {

	Map<Integer, List<Integer>> revisitFrequency;
	Map<Integer, Integer> sampleNodeMapping;

	/**
	 * @param key
	 * @param value
	 */
	public NetworkSample(String key, String value) {
		super(key, value);
		
		sampleNodeMapping = new HashMap<Integer, Integer>();
		revisitFrequency = new HashMap<Integer, List<Integer>>();
	}
	
	
		
	
	/**
	 * Add the given node in the given round to the sample
	 * @param n			Node, added to the sample
	 * @param round		Round in which the node is sampled
	 * @return			true if added
	 */
	public boolean addNodeToSample(Node n, int round){
		if(!initialized()){throw new IllegalStateException("NetworkSample is not initialized!");}
		if(!uniqueRound(round)){throw new IllegalArgumentException("round has to be unique but round = "+round+" was already seen");}
		
		
		if(!sampleNodeMapping.containsKey(n.getIndex())){
			// add node to the sample and initialize the RF for this node
			int newId = sampleNodeMapping.size();
			sampleNodeMapping.put(n.getIndex(), newId);
			
			List<Integer> rF = new LinkedList<Integer>();
			rF.add(round);
			revisitFrequency.put(n.getIndex(), rF);
		} else {
			// no need to add the node to the sample again, just add it to the RF
			List<Integer> rF = revisitFrequency.get(n.getIndex());
			rF.add(round);
			revisitFrequency.put(n.getIndex(), rF);
		}
		
		
		
		return true;
	}
	
	/**
	 * @param n
	 * @return
	 */
	public boolean contains(Node n) {
		if(sampleNodeMapping.containsKey(n.getIndex())){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Return the RevisitFrequency-Map for the whole sampling
	 * Key = nodeID (from the node in the original graph)
	 * Value = List of the rounds in which the node was visited
	 * 
	 * (in this default implementation Value.get(1) is the nodeId in the subgraph induced by the sampling)
	 * @return
	 */
	public Map<Integer, List<Integer>> getRevisitFrequency(){
		return revisitFrequency;
	}
	
	/**
	 * Return the revisitFrequency for the given nodeId
	 * @param nodeId
	 * @return	List of rounds in which the given node is visited.
	 */
	public List<Integer> getRevisitFrequency(int nodeId){
		if(revisitFrequency.containsKey(nodeId)){
			return revisitFrequency.get(nodeId);
		} 
		
		return new LinkedList<Integer>();
	}
	
	/**
	 * Return the sample as Map of:
	 * 	Key = nodeID in the original graph
	 *  Value = nodeID in the subgraph induced by the sampling
	 * @return
	 */
	public Map<Integer, Integer> getSampleNodeMapping(){
		return sampleNodeMapping;
	}
	
	/**
	 * returns the current size of the sample
	 * @return
	 */
	public int getSampleSize(){
		return sampleNodeMapping.size();
	}
	
	/**
	 * Checks if the necessary maps are initialized 
	 * @return
	 */
	private boolean initialized(){
		if(sampleNodeMapping != null && revisitFrequency != null){
			return true;
		}
		
		return false;
	}
	
	/**
	 * checks if the given round is already known
	 * known = any node is visited in this round
	 * @param round
	 * @return
	 */
	private boolean uniqueRound(int round){
		for(List<Integer> l : revisitFrequency.values()){
			if(l.contains(round)){
				return false;
			}
		}
		
		return true;
	}

}
