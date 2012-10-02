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
 * NodePartition.java
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
package gtna.id.node;

import java.util.Random;

import gtna.id.DoubleIdentifier;
import gtna.id.DoublePartition;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author benni
 * 
 */
public class NodePartition extends DoublePartition {

	protected NodeIdentifier id;

	/**
	 * 
	 * @param node
	 *            index of the node this partition is representing
	 */
	public NodePartition(int node) {
		this.id = new NodeIdentifier(node);
	}

	public NodePartition(String string) {
		this(Integer.parseInt(string));
	}

	@Override
	public double distance(DoubleIdentifier id) {
		return 0;
	}

	@Override
	public double distance(DoublePartition p) {
		return 0;
	}

	@Override
	public String asString() {
		return this.id.node + "";
	}

	@Override
	public boolean contains(Identifier id) {
		return this.id.node == ((NodeIdentifier) id).node;
	}

	@Override
	public Identifier getRepresentativeIdentifier() {
		return new NodeIdentifier(this.id.node);
	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		return new NodeIdentifier(this.id.node);
	}

	@Override
	public boolean equals(Partition p) {
		return this.id.node == ((NodePartition) p).id.node;
	}

}
