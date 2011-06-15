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
 * LMCNode.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    "Stefanie Roos";
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-14 : v1 (BS)
 *
 */
package gtna.transformation.sorting.lmc;

import gtna.graph.NodeImpl;
import gtna.transformation.sorting.SortingNode;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class LMCNode extends SortingNode {

	public LMCNode(int index, double pos) {
		super(index, pos);
	}

	public void updateNeighbors() {
		NodeImpl[] out = this.out();
		for (int i = 0; i < out.length; i++) {
			this.knownIDs[i] = ((LMCNode) out[i]).ask(this);
		}
	}

	public void turn() {
		// TODO implement
		System.out.println("performing turn @ node " + this.toString());
	}

	protected double ask(LMCNode caller) {
		return this.getID().pos;
	}
}
