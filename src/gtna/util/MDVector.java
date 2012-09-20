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
 * MDVector.java
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
package gtna.util;

import gtna.id.md.MDIdentifier;

import java.util.Arrays;

/**
 * @author Nico
 * 
 */
public class MDVector {
	private int dimension;

	public MDVector(MDIdentifier id) {
		this.dimension = id.getCoordinates().length;
		this.coordinates = id.getCoordinates().clone();
	}

	public MDVector(int dimension, double[] coordinates) {
		this.dimension = dimension;
		this.coordinates = coordinates;
	}

	/**
	 * @param dimensions
	 * @param i
	 */
	public MDVector(int dimensions, double init) {
		this(dimensions, new double[dimensions]);
		for (int i = 0; i < dimensions; i++) {
			coordinates[i] = init;
		}
	}

	public MDVector(int dimensions) {
		this(dimensions, new double[dimensions]);
	}

	public MDVector(double[] coordinates) {
		this(coordinates.length, coordinates);
	}

	public int getDimension() {
		return this.dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public double[] getCoordinates() {
		return this.coordinates.clone();
	}

	public double getCoordinate(int i) {
		return this.coordinates[i];
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

	public void setCoordinate(int i, double coordinate) {
		this.coordinates[i] = coordinate;
	}

	private double[] coordinates;

	public MDVector divideBy(double divisor) {
		for (int i = 0; i < dimension; i++) {
			coordinates[i] = coordinates[i] / divisor;
		}
		return this;
	}

	public MDVector multiplyWith(double factor) {
		for (int i = 0; i < dimension; i++) {
			coordinates[i] = coordinates[i] * factor;
		}
		return this;
	}

	public MDVector add(MDVector additive) {
		if (additive.getDimension() != this.dimension) {
			throw new RuntimeException("Cannot add with different dimensions");
		}
		double[] additiveVector = additive.getCoordinates();
		for (int i = 0; i < dimension; i++) {
			if (Double.isNaN(additiveVector[i]))
				continue;
			coordinates[i] = coordinates[i] + additiveVector[i];
		}
		return this;
	}

	public MDVector subtract(MDVector subtractor) {
		if (subtractor.getDimension() != this.dimension) {
			throw new RuntimeException(
					"Cannot subtract with different dimensions");
		}
		double[] subtractVector = subtractor.getCoordinates();
		for (int i = 0; i < dimension; i++) {
			coordinates[i] = coordinates[i] - subtractVector[i];
		}
		return this;
	}

	public double dotProduct(MDVector v) {
		if (v.getDimension() != this.dimension) {
			throw new RuntimeException(
					"Cannot create dot product with different dimensions");
		}
		double result = 0;
		for (int i = 0; i < getDimension(); i++) {
			result += getCoordinate(i) * v.getCoordinate(i);
		}
		return result;
	}

	public double angleTo(MDVector v) {
		double cosOfAngle = dotProduct(v) / (this.getNorm() * v.getNorm());
		return Math.toDegrees(Math.acos(cosOfAngle));
	}

	public String toString() {
		return Arrays.toString(coordinates);
	}

	public double getNorm() {
		double result = 0;
		for (int i = 0; i < dimension; i++) {
			result += (coordinates[i] * coordinates[i]);
		}
		return Math.sqrt(result);
	}

	public static MDVector min(MDVector x, MDVector y) {
		if (x.getNorm() < y.getNorm())
			return x;
		else
			return y;
	}

	public MDVector clone() {
		return new MDVector(this.getDimension(), this.getCoordinates());
	}
}
