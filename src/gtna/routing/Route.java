/*
 * ===========================================================
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
 * Route.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.routing;

import gtna.graph.NodeImpl;

import java.util.ArrayList;

/**
 * Interface for representations of the results of a single routing attempt by a
 * routing algorithm. This contains the path taken by the algorithm, a flag for
 * success, and the total number of messages involved in this routing algorithm.
 * 
 * @author benni
 * 
 */
public interface Route {
	/**
	 * 
	 * @return array containing all hops on the routing path, starting at the
	 *         source and ending at the destination
	 */
	public ArrayList<NodeImpl> path();

	/**
	 * 
	 * @return true if the routing was successful, false otherwise
	 */
	public boolean success();

	/**
	 * 
	 * @return total number of message sent throughout the whole network as a
	 *         result of this routing attempt
	 */
	public int messages();

	/**
	 * Add the new node to the end of the current routing path.
	 * 
	 * @param n
	 *            new node to add
	 */
	public void add(NodeImpl n);

	/**
	 * Increments the message counter by 1.
	 */
	public void incMessages();

	/**
	 * Increments the message counter by the given number.
	 * 
	 * @param inc
	 *            number to increments the message counter
	 */
	public void incMessages(int inc);

	/**
	 * Sets the success flag.
	 * 
	 * @param success
	 *            new success flag
	 */
	public void setSuccess(boolean success);
}
