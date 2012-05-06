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
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpace;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;

/**
 * Assigns a randomly selected RingPartitionSimple to every node and stores it
 * as a property with key "ID_SPACE" in the given graph.
 * 
 * @author benni
 * 
 */
public class RandomRingIDSpaceSimple extends Transformation {
	private int realities;

	private double modulus;

	private boolean wrapAround;
	
	private Distance distance;

	public RandomRingIDSpaceSimple() {
		super("RANDOM_RING_ID_SPACE_SIMPLE");
		this.realities = 1;
		this.modulus = 1.0;
		this.wrapAround = true;
		this.distance = RingIdentifierSpace.Distance.RING;
	}
	
	public RandomRingIDSpaceSimple(Distance distance) {
		super("RANDOM_RING_ID_SPACE_SIMPLE", new Parameter[] {
				new StringParameter("DISTANCE", distance.toString()) });
		this.realities = 1;
		this.modulus = 1.0;
		this.wrapAround = true;
		this.distance = distance;
	}

	public RandomRingIDSpaceSimple(int realities, double modulus, boolean wrapAround) {
		super("RANDOM_RING_ID_SPACE_SIMPLE", new Parameter[] {
				new IntParameter("REALITIES", realities),
				new DoubleParameter("MODULUS", modulus),
				new BooleanParameter("WRAP_AROUND", wrapAround) });
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.distance = RingIdentifierSpace.Distance.RING;
	}
	
	public RandomRingIDSpaceSimple(int realities, double modulus, boolean wrapAround, Distance distance) {
		super("RANDOM_RING_ID_SPACE_SIMPLE", new Parameter[] {
				new IntParameter("REALITIES", realities),
				new DoubleParameter("MODULUS", modulus),
				new BooleanParameter("WRAP_AROUND", wrapAround),
				new StringParameter("DISTANCE", distance.toString())});
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.distance = distance;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			RingPartitionSimple[] partitions = new RingPartitionSimple[graph
					.getNodes().length];
			RingIdentifierSpaceSimple idSpace = new RingIdentifierSpaceSimple(
					partitions, this.modulus, this.wrapAround, this.distance);
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new RingPartitionSimple(RingIdentifier.rand(
						rand, idSpace));
			}
			graph.addProperty(graph.getNextKey("ID_SPACE"), idSpace);
		}
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}
