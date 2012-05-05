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
 * SwappingEmbedding.java
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
package gtna.transformation.attackableEmbedding.IQD.kleinberg;

import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding.AttackerSelection;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding.AttackerType;
import gtna.util.parameter.Parameter;

/**
 * @author stef
 * 
 */
public class SwappingEmbedding extends KleinbergEmbedding {

	/**
	 * @param iterations
	 * @param idMethod
	 * @param deMethod
	 * @param distance
	 * @param epsilon
	 * @param checkold
	 * @param parameters
	 */
	public SwappingEmbedding(int iterations, Distance distance, double epsilon) {
		super("SWAPPINGEMBEDDING", iterations, IQDEmbedding.IdentifierMethod.SWAPPING,
				IQDEmbedding.DecisionMethod.METROPOLIS, distance, epsilon,
				false, false, new Parameter[0]);
	}
	
	public SwappingEmbedding(int iterations, Distance distance, double epsilon, AttackerType type, AttackerSelection selection,
			int attackercount) {
		super("SWAPPINGEMBEDDING", iterations, IQDEmbedding.IdentifierMethod.SWAPPING,
				IQDEmbedding.DecisionMethod.METROPOLIS, distance, epsilon,
				false, false, type, selection, attackercount);
	}
	
//	public SwappingEmbedding(int iterations, Distance distance, double epsilon) {
//		super(iterations, IQDEmbedding.IdentifierMethod.SWAPPING,
//				IQDEmbedding.DecisionMethod.METROPOLIS, distance, epsilon,
//				false, false, new Parameter[0]);
//	}
//	
//	public SwappingEmbedding(int iterations, Distance distance, double epsilon, AttackerType type, AttackerSelection selection,
//			int attackercount) {
//		super(iterations, IQDEmbedding.IdentifierMethod.SWAPPING,
//				IQDEmbedding.DecisionMethod.METROPOLIS, distance, epsilon,
//				false, false, type, selection, attackercount);
//	}
	
	

}
