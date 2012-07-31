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

import gtna.communities.ChangeableOverlappingCommunityList;
import gtna.communities.Community;

/**
 * A <code>RWCommunityList</code> is a
 * <code>ChangeableOverlappingCommunityList</code> with some additional
 * functionality needed by the <code>CDRandomWalk</code> community detection
 * algorithm.
 * 
 * @author Philipp Neubrand
 * 
 */
public class RWCommunityList<T extends RWCommunity> extends
		ChangeableOverlappingCommunityList<T> {

	/**
	 * Removes empty communities from the community list.
	 */
	public void removeEmptyCommunities() {
		ArrayList<T> ncs = new ArrayList<T>();
		for (T akt : communities) {
			if (akt.getNodes().length > 0)
				ncs.add(akt);
		}

		communities = ncs;
	}

	/**
	 * Sorts the communities according to their community rank.
	 */
	public void sortCommunities() {
		T temp;
		for (int i = communities.size(); i > 1; i--) {
			for (int j = 0; j < i - 1; j++) {
				if (communities.get(j).getCommunityRank() < communities.get(
						j + 1).getCommunityRank()) {
					temp = communities.get(j);
					communities.set(j, communities.get(j + 1));
					communities.set(j + 1, temp);
				}

			}
		}
	}

	/**
	 * Checks whether or not a node is part of any of the communities.
	 * 
	 * @param node
	 *            The node that should be checked.
	 * @return <code>true</code> if the node is part of any community,
	 *         <code>false</code> else.
	 */
	public boolean containsNode(int node) {
		boolean c = false;
		for (Community akt : communities) {
			if (akt.contains(node)) {
				c = true;
				break;
			}
		}

		return c;
	}

	/**
	 * Checks whether or not a community is part of this
	 * <code>OverlappingCommunityList</code>.
	 * 
	 * @param aktCom
	 *            The community that is to be checked.
	 * @return <code>true</code> if the community is part of this list,
	 *         <code>false</code> otherwise.
	 */
	public boolean contains(Community com) {
		boolean ret = false;
		for (Community akt : communities) {
			if (com.equals(akt))
				ret = true;
		}

		return ret;
	}

}
