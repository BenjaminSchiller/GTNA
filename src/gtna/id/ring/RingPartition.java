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

import gtna.id.DoubleIdentifier;
import gtna.id.DoublePartition;
import gtna.id.Identifier;
import gtna.id.Partition;

import java.util.Random;

/**
 * Implements a partition in the wrapping ID space [0,1). A partition is
 * represented as an interval (start, end] with start beeing excluded from the
 * set of contained identifiers.
 * 
 * @author benni
 * 
 */
public class RingPartition extends DoublePartition {
	protected RingIdentifier start;

	protected RingIdentifier end;

	public RingPartition(RingIdentifier start, RingIdentifier end) {
		this.start = start;
		this.end = end;
	}

	public RingPartition(String string) {
		String[] temp = string.split(Partition.delimiter);
		boolean wrapAround = Boolean.parseBoolean(temp[2]);
		this.start = new RingIdentifier(Double.parseDouble(temp[0]), wrapAround);
		this.end = new RingIdentifier(Double.parseDouble(temp[1]), wrapAround);
	}

	public String toString() {
		return "R (" + this.start.position + ", " + this.end.position
				+ "]";
	}

	@Override
	public double distance(DoubleIdentifier id) {
		if (this.contains(id)) {
			return 0;
		}
		return Math.min(this.start.distance(id), this.end.distance(id));
	}

	@Override
	public double distance(DoublePartition p) {
		return this.distance((DoubleIdentifier) p.getRepresentativeIdentifier());
	}

	@Override
	public String asString() {
		return this.start.position + Partition.delimiter
				+ this.end.position + Partition.delimiter
				+ this.start.wrapAround;
	}

	@Override
	public boolean contains(Identifier id) {
		double pos = ((RingIdentifier) id).position;
		if (this.isWrapping()) {
			return this.start.position < pos
					&& pos <= this.end.position;
		}
		return this.start.position < pos || pos <= this.end.position;
	}

	@Override
	public Identifier getRepresentativeIdentifier() {
		return this.end;
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		if (this.start.position == this.end.position) {
			return new RingIdentifier(this.start.position,
					this.start.wrapAround);
		}
		double r = rand.nextDouble();
		while (r == 0.0) {
			r = rand.nextDouble();
		}
		return new RingIdentifier(
				(this.start.position + this.getIntervalWidth() * r) % 1.0,
				this.start.wrapAround);
	}

	@Override
	public boolean equals(Partition p) {
		return this.start.equals(((RingPartition) p).start)
				&& this.end.equals(((RingPartition) p).end);
	}

	/**
	 * 
	 * @return width of the interval (start, end], i.e. end - start
	 */
	public double getIntervalWidth() {
		if (this.isWrapping()) {
			return this.end.position - this.start.position;
		}
		return 1 + this.end.position - this.start.position;
	}

	/**
	 * 
	 * @return true if this partition wraps around 0.0, i.e., end <= start
	 */
	public boolean isWrapping() {
		return this.end.position <= this.start.position;
	}

	/**
	 * @return the start
	 */
	public RingIdentifier getStart() {
		return this.start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(RingIdentifier start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public RingIdentifier getEnd() {
		return this.end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(RingIdentifier end) {
		this.end = end;
	}

}
