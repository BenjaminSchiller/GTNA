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
 * RandomKademliaIDSpace.java
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
import gtna.networks.p2p.kademlia.KademliaIdentifier;
import gtna.networks.p2p.kademlia.KademliaIdentifierSpace;
import gtna.networks.p2p.kademlia.KademliaPartition;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class RandomKademliaIDSpace extends TransformationImpl implements
		Transformation {
	private int realities;

	private int bits;

	private boolean uniform;

	public RandomKademliaIDSpace(int bits, boolean uniform) {
		super("RANDOM_KADEMLIA_ID_SPACE",
				new String[] { "BITS", "ID_SELECTION" }, new String[] {
						"" + bits, uniform ? "uniform" : "random" });
		this.bits = bits;
		this.uniform = uniform;
		this.realities = 1;
	}

	public RandomKademliaIDSpace(int bits, boolean uniform, int realities) {
		super("RANDOM_KADEMLIA_ID_SPACE", new String[] { "BITS",
				"ID_SELECTION", "REALITIES" }, new String[] { "" + bits,
				uniform ? "uniform" : "random", "" + realities });
		this.bits = bits;
		this.uniform = uniform;
		this.realities = realities;
	}

	@Override
	public Graph transform(Graph g) {
		Random rand = new Random();
		for (int r = 0; r < this.realities; r++) {
			KademliaIdentifierSpace idSpace = new KademliaIdentifierSpace(
					this.bits);
			KademliaIdentifier[] ids = new KademliaIdentifier[g.getNodes().length];
			if (this.uniform) {
				BigInteger stepSize = idSpace.getModulus().divide(
						new BigInteger("" + g.getNodes().length));
				for (int i = 0; i < ids.length; i++) {
					ids[i] = new KademliaIdentifier(idSpace,
							stepSize.multiply(new BigInteger("" + i)));
				}
			} else {
				HashSet<String> idSet = new HashSet<String>();
				for (int i = 0; i < ids.length; i++) {
					KademliaIdentifier id = (KademliaIdentifier) idSpace
							.randomID(rand);
					while (idSet.contains(id.toString())) {
						id = (KademliaIdentifier) idSpace.randomID(rand);
					}
					ids[i] = id;
					idSet.add(id.toString());
				}
			}
			Arrays.sort(ids);

			KademliaPartition[] partitions = new KademliaPartition[ids.length];
			for (int i = 0; i < partitions.length; i++) {
				partitions[i] = new KademliaPartition(ids[i]);
			}

			// Util.randomize(partitions, rand);
			idSpace.setPartitions(partitions);

			g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		}
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return true;
	}

}
