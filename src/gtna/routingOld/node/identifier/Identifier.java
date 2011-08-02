/*
 * ===========================================================
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
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.routingOld.node.identifier;

/**
 * Interface for identifiers that are required by gtna.routing.node.IDNode. Each
 * instance of such an identifier is an element of an identifier space used by
 * the respective system like, e.g., DHTs. These identifier are then used to
 * enable routing using various algorithms.
 * 
 * Conventions: Please follow these conventions if possible and applicable to
 * the respective system.
 * 
 * 1. When using d-dimensional identifier spaces, use [0,1)^d.
 * 
 * 2. Normalize the distance function to produce values \in [0,1).
 * 
 * @author benni
 * 
 */
@Deprecated
public interface Identifier {
	/**
	 * Computes the distance of this identifier to the given one. Note that
	 * according to the second convention, this values should always be \in
	 * [0,1).
	 * 
	 * @param id
	 *            identifier to compare the distance to
	 * @return distance between the two identifiers
	 */
	public double dist(Identifier id);

	/**
	 * Determines if this identifier has the same value as the given one.
	 * 
	 * @param id
	 *            identifier to compare to
	 * @return true if the given identifier's value equals this identifier's,
	 *         false otherwise
	 */
	public boolean equals(Identifier id);
}
