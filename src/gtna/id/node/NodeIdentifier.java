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

import gtna.id.DIdentifier;
import gtna.id.Identifier;

/**
 * @author benni
 * 
 */
public class NodeIdentifier implements DIdentifier, Comparable<NodeIdentifier> {

	public static final int dataValueMod = 10000;

	public static final int dataValueDefault = -1;

	protected int node;

	protected int data;

	public NodeIdentifier(int node) {
		this.node = node;
		this.data = dataValueDefault;
	}

	public NodeIdentifier(int node, int data) {
		this.node = node;
		this.data = data % dataValueMod;
	}

	@Override
	public Double distance(Identifier<Double> id) {
		NodeIdentifier nid = (NodeIdentifier) id;
		if (nid.node == this.node) {
			return 0.0;
		}
		return Double.MAX_VALUE;
	}

	@Override
	public boolean equals(Identifier<Double> id) {
		NodeIdentifier nid = (NodeIdentifier) id;
		return nid.node == this.node && nid.data == this.data;
	}

	@Override
	public int compareTo(NodeIdentifier arg0) {
		return arg0.node - this.node;
	}

	public String toString() {
		return "NID:" + this.node + "/" + this.data;
	}

	public String asString() {
		return this.node + ":" + data;
	}

	public NodeIdentifier fromString(String str) {
		String[] temp = str.split(":");
		return new NodeIdentifier(Integer.parseInt(temp[0]),
				Integer.parseInt(temp[1]));
	}

	public int getNode() {
		return this.node;
	}

	public int getData() {
		return this.getData();
	}

}
