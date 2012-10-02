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
 * MDIdentifier.java
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

import gtna.id.DoubleIdentifier;
import gtna.id.Identifier;

/**
 * @author benni
 * 
 */
public class MDIdentifier extends DoubleIdentifier {

	protected double[] coordinates;

	protected double[] modulus;

	protected boolean wrapAround;

	public MDIdentifier(double[] coordinates, double[] modulus,
			boolean wrapAround) {
		this.coordinates = coordinates;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
	}

	public MDIdentifier(String string) {
		String[] temp = string.split(Identifier.delimiter);
		this.coordinates = new double[(temp.length - 1) / 2];
		for (int i = 0; i < this.coordinates.length; i++) {
			this.coordinates[i] = Double.parseDouble(temp[i]);
		}
		this.modulus = new double[(temp.length - 1) / 2];
		for (int i = 0; i < this.modulus.length; i++) {
			this.modulus[i] = Double.parseDouble(temp[i
					+ this.coordinates.length]);
		}
		this.wrapAround = Boolean.parseBoolean(temp[temp.length - 1]);
	}

	@Override
	public int compareTo(DoubleIdentifier arg0) {
		MDIdentifier id = (MDIdentifier) arg0;
		for (int i = 0; i < this.coordinates.length; i++) {
			if (this.coordinates[i] < id.coordinates[i]) {
				return -1;
			} else if (this.coordinates[i] > id.coordinates[i]) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public double distance(DoubleIdentifier id) {
		double sum = 0.0;
		MDIdentifier mdid = (MDIdentifier) id;

		if (this.wrapAround) {
			for (int i = 0; i < this.coordinates.length; i++) {
				sum += Math.pow(
						Math.min(
								Math.abs(this.coordinates[i]
										- mdid.coordinates[i]),
								Math.min(this.modulus[i] + this.coordinates[i]
										- mdid.coordinates[i], this.modulus[i]
										- this.coordinates[i]
										+ mdid.coordinates[i])), 2);
			}
		} else {
			for (int i = 0; i < this.coordinates.length; i++) {
				sum += Math.pow(this.coordinates[i] - mdid.coordinates[i], 2);
			}
		}

		return Math.sqrt(sum);
	}

	@Override
	public String asString() {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < this.coordinates.length; i++) {
			buff.append(Identifier.delimiter + this.coordinates[i]);
		}
		for (int i = 0; i < this.modulus.length; i++) {
			buff.append(Identifier.delimiter + this.modulus[i]);
		}
		buff.append(this.wrapAround);
		return buff.toString();
	}

	@Override
	public boolean equals(Identifier id) {
		MDIdentifier mdid = (MDIdentifier) id;
		for (int i = 0; i < this.coordinates.length; i++) {
			if (this.coordinates[i] != mdid.coordinates[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the coordinates
	 */
	public double[] getCoordinates() {
		return this.coordinates;
	}

	/**
	 * @param coordinates
	 *            the coordinates to set
	 */
	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * @return the modulus
	 */
	public double[] getModulus() {
		return this.modulus;
	}

	/**
	 * @param modulus
	 *            the modulus to set
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
	 * @param wrapAround
	 *            the wrapAround to set
	 */
	public void setWrapAround(boolean wrapAround) {
		this.wrapAround = wrapAround;
	}
}
