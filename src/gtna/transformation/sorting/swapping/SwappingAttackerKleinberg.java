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
 * SwappingAttackerKleinberg.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.sorting.swapping;

import java.util.Arrays;
import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public class SwappingAttackerKleinberg extends SwappingNode {

	public SwappingAttackerKleinberg(int index, double pos, Swapping swapping) {
		super(index, pos, swapping);
	}

	/**
	 * select ID at biggest distance to any neighbor; send no swap request,
	 * since attacker could even offer a better ID (no knowledge)
	 */
	public void turn(Random rand) {
		double[] neighbors = this.knownIDs.clone();
		this.getID().pos = (maxMiddle(neighbors) + rand.nextDouble()
				* this.swapping.delta) % 1.0;
	}

	/**
	 * return ID close to current requester's ID to keep it from changing its ID
	 */
	protected double ask(SwappingNode caller, Random rand) {
		int index = this.position.get(caller);
		double id = (this.knownIDs[index] + rand.nextDouble()
				* this.swapping.delta) % 1.0;
		return id;
	}

	/**
	 * give ID that is extremely bad for caller: biggest distance to any
	 * neighbor node
	 */
	protected double swap(double callerID, double[] callerNeighborIDs, int ttl,
			Random rand) {
		return (maxMiddle(callerNeighborIDs) + rand.nextDouble()
				* this.swapping.delta) % 1.0;
	}

	public static double maxMiddle(double[] values) {
		Arrays.sort(values);
		double max = 1 - values[values.length - 1] + values[0];
		int index = values.length - 1;
		for (int i = 0; i < values.length - 1; i++) {
			double a = values[i];
			double b = values[i + 1];
			double dist = b - a;
			if (dist > max) {
				max = dist;
				index = i;
			}
		}
		double middle = (values[index] + max / 2) % 1.0;
		return middle;
	}

}
