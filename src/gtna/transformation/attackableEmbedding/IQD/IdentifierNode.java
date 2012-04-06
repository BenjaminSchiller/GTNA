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
 * IdentifierNode.java
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
package gtna.transformation.attackableEmbedding.IQD;

import gtna.graph.Graph;

import java.util.Arrays;
import java.util.Random;

/**
 * @author stef
 *
 */
public abstract class IdentifierNode extends IQDNode {
	
	
	
	/**
	 * @param index
	 * @param g
	 * @param id
	 * @param epsilon: random offset for IDs
	 */
	public IdentifierNode(int index, Graph g, double id, IQDEmbedding embedding) {
		super(index, g, id, embedding);
	}

	

	/* (non-Javadoc)
	 * @see gtna.transformation.attackableEmbedding.IQD.IQDNode#getIdentifiers(java.util.Random)
	 */
	@Override
	public double[] getIdentifiers(Random rand) {
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.ONERANDOM){
			//return a random alternative ID
			return new double[]{this.getID(),rand.nextDouble()};
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.ONERANDOM){
			//return two random alternative IDs
			return new double[]{this.getID(),rand.nextDouble(), rand.nextDouble()};
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.RANDNEIGHBOR){
			double next = this.knownIDs[rand.nextInt(this.knownIDs.length)]+this.embedding.getEpsilon()*rand.nextDouble();
			if (next > 1){
				next--;	
			}
			return new double[]{this.getID(),next};
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.ALLNEIGHBOR){
			double[] res = new double[this.knownIDs.length+1];
			res[0] = this.getID();
			for (int i = 1; i < res.length; i++){
				double next = this.knownIDs[i]+this.embedding.getEpsilon()*rand.nextDouble();
				if (next > 1){
					next--;	
				}
				res[i] = next;
			}
			return res;
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.ALLNEIGHBORMIDDLE){
			double[] sort = this.knownIDs.clone();
			Arrays.sort(sort);
			double[] res = new double[this.knownIDs.length+1];
			res[0] = this.getID();
			for (int i = 1; i < res.length-1; i++){
				res[i] = (sort[i]+sort[i-1])/(double)2 + this.embedding.getEpsilon()*rand.nextDouble();
			}
			res[res.length-1] = (1+sort[0]+sort[sort.length-1])/2 + this.embedding.getEpsilon()*rand.nextDouble();
			while (res[res.length-1] > 1){
				res[res.length-1]--;
			}
			return res;
		}
		return null;
	}
	
	



}
