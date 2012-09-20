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
 * EqualSizeIdSpacePartitioner.java
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
package gtna.transformation.embedding.communities.partitioner.idSpace;

import gtna.communities.Community;
import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.id.ring.RingIdentifierSpaceSimple;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * @author benni
 * 
 */
public class EqualSizeIdSpacePartitioner extends IdSpacePartitioner {

	protected double emptyRatio;

	public EqualSizeIdSpacePartitioner(double emptyRatio) {
		super(
				"ID_SPACE_PARTITIONER_EQUAL_SIZE",
				new Parameter[] { new DoubleParameter("EMPTY_RATIO", emptyRatio) });
		this.emptyRatio = emptyRatio;
	}

	@Override
	public double[][] getIntervals(IdentifierSpace ids, Graph g,
			Community[] communities) {
		if (!(ids instanceof RingIdentifierSpaceSimple)) {
			return null;
		}

		double[][] intervals = new double[communities.length][2];

		double totalWidth = 1.0 / (double) communities.length;
		double intervalWidth = totalWidth / (1.0 + this.emptyRatio);
		// double emptyWidth = totalWidth * this.emptyRatio
		// / (1.0 + this.emptyRatio);

		for (int i = 0; i < communities.length; i++) {
			intervals[i][0] = totalWidth * (double) i;
			intervals[i][1] = intervals[i][0] + intervalWidth;
		}

		return intervals;
	}

}
