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
 * PlaneIdentifierSpaceSimple.java
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
package gtna.id.plane;

import gtna.id.DoubleIdentifierSpace;
import gtna.id.Identifier;
import gtna.id.Partition;
import gtna.io.Filereader;
import gtna.io.Filewriter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class PlaneIdentifierSpaceSimple extends DoubleIdentifierSpace {

	protected double xModulus;

	protected double yModulus;

	protected boolean wrapAround;

	/**
	 * 
	 * @param partitions
	 * @param xModulus
	 * @param yModulus
	 * @param wrapAround
	 */
	public PlaneIdentifierSpaceSimple(Partition[] partitions, double xModulus,
			double yModulus, boolean wrapAround) {
		super(partitions);
		this.xModulus = xModulus;
		this.yModulus = yModulus;
		this.wrapAround = wrapAround;
	}

	/**
	 * 
	 */
	public PlaneIdentifierSpaceSimple() {
		this(null, -1.0, -1.0, false);
	}

	@Override
	public double getMaxDistance() {
		if (this.wrapAround) {
			return Math.sqrt(this.xModulus * this.xModulus / 4 + this.yModulus
					* this.yModulus / 4);
		} else {
			return Math.sqrt(this.xModulus * this.xModulus + this.yModulus
					* this.yModulus);
		}
	}

	@Override
	protected void writeParameters(Filewriter fw) {
		this.writeParameter(fw, "X modulus", this.xModulus);
		this.writeParameter(fw, "Y modulus", this.yModulus);
		this.writeParameter(fw, "Wrap around", this.wrapAround);

	}

	@Override
	protected void readParameters(Filereader fr) {
		this.xModulus = this.readDouble(fr);
		this.yModulus = this.readDouble(fr);
		this.wrapAround = this.readBoolean(fr);
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		return new PlaneIdentifier(rand.nextDouble() * this.xModulus,
				rand.nextDouble() * this.yModulus, this.xModulus,
				this.yModulus, this.wrapAround);
	}

	/**
	 * @return the xModulus
	 */
	public double getxModulus() {
		return this.xModulus;
	}

	/**
	 * @param xModulus
	 *            the xModulus to set
	 */
	public void setxModulus(double xModulus) {
		this.xModulus = xModulus;
	}

	/**
	 * @return the yModulus
	 */
	public double getyModulus() {
		return this.yModulus;
	}

	/**
	 * @param yModulus
	 *            the yModulus to set
	 */
	public void setyModulus(double yModulus) {
		this.yModulus = yModulus;
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
