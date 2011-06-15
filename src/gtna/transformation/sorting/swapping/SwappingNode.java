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
 * SwappingNode.java
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
package gtna.transformation.sorting.swapping;

import gtna.graph.NodeImpl;
import gtna.transformation.sorting.SortingNode;

import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class SwappingNode extends SortingNode {
	public static final double NO_SWAP = Double.MIN_VALUE;

	public SwappingNode(int index, double pos) {
		super(index, pos);
	}

	public void updateNeighbors() {
		NodeImpl[] out = this.out();
		for (int i = 0; i < out.length; i++) {
			this.knownIDs[i] = ((SwappingNode) out[i]).ask(this);
		}
	}

	public void turn() {
		// TODO implement
		System.out.println("performing turn @ node " + this.toString());
	}

	protected double ask(SwappingNode caller) {
		return this.getID().pos;
	}

	/**
	 * 
	 * @param caller
	 * @param callerNeighbors
	 * @param ttl
	 * @return ID to change for; or Double.MIN_VALUE in case of no swap
	 */
	protected double swap(double callerID, double[] callerNeighborIDs, int ttl,
			Random rand) {
		// TODO implement
		if (ttl - 1 <= 0) {
			// compute D1 & D2
			// check if D1*D2 >= P
			// if to return this.getID().pos
			// return NO_SWAP otherwise
		} else {
			((SwappingNode) this.out()[rand.nextInt(this.out().length)]).swap(
					callerID, callerNeighborIDs, ttl - 1, rand);
		}
		return SwappingNode.NO_SWAP;
	}

}
