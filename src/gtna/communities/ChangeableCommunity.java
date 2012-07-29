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
 * ChangeableCommunity.java
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

import gtna.util.Config;

import java.util.ArrayList;

/**
 * A <code>ChangeableCommunity</code> is a <code>Community</code> that can be
 * altered by adding or removing nodes after its creation.
 * 
 * @author Philipp Neubrand
 * 
 */
public class ChangeableCommunity extends Community {
	protected ArrayList<Integer> nodes = new ArrayList<Integer>();

	/**
	 * Standard Constructor for a <code>ChangeableCommunity</code>, supplied are
	 * the ID and an <code>ArrayList<Integer></code> containing the nodes of the
	 * community.
	 * 
	 * @param index
	 *            The ID of this community.
	 * @param nodes
	 *            The list of nodes of this community.
	 */
	public ChangeableCommunity(int index, ArrayList<Integer> nodes) {
		super(index);
		for (Integer akt : nodes) {
			this.nodes.add(akt);
		}
	}

	/**
	 * Standard Constructor for a <code>ChangeableCommunity</code>, supplied are
	 * the ID and an <code>int[]</code> containing the nodes of this community.
	 * 
	 * @param index
	 *            The ID of this community.
	 * @param nodes
	 *            The nodes of this community.
	 */
	public ChangeableCommunity(int index, int[] nodes) {
		super(index);
		for (int akt : nodes) {
			this.nodes.add(akt);
		}
	}

	/**
	 * Standard Constructor for an empty <code>ChangeableCommunity</code> with
	 * only an ID.
	 * 
	 * @param index
	 *            The ID of this Community.
	 */
	public ChangeableCommunity(int index) {
		super(index);
	}

	/**
	 * Constructor to create a <code>ChangeableCommunity</code> from the String
	 * that is returned by <code>getString()</code>.
	 * 
	 * @param string
	 *            The string from which to create the Community.
	 */
	public ChangeableCommunity(String string) {
		super();
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		String[] temp1 = string.split(sep1);
		this.index = Integer.parseInt(temp1[0]);
		int tempInt = 0;
		if (temp1.length < 2 || temp1[1].length() == 0) {

		} else {
			String[] temp2 = temp1[1].split(sep2);

			for (int i = 0; i < temp2.length; i++) {
				tempInt = Integer.parseInt(temp2[i]);
				nodes.add(tempInt);
			}
		}
	}

	@Override
	public int[] getNodes() {
		int[] ret = new int[nodes.size()];
		int i = 0;
		for (int akt : nodes) {
			ret[i] = akt;
			i++;
		}

		return ret;
	}

	@Override
	public int size() {
		return nodes.size();
	}

	/**
	 * Adds a node to be added to the <code>Community</code>
	 * 
	 * @param node
	 *            The node to be added.
	 */
	public void addNode(int node) {
		nodes.add(node);
	}

	/**
	 * Tries to remove the supplied node from this <code>Community</code>. If it
	 * is not part of it, nothing will be changed.
	 * 
	 * @param node
	 *            The node that is to be removed.
	 */
	public void removeNode(int node) {
		nodes.remove(node);
	}

	public boolean contains(int node) {
		boolean ret = false;
		for (int akt : nodes) {
			if (node == akt)
				ret = true;
		}

		return ret;
	}

	/**
	 * Setter for the ID of this <code>Community</code>.
	 * 
	 * @param index
	 *            The new ID of this community.
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		String sep1 = Config.get("GRAPH_PROPERTY_SEPARATOR_1");
		String sep2 = Config.get("GRAPH_PROPERTY_SEPARATOR_2");
		StringBuffer buff = new StringBuffer(this.index + sep1);
		if (this.nodes.size() == 0) {
			return buff.toString();
		}

		int i = 0;
		for (int akt : nodes) {
			if (i != 0)
				buff.append(sep2);
			buff.append(akt);
			i++;
		}
		return buff.toString();
	}

}
