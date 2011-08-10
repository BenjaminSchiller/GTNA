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
 * PlanePartitionSimple.java
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
import gtna.id.Partition;

/**
 * @author benni
 * 
 */
public class PlanePartitionSimple implements Partition {
	private PlaneID id;

	public PlanePartitionSimple(PlaneID id) {
		this.id = id;
	}

	@Override
	public double distance(ID id) {
		return this.id.distance(id);
	}

	@Override
	public boolean equals(Partition partition) {
		return this.id.equals((((PlanePartitionSimple) partition).getId()));
	}

	@Override
	public boolean contains(ID id) {
		return this.id.equals(id);
	}

	public String toString() {
		return "PlanePartitionSimple(" + this.id.getX() + "/" + this.id.getY()
				+ ")";
	}

	public PlanePartitionSimple(String stringRepresentation) {
		String[] temp = stringRepresentation.replace("(", "").replace(")", "")
				.split("/");
		this.id = new PlaneID(Double.parseDouble(temp[0]),
				Double.parseDouble(temp[1]));
	}

	public String getStringRepresentation() {
		return "(" + this.id.getX() + "/" + this.id.getY() + ")";
	}

	/**
	 * @return the id
	 */
	public PlaneID getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(PlaneID id) {
		this.id = id;
	}

}
