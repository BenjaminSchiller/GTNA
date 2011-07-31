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
 * RingID.java
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
package gtna.id;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RingID implements ID, Comparable<RingID> {
	private double pos;

	public RingID(double pos) {
		this.pos = pos;
	}

	@Override
	public double distance(ID id) {
		double dest = ((RingID) id).pos;
		double dist = this.pos <= dest ? dest - this.pos : dest + 1 - this.pos;
		return dist <= 0.5 ? dist : 1 - dist;
	}

	@Override
	public boolean equals(ID id) {
		return this.pos == ((RingID) id).pos;
	}

	public static RingID rand(Random rand) {
		return new RingID(rand.nextDouble());
	}

	/**
	 * @return the pos
	 */
	public double getPos() {
		return this.pos;
	}

	/**
	 * @param pos
	 *            the pos to set
	 */
	public void setPos(double pos) {
		this.pos = pos;
	}

	public String toString() {
		return "RingID(" + this.pos + ")";
	}

	@Override
	public int compareTo(RingID id) {
		if (id.getPos() < this.pos) {
			return 1;
		} else if (id.getPos() > this.pos) {
			return -1;
		} else {
			return 0;
		}
	}
}
