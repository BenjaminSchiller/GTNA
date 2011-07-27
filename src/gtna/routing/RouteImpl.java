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
 * RouteImpl.java
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

import gtna.graph.Node;

import java.util.ArrayList;

/**
 * Implements all features of the Path interface.
 * 
 * @author benni
 * 
 */
public class RouteImpl implements Route {
	private ArrayList<Node> path;

	private boolean success;

	private int messages;

	/**
	 * 
	 * @param route
	 *            array containing all hops on the routing path, starting at the
	 *            source and ending at the destination
	 * @param success
	 *            flag that indicates if this particular routing attempt was
	 *            successful, i.e., the last node of the path is the target node
	 *            or contains the requested information
	 * @param messages
	 *            total number of message sent as a result of this routing
	 *            attempt (equals path.length-1 in most cased but may differ
	 *            for, e.g., algorithms using parallel requests)
	 */
	public RouteImpl(ArrayList<Node> path, boolean success, int messages) {
		this.path = path;
		this.success = success;
		this.messages = messages;
	}

	public RouteImpl() {
		this.path = new ArrayList<Node>();
		this.success = false;
		this.messages = 0;
	}

	public void add(Node n) {
		this.path.add(n);
	}

	public void incMessages() {
		this.messages++;
	}

	public void incMessages(int inc) {
		this.messages += inc;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ArrayList<Node> path() {
		return this.path;
	}

	public boolean success() {
		return this.success;
	}

	public int messages() {
		return this.messages;
	}
}
