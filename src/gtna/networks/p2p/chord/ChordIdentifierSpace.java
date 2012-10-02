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
 * ChordIdentifierSpace.java
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

import gtna.id.BigIntegerIdentifierSpace;
import gtna.id.Identifier;
import gtna.io.Filereader;
import gtna.io.Filewriter;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class ChordIdentifierSpace extends BigIntegerIdentifierSpace {

	protected int bits;

	protected BigInteger modulus;

	public ChordIdentifierSpace(ChordPartition[] partitions, int bits) {
		super(partitions);
		this.bits = bits;
		this.modulus = BigInteger.ONE.add(BigInteger.ONE).pow(this.bits);
	}

	@Override
	public BigInteger getMaxDistance() {
		return this.modulus;
	}

	@Override
	protected void writeParameters(Filewriter fw) {
		this.writeParameter(fw, "Bits", this.bits);
	}

	@Override
	protected void readParameters(Filereader fr) {
		this.bits = this.readInt(fr);
		this.modulus = BigInteger.ONE.add(BigInteger.ONE).pow(this.bits);
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		return new ChordIdentifier(new BigInteger(this.bits, rand), this.bits);
	}

	/**
	 * @return the bits
	 */
	public int getBits() {
		return this.bits;
	}

	/**
	 * @param bits the bits to set
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
