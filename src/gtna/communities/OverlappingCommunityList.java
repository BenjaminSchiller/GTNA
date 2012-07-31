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
import java.util.HashMap;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.io.Filereader;
import gtna.io.Filewriter;
import gtna.util.Config;

/**
 * An <code>OverlappingCommunityList</code> is nearly identical to a
 * <code>CommunityList</code>, only that by convention a node can be in more
 * than one community of this community list. This means that
 * <code>getCommunityOfNode(int node)</code> now returns a
 * <code>Community[]</code> of all the communities in which the node is.
 * 
 * @author Philipp Neubrand
 * 
 */
public class OverlappingCommunityList implements GraphProperty {
	private Community[] communities;
	private HashMap<Integer, ArrayList<Community>> communityOfNode;

	/**
	 * Dummy constructor to be able to create empty
	 * <code>OverlappingCommunityList</code> objects from within extending
	 * classes.
	 */
	protected OverlappingCommunityList() {

	}

	/**
	 * Standard constructor for a <code>CommunityList</code> from an
	 * <code>ArrayList<Community></code> containing all the communities for the
	 * list.
	 * 
	 * @param communities
	 *            The communities for this <code>CommunityList</code>.
	 */
	public OverlappingCommunityList(ArrayList<Community> communities) {
		this.communities = new Community[communities.size()];
		for (int i = 0; i < communities.size(); i++) {
			this.communities[i] = communities.get(i);
		}
		this.computeCommunityOfNodes();
	}

	/**
	 * Standard constructor for a <code>OverlappingCommunityList/code> from a
	 * <code>Community[]</code> containing all the communities for the list.
	 * 
	 * @param communities
	 *            The communities for this <code>OverlappingCommunityList</code>
	 *            .
	 */
	public OverlappingCommunityList(Community[] communities) {
		this.communities = communities;
		this.computeCommunityOfNodes();
	}

	private void computeCommunityOfNodes() {
		this.communityOfNode = new HashMap<Integer, ArrayList<Community>>();
		for (Community c : this.communities) {
			for (int n : c.getNodes()) {
				if (!communityOfNode.containsKey(n))
					communityOfNode.put(n, new ArrayList<Community>());
				this.communityOfNode.get(n).add(c);
			}
		}
	}

	/**
	 * Getter for the communities of the supplied node.
	 * 
	 * @param node
	 *            The index of the node to be looked up.
	 * @return The <code>Community</code> of the supplied node.
	 */
	public ArrayList<Community> getCommunityOfNode(int id) {
		return communityOfNode.get(id);
	}

	/**
	 * Getter for the communities in this community list.
	 * 
	 * @return The communities in this community list.
	 */
	public Community[] getCommunities() {
		return communities;
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
