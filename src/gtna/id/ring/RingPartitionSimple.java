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

import gtna.id.DIdentifier;
import gtna.id.DPartition;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author benni
 * 
 */
public class RingPartitionSimple implements DPartition {
	private RingIdentifier id;

	public RingPartitionSimple(RingIdentifier id) {
		this.id = id;
	}

	public RingPartitionSimple(String string, RingIdentifierSpace idSpace) {
		string = string.replace("(", "").replace(")", "");
		this.id = new RingIdentifier(Double.parseDouble(string), idSpace);
	}

	public RingPartitionSimple(String string) {
		this(string, null);
	}

	public String toString() {
		return "[" + this.id.getPosition() + "]";
	}

	@Override
	public Double distance(Identifier<Double> id) {
		return this.id.distance(id);
	}

	@Override
	public boolean equals(Partition<Double> partition) {
		return this.id.equals(((RingPartitionSimple) partition).getId());
	}

	@Override
	public boolean contains(Identifier<Double> id) {
		return this.id.equals((RingIdentifier) id);
	}

	@Override
	public DIdentifier getRepresentativeID() {
		return this.id;
	}

	/**
	 * @return the id
	 */
	public RingIdentifier getId() {
		return this.id;
	}

}
