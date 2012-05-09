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
 * KleinbergNode.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.attackableEmbedding.md.kleinberg;

import gtna.graph.Graph;
import gtna.transformation.attackableEmbedding.md.AttackerMDNode;
import gtna.transformation.attackableEmbedding.md.IQDMDEmbedding;

import java.util.Random;

/**
 * @author stef calculate product of edge's length
 */
public class KleinbergMDNode extends AttackerMDNode {

	/**
	 * @param index
	 * @param g
	 * @param id
	 * @param embedding
	 */
	public KleinbergMDNode(int index, Graph g, KleinbergMDEmbedding embedding,
			boolean isAttacker) {
		super(index, g, embedding, isAttacker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.attackableEmbedding.IQD.IQDNode#getQuality(java.util
	 * .Random, double[])
	 */
	@Override
	public double[] getQuality(Random rand, double[][] ids) {
		double[] q = new double[ids.length];
		for (int i = 0; i < ids.length; i++) {
			q[i] = 1;
			for (int j = 0; j < this.knownIDs.length; j++) {
				if (!this.equalArrays(ids[i], this.knownIDs[j]) || !this.neighbor) {
					q[i] = q[i]
							* this.embedding.computeDistance(ids[i],
									this.knownIDs[j]);
				} else {
					q[i] = q[i]
							* this.embedding.computeDistance(ids[i],
									ids[(i + 1) % 2]);
				}
			}
			if (this.embedding.getIdMethod() == IQDMDEmbedding.IdentifierMethod.SWAPPING) {
				for (int k = 0; k < this.swapped.length; k++) {
					if (!this.equalArrays(ids[(i + 1) % 2], swapped[k]) || !this.neighbor) {
						q[i] = q[i]
								* this.embedding.computeDistance(
										ids[(i + 1) % 2], this.swapped[k]);
					} else {
						q[i] = q[i]
								* this.embedding.computeDistance(ids[i],
										ids[(i + 1) % 2]);
					}
				}
			}
			q[i] = Math.pow(q[i], ids[0].length);
			if (q[i] == 0) {
				q[i] = Double.MAX_VALUE;
			} else {
				q[i] = 1 / q[i];
			}
		}

		return q;
	}

}
