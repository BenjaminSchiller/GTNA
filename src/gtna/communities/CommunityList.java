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
 * Communities.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.communities;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A <code>CommunityList</code> is a list of <code>Community</code> objects. The
 * convention is that every node can only be part in one of the communities,
 * however for performance reasons there is no code checking this. If
 * overlapping communities are needed, the class
 * <code>OverlappingCommunityList</code> should be used instead.
 * 
 * After creation the <code>CommunityList</code> can not be altered. If changes
 * after creation are needed, <code>ChangeableCommunityList</code> should be
 * used.
 * 
 * @author Benjamin Schiller
 * 
 */
public class CommunityList implements GraphProperty {
	private Community[] communities;

	private int[] communityOfNode;

	/**
	 * Dummy constructor to be able to create empty <code>CommunityList</code>
	 * objects from within extending classes.
	 */
	protected CommunityList() {

	}

	/**
	 * Standard constructor for a <code>CommunityList</code> from a
	 * <code>HashMap<Integer, Integer></code> containing a mapping from node
	 * indices to normalized community indices.
	 * 
	 * @param map
	 *            The mapping of node indices to community indices.
	 */
	public CommunityList(HashMap<Integer, Integer> map) {
		this(CommunityList.compute(map));
	}

	private static Community[] compute(HashMap<Integer, Integer> map) {
		int max = 0;
		for (int value : map.values()) {
			if (value > max) {
				max = value;
			}
		}
		HashMap<Integer, ArrayList<Integer>> communities = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 0; i <= max; i++) {
			communities.put(i, new ArrayList<Integer>());
		}
		for (int index : map.keySet()) {
			int community = map.get(index);
			communities.get(community).add(index);
		}
		Community[] c = new Community[max + 1];
		for (int i = 0; i < c.length; i++) {
			c[i] = new Community(i, communities.get(i));
		}
		return c;
	}

	/**
	 * Standard constructor for a <code>CommunityList</code> from an
	 * <code>ArrayList<Community></code> containing all the communities for the
	 * list.
	 * 
	 * @param communities
	 *            The communities for this <code>CommunityList</code>.
	 */
	public CommunityList(ArrayList<Community> communities) {
		this.communities = new Community[communities.size()];
		for (int i = 0; i < communities.size(); i++) {
			this.communities[i] = communities.get(i);
		}
		this.computeCommunityOfNodes();
	}

	/**
	 * Standard constructor for a <code>CommunityList/code> from a
	 * <code>Community[]</code> containing all the communities for the list.
	 * 
	 * @param communities
	 *            The communities for this <code>CommunityList</code>.
	 */
	public CommunityList(Community[] communities) {
		this.communities = communities;
		this.computeCommunityOfNodes();
	}

	private void computeCommunityOfNodes() {
		int sum = 0;
		for (Community c : this.communities) {
			sum += c.getNodes().length;
		}
		this.communityOfNode = new int[sum];
		for (Community c : this.communities) {
			for (int n : c.getNodes()) {
				this.communityOfNode[n] = c.getIndex();
			}
		}
	}

	/**
	 * Getter for the communities of this <code>CommunityList</code>.
	 * 
	 * @return A <code>Community[]</code> containing all the communities of this
	 *         <code>CommunityList</code>.
	 */
	public Community[] getCommunities() {
		return communities;
	}

	/**
	 * Getter for the <code>Community</code> of the supplied node.
	 * 
	 * @param node
	 *            The index of the node to be looked up.
	 * @return The <code>Community</code> of the supplied node.
	 */
	public Community getCommunityOfNode(int node) {
		return communities[communityOfNode[node]];
	}

	@Override
	public boolean write(String filename, String key) {
		Filewriter fw = new Filewriter(filename);

		// CLASS
		fw.writeComment(Config.get("GRAPH_PROPERTY_CLASS"));
		fw.writeln(this.getClass().getCanonicalName().toString());

		// KEYS
		fw.writeComment(Config.get("GRAPH_PROPERTY_KEY"));
		fw.writeln(key);

		// # OF COMMUNITIES
		fw.writeComment("Communities");
		fw.writeln(this.communities.length);

		fw.writeln();

		// LIST OF COMMUNITIES
		for (Community community : this.communities) {
			fw.writeln(community.toString());
		}

		return fw.close();
	}

	@Override
	public void read(String filename, Graph graph) {
		Filereader fr = new Filereader(filename);

		// CLASS
		fr.readLine();

		// KEYS
		String key = fr.readLine();

		// # OF COMMUNITIES
		int communities = Integer.parseInt(fr.readLine());
		this.communities = new Community[communities];

		// COMMUNITIES
		String line = null;
		int index = 0;
		while ((line = fr.readLine()) != null) {
			this.communities[index++] = new Community(line);
		}

		fr.close();

		this.computeCommunityOfNodes();

		graph.addProperty(key, this);
	}
}
