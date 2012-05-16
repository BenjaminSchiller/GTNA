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
 * SwappingMDEmbedding.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.attackableEmbedding.md.kleinberg;

import gtna.id.md.MDIdentifierSpaceSimple.DistanceMD;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding.AttackerSelection;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding.AttackerType;
import gtna.transformation.attackableEmbedding.md.IQDMDEmbedding;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

/**
 * @author stef
 * 
 */
public class SwappingMDEmbedding extends KleinbergMDEmbedding {

	public SwappingMDEmbedding(int iterations, int dimension) {
		this(iterations, dimension, DistanceMD.EUCLIDEAN);
	}

	public SwappingMDEmbedding(int iterations, int dimension, AttackerType type,
			AttackerSelection selection, int attackercount) {
		this(iterations, dimension, DistanceMD.EUCLIDEAN, type, selection,attackercount);
	}
	
	public SwappingMDEmbedding(int iterations, int dimension, DistanceMD dist) {
		super("SWAPPING_MD_EMBEDDING", iterations,
				IQDMDEmbedding.IdentifierMethod.SWAPPING,
				IQDMDEmbedding.DecisionMethod.METROPOLIS, dist,
				dimension, 1E-13, false, false,
				new Parameter[] {new IntParameter("ITERATIONS",
						iterations), new IntParameter("DIMENSION",
								dimension),  new StringParameter("DISTANCE",
										dist.toString()) },
				false);
	}

	public SwappingMDEmbedding(int iterations, int dimension, DistanceMD dist, AttackerType type,
			AttackerSelection selection, int attackercount) {
		super("SWAPPING_MD_EMBEDDING", iterations,
				IQDMDEmbedding.IdentifierMethod.SWAPPING,
				IQDMDEmbedding.DecisionMethod.METROPOLIS, dist,
				dimension, 1E-13, false, false, type, selection, attackercount,
				new Parameter[] {new IntParameter("ITERATIONS",
						iterations), new IntParameter("DIMENSION",
								dimension), new StringParameter("DISTANCE",
										dist.toString()) },
				false);
	}

}
