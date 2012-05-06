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
 * RegionCoverageNode.java
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
package gtna.transformation.attackableEmbedding.IQD.RegionCoverage;

import gtna.graph.Graph;
import gtna.id.ring.RingIdentifierSpace.Distance;
import gtna.transformation.attackableEmbedding.IQD.AttackerNode;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding.IdentifierMethod;

import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 * 
 */
public class RegionCoverageNode extends AttackerNode {

	/**
	 * @param index
	 * @param g
	 * @param embedding
	 */
	public RegionCoverageNode(int index, Graph g,
			RegionCoverageEmbedding embedding, boolean isAttacker) {
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
	public double[] getQuality(Random rand, double[] ids) {
		double[] res = new double[ids.length];
		double log2 = Math.log(2);
		double dist;
		int r;
		int max = ((RegionCoverageEmbedding) this.embedding).getMax();
		for (int i = 0; i < res.length; i++) {
			Vector<Integer> numb = new Vector<Integer>(this.knownIDs.length);
			for (int j = 0; j < this.knownIDs.length; j++) {
				if (ids[i] != this.knownIDs[j]) {
					dist = this.embedding.computeDistance(ids[i],
							this.knownIDs[j]);
				} else {
					dist = this.embedding.computeDistance(ids[i],
							ids[(i + 1) % 2]);
				}
				r = (int) (Math.min(
						Math.ceil(-Math.log(Math.abs(dist)) / log2), max) * Math
						.signum(dist));
				if (!numb.contains(r)) {
					numb.add(r);
				}
				if (this.embedding.getDistance() == Distance.SIGNED
						&& numb.size() == 2 * (max - 1)) {
					break;
				}
				if (this.embedding.getDistance() == Distance.RING
						&& numb.size() == (max - 1)) {
					break;
				}
				if (this.embedding.getDistance() == Distance.CLOCKWISE
						&& numb.size() == max) {
					break;
				}
			}
			res[i] = numb.size();

			if (this.embedding.getIdMethod() == IdentifierMethod.SWAPPING) {
				numb = new Vector<Integer>(this.swapped.length);
				for (int j = 0; j < this.swapped.length; j++) {
					if (ids[i] != this.swapped[j]) {
						dist = this.embedding.computeDistance(ids[(i + 1) % 2],
								this.swapped[j]);
					} else {
						dist = this.embedding.computeDistance(ids[(i + 1) % 2],
								ids[i]);
					}
					r = (int) (Math.min(
							Math.ceil(-Math.log(Math.abs(dist)) / log2), max) * Math
							.signum(dist));
					if (!numb.contains(r)) {
						numb.add(r);
					}
					if (this.embedding.getDistance() == Distance.SIGNED
							&& numb.size() == 2 * (max - 1)) {
						break;
					}
					if (this.embedding.getDistance() == Distance.RING
							&& numb.size() == (max - 1)) {
						break;
					}
					if (this.embedding.getDistance() == Distance.CLOCKWISE
							&& numb.size() == max) {
						break;
					}
				}
				res[i] = res[i] + numb.size();
			}
		}
		return res;
	}

}
