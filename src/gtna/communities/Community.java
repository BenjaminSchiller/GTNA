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
 * Community.java
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

import gtna.util.Config;

import java.util.ArrayList;

/**
 * A <code>Community</code> is basically a cluster of nodes with an ID. A
 * <code>Community</code> object can not be changed after creation,
 * <code>ChangeableCommunity</code> allows alteration after creation.
 * 
 * @author Benjamin Schiller
 * 
 */
public class Community {
	// the index of the community
	protected int index;
	// the nodes in this community
	private int[] nodes;

	/**
	 * Standard Constructor for a <code>Community</code>, supplied are the ID
	 * and an <code>ArrayList<Integer></code> containing the nodes of the
	 * community.
	 * 
	 * @param index
	 *            The ID of this community.
	 * @param nodes
	 *            The list of nodes of this community.
	 */
	public Community(int index, ArrayList<Integer> nodes) {
		this.index = index;
		this.nodes = new int[nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			this.nodes[i] = nodes.get(i);
		}
	}

	/**
	 * Standard Constructor for a <code>Community</code>, supplied are the ID
	 * and an <code>int[]</code> containing the nodes of this community.
	 * 
	 * @param index
	 *            The ID of this community.
	 * @param nodes
	 *            The nodes of this community.
	 */
	public Community(int index, int[] nodes) {
		this.index = index;
		this.nodes = nodes;
	}

	/**
	 * Constructor to create a <code>Community</code> from the String that is
	 * returned by <code>getString()</code>.
	 * 
	 * @param string
	 *            The string from which to create the Community.
	 */
	public Community(String string) {
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		String[] temp1 = string.split(sep1);
		this.index = Integer.parseInt(temp1[0]);
		if (temp1.length < 2 || temp1[1].length() == 0) {
			this.nodes = new int[0];
		} else {
			String[] temp2 = temp1[1].split(sep2);
			this.nodes = new int[temp2.length];
			for (int i = 0; i < temp2.length; i++) {
				this.nodes[i] = Integer.parseInt(temp2[i]);
			}
		}
	}

	/**
	 * Dummy empty constructors to be able to create empty communities from
	 * within extending classes.
	 * 
	 */
	protected Community(int index) {
		this.index = index;
	}

	protected Community() {
	}

	public String toString() {
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		StringBuffer buff = new StringBuffer(this.index + sep1);
		if (this.nodes.length == 0) {
			return buff.toString();
		}
		buff.append(this.nodes[0]);
		for (int i = 1; i < this.nodes.length; i++) {
			buff.append(sep2 + this.nodes[i]);
		}
		return buff.toString();
	}

	/**
	 * Getter for the nodes of this Community.
	 * 
	 * @return The nodes of this Community.
	 */
	public int[] getNodes() {
		return this.nodes;
	}

	/**
	 * Getter for the ID of this Community.
	 * 
	 * @return The ID of this Community.
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * Getter for the size of this Community.
	 * 
	 * @return The Community size.
	 */
	public int size() {
		return getNodes().length;
	}

	/**
	 * Checks whether or not the node with the supplied ID is in this Community
	 * or not.
	 * 
	 * @param node
	 *            The node that is to be checked.
	 * @return <code>true</code> if the node is in this Community,
	 *         <code>false</code> else.
	 */
	public boolean contains(int node) {
		boolean ret = false;
		for (int akt : nodes) {
			if (node == akt)
				ret = true;
		}

		return ret;
	}

}
