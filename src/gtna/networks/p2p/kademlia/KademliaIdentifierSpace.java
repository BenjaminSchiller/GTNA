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
 * KademliaIdentifierSpace.java
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
package gtna.networks.p2p.kademlia;

import gtna.graph.Graph;
import gtna.id.BIIdentifierSpace;
import gtna.id.Identifier;
import gtna.id.Partition;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class KademliaIdentifierSpace implements BIIdentifierSpace {
	private int bits;

	private BigInteger modulus;

	private BigInteger maxDistance;

	private KademliaPartition[] partitions;

	public KademliaIdentifierSpace() {
		this.bits = 0;
		this.modulus = BigInteger.ZERO;
		this.maxDistance = BigInteger.ZERO;
		this.partitions = new KademliaPartition[] {};
	}

	public KademliaIdentifierSpace(int bits) {
		this.bits = bits;
		this.modulus = (new BigInteger("2")).pow(this.bits);
		this.maxDistance = this.modulus.divide(new BigInteger("2"));
		this.partitions = new KademliaPartition[] {};
	}

	@Override
	public Partition<BigInteger>[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(Partition<BigInteger>[] partitions) {
		this.partitions = (KademliaPartition[]) partitions;
	}

	@Override
	public Identifier<BigInteger> randomID(Random rand) {
		return KademliaIdentifier.rand(rand, this);
	}

	@Override
	public BigInteger getMaxDistance() {
		return this.maxDistance;
	}

	/**
	 * @return the bits
	 */
	public int getBits() {
		return this.bits;
	}

	/**
	 * @return the modulus
	 */
	public BigInteger getModulus() {
		return this.modulus;
	}

	@Override
	public boolean write(String filename, String key) {
		// TODO implement writing of KademliaIdentifierSpace
		return false;
	}

	@Override
	public void read(String filename, Graph graph) {
		// TODO implement reading of KademliaIdentifierSpace
	}
	
	public String toString(){
		return "KademliaIdSpace: " + this.bits + " / " + this.modulus;
	}
}
