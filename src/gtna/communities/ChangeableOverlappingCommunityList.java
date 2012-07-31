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
 * ChangeableOverlappingCommunityList.java
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

import gtna.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Philipp Neubrand
 * 
 */
public class ChangeableOverlappingCommunityList<T extends Community> extends
		OverlappingCommunityList {
	protected ArrayList<T> communities = new ArrayList<T>();
	protected HashMap<Integer, ArrayList<Community>> nodeBuffer = new HashMap<Integer, ArrayList<Community>>();

	/**
	 * Standard constructor, creates an empty
	 * <code>ChangeableOverlappingCommunityList</code>.
	 */
	public ChangeableOverlappingCommunityList() {

	}

	/**
	 * Standard constructor for a <code>ChangeableCommunityList</code>, supplied
	 * is an <code>ArrayList<T></code> containing the communities of the list.
	 * 
	 * @param communities
	 *            The communities of the list.
	 */
	public ChangeableOverlappingCommunityList(ArrayList<T> communities) {
		super();
		for (T akt : communities) {
			this.communities.add(akt);
		}

		this.computeCommunityOfNodes();
	}

	protected void computeCommunityOfNodes() {
		for (T akt : communities) {
			for (int node : akt.getNodes()) {
				if (!nodeBuffer.containsKey(node))
					nodeBuffer.put(node, new ArrayList<Community>());

				nodeBuffer.get(node).add(akt);
			}
		}
	}

	/**
	 * Standard constructor for a <code>ChangeableCommunityList</code>, supplied
	 * is an <code>T[]</code> containing the communities of the list.
	 * 
	 * @param communities
	 *            The communities of the list.
	 */
	public ChangeableOverlappingCommunityList(T[] communities) {
		super();
		for (T akt : communities) {
			this.communities.add(akt);
		}

		this.computeCommunityOfNodes();
	}

	// The cast is save as only Ts can be added to the list
	@SuppressWarnings("unchecked")
	@Override
	public T[] getCommunities() {
		return (T[]) communities.toArray(new Object[communities.size()]);
	}

	@Override
	public ArrayList<Community> getCommunityOfNode(int nodeIndex) {
		return nodeBuffer.get(nodeIndex);
	}

	@Override
	public boolean write(String filename, String key) {
		return false;
	}

	@Override
	public void read(String filename, Graph graph) {

	}

	/**
	 * Adds the supplied community to the community list.
	 * 
	 * @param com
	 *            The community to be added.
	 */
	public void addCommunity(T com) {
		communities.add(com);
		for (int node : com.getNodes()) {
			if (!nodeBuffer.containsKey(node))
				nodeBuffer.put(node, new ArrayList<Community>());
			nodeBuffer.get(node).add(com);
		}
	}

	/**
	 * Removes the supplied community from the list.
	 * 
	 * @param com
	 *            The community that is to be removed.
	 */
	public void removeCommunity(T com) {
		communities.remove(com);
		for (int node : com.getNodes()) {
			nodeBuffer.get(node).remove(com);
		}
	}
}
