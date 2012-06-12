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
 * CommunityList.java
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

import gtna.graph.Node;

/**
 * @author Flipp
 *
 */
public class RWCommunityList {
	ArrayList<RWCommunity> cs = new ArrayList<RWCommunity>();

	/**
	 * @return
	 */
	public RWCommunity[] getCommunities() {
		return cs.toArray(new RWCommunity[cs.size()]);
	}

	/**
	 * 
	 */
	public void sortCommunities() {
		RWCommunity temp;
		System.out.println("Sorting");
		for(int i = cs.size(); i > 1; i--){
			for(int j = 0; j < i-1; j++){
				if(cs.get(j).computeGamma() < cs.get(j+1).computeGamma()){
					temp = cs.get(j);
					cs.set(j, cs.get(j+1));
					cs.set(j+1, temp);
				}
					
			}
		}
		
		
	}

	/**
	 * 
	 */
	public void removeEmptyCommunities() {
		ArrayList<RWCommunity> ncs = new ArrayList<RWCommunity>();
		for(RWCommunity akt : cs){
			if(akt.getNodes().length > 0)
				ncs.add(akt);
		}
		if(ncs.size() < cs.size())
			System.out.println("Removed " + (cs.size() - ncs.size()) );
		cs = ncs;
		
	}

	/**
	 * @param aktCom
	 * @return
	 */
	public boolean contains(RWCommunity aktCom) {
		boolean ret = false;
		for(RWCommunity akt : cs){
			if(aktCom.equals(akt))
				ret = true;
		}
		
		return ret;
	}

	/**
	 * @param aktCom
	 */
	public void add(RWCommunity aktCom) {
		cs.add(aktCom);
	}

	/**
	 * @param a
	 * @return
	 */
	public boolean containsNode(Node a) {
		boolean c = false;
		for(RWCommunity akt : cs){
			if(akt.contains(a))
				c = true;
		}
		
		return c;
	}

}
