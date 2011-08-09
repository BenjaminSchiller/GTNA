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
 * RingPartitionSingleID.java
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

import gtna.id.ID;
import gtna.id.Partition;

/**
 * @author benni
 * 
 */
public class RingPartitionSimple implements Partition {
	private RingID id;

	public RingPartitionSimple(RingID id) {
		this.id = id;
	}

	@Override
	public double distance(ID id) {
		return this.id.distance(id);
	}

	@Override
	public boolean equals(Partition partition) {
		return this.id.equals(((RingPartitionSimple) partition).getId());
	}

	@Override
	public boolean contains(ID id) {
		return this.id.equals((RingID) id);
	}

	public String toString() {
		return "RingParitionSimple(" + this.id.getPosition() + ")";
	}

	public RingPartitionSimple(String stringRepresentation) {
		stringRepresentation = stringRepresentation.replace("(", "").replace(
				")", "");
		this.id = new RingID(Double.parseDouble(stringRepresentation));
	}

	public String getStringRepresentation() {
		return "(" + this.id.getPosition() + ")";
	}

	/**
	 * @return the id
	 */
	public RingID getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(RingID id) {
		this.id = id;
	}

}
