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

import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class LMCNode extends SortingNode {
	protected LMC lmc;

	public LMCNode(int index, double pos, LMC lmc) {
		super(index, pos);
		this.lmc = lmc;
	}

	public void updateNeighbors(Random rand) {
		NodeImpl[] out = this.out();
		for (int i = 0; i < out.length; i++) {
			this.knownIDs[i] = ((LMCNode) out[i]).ask(this, rand);
		}
	}

	public void turn(Random rand) {
		// TODO implement
		System.out.println("performing turn @ LMCNode " + this.index());
	}

	protected double ask(LMCNode caller, Random rand) {
		return this.getID().pos;
	}
}
