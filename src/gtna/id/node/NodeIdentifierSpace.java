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

import java.util.Random;

import gtna.graph.Graph;
import gtna.id.DIdentifierSpace;
import gtna.id.Identifier;
import gtna.id.Partition;

/**
 * @author benni
 * 
 */
public class NodeIdentifierSpace implements DIdentifierSpace {

	protected NodePartition[] partitions;

	public NodeIdentifierSpace() {
		this.partitions = new NodePartition[0];
	}

	public NodeIdentifierSpace(NodePartition[] partitions) {
		this.partitions = partitions;
	}

	@Override
	public Partition<Double>[] getPartitions() {
		return this.partitions;
	}

	@Override
	public void setPartitions(Partition<Double>[] partitions) {
		this.partitions = (NodePartition[]) partitions;
	}

	@Override
	public Identifier<Double> randomID(Random rand) {
		int node = rand.nextInt(this.partitions.length);
		int data = rand.nextInt(NodeIdentifier.dataValueMod);
		return new NodeIdentifier(node, data);
	}

	@Override
	public Double getMaxDistance() {
		return Double.MAX_VALUE;
	}

	@Override
	public boolean write(String filename, String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void read(String filename, Graph graph) {
		// TODO Auto-generated method stub

	}

}
