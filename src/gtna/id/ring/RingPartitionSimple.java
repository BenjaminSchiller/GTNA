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
 * RingPartitionSingleID.java
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

import java.util.Random;

import gtna.id.DoubleIdentifier;
import gtna.id.DoublePartition;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author benni
 * 
 */
public class RingPartitionSimple extends DoublePartition {

	protected RingIdentifier identifier;

	public RingPartitionSimple(RingIdentifier identifier) {
		this.identifier = identifier;
	}

	public RingPartitionSimple(String string) {
		String[] temp = string.split(Partition.delimiter);
		this.identifier = new RingIdentifier(Double.parseDouble(temp[0]),
				Boolean.parseBoolean(temp[1]));
	}

	public String toString() {
		return "R [" + this.identifier.position + "]";
	}

	@Override
	public double distance(DoubleIdentifier id) {
		return this.identifier.distance(id);
	}

	@Override
	public double distance(DoublePartition p) {
		return this.distance(((RingPartitionSimple) p).identifier);
	}

	@Override
	public String asString() {
		return this.identifier.position + Partition.delimiter
				+ this.identifier.wrapAround;
	}

	@Override
	public boolean contains(Identifier id) {
		return this.identifier.equals(id);
	}

	@Override
	public Identifier getRepresentativeIdentifier() {
		return new RingIdentifier(this.identifier.position,
				this.identifier.wrapAround);
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		return new RingIdentifier(this.identifier.position,
				this.identifier.wrapAround);
	}

	@Override
	public boolean equals(Partition p) {
		return this.identifier
				.equals(((RingPartitionSimple) p).identifier);
	}

	/**
	 * @return the identifier
	 */
	public RingIdentifier getIdentifier() {
		return this.identifier;
	}

	/**
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(RingIdentifier identifier) {
		this.identifier = identifier;
	}

}
