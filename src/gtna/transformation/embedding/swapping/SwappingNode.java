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
package gtna.transformation.embedding.swapping;


/**
 * @author "Benjamin Schiller"
 * 
 */
// TODO reimplement SwappingNode
public class SwappingNode {
	// public class SwappingNode extends EmbeddingNode {
	//
	// public static final double NO_SWAP = Double.MIN_VALUE;
	//
	// protected Swapping swapping;
	//
	// public SwappingNode(int index, double pos, Swapping swapping) {
	// super(index, pos);
	// this.swapping = swapping;
	// }
	//
	// public void updateNeighbors(Random rand) {
	// Node[] out = this.out();
	// for (int i = 0; i < out.length; i++) {
	// this.knownIDs[i] = ((SwappingNode) out[i]).ask(this, rand);
	// }
	// }
	//
	// public void turn(Random rand) {
	// // initiate swap
	// Node[] out = this.out();
	// double loc = ((SwappingNode) out[rand.nextInt(out.length)]).swap(this
	// .getID().pos, this.knownIDs, 6, rand);
	// if (loc != SwappingNode.NO_SWAP) {
	// this.getID().pos = loc;
	// }
	// }
	//
	// protected double ask(SwappingNode caller, Random rand) {
	// return this.getID().pos;
	// }
	//
	// /**
	// *
	// * @param caller
	// * @param callerNeighbors
	// * @param ttl
	// * @return ID to change for; or Double.MIN_VALUE in case of no swap
	// */
	// protected double swap(double callerID, double[] callerNeighborIDs, int
	// ttl,
	// Random rand) {
	//
	// // case a) determine if nodes should swap
	// if (ttl - 1 <= 0) {
	// // calculate coefficient
	// double before = 1;
	// double after = 1;
	// RingID neighborID = new RingID(rand.nextDouble());
	// RingID partnerID = new RingID(callerID);
	// for (int i = 0; i < this.out().length; i++) {
	// neighborID.pos = this.knownIDs[i];
	// before = before * this.dist(neighborID);
	// after = after * partnerID.dist(neighborID);
	// }
	// for (int i = 0; i < callerNeighborIDs.length; i++) {
	// neighborID.pos = callerNeighborIDs[i];
	// before = before * partnerID.dist(neighborID);
	// after = after * this.dist(neighborID);
	// }
	// // decide if to swap
	// if (rand.nextDouble() < before / after) {
	// double loc = this.getID().pos;
	// this.setID(partnerID);
	// return loc;
	// }
	// } else {
	// // case 2: forward
	// return ((SwappingNode) this.out()[rand.nextInt(this.out().length)])
	// .swap(callerID, callerNeighborIDs, ttl - 1, rand);
	// }
	// return SwappingNode.NO_SWAP;
	// }

}
