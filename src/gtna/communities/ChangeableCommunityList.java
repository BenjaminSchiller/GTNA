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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * A <code>ChangeableCommunityList</code> is a <code>CommunityList</code> that
 * can be altered after creation. The class is generic so that it can be
 * "specialized" for different <code>Community</code> classes. If a
 * <code>ChangeableCommunityList</code> with different <code>Community</code>
 * classes is needed, the List can still be created with <code>Community</code>
 * as the generic class.
 * 
 * @author Philipp Neubrand
 * 
 */
public class ChangeableCommunityList<T extends Community> extends CommunityList {
	protected HashMap<Integer, T> communities = new HashMap<Integer, T>();
	protected HashMap<Integer, T> nodeBuffer = new HashMap<Integer, T>();

	/**
	 * Standard constructor, creates an empty
	 * <code>ChangeableCommunityList</code>.
	 */
	public ChangeableCommunityList() {

	}

	/**
	 * Standard constructor for a <code>ChangeableCommunityList</code>, supplied
	 * is an <code>ArrayList<T></code> containing the communities of the list.
	 * 
	 * @param communities
	 *            The communities of the list.
	 */
	public ChangeableCommunityList(ArrayList<T> communities) {
		super();
		for (T akt : communities) {
			this.communities.put(akt.getIndex(), akt);
		}
		
		this.computeCommunityOfNodes();
	}

	/**
	 * Standard constructor for a <code>ChangeableCommunityList</code>, supplied
	 * is an <code>T[]</code> containing the communities of the list.
	 * 
	 * @param communities
	 *            The communities of the list.
	 */
	public ChangeableCommunityList(T[] communities) {
		super();
		for (T akt : communities) {
			this.communities.put(akt.getIndex(), akt);
		}
		
		this.computeCommunityOfNodes();
	}
	
	protected void computeCommunityOfNodes(){
		for(T akt : communities.values()){
			for(int node : akt.getNodes())
				nodeBuffer.put(node, akt);
		}
	}

	// The cast is save as only Ts can be added to the list
	@SuppressWarnings("unchecked")
	@Override
	public T[] getCommunities() {
		T[] array = (T[]) Array.newInstance( communities.values().iterator().next().getClass(), communities.size() );
		return (T[]) communities.values().toArray(array);
	}

	@Override
	public T getCommunityOfNode(int nodeIndex) {
		return nodeBuffer.get((Integer) nodeIndex);
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		// CLASS
		fw.writeComment(Config.get("GRAPH_PROPERTY_CLASS"));
		fw.writeln(CommunityList.class.getClass().getCanonicalName().toString());

		// KEYS
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);

		// # OF COMMUNITIES
		fw.writeComment("Communities");
		fw.writeln(this.communities.size());

		fw.writeln();

		// LIST OF COMMUNITIES
		for (Community community : this.communities.values()) {
			fw.writeln(community.toString());
		}

		return fw.close();
	}

	/**
	 * Adds a community of type <T> to this list.
	 * 
	 * @param com
	 *            The community to be added.
	 */
	public void addCommunity(T com) {
		communities.put(com.getIndex(), com);
		for (int akt : com.getNodes())
			nodeBuffer.put(akt, com);

	}

	/**
	 * Removes the community from the list.
	 * 
	 * @param com
	 *            The community to be removed.
	 */
	public void removeCommunity(T com) {
		
		communities.remove(com.getIndex());
		for (int akt : com.getNodes())
			nodeBuffer.remove(akt);

	}

	/**
	 * Getter for the community with the supplied ID.
	 * 
	 * @param id
	 *            The ID that is to be looked up.
	 * @return The community with the supplied ID.
	 */
	public T getCommunityByID(int id) {
		return communities.get(id);
	}

	/**
	 * Sets the community mapping of the supplied node to the supplied
	 * community.
	 * 
	 * @param node
	 *            The node that is to be set.
	 * @param community
	 *            The community of the node.
	 */
	public void setCommunity(int node, T community) {
		nodeBuffer.put(node, community);
	}

}
