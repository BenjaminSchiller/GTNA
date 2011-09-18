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

	private PlaneIDSpaceSimple idSpace;

	public PlaneID(double x, double y, PlaneIDSpaceSimple idSpace) {
		this.x = x % idSpace.getModulusX();
		this.y = y % idSpace.getModulusY();
		this.idSpace = idSpace;
	}

	public PlaneID(String string, PlaneIDSpaceSimple idSpace) {
		String[] temp = string.replace("(", "").replace(")", "").split("/");
		this.x = Integer.parseInt(temp[0]) % idSpace.getModulusX();
		this.y = Integer.parseInt(temp[1]) % idSpace.getModulusY();
		this.idSpace = idSpace;
	}

	public PlaneID(String string) {
		this(string, null);
	}

	public String toString() {
		return "(" + this.x + "/" + this.y + ")";
	}

	@Override
	public double distance(ID id) {
		PlaneID to = (PlaneID) id;
		if (this.idSpace.isWrapAround()) {
			double dx = Math.abs(this.x - to.getX())
					% (this.idSpace.getModulusX() / 2.0);
			double dy = Math.abs(this.y - to.getY())
					% (this.idSpace.getModulusY() / 2.0);
			return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		} else {
			double dx = this.x - to.getX();
			double dy = this.y - to.getY();
			return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		}
	}

	@Override
	public boolean equals(ID id) {
		return this.x == ((PlaneID) id).getX()
				&& this.y == ((PlaneID) id).getY();
	}

	public static PlaneID rand(Random rand, PlaneIDSpaceSimple idSpace) {
		return new PlaneID(rand.nextDouble() * idSpace.getModulusX(),
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

	@Override
	public int compareTo(PlaneID o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
