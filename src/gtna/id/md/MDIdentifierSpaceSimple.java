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
 * MDIdentifierSpaceSimple.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.id.md;

import gtna.id.DoubleIdentifierSpace;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.io.Filereader;
import gtna.io.Filewriter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class MDIdentifierSpaceSimple extends DoubleIdentifierSpace {

	protected double[] modulus;

	protected boolean wrapAround;

	/**
	 * 
	 * @param partitions
	 * @param dimensions
	 */
	public MDIdentifierSpaceSimple(MDPartitionSimple[] partitions,
			double[] modulus, boolean wrapAround) {
		super(partitions);
		this.modulus = modulus;
		this.wrapAround = wrapAround;
	}

	/**
	 * 
	 */
	public MDIdentifierSpaceSimple() {
		this(null, null, false);
	}

	@Override
	public double getMaxDistance() {
		double sum = 0.0;

		if (this.wrapAround) {
			for (double mod : this.modulus) {
				sum += mod * mod / 4;
			}
		} else {
			for (double mod : this.modulus) {
				sum += mod * mod;
			}
		}
		return Math.sqrt(sum);
	}

	@Override
	protected void writeParameters(Filewriter fw) {
		StringBuffer buff = new StringBuffer();
		buff.append(this.modulus[0]);
		for (int i = 1; i < this.modulus.length; i++) {
			buff.append(IdentifierSpace.delimiter + this.modulus[i]);
		}
		this.writeParameter(fw, "Modulus", buff.toString());

		this.writeParameter(fw, "Wrap around", this.wrapAround);
	}

	@Override
	protected void readParameters(Filereader fr) {
		String[] temp = this.readString(fr).split(IdentifierSpace.delimiter);
		this.modulus = new double[temp.length];
		for (int i = 0; i < temp.length; i++) {
			this.modulus[i] = Double.parseDouble(temp[i]);
		}

		this.wrapAround = this.readBoolean(fr);
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		double[] coordinates = new double[this.modulus.length];
		for (int i = 0; i < coordinates.length; i++) {
			coordinates[i] = rand.nextDouble() * this.modulus[i];
			if (this.wrapAround) {
				coordinates[i] = coordinates[i] % this.modulus[i];
			}
		}
		return new MDIdentifier(coordinates, this.modulus, this.wrapAround);
	}

	/**
	 * @return the modulus
	 */
	public double[] getModulus() {
		return this.modulus;
	}

	/**
	 * @param modulus the modulus to set
	 */
	public void setModulus(double[] modulus) {
		this.modulus = modulus;
	}

	/**
	 * @return the wrapAround
	 */
	public boolean isWrapAround() {
		return this.wrapAround;
	}

	/**
	 * @param wrapAround the wrapAround to set
	 */
	public void setWrapAround(boolean wrapAround) {
		this.wrapAround = wrapAround;
	}

}
