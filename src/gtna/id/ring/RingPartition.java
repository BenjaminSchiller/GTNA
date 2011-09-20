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

import gtna.id.DIdentifier;
import gtna.id.DPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author benni
 * 
 */
public class RingPartition implements DPartition {
	private RingIdentifier start;

	private RingIdentifier end;

	public RingPartition(RingIdentifier start, RingIdentifier end) {
		this.start = start;
		this.end = end;
	}

	public RingPartition(String string, RingIdentifierSpace idSpace) {
		String[] temp = string.replace("(", "").replace("]", "").split(",");
		this.start = new RingIdentifier(Double.parseDouble(temp[0]), idSpace);
		this.end = new RingIdentifier(Double.parseDouble(temp[1]), idSpace);
	}

	public RingPartition(String string) {
		this(string, null);
	}

	public String toString() {
		return "(" + this.start.getPosition() + "," + this.end.getPosition()
				+ "]";
	}

	@Override
	public Double distance(Identifier<Double> id) {
		if (this.contains(id)) {
			return 0.0;
		}
		return Math.min(this.start.distance((RingIdentifier) id),
				this.end.distance((RingIdentifier) id));
	}

	@Override
	public boolean equals(Partition<Double> partition) {
		return this.start.equals(((RingPartition) partition).getStart())
				&& this.end.equals(((RingPartition) partition).getEnd());
	}

	@Override
	public boolean contains(Identifier<Double> id) {
		if (this.start.getPosition() < ((RingIdentifier) id).getPosition()
				&& this.end.getPosition() >= ((RingIdentifier) id).getPosition()) {
			return true;
		}
		if (this.start.getPosition() > this.end.getPosition()
				&& (this.start.getPosition() < ((RingIdentifier) id).getPosition() || this.end
						.getPosition() >= ((RingIdentifier) id).getPosition())) {
			return true;
		}
		return false;
	}

	@Override
	public DIdentifier getRepresentativeID() {
		return this.end;
	}

	/**
	 * @return the start
	 */
	public RingIdentifier getStart() {
		return this.start;
	}

	/**
	 * @return the end
	 */
	public RingIdentifier getEnd() {
		return this.end;
	}

}
