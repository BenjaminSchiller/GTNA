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
package gtna.transformation.attackableEmbedding.swapping;

import gtna.graph.Graph;
import gtna.id.ring.RingIdentifier;

import java.util.Random;

/**
 * attacker behaving randomly
 * 
 * @author stefanieroos
 * 
 */
public class SwappingAttackerConvergence extends SwappingNode {

	public SwappingAttackerConvergence(int index, Graph g, Swapping swapping) {
		super(index, g, swapping);
	}

	/**
	 * offer randomly chosen id to a random node (TTL taken randomly from [1, 6]
	 * use neighborhood at furthest distance (id + 0.5 + \delta) to make
	 * receiving node accept it at all times
	 */
	public void turn(Random rand) {
		// select random id
		RingIdentifier ID = this.swapping.getIds()[this.getIndex()];
		ID.setPosition(rand.nextDouble());

		// offer random id
		int ttl = rand.nextInt(6) + 1;
		double id = rand.nextDouble();
		double[] neighbors = new double[this.getOutDegree()];
		for (int i = 0; i < neighbors.length; i++) {
			neighbors[i] = (id + 0.5 + rand.nextDouble() * this.swapping.delta) % 1.0;
		}

		((SwappingNode) this.swapping.getRandomNeighbor(this.getGraph(),
				this.getIncomingEdges(), rand)).swap(id, neighbors, ttl, rand);
	}

	/**
	 * return randomly chosen id
	 */
	protected double ask(SwappingNode caller, Random rand) {
		return rand.nextDouble();
	}

	/**
	 * return randomly chosen id
	 */
	protected double swap(double callerID, double[] callerNeighborIDs, int ttl,
			Random rand) {
		return rand.nextDouble();
	}

}
