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
package gtna.transformation.communities.fastunfolding;

import java.util.Collection;
import java.util.HashMap;
import gtna.graph.Node;

/**
 * Helper class for the FastUnfolding community detection algorithm, is pretty
 * similar to gtna.communities.CommunityList but provides additional
 * functionality.
 * 
 * @author Philipp Neubrand
 * 
 */
public class FastUnfoldingHelperCommunityList {

	private HashMap<Integer, FastUnfoldingHelperCommunity> comsByID = new HashMap<Integer, FastUnfoldingHelperCommunity>();
	private HashMap<Node, FastUnfoldingHelperCommunity> comsByNode = new HashMap<Node, FastUnfoldingHelperCommunity>();

	public Collection<FastUnfoldingHelperCommunity> getCommunities() {
		return comsByID.values();
	}

	public FastUnfoldingHelperCommunity getCommunityByNode(Node aktNode) {
		return comsByNode.get(aktNode);
	}

	public FastUnfoldingHelperCommunity getCommunityByID(int newCom) {
		return comsByID.get(newCom);
	}

	public void add(FastUnfoldingHelperCommunity community) {
		comsByID.put(community.getIndex(), community);
	}

	public void removeCom(FastUnfoldingHelperCommunity tempC) {
		comsByID.remove(tempC.getIndex());

	}

	public void normalizeIDs() {
		HashMap<Integer, FastUnfoldingHelperCommunity> n = new HashMap<Integer, FastUnfoldingHelperCommunity>();
		int i = 0;
		for (FastUnfoldingHelperCommunity akt : comsByID.values()) {

			n.put(i, akt);
			akt.setID(i);
			i++;
		}
		comsByID = n;
	}

	public void setCommunity(Node node, FastUnfoldingHelperCommunity community) {
		comsByNode.put(node, community);

	}

}
