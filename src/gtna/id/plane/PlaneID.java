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
 * PlaneID.java
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

import gtna.id.ID;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class PlaneID implements ID, Comparable<PlaneID> {
	private double x;

	private double y;

	public PlaneID(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double distance(ID id) {
		PlaneID to = (PlaneID) id;
		double temp = Math.pow(this.x - to.getX(), 2);
		temp += Math.pow(this.y - to.getY(), 2);
		return Math.sqrt(temp);
	}

	@Override
	public boolean equals(ID id) {
		return this.x == ((PlaneID) id).getX()
				&& this.y == ((PlaneID) id).getY();
	}

	public static PlaneID rand(Random rand) {
		return new PlaneID(rand.nextDouble(), rand.nextDouble());
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * @param x
	 *            the x to set
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
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	public String toString() {
		return "PlaneID(" + this.x + ", " + this.y + ")";
	}

	@Override
	public int compareTo(PlaneID o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
