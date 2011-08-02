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
 * RandomRingID.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.id;

import gtna.graph.Graph;
import gtna.id.ring.RingID;
import gtna.id.ring.RingIDSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.Random;

/**
 * Assigns a randomly selected RingPartitionSimple to every node and stores it
 * as a property with key "ID_SPACE" in the given graph.
 * 
 * @author benni
 * 
 */
public class RandomRingIDSpaceSimple extends TransformationImpl implements
		Transformation {
	private int realities;

	public RandomRingIDSpaceSimple() {
		super("RANDOM_RING_ID_SPACE_SIMPLE", new String[] {}, new String[] {});
		this.realities = 1;
	}

	public RandomRingIDSpaceSimple(int realities) {
		super("RANDOM_RING_ID_SPACE_SIMPLE", new String[] { "REALITIES" },
				new String[] { "" + realities });
		this.realities = realities;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			RingPartitionSimple[] partitions = new RingPartitionSimple[graph
					.getNodes().length];
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new RingPartitionSimple(RingID.rand(rand));
			}
			RingIDSpaceSimple idSpace = new RingIDSpaceSimple(partitions);
			graph.addGraphProperty("ID_SPACE_" + r, idSpace);
		}
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}
