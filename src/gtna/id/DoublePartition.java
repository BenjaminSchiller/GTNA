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
 * DPartition.java
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
package gtna.id;

/**
 * @author benni
 * 
 */
public abstract class DoublePartition extends Partition {
	/**
	 * @param id
	 * @return distance from this partition to the identifier $id
	 */
	public abstract double distance(DoubleIdentifier id);

	/**
	 * @param id
	 * @return distance from this partition to the partition $p
	 */
	public abstract double distance(DoublePartition p);

	@Override
	public boolean isCloser(Identifier to, Identifier than) {
		return this.distance((DoubleIdentifier) to) < this
				.distance((DoubleIdentifier) than);
	}

	@Override
	public int getClosestNode(int[] nodes, Partition[] partitions) {
		if (nodes.length <= 0) {
			return -1;
		}
		int closest = nodes[0];
		double distance = ((DoublePartition) partitions[closest]).distance(this);

		for (int i = 1; i < nodes.length; i++) {
			double d = ((DoublePartition) partitions[nodes[i]]).distance(this);
			if (d < distance) {
				closest = nodes[i];
				distance = d;
			}
		}
		return closest;
	}
}
