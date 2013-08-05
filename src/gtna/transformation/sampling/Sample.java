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
 * Sample.java
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

import gtna.graph.GraphProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tim
 *
 */
public class Sample extends GraphProperty {
	private Map<Integer, Integer> nodeIdMapping;
	private Map<Integer, List<Integer>> revisitFrequency;
	
	
	/**
	 * 
	 */
	public Sample() {
		this.nodeIdMapping = new HashMap<Integer, Integer>();
		this.revisitFrequency = new HashMap<Integer, List<Integer>>();
	}

	/* (non-Javadoc)
	 * @see gtna.graph.GraphProperty#write(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean write(String filename, String key) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see gtna.graph.GraphProperty#read(java.lang.String)
	 */
	@Override
	public String read(String filename) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Returns the old node id of a sampled node or a negative integer if the node is not sampled
	 * @param newId	:	new node id
	 * @return			old node id <br>
	 * 					integer < 0 if not sampled
	 */
	public int getOldNodeId(int newId){
		if(nodeIdMapping.containsValue(newId)){
			for(Map.Entry<Integer, Integer> e : nodeIdMapping.entrySet()){
				if(e.getValue() == newId){
					return e.getKey();
				}
			}
		}
		
		return -1;
	}

	/**
	 * Returns the new node id of a sampled node or a negative integer if the node is not sampled
	 * @param oldId : node id in the original graph
	 * @return			new node id <br>
	 * 					integer < 0 if not sampled
	 */
	public int getNewNodeId(int oldId){
		if(nodeIdMapping.containsKey(oldId)){
			return nodeIdMapping.get(oldId);
		} else return -1;
		
	}
}
