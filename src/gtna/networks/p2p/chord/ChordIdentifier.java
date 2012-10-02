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
 * ChordIdentifier.java
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
import gtna.id.Identifier;

import java.math.BigInteger;

/**
 * @author benni
 * 
 */
public class ChordIdentifier extends BigIntegerIdentifier {

	protected BigInteger position;

	protected int bits;

	protected BigInteger modulus;

	public ChordIdentifier(BigInteger position, int bits) {
		this.position = position;
		this.bits = bits;
		this.modulus = BigInteger.ONE.add(BigInteger.ONE).pow(this.bits);
	}

	public ChordIdentifier(String string) {
		String[] temp = string.split(Identifier.delimiter);
		this.position = new BigInteger(temp[0]);
		this.bits = Integer.parseInt(temp[1]);
		this.modulus = BigInteger.ONE.add(BigInteger.ONE).pow(this.bits);
	}

	public String toString() {
		return "Chord:" + this.position;
	}

	@Override
	public int compareTo(BigIntegerIdentifier o) {
		return this.position.compareTo(((ChordIdentifier) o).getPosition());
	}

	@Override
	public BigInteger distance(BigIntegerIdentifier id) {
		BigInteger pos = ((ChordIdentifier) id).getPosition();

		if (this.position.compareTo(pos) == -1)
			return pos.subtract(this.position);

		return this.modulus.subtract(this.position).add(pos);
	}

	@Override
	public String asString() {
		return this.position.toString() + Identifier.delimiter + this.bits;
	}

	@Override
	public boolean equals(Identifier id) {
		return this.position.equals(((ChordIdentifier) id).getPosition())
				&& this.bits == ((ChordIdentifier) id).getBits();
	}

	/**
	 * @return the position
	 */
	public BigInteger getPosition() {
		return this.position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(BigInteger position) {
		this.position = position;
	}

	/**
	 * @return the bits
	 */
	public int getBits() {
		return this.bits;
	}

	/**
	 * @param bits
	 *            the bits to set
	 */
	public void setBits(int bits) {
		this.bits = bits;
		this.modulus = BigInteger.ONE.add(BigInteger.ONE).pow(this.bits);
	}

	/**
	 * @return the modulus
	 */
	public BigInteger getModulus() {
		return this.modulus;
	}

}
