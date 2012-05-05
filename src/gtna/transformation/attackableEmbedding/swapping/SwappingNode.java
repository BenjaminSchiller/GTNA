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
package gtna.transformation.attackableEmbedding.swapping;

import gtna.graph.Graph;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;

import java.util.Random;


/**
 * regular swapping behavior
 * @author stefanieroos
 *
 */
public class SwappingNode extends AttackableEmbeddingNode {
	
	 public static final double NO_SWAP = Double.MIN_VALUE;
	
	 protected Swapping swapping;
	
	 public SwappingNode(int index, Graph g, Swapping swapping) {
	 super(index, g);
	 this.swapping = swapping;
	 }
	
	 public void updateNeighbors(Random rand) {
	 for (int i = 0; i < this.getOutDegree(); i++) {
	     this.knownIDs[i] = ((SwappingNode) this.getGraph().getNode(this.getOutgoingEdges()[i])).ask(this, rand);
	 }
	 }
	
	 public void turn(Random rand) {
	 // initiate swap
	RingIdentifier[] ids = this.swapping.getIds();  	 
	 double loc = ((SwappingNode) 
			 this.getGraph().getNode(this.getOutgoingEdges()[rand.nextInt(this.getOutDegree())])).swap(
					 ids[this.getIndex()].getPosition(), this.knownIDs, 6, rand);
	 if (loc != SwappingNode.NO_SWAP) {
	    ids[this.getIndex()].setPosition(loc);
	 }
	 }
	
	 protected double ask(SwappingNode caller, Random rand) {
	 return this.swapping.getIds()[this.getIndex()].getPosition();
	 }
	
	 /**
	 *
	 * @param caller
	 * @param callerNeighbors
	 * @param ttl
	 * @return ID to change for; or Double.MIN_VALUE in case of no swap
	 */
	 protected double swap(double callerID, double[] callerNeighborIDs, int
	 ttl,
	 Random rand) {
	
	 // case a) determine if nodes should swap
	 if (ttl - 1 <= 0) {
	 // calculate coefficient
	 double before = 1;
	 double after = 1;
	 RingIdentifier neighborID = RingIdentifier.rand(rand, (RingIdentifierSpace) this.swapping.getIdspace());
	 RingIdentifier partnerID = new RingIdentifier(callerID, (RingIdentifierSpace) this.swapping.getIdspace());
	 RingIdentifier id = this.swapping.getIds()[this.getIndex()]; 
	 for (int i = 0; i < this.getOutDegree(); i++) {
	 neighborID.setPosition(this.knownIDs[i]);
	 if (this.knownIDs[i] != partnerID.getPosition()){
	 before = before * id.distance(neighborID);
	 after = after * partnerID.distance(neighborID);
	 } else {
		 before = before * id.distance(partnerID);
		 after = after * partnerID.distance(id); 
	 }
	 }
	 for (int i = 0; i < callerNeighborIDs.length; i++) {
	 neighborID.setPosition(callerNeighborIDs[i]);
	 if (callerNeighborIDs[i] != id.getPosition()){
	    before = before * partnerID.distance(neighborID);
	    after = after * id.distance(neighborID);
	 } else {
		 before = before * partnerID.distance(id);
		 after = after * id.distance(partnerID); 
	 }
	 }
	 // decide if to swap
	 if (rand.nextDouble() < before / after) {
	 double loc = id.getPosition();
	 id.setPosition(callerID);
	 return loc;
	 }
	 } else {
	 // case 2: forward
	 return ((SwappingNode) this.getGraph().getNode(this.getOutgoingEdges()[rand.nextInt(this.getOutDegree())]))
	 .swap(callerID, callerNeighborIDs, ttl - 1, rand);
	 }
	 return SwappingNode.NO_SWAP;
	 }

}
