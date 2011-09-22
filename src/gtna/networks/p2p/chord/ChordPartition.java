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

import gtna.id.BIIdentifier;
import gtna.id.BIPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

import java.math.BigInteger;

/**
 * @author benni
 * 
 */
public class ChordPartition implements BIPartition {
	private ChordIdentifier pred;

	private ChordIdentifier succ;

	public ChordPartition(ChordIdentifier pred, ChordIdentifier succ) {
		this.pred = pred;
		this.succ = succ;
	}

	@Override
	public BigInteger distance(Identifier<BigInteger> id) {
		if (this.contains(id)) {
			return BigInteger.ZERO;
		}
		BigInteger compare = ((ChordIdentifier) id).getId();
		return this.succ.distance(id);
//		if (this.succ.getId().compareTo(compare) == -1) {
//			return compare.subtract(this.succ.getId()).mod(
//					this.succ.getIdSpace().getModulus());
//		} else {
//			return compare.add(this.succ.getIdSpace().getModulus())
//					.subtract(this.succ.getId())
//					.mod(this.succ.getIdSpace().getModulus());
//		}
	}

	@Override
	public boolean equals(Partition<BigInteger> partition) {
		ChordPartition compare = (ChordPartition) partition;
		return this.pred.equals(compare.getPred())
				&& this.succ.equals(compare.getSucc());
	}

	@Override
	public boolean contains(Identifier<BigInteger> id) {
		BigInteger v = ((ChordIdentifier) id).getId();
		BigInteger p = this.pred.getId();
		BigInteger s = this.succ.getId();
		if (this.pred.getId().compareTo(this.succ.getId()) == -1) {
			return p.compareTo(v) == -1 && v.compareTo(s) != 1;
		} else {
			return p.compareTo(v) == -1 || v.compareTo(s) != 1;
		}
	}

	@Override
	public BIIdentifier getRepresentativeID() {
		return this.succ;
	}

	/**
	 * @return the pred
	 */
	public ChordIdentifier getPred() {
		return this.pred;
	}

	/**
	 * @param pred
	 *            the pred to set
	 */
	public void setPred(ChordIdentifier pred) {
		this.pred = pred;
	}

	/**
	 * @return the succ
	 */
	public ChordIdentifier getSucc() {
		return this.succ;
	}

	/**
	 * @param succ
	 *            the succ to set
	 */
	public void setSucc(ChordIdentifier succ) {
		this.succ = succ;
	}

	public String toString() {
		return "(" + this.pred.getId() + ", " + this.succ.getId() + "]";
	}

}
