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
 * ChangeableCommunityList.java
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
package gtna.communities;

import java.util.ArrayList;
import java.util.HashMap;

import gtna.graph.Graph;

/**
 * @author Flipp
 *
 */
public class ChangeableCommunityList<T extends Community> extends CommunityList {
	protected HashMap<Integer, T> communities = new HashMap<Integer, T>();
	protected HashMap<Integer, T> nodeBuffer = new HashMap<Integer, T>();
	
	public ChangeableCommunityList(){
		
	}

	public ChangeableCommunityList(ArrayList<T> communities) {
		super();
		for(T akt : communities){
			this.communities.put(akt.getIndex(), akt);
		}
	}


	public ChangeableCommunityList(T[] communities) {
		super();
		for(T akt : communities){
			this.communities.put(akt.getIndex(), akt);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] getCommunities() {
		return (T[]) communities.values().toArray(new Object[communities.size()]);
	}


	@Override
	public T getCommunityOfNode(int nodeIndex) {
		return nodeBuffer.get(nodeIndex);
	}


	@Override
	public boolean write(String filename, String key) {
		return false;
	}


	@Override
	public void read(String filename, Graph graph) {
		

	}
	
	public void addCommunity(T com){ 
		communities.put(com.getIndex(), com);
		for(int akt : com.getNodes())
			nodeBuffer.put(akt, com);
		
	}
	
	public void removeCommunity(T com){
		communities.remove(com);
		for(int akt : com.getNodes())
			nodeBuffer.remove(akt);
		
	}
	
	public T getCommunityByID(int id){
		return communities.get(id);
	}
	
	public void setCommunity(int node, T community) {
		nodeBuffer.put(node, community);
	}

	
	
	
	
}
