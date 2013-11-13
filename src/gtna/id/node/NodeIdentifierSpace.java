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
 * NodeIdentifierSpace.java
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

import gtna.id.DoubleIdentifierSpace;
import gtna.id.Identifier;
import gtna.io.Filereader;
import gtna.io.Filewriter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class NodeIdentifierSpace extends DoubleIdentifierSpace {

	/**
	 * @param partitions
	 */
	public NodeIdentifierSpace(NodePartition[] partitions) {
		super(partitions);
	}

	public NodeIdentifierSpace() {
		this(new NodePartition[0]);
	}

	@Override
	public double getMaxDistance() {
		return 0;
	}

	@Override
	protected void writeParameters(Filewriter fw) {

	}

	@Override
	protected void readParameters(Filereader fr) {

	}

	@Override
	public Identifier getRandomIdentifier(Random rand) {
		return new NodeIdentifier(rand.nextInt(this.partitions.length));
	}

}
