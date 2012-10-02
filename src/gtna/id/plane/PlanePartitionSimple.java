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

import gtna.id.DoubleIdentifier;
import gtna.id.DoublePartition;
import gtna.id.Identifier;
import gtna.id.Partition;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class PlanePartitionSimple extends DoublePartition {

	protected PlaneIdentifier id;

	public PlanePartitionSimple(PlaneIdentifier position) {
		this.id = position;
	}

	public PlanePartitionSimple(String string) {
		this.id = new PlaneIdentifier(string);
	}

	@Override
	public double distance(DoubleIdentifier id) {
		return this.id.distance(id);
	}

	@Override
	public double distance(DoublePartition p) {
		return this.id.distance(((PlanePartitionSimple) p).id);
	}

	@Override
	public String asString() {
		return this.id.asString();
	}

	@Override
	public boolean contains(Identifier id) {
		return this.id.equals(id);
	}

	@Override
	public Identifier getRepresentativeIdentifier() {
		return this.id;
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		return this.id;
	}

	@Override
	public boolean equals(Partition p) {
		return this.id.equals(((PlanePartitionSimple) p).id);
	}

	/**
	 * @return the id
	 */
	public PlaneIdentifier getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(PlaneIdentifier id) {
		this.id = id;
	}

}
