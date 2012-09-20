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
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.IntParameter;
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

	private int bits;

	private boolean uniform;

	public RandomChordIDSpace(int bits, boolean uniform) {
		super("RANDOM_CHORD_ID_SPACE", new Parameter[] {
				new IntParameter("BITS", bits),
				new BooleanParameter("ID_SELECTION", uniform) });
		this.bits = bits;
		this.uniform = uniform;
	}

	public RandomChordIDSpace(int bits, boolean uniform, int realities) {
		super("RANDOM_CHORD_ID_SPACE", new Parameter[] {
				new IntParameter("BITS", bits),
				new BooleanParameter("ID_SELECTION", uniform) });
		this.bits = bits;
		this.uniform = uniform;
	}

	@Override
	public Graph transform(Graph graph) {
		Random rand = new Random();
		BigInteger modulus = BigInteger.ONE.add(BigInteger.ONE).pow(this.bits);
		ChordIdentifier[] ids = new ChordIdentifier[graph.getNodes().length];
		if (this.uniform) {
			BigInteger stepSize = modulus.divide(new BigInteger(""
					+ graph.getNodes().length));
			for (int i = 0; i < ids.length; i++) {
				ids[i] = new ChordIdentifier(stepSize.multiply(new BigInteger(
						"" + i)), this.bits);
			}
		} else {
			HashSet<String> idSet = new HashSet<String>();
			for (int i = 0; i < ids.length; i++) {
				ChordIdentifier id = new ChordIdentifier(new BigInteger(
						this.bits, rand), this.bits);
				while (idSet.contains(id.toString())) {
					id = new ChordIdentifier(new BigInteger(this.bits, rand),
							this.bits);
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

		ChordIdentifierSpace idSpace = new ChordIdentifierSpace(partitions, this.bits);
		
		graph.addProperty(graph.getNextKey("ID_SPACE"), idSpace);
		return graph;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}
