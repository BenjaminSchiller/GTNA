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
package gtna.transformation.attackableEmbedding.swapping;

import gtna.graph.Graph;
import gtna.id.ring.RingIdentifier;

import java.util.Random;


/**
 * Attacker preventing nodes from adapting 
 * @author stefanieroos
 *
 */
public class SwappingAttackerKleinberg extends SwappingNode {
	
	 public SwappingAttackerKleinberg(int index, Graph g, Swapping
	 swapping) {
	 super(index, g, swapping);
	 }
	
	 /**
	 * select ID at biggest distance to any neighbor; send no swap request,
	 * since attacker could even offer a better ID (no knowledge)
	 */
	 public void turn(Random rand) {
	 double[] neighbors = this.knownIDs.clone();
	 RingIdentifier id = this.swapping.getIds()[this.getIndex()];
	 id.setPosition(maxMiddle(neighbors) + rand.nextDouble() * this.swapping.delta);
	 }
	
	 /**
	 * return ID close to current requester's ID to keep it from changing its
	 ID
	 */
	 protected double ask(SwappingNode caller, Random rand) {
	if (caller instanceof SwappingAttackerKleinberg){
		return super.ask(caller,rand);
	}
	 double id = (caller.ask(this, rand)+ rand.nextDouble()
	 * this.swapping.delta) % 1.0;
	 return id;
	 }
	
	 /**
	 * give ID that is extremely bad for caller: biggest distance to any
	 * neighbor node
	 */
	 protected double swap(double callerID, double[] callerNeighborIDs, int
	 ttl,
	 Random rand) {
	 return (maxMiddle(callerNeighborIDs) + rand.nextDouble()
	 * this.swapping.delta) % 1.0;
	 }
	
	 

}
