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
package gtna.communities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A <code>NormalizableCommunityList</code> is a
 * <code>ChangeableCommunityList</code> that can normalize the IDs of the
 * communities it contains. Normalized community IDs are between 0 and (n - 1)
 * where.
 * 
 * This could theoretically be done in the <code>ChangeableCommunityList</code>
 * class, but normalizing is only possible with at least
 * <code>ChangeableCommunity</code>. So in order to not limit the use of
 * <code>ChangableCommunity</code> it is an extra class.
 * 
 * @author Philipp Neubrand
 * 
 */
public class NormalizableCommunityList<T extends ChangeableCommunity> extends
		ChangeableCommunityList<T> {

	/**
	 * Standard constructor, creates an empty
	 * <code>NormalizableCommunityList</code>.
	 */
	public NormalizableCommunityList() {
		super();
	}

	/**
	 * Standard constructor for a <code>NormalizableCommunityList</code>,
	 * supplied is an <code>ArrayList<T></code> containing the communities of
	 * the list.
	 * 
	 * @param communities
	 *            The communities of the list.
	 */
	public NormalizableCommunityList(ArrayList<T> communities) {
		super(communities);
	}

	/**
	 * Standard constructor for a <code>NormalizableCommunityList</code>,
	 * supplied is an <code>T[]</code> containing the communities of the list.
	 * 
	 * @param communities
	 *            The communities of the list.
	 */
	public NormalizableCommunityList(T[] communities) {
		super(communities);
	}

	/**
	 * Normalizes the IDs of the communities, making sure the IDs are between 0
	 * and (size() - 1).
	 */
	public void normalizeIDs() {
		HashMap<Integer, T> n = new HashMap<Integer, T>();
		nodeBuffer = new HashMap<Integer, T>();
		int i = 0;
		for (T akt : communities.values()) {

			n.put(i, akt);
			akt.setIndex(i);
			for (int aktNode : akt.getNodes())
				nodeBuffer.put(aktNode, akt);
			i++;
		}
		communities = n;
	}

}
