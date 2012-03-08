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
 * RandomRingID.java
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
package gtna.transformation.id;

import gtna.graph.Graph;
import gtna.networks.p2p.chord.ChordIdentifier;
import gtna.networks.p2p.chord.ChordIdentifierSpace;
import gtna.networks.p2p.chord.ChordPartition;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * Assigns a randomly selected RingPartition to every node and stores it as a
 * property with key "ID_SPACE" in the given graph.
 * 
 * @author benni
 * 
 */
public class RandomChordIDSpace extends Transformation {
	private int realities;

	private int bits;

	private boolean uniform;

	public RandomChordIDSpace(int bits, boolean uniform) {
		super("RANDOM_CHORD_ID_SPACE", new Parameter[] {
				new Parameter("BITS", "" + bits),
				new Parameter("ID_SELECTION", "" + uniform) });
		this.bits = bits;
		this.uniform = uniform;
		this.realities = 1;
	}

	public RandomChordIDSpace(int bits, boolean uniform, int realities) {
		super("RANDOM_CHORD_ID_SPACE", new Parameter[] {
				new Parameter("BITS", "" + bits),
				new Parameter("REALITIES", "" + realities),
				new Parameter("ID_SELECTION", "" + uniform) });
		this.bits = bits;
		this.uniform = uniform;
		this.realities = realities;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			ChordIdentifierSpace idSpace = new ChordIdentifierSpace(this.bits);
			ChordIdentifier[] ids = new ChordIdentifier[graph.getNodes().length];
			if (this.uniform) {
				BigInteger stepSize = idSpace.getModulus().divide(
						new BigInteger("" + graph.getNodes().length));
				for (int i = 0; i < ids.length; i++) {
					ids[i] = new ChordIdentifier(idSpace,
							stepSize.multiply(new BigInteger("" + i)));
				}
			} else {
				HashSet<String> idSet = new HashSet<String>();
				for (int i = 0; i < ids.length; i++) {
					ChordIdentifier id = (ChordIdentifier) idSpace
							.randomID(rand);
					while (idSet.contains(id.toString())) {
						id = (ChordIdentifier) idSpace.randomID(rand);
					}
					ids[i] = id;
					idSet.add(id.toString());
				}
			}
			Arrays.sort(ids);

			ChordPartition[] partitions = new ChordPartition[ids.length];
			partitions[0] = new ChordPartition(ids[ids.length - 1], ids[0]);
			for (int i = 1; i < partitions.length; i++) {
				partitions[i] = new ChordPartition(ids[i - 1], ids[i]);
			}

			// Util.randomize(partitions, rand);
			idSpace.setPartitions(partitions);

			graph.addProperty(graph.getNextKey("ID_SPACE"), idSpace);
		}
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}
