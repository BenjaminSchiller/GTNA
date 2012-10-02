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
 * RingIdentifierSpaceSimple.java
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

import gtna.id.DoubleIdentifierSpace;
import gtna.id.Identifier;
import gtna.io.Filereader;
import gtna.io.Filewriter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class RingIdentifierSpaceSimple extends DoubleIdentifierSpace {

	protected boolean wrapAround;

	/**
	 * 
	 * @param partitions
	 * @param wrapAround
	 */
	public RingIdentifierSpaceSimple(RingPartitionSimple[] partitions,
			boolean wrapAround) {
		super(partitions);
		this.wrapAround = wrapAround;
	}

	/**
	 * 
	 */
	public RingIdentifierSpaceSimple() {
		this(null, false);
	}

	@Override
	public double getMaxDistance() {
		if (this.wrapAround)
			return 0.5;

		return 1.0;
	}

	@Override
	protected void writeParameters(Filewriter fw) {
		this.writeParameter(fw, "Wrap around", this.wrapAround);
	}

	@Override
	protected void readParameters(Filereader fr) {
		this.wrapAround = this.readBoolean(fr);
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		return new RingIdentifier(rand.nextDouble(), this.wrapAround);
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
