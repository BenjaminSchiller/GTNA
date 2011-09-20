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
 * ChordID.java
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

import gtna.id.BIID;
import gtna.id.Identifier;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class ChordID implements BIID, Comparable<ChordID> {
	private ChordIDSpace idSpace;

	private BigInteger id;

	public ChordID(ChordIDSpace idSpace, BigInteger id) {
		this.idSpace = idSpace;
		this.id = id;
	}

	@Override
	public int compareTo(ChordID arg0) {
		return this.id.compareTo(((ChordID) arg0).getId());
	}

	public BigInteger distance(Identifier<BigInteger> id) {
		BigInteger value = ((ChordID) id).getId();
		BigInteger sub1 = this.id.subtract(value).abs();
		BigInteger sub2 = this.idSpace.getModulus().subtract(sub1);
		return sub1.min(sub2);
	}

	@Override
	public boolean equals(Identifier<BigInteger> id) {
		return this.id.equals(((ChordID) id).getId());
	}

	public static ChordID rand(Random rand, ChordIDSpace idSpace) {
		return new ChordID(idSpace, new BigInteger(idSpace.getBits(), rand));
	}

	/**
	 * @return the idSpace
	 */
	public ChordIDSpace getIdSpace() {
		return this.idSpace;
	}

	/**
	 * @param idSpace
	 *            the idSpace to set
	 */
	public void setIdSpace(ChordIDSpace idSpace) {
		this.idSpace = idSpace;
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
	}

	public String toString() {
		return "ChordID(" + this.id + ")";
	}

}
