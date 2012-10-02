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
 * ChordPartition.java
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
package gtna.networks.p2p.chord;

import gtna.id.BigIntegerIdentifier;
import gtna.id.BigIntegerPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class ChordPartition extends BigIntegerPartition {

	protected ChordIdentifier start;

	protected ChordIdentifier end;

	public ChordPartition(ChordIdentifier start, ChordIdentifier end) {
		this.start = start;
		this.end = end;
	}

	public ChordPartition(String string) {
		String[] temp = string.split(Partition.delimiter);
		int bits = Integer.parseInt(temp[2]);
		this.start = new ChordIdentifier(new BigInteger(temp[0]), bits);
		this.end = new ChordIdentifier(new BigInteger(temp[1]), bits);
	}

	public String toString() {
		return "Chord (" + this.start.getPosition() + ", "
				+ this.end.getPosition() + "]";
	}

	@Override
	public BigInteger distance(BigIntegerIdentifier id) {
		if (this.contains(id))
			return BigInteger.ZERO;

		return this.end.distance(id);
	}

	@Override
	public BigInteger distance(BigIntegerPartition p) {
		return this.end.distance(((ChordPartition) p).getEnd());
	}

	@Override
	public String asString() {
		return this.start.position + Partition.delimiter + this.end.position
				+ Partition.delimiter + this.start.getBits();
	}

	@Override
	public boolean contains(Identifier id) {
		BigInteger start = this.start.position;
		BigInteger end = this.end.position;
		BigInteger pos = ((ChordIdentifier) id).position;

		if (!this.isWrapping())
			return start.compareTo(pos) < 0 && pos.compareTo(end) <= 0;

		return start.compareTo(pos) < 0 || pos.compareTo(end) <= 0;
	}

	@Override
	public Identifier getRepresentativeIdentifier() {
		return new ChordIdentifier(this.end.position, this.end.getBits());
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Partition p) {
		return this.start.equals(((ChordPartition) p).getStart())
				&& this.end.equals(((ChordPartition) p).getEnd());
	}

	public BigInteger getIntervalWidth() {
		if (!this.isWrapping())
			return this.end.position.subtract(this.start.position);

		return this.start.modulus.add(this.end.position).subtract(
				this.start.position);
	}

	/**
	 * 
	 * @return true if this partition is wrapping around 0, i.e., end <= start
	 */
	public boolean isWrapping() {
		return this.end.position.compareTo(this.start.position) <= 0;
	}

	/**
	 * @return the start
	 */
	public ChordIdentifier getStart() {
		return this.start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(ChordIdentifier start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public ChordIdentifier getEnd() {
		return this.end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(ChordIdentifier end) {
		this.end = end;
	}

}
