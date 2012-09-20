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
 * RandomRingIDSpaceCommunities.java
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

import gtna.communities.Community;
import gtna.communities.CommunityList;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RandomRingIDSpaceSimpleCommunities extends Transformation {
	private double modulus;

	private boolean wrapAround;

	/**
	 * 
	 */
	public RandomRingIDSpaceSimpleCommunities() {
		super("RANDOM_RING_ID_SPACE_SIMPLE_COMMUNITIES");
		this.modulus = 1.0;
		this.wrapAround = true;
	}

	/**
	 * 
	 * @param realities
	 * @param modulus
	 * @param wrapAround
	 */
	public RandomRingIDSpaceSimpleCommunities(double modulus, boolean wrapAround) {
		super("RANDOM_RING_ID_SPACE_SIMPLE_COMMUNITIES", new Parameter[] {
				new DoubleParameter("MODULUS", modulus),
				new BooleanParameter("WRAP_AROUND", wrapAround) });
		this.modulus = modulus;
		this.wrapAround = wrapAround;
	}

	@Override
	public Graph transform(Graph g) {
		Random rand = new Random();
		GraphProperty[] gps = g.getProperties("COMMUNITIES");
		for (GraphProperty gp : gps) {
			CommunityList cs = (CommunityList) gp;
			RingPartitionSimple[] partitions = new RingPartitionSimple[g
					.getNodes().length];
			RingIdentifierSpaceSimple idSpace = new RingIdentifierSpaceSimple(
					partitions, this.wrapAround);
			double size = this.modulus / (double) cs.getCommunities().length;
			for (Community c : cs.getCommunities()) {
				double start = (double) c.getIndex() * size;
				for (int i = 0; i < c.getNodes().length; i++) {
					partitions[c.getNodes()[i]] = new RingPartitionSimple(
							new RingIdentifier(
									start + rand.nextDouble() * size,
									wrapAround));
				}
			}
			g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		}
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.hasProperty("COMMUNITIES_0");
	}

}
