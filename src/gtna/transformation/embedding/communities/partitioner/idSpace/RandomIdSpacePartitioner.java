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
 * RandomIdSpacePartitioner.java
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

import java.util.Arrays;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class RandomIdSpacePartitioner extends IdSpacePartitioner {

	public RandomIdSpacePartitioner() {
		super("ID_SPACE_PARTITIONER_RANDOM");
	}

	@Override
	public double[][] getIntervals(IdentifierSpace ids, Graph g,
			Community[] communities) {
		if (!(ids instanceof RingIdentifierSpaceSimple)) {
			return null;
		}

		Random rand = new Random();

		double[] pos = new double[communities.length];
		for (int i = 0; i < pos.length; i++) {
			pos[i] = rand.nextDouble();
		}
		Arrays.sort(pos);
		pos[0] = 0;

		double[][] intervals = new double[communities.length][2];
		for (int i = 0; i < intervals.length; i++) {
			intervals[i][0] = pos[i];
			if (i == intervals.length - 1) {
				intervals[i][1] = 1.0;
			} else {
				intervals[i][1] = pos[i + 1];
			}
		}

		return intervals;
	}

}
