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
package gtna.transformation.attackableEmbedding.IQD.kleinberg;

import gtna.graph.Graph;
import gtna.transformation.attackableEmbedding.IQD.AttackerNode;
import gtna.transformation.attackableEmbedding.IQD.IQDEmbedding;

import java.util.Random;

/**
 * @author stef
 *
 */
public class KleinbergNode extends AttackerNode{

	/**
	 * @param index
	 * @param g
	 * @param id
	 * @param embedding
	 */
	public KleinbergNode(int index, Graph g,  KleinbergEmbedding embedding, boolean isAttacker) {
		super(index, g, embedding, isAttacker);
	}

	/* (non-Javadoc)
	 * @see gtna.transformation.attackableEmbedding.IQD.IQDNode#getQuality(java.util.Random, double[])
	 */
	@Override
	public double[] getQuality(Random rand, double[] ids) {
		double[] q = new double[ids.length];
		for (int i = 0; i < ids.length; i++){
			q[i] = 1;
			for (int j = 0; j < this.knownIDs.length; j++){
				if (ids[i] != this.knownIDs[j])
				q[i] = q[i]*this.embedding.computeDistance(this.knownIDs[j], ids[i]);
			}
			if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.SWAPPING){
				for (int k = 0; k < this.swapped.length; k++){
					if (ids[i] != this.swapped[k])
					q[i] = q[i]*this.embedding.computeDistance(this.swapped[k], ids[i]);
				}
			}
			q[i] = 1/q[i];
		}
		
		return q;
	}

}
