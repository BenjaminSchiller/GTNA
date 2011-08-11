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

import gtna.id.BIID;
import gtna.id.BIPartition;

import java.math.BigInteger;

/**
 * @author benni
 * 
 */
public class ChordPartition implements BIPartition {
	private ChordID pred;

	private ChordID succ;

	public ChordPartition(ChordID pred, ChordID succ) {
		this.pred = pred;
		this.succ = succ;
	}

	@Override
	public BigInteger distance(BIID id) {
		return this.succ.distance(id);
	}

	@Override
	public boolean equals(BIPartition partition) {
		ChordPartition compare = (ChordPartition) partition;
		return this.pred.equals(compare.getPred())
				&& this.succ.equals(compare.getSucc());
	}

	@Override
	public boolean contains(BIID id) {
		BigInteger v = ((ChordID) id).getId();
		BigInteger p = this.pred.getId();
		BigInteger s = this.succ.getId();
		if (this.pred.getId().compareTo(this.succ.getId()) == -1) {
			return p.compareTo(v) == -1 && v.compareTo(s) != 1;
		} else {
			return p.compareTo(v) == -1 || v.compareTo(s) != 1;
		}
	}

	/**
	 * @return the pred
	 */
	public ChordID getPred() {
		return this.pred;
	}

	/**
	 * @param pred
	 *            the pred to set
	 */
	public void setPred(ChordID pred) {
		this.pred = pred;
	}

	/**
	 * @return the succ
	 */
	public ChordID getSucc() {
		return this.succ;
	}

	/**
	 * @param succ
	 *            the succ to set
	 */
	public void setSucc(ChordID succ) {
		this.succ = succ;
	}
	
	public String toString(){
		return "(" + this.pred.getId() + ", " + this.succ.getId() + "]";
	}

}
