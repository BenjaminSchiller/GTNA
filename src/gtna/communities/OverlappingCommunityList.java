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
 * overlappingCommunityList.java
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

import gtna.graph.Graph;
import gtna.graph.GraphProperty;

/**
 * @author Philipp Neubrand
 *
 */
public class OverlappingCommunityList<T extends Community> implements GraphProperty {
	protected ArrayList<T> communities = new ArrayList<T>();
	
	public void removeEmptyCommunities() {

		ArrayList<T> ncs = new ArrayList<T>();
		for(T akt : communities){
			if(akt.getNodes().length > 0)
				ncs.add(akt);
		}

		communities = ncs;
	}
	
	public boolean contains(T aktCom) {
		boolean ret = false;
		for(T akt : communities){
			if(aktCom.equals(akt))
				ret = true;
		}
		
		return ret;
	}
	
	public boolean containsNode(int node) {
		boolean c = false;
		for(T akt : communities){
			if(akt.contains(node))
				c = true;
		}
		
		return c;
	}
	
	@SuppressWarnings("unchecked")
	public T[] getCommunities(){
		return (T[]) communities.toArray(new Object[communities.size()]);
	}
	
	/**
	 * @param aktCom
	 */
	public void addCommunity(T aktCom) {
		communities.add(aktCom);
	}


	@Override
	public boolean write(String filename, String key) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub
		
	}

}
