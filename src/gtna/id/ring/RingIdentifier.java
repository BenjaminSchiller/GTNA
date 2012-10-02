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
 * RingIdentifier.java
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
package gtna.id.ring;

import gtna.id.DoubleIdentifier;
import gtna.id.Identifier;

/**
 * Implements an ID in the wrapping ID space [0,1) (i.e. a ring). Distance
 * computations are performed with or without wrap-around depending on the flag.
 * When creating a RingID or setting a new position, the position is computed
 * modulo 1.0.
 * 
 * @author benni
 * 
 */
public class RingIdentifier extends DoubleIdentifier {
	protected double position;

	protected boolean wrapAround;

	public RingIdentifier(double position, boolean wrapAround) {
		this.position = position % 1.0;
		this.wrapAround = wrapAround;
	}

	public RingIdentifier(String string) {
		String[] temp = string.split(Identifier.delimiter);
		this.position = Double.parseDouble(temp[0]) % 1.0;
		this.wrapAround = Boolean.parseBoolean(temp[1]);
	}

	public String toString() {
		return "R:" + this.position;
	}

	@Override
	public int compareTo(DoubleIdentifier o) {
		if (this.position < ((RingIdentifier) o).position)
			return -1;
		else if (this.position == ((RingIdentifier) o).position)
			return 0;
		else
			return 1;
	}

	@Override
	public double distance(DoubleIdentifier id) {
		double pos = ((RingIdentifier) id).position;

		if (!this.wrapAround)
			return Math.abs(pos - this.position);

		return Math.min(Math.abs(this.position - pos),
				Math.min(1.0 + this.position - pos, 1.0 - this.position + pos));
	}

	@Override
	public String asString() {
		return this.position + Identifier.delimiter + this.wrapAround;
	}

	@Override
	public boolean equals(Identifier id) {
		return id instanceof RingIdentifier
				&& this.position == ((RingIdentifier) id).position
				&& this.wrapAround == ((RingIdentifier) id).wrapAround;
	}

	/**
	 * @return the position
	 */
	public double getPosition() {
		return this.position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(double position) {
		this.position = position % 1.0;
	}

	/**
	 * @return the wrapAround
	 */
	public boolean isWrapAround() {
		return this.wrapAround;
	}

	/**
	 * @param wrapAround
	 *            the wrapAround to set
	 */
	public void setWrapAround(boolean wrapAround) {
		this.wrapAround = wrapAround;
	}
}
