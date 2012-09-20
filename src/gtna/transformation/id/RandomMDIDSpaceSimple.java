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
import gtna.id.md.MDPartitionSimple;
import gtna.transformation.Transformation;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleArrayParameter;
import gtna.util.parameter.Parameter;

import java.util.Random;

/**
 * @author Nico
 * 
 */
public class RandomMDIDSpaceSimple extends Transformation {

	private double[] modulus;

	private boolean wrapAround;

	public RandomMDIDSpaceSimple() {
		super("RANDOM_MD_ID_SPACE_SIMPLE");
		this.modulus = new double[] { 1.0 };
		this.wrapAround = false;
	}

	public RandomMDIDSpaceSimple(double[] modulus, boolean wrapAround) {
		super("RANDOM_MD_ID_SPACE_SIMPLE", new Parameter[] {
				new DoubleArrayParameter("MODULI", modulus),
				new BooleanParameter("WRAPAROUND", wrapAround) });
		this.modulus = modulus;
		this.wrapAround = wrapAround;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		MDPartitionSimple[] partitions = new MDPartitionSimple[graph.getNodes().length];
		MDIdentifierSpaceSimple idSpace = new MDIdentifierSpaceSimple(
				partitions, this.modulus, this.wrapAround);
		for (int i = 0; i < partitions.length; i++) {
			partitions[i] = new MDPartitionSimple(
					(MDIdentifier) idSpace.getRandomIdentifier(rand));
		}
		graph.addProperty(graph.getNextKey("ID_SPACE"), idSpace);
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}
}
