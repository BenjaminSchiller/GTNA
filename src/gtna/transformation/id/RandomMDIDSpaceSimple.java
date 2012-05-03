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
 * RandomMDIDSpaceSimple.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.id;

import gtna.graph.Graph;
import gtna.id.md.MDIdentifier;
import gtna.id.md.MDIdentifierSpaceSimple;
import gtna.id.md.MDIdentifierSpaceSimple.DistanceMD;
import gtna.id.md.MDPartitionSimple;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleArrayParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.Random;

/**
 * @author Nico
 * 
 */
public class RandomMDIDSpaceSimple extends Transformation {

	private int realities;
	private double[] modulus;
	private boolean wrapAround;
	private DistanceMD dist;

	public RandomMDIDSpaceSimple() {
		super("RANDOM_MD_ID_SPACE_SIMPLE");
		this.realities = 1;
	}

	public RandomMDIDSpaceSimple(int realities, double[] modulus,
			boolean wrapAround, DistanceMD dist) {
		super("RANDOM_MD_ID_SPACE_SIMPLE", new Parameter[] {
				new IntParameter("REALITIES", realities),
				new DoubleArrayParameter("MODULI", modulus),
				new BooleanParameter("WRAPAROUND", wrapAround),
				new StringParameter("DISTANCE", dist.toString())});
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.dist = dist;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			MDPartitionSimple[] partitions = new MDPartitionSimple[graph
					.getNodes().length];
			MDIdentifierSpaceSimple idSpace = new MDIdentifierSpaceSimple(
					partitions, this.modulus, this.wrapAround, this.dist);
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new MDPartitionSimple(MDIdentifier.rand(rand,
						idSpace));
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
