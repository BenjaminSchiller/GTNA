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
 * Partition.java
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

import java.util.Random;

/**
 * @author benni
 * 
 */
public abstract class Partition {
	public static final String delimiter = ":";

	public int hashCode() {
		return this.asString().hashCode();
	}

	/**
	 * 
	 * @return String representation of this partition that can be parsed and
	 *         interpreted to create a new instance of this partition type. For
	 *         creating new instances of an partition, each partition must
	 *         supply a constructor with a single String parameter which is
	 *         equal to the one created by this method.
	 */
	public abstract String asString();

	/**
	 * 
	 * @param id
	 * @return true if this partition contains the identifier $id; false
	 *         otherwise
	 */
	public abstract boolean contains(Identifier id);

	/**
	 * 
	 * @param to
	 * @param than
	 * @return true if the distance of this partition to the identifier $to is
	 *         smaller than or equal to the distance of this partition to the
	 *         identifier $than; false otherwise
	 */
	public abstract boolean isCloser(Identifier to, Identifier than);

	/**
	 * CONVENTION: in case the distance between this partition and two or more
	 * partitions is equal, the first node in the list is returned
	 * 
	 * @param nodes
	 *            list of node indices
	 * @param partitions
	 *            list of all partitions
	 * @return index of the node in $nodes whose partition has the smallest
	 *         distance to this partition
	 */
	public abstract int getClosestNode(int[] nodes, Partition[] partitions);

	/**
	 * 
	 * @return identifier that is representative for this partition, e.g., B in
	 *         the case of an interval (A, B]
	 */
	public abstract Identifier getRepresentativeIdentifier();

	/**
	 * 
	 * @param rand
	 * @return random identifier selected uniformly from all possible
	 *         identifiers in this partition
	 */
	public abstract Identifier getRandomIdentifier(Random rand);

	/**
	 * 
	 * @param p
	 * @return true if this partition equals the given one; false otherwise
	 */
	public abstract boolean equals(Partition p);
}
