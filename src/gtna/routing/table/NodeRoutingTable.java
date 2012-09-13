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
 * NodeRoutingTable.java
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
package gtna.routing.table;

import gtna.id.Identifier;
import gtna.id.node.NodeIdentifier;

/**
 * @author benni
 * 
 */
public class NodeRoutingTable extends RoutingTable {

	protected int node;

	protected int[] nextHop;

	private static final String delimiter = ";";

	public NodeRoutingTable(int node, int[] nextHop) {
		this.node = node;
		this.nextHop = nextHop;
	}

	@Override
	public String asString() {
		StringBuffer buff = new StringBuffer(this.node + "");
		for (int i = 0; i < this.nextHop.length; i++) {
			buff.append(delimiter + this.nextHop[i]);
		}
		return buff.toString();
	}

	@Override
	public void fromString(String str) {
		String[] temp = str.split(delimiter);
		this.node = Integer.parseInt(temp[0]);
		this.nextHop = new int[temp.length - 1];
		for (int i = 1; i < temp.length; i++) {
			this.nextHop[i - 1] = Integer.parseInt(temp[i]);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int getNextHop(Identifier target) {
		NodeIdentifier nid = (NodeIdentifier) target;
		return this.nextHop[nid.getNode()];
	}

}
