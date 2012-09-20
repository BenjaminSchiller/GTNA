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
 * Identifier.java
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
public abstract class Identifier {
	public static final String delimiter = ";";

	public int hashCode() {
		return this.asString().hashCode();
	}

	/**
	 * 
	 * @return String representation of this identifier that can be parsed and
	 *         interpreted to create a new instance of this identifier type. For
	 *         creating new instances of an identifier, each identifier must
	 *         supply a constructor with a single String parameter which is
	 *         equal to the one created by this method.
	 */
	public abstract String asString();

	/**
	 * true if dist(this, to) <= dist(this, than)
	 * 
	 * @param to
	 * @param than
	 * @return true if the distance of this identifier to the identifier $to is
	 *         smaller than or equal to the distance of this identifier to the
	 *         identifier $than; false otherwise
	 */
	public abstract boolean isCloser(Identifier to, Identifier than);

	/**
	 * true if dist(to, this) <= dist(than, this)
	 * 
	 * @param to
	 * @param than
	 * @return true if the distance of this identifier to the partition $to is
	 *         smaller than or equal to the distance of this identifier to the
	 *         partition $than; false otherwise
	 */
	public abstract boolean isCloser(Partition to, Partition than);

	/**
	 * CONVENTION: in case the distance between this identifier and two or more
	 * partitions is equal, the first node in the list is returned
	 * 
	 * @param nodes
	 *            list of node indices
	 * @param partitions
	 *            list of all partitions
	 * @return index of the node in $nodes whose partition has the smallest
	 *         distance to this identifier
	 */
	public abstract int getClosestNode(int[] nodes, Partition[] partitions);

	/**
	 * 
	 * @param id
	 * @return true if this identifier equals the given one; false otherwise
	 */
	public abstract boolean equals(Identifier id);
}
