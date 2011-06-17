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
 * SwappingAttackerKleinberg.java
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

import java.util.Arrays;
import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class SwappingAttackerKleinberg extends SwappingNode {

	public SwappingAttackerKleinberg(int index, double pos, Swapping swapping) {
		super(index, pos, swapping);
	}

	public void turn(Random rand) {
		// Do nothing: cannot know which iD is good/bad for neighbor
		//likely to be accepted...
		
		//System.out.println("performing turn @ SwappingAttackerKleinberg "
			//	+ this.toString());
		
		
		/*int index = rand.nextInt(this.out().length);
		double loc = this.knownIDs[index] + rand.nextDouble()*SwappingNode.epsilon;
		if (loc > 1){
			loc--;
		}
		double[] locs = new double[this.out().length];
		for (int i = 0; i < locs.length; i++){
			locs[i] = loc + 0.5 + rand.nextDouble()*SwappingNode.epsilon;
			if (locs[i] > 1){
				locs[i]--;
			}
		}
		
		
		
		((SwappingNode)this.out()[index]).swap(loc, locs, 1, rand);*/
		
	}

	/**
	 * prevent to be close to neighbor to keep it from adjusting
	 */
	protected double ask(SwappingNode caller, Random rand) {
		int index = this.position.get(caller);
		double loc = this.knownIDs[index] + rand.nextDouble()*SwappingNode.epsilon;
		if (loc > 1){
			loc--;
		}
		return loc;
	}

	/**
	 * give ID that is extremely bad for caller:
	 * biggest minimal distance to node
	 */
	protected double swap(double callerID, double[] callerNeighborIDs, int ttl,
			Random rand) {
		// TODO implement
		Arrays.sort(callerNeighborIDs);
		double max = 0;
		int index = -1;
		for (int i = 0; i < callerNeighborIDs.length-1; i++){
			double dist = callerNeighborIDs[i+1] - callerNeighborIDs[i];
			if (dist > max){
				max = dist;
				index = i;
			}
		}
		double dist = 1-callerNeighborIDs[callerNeighborIDs.length-1] + callerNeighborIDs[0];
		double loc;
		if (dist > max){
			loc = (1+callerNeighborIDs[0] + callerNeighborIDs[callerNeighborIDs.length-1])/2;
		} else {
			loc = (callerNeighborIDs[index] + callerNeighborIDs[index+1])/2;
		}
		if (loc > 1){
			loc--;
		}
		return loc;
	}

}
