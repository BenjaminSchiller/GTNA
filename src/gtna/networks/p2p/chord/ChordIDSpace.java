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
 * ChordIDSpace.java
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

import gtna.graph.Graph;
import gtna.id.BIID;
import gtna.id.BIIDSpace;
import gtna.id.BIPartition;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class ChordIDSpace implements BIIDSpace {
	private int bits;

	private BigInteger modulus;

	private ChordPartition[] partitions;

	public ChordIDSpace(int bits) {
		this.bits = bits;
		this.modulus = BigInteger.ONE.add(BigInteger.ONE).pow(this.bits);
	}

	@Override
	public BIPartition[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(BIPartition[] partitions) {
		this.partitions = (ChordPartition[]) partitions;
	}

	@Override
	public BIID randomID(Random rand) {
		return ChordID.rand(rand, this);
	}

	@Override
	public boolean write(String filename, String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub

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
	}

	/**
	 * @return the modulus
	 */
	public BigInteger getModulus() {
		return this.modulus;
	}

	/**
	 * @param modulus
	 *            the modulus to set
	 */
	public void setModulus(BigInteger modulus) {
		this.modulus = modulus;
	}

	public String toString() {
		return this.bits + "-bit (% " + this.modulus + ")";
	}

}
