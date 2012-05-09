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
 * EuclideanDDMDEmbedding.java
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
package gtna.transformation.attackableEmbedding.md.distanceDiversity;

import gtna.id.md.MDIdentifierSpaceSimple.DistanceMD;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding.AttackerSelection;
import gtna.transformation.attackableEmbedding.IQD.AttackerIQDEmbedding.AttackerType;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.DecisionMethod;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.IdentifierMethod;

/**
 * @author stef standard parameter for DD embedding using an euclidean space
 */
public class StandardDDMDEmbedding extends DistanceDiversityMDEmbedding {

	public StandardDDMDEmbedding(int iterations, int dimensions) {
		this(iterations, dimensions, DistanceMD.EUCLIDEAN);
	}

	public StandardDDMDEmbedding(int iterations, int dimensions,
			AttackerType type, AttackerSelection selection, int count) {
		this(iterations, dimensions, DistanceMD.EUCLIDEAN, type, selection, count);
	}

	public StandardDDMDEmbedding(int iterations, int dimensions, DistanceMD dist) {
		super(iterations, IdentifierMethod.ONERANDOM,
				DecisionMethod.BESTPREFEROLD, dist, dimensions, 1E-13, false,
				true, 20, 0.5, false);
	}

	public StandardDDMDEmbedding(int iterations, int dimensions,
			DistanceMD dist, AttackerType type, AttackerSelection selection,
			int count) {
		super(iterations, IdentifierMethod.ONERANDOM,
				DecisionMethod.BESTPREFEROLD, dist, dimensions, 1E-13, false,
				true, 20, 0.5, type, selection, count, false);
	}

}
