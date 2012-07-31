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
 * NodePicker.java
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

import gtna.graph.Node;
import gtna.util.parameter.Parameter;

/**
 * A <code>NodePicker</code> is basically a container that gets filled with
 * nodes and then gradually returns them in a specific order.
 * 
 * @author Philipp Neubrand
 * 
 */
public interface NodePicker {

	/**
	 * Adds the supplied nodes to this NodePicker.
	 * 
	 * @param nodes
	 *            The nodes to be added.
	 */
	public void addAll(Node[] nodes);

	/**
	 * Checks whether or not this NodePicker still has nodes.
	 * 
	 * @return <code>true</code> if no nodes are left, <code>false</code>
	 *         otherwise.
	 */
	public boolean empty();

	/**
	 * Getter for the next node.
	 * 
	 * @return The next node.
	 */
	public Node pop();

	/**
	 * Removes the supplied node from this NodePicker.
	 * 
	 * @param akt
	 *            The node to be removed.
	 */
	public void remove(int akt);

	/**
	 * Getter for the <code>Parameter[]</code> containing the configuration
	 * parameters for the NodePicker.
	 * 
	 * @return A <code>Parameter[]</code> containing the configuration
	 *         parameters.
	 */
	public Parameter[] getParameterArray();

	/**
	 * Getter for the number of nodes remaining in this NodePicker.
	 * 
	 * @return The number of nodes in this NodePicker.
	 */
	public int size();

}