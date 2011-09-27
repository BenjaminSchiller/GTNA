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
 * MDPartitionSimple.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.id.md;

import gtna.id.DIdentifier;
import gtna.id.DPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author Nico
 *
 */
public class MDPartitionSimple implements DPartition {
	private MDIdentifier id;

	public MDPartitionSimple(MDIdentifier id) {
		this.id = id;
	}

	public MDPartitionSimple(String string, MDIdentifierSpaceSimple idSpace) {
		this.id = new MDIdentifier(string, idSpace);
	}

	public MDPartitionSimple(String string) {
		this(string, null);
	}

	public String toString() {
		return this.id.toString();
	}
	
	@Override
	public Double distance(Identifier<Double> id) {
		return this.id.distance(id);
	}
	
	@Override
	public boolean equals(Partition<Double> partition) {
		return this.id.equals((((MDPartitionSimple) partition).getId()));
	}

	@Override
	public boolean contains(Identifier<Double> id) {
		return this.id.equals(id);
	}

	@Override
	public DIdentifier getRepresentativeID() {
		return this.id;
	}
	
	/**
	 * @return the id
	 */
	public MDIdentifier getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(MDIdentifier id) {
		this.id = id;
	}	
}
