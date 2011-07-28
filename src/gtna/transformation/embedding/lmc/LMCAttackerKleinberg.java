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
 * LMCAttackerKleinberg.java
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
package gtna.transformation.embedding.lmc;


/**
 * @author "Benjamin Schiller"
 * 
 */
// TODO reimplement LMCAttackerKleinberg
public class LMCAttackerKleinberg {
	// public class LMCAttackerKleinberg extends LMCNode {
	//
	// public LMCAttackerKleinberg(int index, double pos, LMC lmc) {
	// super(index, pos, lmc);
	// }
	//
	// /**
	// * select ID at longest distance to any node
	// */
	// public void turn(Random rand) {
	// double[] neighbors = this.knownIDs.clone();
	// this.getID().pos = (SwappingAttackerKleinberg.maxMiddle(neighbors) + rand
	// .nextDouble()
	// * this.lmc.delta) % 1.0;
	// }
	//
	// /**
	// * return ID close to neighbor to keep it from changing IDs
	// */
	// protected double ask(LMCNode caller, Random rand) {
	// int index = this.position.get(caller);
	// if (LMC.MODE_RESTRICTED.equals(this.lmc.mode)) {
	// return (this.knownIDs[index] + this.lmc.delta
	// * (1.0 + rand.nextDouble())) % 1.0;
	// } else {
	// return (this.knownIDs[index] + this.lmc.delta * rand.nextDouble()) % 1.0;
	// }
	// }

}
