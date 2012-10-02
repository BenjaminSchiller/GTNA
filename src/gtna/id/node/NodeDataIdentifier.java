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
 * NodeDataIdentifier.java
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
public class NodeDataIdentifier extends NodeIdentifier {

	protected int data;

	/**
	 * 
	 * @param node
	 *            index of the node represented by this identifier
	 * @param data
	 *            value for the data item represented by this identifier
	 */
	public NodeDataIdentifier(int node, int data) {
		super(node);
		this.data = data;
	}

	public NodeDataIdentifier(String string) {
		this(Integer.parseInt(string.split(Identifier.delimiter)[0]), Integer
				.parseInt(string.split(Identifier.delimiter)[1]));
	}

	@Override
	public int compareTo(DoubleIdentifier o) {
		int ct = super.compareTo(o);
		if (ct != 0) {
			return ct;
		}
		NodeDataIdentifier id = (NodeDataIdentifier) o;
		if (this.data < id.data) {
			return -1;
		} else if (this.data > id.data) {
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
		return this.node + Identifier.delimiter + this.data;
	}

	@Override
	public boolean equals(Identifier id) {
		return super.equals(id) && this.data == ((NodeDataIdentifier) id).data;
	}

}
