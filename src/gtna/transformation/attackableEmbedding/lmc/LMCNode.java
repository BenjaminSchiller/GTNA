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
package gtna.transformation.attackableEmbedding.lmc;

import gtna.graph.Graph;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;

import java.util.Random;

/**
 * regular node behaviors in LMC
 * 
 * @author stefanieroos
 * 
 */

public class LMCNode extends AttackableEmbeddingNode {

	protected LMC lmc;

	public LMCNode(int index, Graph g, LMC lmc) {
		super(index, g);
		this.lmc = lmc;
	}

	public void updateNeighbors(Random rand) {
		int[] out = this.getOutgoingEdges();
		for (int i = 0; i < out.length; i++) {
			this.knownIDs[i] = ((LMCNode) this.getGraph().getNode(out[i])).ask(
					this, rand);
		}
	}

	/**
	 * regular LMC: turn; two steps: 1) propose new ID 2) check if new ID is
	 * accepted
	 */
	public void turn(Random rand) {
		// double loc;
		// if (rand.nextDouble() < lmc.P) {
		// if (rand.nextBoolean()) {
		// loc = this.knownIDs[rand.nextInt(knownIDs.length)] + lmc.delta
		// + rand.nextDouble() * lmc.C * lmc.delta;
		// while (loc > 1) {
		// loc--;
		// }
		// } else {
		// loc = this.knownIDs[rand.nextInt(knownIDs.length)] - lmc.delta
		// - rand.nextDouble() * lmc.C * lmc.delta;
		// while (loc < 0) {
		// loc++;
		// }
		// }
		// } else {
		// loc = rand.nextDouble();
		// }

		double before = 1;
		double after = 1;
		RingIdentifier[] ids = this.lmc.getIds();
		RingIdentifier newID = RingIdentifier.rand(rand,
				(RingIdentifierSpace) this.lmc.getIdspace());
		RingIdentifier neighborID = RingIdentifier.rand(rand,
				(RingIdentifierSpace) this.lmc.getIdspace());
		;
		RingIdentifier id = ids[this.getIndex()];
		for (int i = 0; i < knownIDs.length; i++) {
			neighborID.setPosition(this.knownIDs[i]);
			double dist = id.distance(neighborID);
			before = before * dist;
			dist = newID.distance(neighborID);
			if (lmc.mode.equals(LMC.MODE_RESTRICTED) && dist < lmc.delta) {
				return;
			}
			after = after * dist;
		}
		if (rand.nextDouble() < before / after) {
			ids[this.getIndex()].setPosition(newID.getPosition());
		}
	}

	protected double ask(LMCNode caller, Random rand) {

		return this.lmc.getIds()[this.getIndex()].getPosition();
	}
}
