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

import java.util.Collection;
import java.util.HashMap;
import gtna.graph.Node;

/**
 * @author Flipp
 * 
 */
public class CommunityList {

	private HashMap<Integer, Community> comsByID = new HashMap<Integer, Community>();
	private HashMap<Node, Community> comsByNode = new HashMap<Node, Community>();

	/**
	 * @return
	 */
	public Collection<Community> getCommunities() {
		return comsByID.values();
	}

	/**
	 * @param aktNode
	 * @return
	 */
	public Community getCommunityByNode(Node aktNode) {
		return comsByNode.get(aktNode);	}

	/**
	 * @param newCom
	 * @return
	 */
	public Community getCommunityByID(int newCom) {
		return comsByID.get(newCom);
	}

	/**
	 * @param community
	 */
	public void add(Community community) {
		comsByID.put(community.getIndex(), community);
	}

	/**
	 * @param tempC
	 */
	public void removeCom(Community tempC) {
		comsByID.remove(tempC.getIndex());

	}

	/**
	 * 
	 */
	public void normalizeIDs() {
		HashMap<Integer, Community> n = new HashMap<Integer, Community>();
		int i = 0;
		for (Community akt : comsByID.values()) {

			n.put(i, akt);
			akt.setID(i);
			i++;
		}
		comsByID = n;
	}

	/**
	 * @param node
	 * @param community
	 */
	public void setCommunity(Node node, Community community) {
		comsByNode.put(node,community);
		
	}

}
