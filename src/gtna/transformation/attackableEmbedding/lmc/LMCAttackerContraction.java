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
 * LMCAttackerContraction.java
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
package gtna.transformation.attackableEmbedding.lmc;

import gtna.graph.Graph;

import java.util.Random;

/**
 * attacker that tries to make the ID space contract in one point
 * 
 * @author stefanieroos
 * 
 */

public class LMCAttackerContraction extends LMCNode {

	// private LMCNode neighbor = null;

	private int index = -1;

	public LMCAttackerContraction(int index, Graph g, LMC lmc) {
		super(index, g, lmc);
	}

	/**
	 * choose an ID close to the selected neighbor
	 */
	public void turn(Random rand) {
		this.lmc.getIds()[this.getIndex()].setPosition(this.ask(this, rand));
	}

	/**
	 * return an ID close to one selected neighbor hoping all neighbors will
	 * converge to this position
	 */
	protected double ask(LMCNode caller, Random rand) {
		// choose a random neighbor to contract to
		if (this.index == -1) {
			this.index = rand.nextInt(this.getOutDegree());
		}

		// return ID close to the selected neighbor
		if (LMC.MODE_RESTRICTED.equals(this.lmc.mode)) {
			return (this.knownIDs[this.index] + this.lmc.delta
					* (1.0 + rand.nextDouble())) % 1.0;
		} else {
			return (this.knownIDs[this.index] + this.lmc.delta
					* rand.nextDouble()) % 1.0;
		}
	}
}
