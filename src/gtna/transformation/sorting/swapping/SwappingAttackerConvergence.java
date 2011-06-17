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
 * SwappingAttackerConvergence.java
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
package gtna.transformation.sorting.swapping;

import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * idea: offer random ids to prevent convergence
 */
public class SwappingAttackerConvergence extends SwappingNode {

	public SwappingAttackerConvergence(int index, double pos, Swapping swapping) {
		super(index, pos, swapping);
	}

	/**
	 * offer randomly chosen id to neighbors
	 */
	public void turn(Random rand) {
		
		//System.out.println("performing turn @ SwappingAttackerConvergence "
			//	+ this.toString());
		
		double loc = rand.nextDouble();
	    double[] locs = new double[this.out().length];
		for (int i = 0; i < locs.length; i++){
			locs[i] = loc +0.5 + rand.nextDouble()*SwappingNode.epsilon;
			if (locs[i] > 1){
				locs[i]--;
			}
		}
		((SwappingNode)this.out()[rand.nextInt(this.out().length)]).swap(loc, locs, 1, rand);
	}

	/**
	 * return randomly chosen id
	 */
	protected double ask(SwappingNode caller, Random rand) {
		// TODO implement
		return rand.nextDouble();
	}

	/**
	 * return randomly chosen id
	 */
	protected double swap(double callerID, double[] callerNeighborIDs, int ttl,
			Random rand) {
		// TODO implement
		return rand.nextDouble();
	}

}
