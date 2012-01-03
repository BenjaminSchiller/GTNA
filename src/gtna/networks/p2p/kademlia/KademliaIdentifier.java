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
 * KademliaIdentifier.java
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

import gtna.id.BIIdentifier;
import gtna.id.Identifier;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class KademliaIdentifier implements BIIdentifier,
		Comparable<KademliaIdentifier> {
	private KademliaIdentifierSpace idSpace;

	private BigInteger id;

	private boolean[] bits;

	public KademliaIdentifier(KademliaIdentifierSpace idSpace, BigInteger id) {
		this.idSpace = idSpace;
		this.setId(id);
	}

	@Override
	public BigInteger distance(Identifier<BigInteger> id) {
		return this.id.xor(((KademliaIdentifier) id).getId());
	}

	public int prefix(KademliaIdentifier id) {
		for (int i = 0; i < this.bits.length; i++) {
			if (this.bits[i] ^ id.getBits()[i]) {
				return i;
			}
		}
		return this.bits.length;
	}

	@Override
	public boolean equals(Identifier<BigInteger> id) {
		return this.id.equals(((KademliaIdentifier) id).getId());
	}

	@Override
	public int compareTo(KademliaIdentifier id) {
		return this.id.compareTo(id.getId());
	}

	public static KademliaIdentifier rand(Random rand,
			KademliaIdentifierSpace idSpace) {
		return new KademliaIdentifier(idSpace, new BigInteger(
				idSpace.getBits(), rand));
	}

	public String toString() {
		String temp = this.id.toString(2);
		while (temp.length() < this.getIdSpace().getBits()) {
			temp = "0" + temp;
		}
		String temp2 = "";
		for (boolean b : this.bits) {
			temp2 += b ? "1" : "0";
		}
		return "KademliaID: " + temp + " " + temp2;
	}

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
		this.bits = new boolean[this.idSpace.getBits()];
		String str = this.id.toString(2);
		for (int i = 0; i < str.length(); i++) {
			this.bits[this.bits.length - str.length() + i] = (str.charAt(i) == '1');
		}
	}

	/**
	 * @return the idSpace
	 */
	public KademliaIdentifierSpace getIdSpace() {
		return this.idSpace;
	}

	/**
	 * @return the bits
	 */
	public boolean[] getBits() {
		return this.bits;
	}

}
