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
 * CBufferingCommunity.java
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
 * A <code>CBufferingCommunity</code> is a <code>ChangeableCommunity</code> that
 * has an additional buffer to speed up the <code>contains(int node)</code>
 * function by a rather big factor.
 * 
 * @author Philipp Neubrand
 * 
 */
public class CBufferingCommunity extends ChangeableCommunity {
	private HashMap<Integer, Integer> cBuffer = new HashMap<Integer, Integer>();

	@Override
	public void addNode(int add) {
		super.addNode(add);
		cBuffer.put(add, add);
	}

	@Override
	public void removeNode(int node) {
		super.removeNode(node);
		cBuffer.remove(node);
	}

	/**
	 * Standard Constructor for a <code>CBufferingCommunity</code>, supplied are
	 * the ID and an <code>ArrayList<Integer></code> containing the nodes of the
	 * community.
	 * 
	 * @param index
	 *            The ID of this community.
	 * @param nodes
	 *            The list of nodes of this community.
	 */
	public CBufferingCommunity(int index, ArrayList<Integer> nodes) {
		super(index, nodes);
		for (int akt : nodes) {
			cBuffer.put(akt, akt);
		}
	}

	/**
	 * Standard Constructor for a <code>CBufferingCommunity</code>, supplied are
	 * the ID and an <code>int[]</code> containing the nodes of this community.
	 * 
	 * @param index
	 *            The ID of this community.
	 * @param nodes
	 *            The nodes of this community.
	 */
	public CBufferingCommunity(int index, int[] nodes) {
		super(index, nodes);
		for (int akt : nodes) {
			cBuffer.put(akt, akt);
		}
	}

	/**
	 * Standard Constructor for an empty <code>ChangeableCommunity</code> with
	 * only an ID.
	 * 
	 * @param index
	 *            The ID of this Community.
	 */
	public CBufferingCommunity(int index) {
		super(index);
	}

	/**
	 * Constructor to create a <code>ChangeableCommunity</code> from the String
	 * that is returned by <code>getString()</code>.
	 * 
	 * @param string
	 *            The string from which to create the Community.
	 */
	public CBufferingCommunity(String string) {
		super(string);
		for (int akt : nodes) {
			cBuffer.put(akt, akt);
		}
	}

	public boolean contains(int node) {
		// Instead of iterating through all the nodes, we just check the buffer
		return cBuffer.containsKey(node);
	}

}
