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

import gtna.id.DoubleIdentifier;
import gtna.id.Identifier;

/**
 * @author benni
 * 
 */
public class PlaneIdentifier extends DoubleIdentifier {

	protected double x;

	protected double y;

	protected double xModulus;

	protected double yModulus;

	protected boolean wrapAround;

	public PlaneIdentifier(double x, double y, double xModulus,
			double yModulus, boolean wrapAround) {
		this.x = x;
		this.y = y;
		this.xModulus = xModulus;
		this.yModulus = yModulus;
		this.wrapAround = wrapAround;
	}

	public PlaneIdentifier(String string){
		String[] temp = string.split(Identifier.delimiter);
		this.x = Double.parseDouble(temp[0]);
		this.y = Double.parseDouble(temp[1]);
		this.xModulus = Double.parseDouble(temp[2]);
		this.yModulus = Double.parseDouble(temp[3]);
		this.wrapAround= Boolean.parseBoolean(temp[4]);
	}

	@Override
	public int compareTo(DoubleIdentifier arg0) {
		if (this.x < ((PlaneIdentifier) arg0).x) {
			return -1;
		} else if (this.x > ((PlaneIdentifier) arg0).x) {
			return 1;
		} else if (this.y < ((PlaneIdentifier) arg0).y) {
			return -1;
		} else if (this.y > ((PlaneIdentifier) arg0).y) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public double distance(DoubleIdentifier id) {
		PlaneIdentifier to = (PlaneIdentifier) id;
		if (this.wrapAround) {
			double dx = Math.min(
					Math.abs(this.x - to.x),
					Math.min(this.xModulus + this.x - to.x, this.xModulus
							- this.x + to.x));
			double dy = Math.min(
					Math.abs(this.y - to.y),
					Math.min(this.yModulus + this.y - to.y, this.yModulus
							- this.y + to.y));
			return Math.sqrt(dx * dx + dy * dy);
		} else {
			double dx = this.x - to.x;
			double dy = this.y - to.y;
			return Math.sqrt(dx * dx + dy * dy);
		}
	}

	@Override
	public String asString() {
		return this.x + Identifier.delimiter + this.y + Identifier.delimiter
				+ this.xModulus + Identifier.delimiter + this.yModulus
				+ Identifier.delimiter + this.wrapAround;
	}

	@Override
	public boolean equals(Identifier id) {
		PlaneIdentifier pid = (PlaneIdentifier) id;
		return this.x == pid.x && this.y == pid.y
				&& this.xModulus == pid.xModulus
				&& this.yModulus == pid.yModulus
				&& this.wrapAround == pid.wrapAround;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the xModulus
	 */
	public double getxModulus() {
		return this.xModulus;
	}

	/**
	 * @param xModulus the xModulus to set
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
	 * @param yModulus the yModulus to set
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
	 * @param wrapAround the wrapAround to set
	 */
	public void setWrapAround(boolean wrapAround) {
		this.wrapAround = wrapAround;
	}

}
