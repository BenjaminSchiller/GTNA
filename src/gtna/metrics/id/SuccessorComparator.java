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
 * SuccessorComparator.java
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
package gtna.metrics.id;

import gtna.id.Partition;
import gtna.id.ring.RingIdentifier;
import gtna.util.ArrayUtils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author benni
 * 
 */
public class SuccessorComparator implements Comparator<Integer> {
	private Partition[] partitions;

	public SuccessorComparator(Partition[] partitions) {
		this.partitions = partitions;
	}

	@Override
	public int compare(Integer n1, Integer n2) {
		RingIdentifier id1 = (RingIdentifier) this.partitions[n1]
				.getRepresentativeIdentifier();
		RingIdentifier id2 = (RingIdentifier) this.partitions[n2]
				.getRepresentativeIdentifier();
		double dist = id2.getPosition() - id1.getPosition();
		if (dist == 0) {
			return 0;
		} else if (dist < 0) {
			return 1;
		} else {
			return -1;
		}
	}

	public static int[] getNodesSorted(Partition[] partitions) {
		Integer[] nodesSorted = ArrayUtils.initIntegerArray(partitions.length);

		SuccessorComparator sc = new SuccessorComparator(partitions);
		Arrays.sort(nodesSorted, sc);

		return ArrayUtils.toIntArray(nodesSorted);
	}

}
