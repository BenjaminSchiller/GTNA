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
 * LMCWCAttackerConvergence.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.sorting.lmc;


import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class LMCWCAttackerConvergence extends LMCWCNode {

	public LMCWCAttackerConvergence(int index, double pos, LMC lmc) {
		super(index, pos, lmc);
	}

	public void turn(Random rand) {
		// TODO Auto-generated method stub
		System.out.println("performing turn @ LMCWCAttackerConvergence "
				+ this.index());
	}

	protected double ask(LMCWCNode caller, double callerID,
			double[] callerNeighborIDs, Random rand) {
		// TODO implement
		return this.getID().pos;
	}

}
