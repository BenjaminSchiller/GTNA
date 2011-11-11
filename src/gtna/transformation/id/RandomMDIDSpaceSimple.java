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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

import gtna.graph.Graph;
import gtna.id.md.*;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

/**
 * @author Nico
 *
 */
public class RandomMDIDSpaceSimple extends TransformationImpl implements
Transformation {

	private int realities;
	private double[] modulus;
	private boolean wrapAround;
	private MDIdentifierSpaceSimple idSpace;
	
	public RandomMDIDSpaceSimple() {
		super("RANDOM_MD_ID_SPACE_SIMPLE", new String[] {}, new String[] {});
		this.realities = 1;
	}

	public RandomMDIDSpaceSimple(int realities, double[] modulus, boolean wrapAround) {
		super("RANDOM_MD_ID_SPACE_SIMPLE", new String[] { "REALITIES",
				"DIMENSIONS", "MODULUS", "WRAP_AROUND" }, new String[] {
				"" + realities, "" + modulus.length, Arrays.toString(modulus), "" + wrapAround });
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
	}
	
	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			MDPartitionSimple[] partitions = new MDPartitionSimple[graph
					.getNodes().length];
			this.idSpace = new MDIdentifierSpaceSimple(partitions,
					this.modulus, this.wrapAround);
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
	
	public MDIdentifierSpaceSimple getIdSpace() {
		return this.idSpace;
	}

}
