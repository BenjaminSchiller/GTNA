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
 * RingPartition.java
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

import gtna.id.ID;
import gtna.id.Partition;

/**
 * @author benni
 * 
 */
public class RingPartition implements Partition {
	private RingID start;

	private RingID end;

	public RingPartition(RingID start, RingID end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public double distance(ID id) {
		if (this.contains(id)) {
			return 0;
		}
		return Math.min(this.start.distance((RingID) id),
				this.end.distance((RingID) id));
	}

	@Override
	public boolean equals(Partition partition) {
		return this.start.equals(((RingPartition) partition).getStart())
				&& this.end.equals(((RingPartition) partition).getEnd());
	}

	@Override
	public boolean contains(ID id) {
		if (this.start.getPosition() < ((RingID) id).getPosition()
				&& this.end.getPosition() >= ((RingID) id).getPosition()) {
			return true;
		}
		if (this.start.getPosition() > this.end.getPosition()
				&& (this.start.getPosition() < ((RingID) id).getPosition() || this.end
						.getPosition() >= ((RingID) id).getPosition())) {
			return true;
		}
		return false;
	}

	@Override
	public ID getRepresentativeID() {
		return this.end;
	}

	public String toString() {
		return "RingPartition]" + this.start.getPosition() + ", "
				+ this.end.getPosition() + "]";
	}

	public RingPartition(String stringRepresentation, RingIDSpace idSpace) {
		stringRepresentation = stringRepresentation.replace("(", "").replace(
				"]", "");
		String[] temp = stringRepresentation.split(",");
		this.start = new RingID(Double.parseDouble(temp[0]), idSpace);
		this.end = new RingID(Double.parseDouble(temp[1]), idSpace);
	}

	public String getStringRepresentation() {
		return "(" + this.start.getPosition() + "," + this.end.getPosition()
				+ "]";
	}

	/**
	 * @return the start
	 */
	public RingID getStart() {
		return this.start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(RingID start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public RingID getEnd() {
		return this.end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(RingID end) {
		this.end = end;
	}

}
