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
 * NodeIdentifier.java
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
package gtna.id.node;

import gtna.id.DoubleIdentifier;
import gtna.id.Identifier;

/**
 * @author benni
 * 
 */
public class NodeIdentifier extends DoubleIdentifier {

	protected int node;

	/**
	 * 
	 * @param node
	 *            index of the node represented by this identifier
	 */
	public NodeIdentifier(int node) {
		this.node = node;
	}

	public NodeIdentifier(String string) {
		this(Integer.parseInt(string));
	}

	@Override
	public int compareTo(DoubleIdentifier arg0) {
		NodeIdentifier id = (NodeIdentifier) arg0;
		if (this.node < id.node) {
			return -1;
		} else if (this.node > id.node) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public double distance(DoubleIdentifier id) {
		return 0;
	}

	@Override
	public String asString() {
		return this.node + "";
	}

	@Override
	public boolean equals(Identifier id) {
		return this.node == ((NodeIdentifier) id).node;
	}

	/**
	 * @return the node
	 */
	public int getNode() {
		return this.node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(int node) {
		this.node = node;
	}

}
