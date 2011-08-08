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
 * SortingNodeImpl.java
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
package gtna.transformationOld.embedding;


/**
 * @author "Benjamin Schiller"
 * 
 */
// TODO reimplement EmbeddingNode
public abstract class EmbeddingNode {
	// public abstract class EmbeddingNode extends RingNode {
	//
	// protected HashMap<EmbeddingNode, Integer> position;
	//
	// protected double[] knownIDs;
	//
	// public EmbeddingNode(int index, double pos) {
	// super(index, pos);
	// }
	//
	// /**
	// * must be called after creating the outgoing edges of this node
	// */
	// public void initKnownIDs() {
	// this.position = new HashMap<EmbeddingNode, Integer>(this.out().length);
	// Node[] out = this.out();
	// this.knownIDs = new double[out().length];
	// for (int i = 0; i < out.length; i++) {
	// this.position.put((EmbeddingNode) out[i], i);
	// }
	// }
	//
	// public abstract void updateNeighbors(Random rand);
	//
	// public abstract void turn(Random rand);

}
