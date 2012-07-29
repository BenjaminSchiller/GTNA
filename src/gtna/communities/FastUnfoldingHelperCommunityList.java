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
 * Helper class for the FastUnfolding community detection algorithm, is pretty
 * similar to gtna.communities.CommunityList but provides additional
 * functionality.
 * 
 * @author Philipp Neubrand
 * 
 */
public class FastUnfoldingHelperCommunityList<T extends ChangeableCommunity> extends ChangeableCommunityList<T> {
	
	public FastUnfoldingHelperCommunityList(){
	  super();	
	}
	
	public FastUnfoldingHelperCommunityList(ArrayList<T> communities) {
		super(communities);
	}

	public FastUnfoldingHelperCommunityList(T[] communities) {
		super(communities);
	}

	public void normalizeIDs() {
		HashMap<Integer, T> n = new HashMap<Integer, T>();
		int i = 0;
		for (T akt : communities.values()) {

			n.put(i, akt);
			akt.setIndex(i);
			i++;
		}
		communities = n;
	}

}
