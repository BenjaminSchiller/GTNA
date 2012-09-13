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
 * PlaneIdentifier.java
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

import gtna.id.DIdentifier;
import gtna.id.Identifier;
import gtna.id.ring.RingIdentifier;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class PlaneIdentifier extends DIdentifier implements
		Comparable<PlaneIdentifier> {
	private double x;

	private double y;

	private PlaneIdentifierSpaceSimple idSpace;

	public PlaneIdentifier(double x, double y,
			PlaneIdentifierSpaceSimple idSpace) {
		this.x = x % idSpace.getModulusX();
		this.y = y % idSpace.getModulusY();
		this.idSpace = idSpace;
	}

	public PlaneIdentifier(String string, PlaneIdentifierSpaceSimple idSpace) {
		String[] temp = string.replace("(", "").replace(")", "").split("/");
		this.x = Double.parseDouble(temp[0]) % idSpace.getModulusX();
		this.y = Double.parseDouble(temp[1]) % idSpace.getModulusY();
		this.idSpace = idSpace;
	}

	public PlaneIdentifier(String string) {
		this(string, null);
	}

	public String toString() {
		return "(" + this.x + "/" + this.y + ")";
	}

	@Override
	public Double distance(Identifier<Double> id) {
		PlaneIdentifier to = (PlaneIdentifier) id;
		if (this.idSpace.isWrapAround()) {
			// double dx = Math.abs(this.x - to.getX())
			// % (this.idSpace.getModulusX() / 2.0);
			// double dy = Math.abs(this.y - to.getY())
			// % (this.idSpace.getModulusY() / 2.0);
			double dx = Math.min(
					Math.abs(this.x - to.getX()),
					Math.min(
							this.getIdSpace().getModulusX() + this.x
									- to.getX(), this.getIdSpace()
									.getModulusX() - this.x + to.getX()));
			double dy = Math.min(
					Math.abs(this.y - to.getY()),
					Math.min(
							this.getIdSpace().getModulusY() + this.y
									- to.getY(), this.getIdSpace()
									.getModulusY() - this.y + to.getY()));
			return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		} else {
			double dx = this.x - to.getX();
			double dy = this.y - to.getY();
			return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		}
	}

	@Override
	public boolean equals(Identifier<Double> id) {
		return this.x == ((PlaneIdentifier) id).getX()
				&& this.y == ((PlaneIdentifier) id).getY();
	}

	public static PlaneIdentifier rand(Random rand,
			PlaneIdentifierSpaceSimple idSpace) {
		return new PlaneIdentifier(rand.nextDouble() * idSpace.getModulusX(),
				rand.nextDouble() * idSpace.getModulusY(), idSpace);
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the idSpace
	 */
	public PlaneIdentifierSpaceSimple getIdSpace() {
		return this.idSpace;
	}

	@Override
	public int compareTo(PlaneIdentifier o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PlaneIdentifier)) {
			return false;
		}
		PlaneIdentifier id = (PlaneIdentifier) obj;
		return id.getX() == this.x && id.getY() == this.y;
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

}
