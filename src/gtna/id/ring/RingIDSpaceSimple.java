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
 * RingIDSpaceSimple.java
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
package gtna.id.ring;

import gtna.graph.GraphProperty;
import gtna.id.IDSpace;
import gtna.id.Partition;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RingIDSpaceSimple implements IDSpace {
	private RingPartitionSimple[] partitions;

	public RingIDSpaceSimple(RingPartitionSimple[] partitions) {
		this.partitions = partitions;
	}

	@Override
	public Partition[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(Partition[] partitions) {
		this.partitions = (RingPartitionSimple[]) partitions;
	}

	@Override
	public RingID randomID(Random rand) {
		return this.partitions[rand.nextInt(this.partitions.length)].getId();
	}

	@Override
	public boolean write(String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GraphProperty read(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

}
