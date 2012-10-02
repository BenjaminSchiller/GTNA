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

import gtna.id.DoubleIdentifier;
import gtna.id.DoublePartition;
import gtna.id.Identifier;
import gtna.id.Partition;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class MDPartitionSimple extends DoublePartition {

	protected MDIdentifier identifier;

	public MDPartitionSimple(MDIdentifier id) {
		this.identifier = id;
	}

	public MDPartitionSimple(String string) {
		this.identifier = new MDIdentifier(string);
	}

	@Override
	public double distance(DoubleIdentifier id) {
		return this.identifier.distance(id);
	}

	@Override
	public double distance(DoublePartition p) {
		return this.identifier.distance(identifier);
	}

	@Override
	public String asString() {
		return this.identifier.asString();
	}

	@Override
	public boolean contains(Identifier id) {
		return this.identifier.equals(id);
	}

	@Override
	public Identifier getRepresentativeIdentifier() {
		return this.identifier;
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		return new MDIdentifier(this.identifier.coordinates.clone(),
				this.identifier.modulus.clone(), this.identifier.wrapAround);
	}

	@Override
	public boolean equals(Partition p) {
		return this.identifier.equals(((MDPartitionSimple) p).identifier);
	}

	/**
	 * @return the identifier
	 */
	public MDIdentifier getIdentifier() {
		return this.identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(MDIdentifier identifier) {
		this.identifier = identifier;
	}
}
