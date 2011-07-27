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
 * SwappingAttackerContraction.java
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
package gtna.transformation.embedding.swapping;

import gtna.graph.NodeImpl;

import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class SwappingAttackerContraction extends SwappingNode {

	private SwappingNode neighbor;

	private int index;

	public SwappingAttackerContraction(int index, double pos, Swapping swapping) {
		super(index, pos, swapping);
	}

	/**
	 * try to distribute an ID close to a neighbor
	 */
	public void turn(Random rand) {
		// select a random neighbor
		if (this.neighbor == null) {
			NodeImpl[] out = this.out();
			this.index = rand.nextInt(out.length);
			this.neighbor = (SwappingNode) out[this.index];
		}

		// select own ID close to neighbor
		this.getID().pos = (this.knownIDs[this.index] + rand.nextDouble()
				* this.swapping.delta) % 1.0;

		// select ID close to neighbor + furthest neighbors
		double id = (this.knownIDs[this.index] + rand.nextDouble()
				* this.swapping.delta) % 1.0;
		double[] neighbors = new double[this.out().length];
		for (int i = 0; i < neighbors.length; i++) {
			neighbors[i] = (id + 0.5 + rand.nextDouble() * this.swapping.delta) % 1.0;
		}

		// select ttl
		int ttl = rand.nextInt(6) + 1;

		// select starting node
		NodeImpl[] out = this.out();
		SwappingNode start = (SwappingNode) out[rand.nextInt(out.length)];

		// send swap request
		start.swap(id, neighbors, ttl, rand);
	}

	/**
	 * return ID close to selected neighbor
	 */
	protected double ask(SwappingNode caller, Random rand) {
		// select a random neighbor
		if (this.neighbor == null) {
			NodeImpl[] out = this.out();
			this.index = rand.nextInt(out.length);
			this.neighbor = (SwappingNode) out[this.index];
		}

		// return ID close to neighbor's current ID
		double id = (this.knownIDs[this.index] + rand.nextDouble()
				* this.swapping.delta) % 1.0;
		return id;
	}

	/**
	 * return ID close to selected neighbor
	 */
	protected double swap(double callerID, double[] callerNeighborIDs, int ttl,
			Random rand) {
		return this.ask(this, rand);
	}

}
