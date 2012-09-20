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
 * @author stef choice of identifiers
 * 
 */
public abstract class IdentifierNode extends IQDNode {

	public static int TTL = 6;
	protected double[] swapped;

	/**
	 * @param index
	 * @param g
	 * @param identifier
	 * @param epsilon
	 *            : random offset for IDs
	 */
	public IdentifierNode(int index, Graph g, IQDEmbedding embedding) {
		super(index, g, embedding);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.attackableEmbedding.IQD.IQDNode#getIdentifiers(java
	 * .util.Random)
	 */
	@Override
	public double[] getIdentifiers(Random rand) {
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.ONERANDOM) {
			// return a random alternative ID
			return new double[] { rand.nextDouble(), this.getID() };
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.TWORANDOM) {
			// return two random alternative IDs
			return new double[] { rand.nextDouble(), rand.nextDouble(),
					this.getID() };
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.SWAPPING) {
			double[][] ids = this.swap(this.getID(), this.knownIDs, TTL, rand);
			this.partnerID = (int) ids[0][1];
			this.swapped = ids[1];
			return new double[] { ids[0][0], this.getID() };
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.RANDNEIGHBOR) {
			// return an ID close to one neighbor
			double next = this.knownIDs[rand.nextInt(this.knownIDs.length)]
					- this.embedding.getEpsilon() * rand.nextDouble();
			if (next < 0) {
				next++;
			}
			if (this.embedding.isCheckold()) {
				boolean found = false;
				for (int i = 0; i < knownIDs.length; i++) {
					if (Math.abs(this.embedding.computeDistance(knownIDs[i],
							this.id)) < this.embedding.getEpsilon()) {
						found = true;
						break;
					}
				}
				if (!found) {
					return new double[] { next };
				}
			}
			return new double[] { next, this.getID() };
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.RANDNEIGHBORMIDDLE) {
			// return an ID close to one neighbor
			double[] sort = this.knownIDs.clone();
			Arrays.sort(sort);
			int chosen = rand.nextInt(this.knownIDs.length);
			double next = (sort[chosen] + sort[(chosen + 1) % sort.length])
					/ (double) 2 - this.embedding.getEpsilon()
					* rand.nextDouble();
			if (next < 0) {
				next++;
			}
			if (this.embedding.isCheckold()) {
				boolean found = false;
				for (int i = 0; i < knownIDs.length; i++) {
					if (Math.abs(this.embedding.computeDistance(
							(sort[i] + sort[(i + 1) % sort.length])
									/ (double) 2, this.id)) < this.embedding
							.getEpsilon()) {
						found = true;
						break;
					}
				}
				if (!found) {
					return new double[] { next };
				}
			}
			return new double[] { next, this.getID() };
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.ALLNEIGHBOR) {
			// return IDs close to all neighbors
			boolean found = true;
			if (this.embedding.isCheckold()) {
				found = false;
				for (int i = 0; i < knownIDs.length; i++) {
					if (Math.abs(this.embedding.computeDistance(knownIDs[i],
							this.id)) < this.embedding.getEpsilon()) {
						found = true;
						break;
					}
				}
			}
			double[] res;
			if (found) {
				res = new double[this.knownIDs.length + 1];
				res[res.length - 1] = this.getID();
			} else {
				res = new double[this.knownIDs.length];
			}
			for (int i = 0; i < this.knownIDs.length; i++) {
				double next = this.knownIDs[i] - this.embedding.getEpsilon()
						* rand.nextDouble();
				if (next < 0) {
					next++;
				}
				res[i] = next;
			}
			return res;
		}
		if (this.embedding.getIdMethod() == IQDEmbedding.IdentifierMethod.ALLNEIGHBORMIDDLE) {
			// return IDs in the middle of all neighbor-pairs
			double[] sort = this.knownIDs.clone();
			Arrays.sort(sort);
			boolean found = true;
			if (this.embedding.isCheckold()) {
				found = false;
				for (int i = 0; i < knownIDs.length; i++) {
					if (Math.abs(this.embedding.computeDistance(
							(sort[i] + sort[(i + 1) % sort.length])
									/ (double) 2, this.id)) < this.embedding
							.getEpsilon()) {
						found = true;
						break;
					}
				}
			}
			double[] res;
			if (found) {
				res = new double[this.knownIDs.length + 1];
				res[res.length - 1] = this.getID();
			} else {
				res = new double[this.knownIDs.length];
			}
			for (int i = 1; i < this.knownIDs.length; i++) {
				res[i] = (sort[i] + sort[i - 1]) / (double) 2
						- this.embedding.getEpsilon() * rand.nextDouble();
			}
			res[this.knownIDs.length - 1] = (1 + sort[0] + sort[sort.length - 1])
					/ 2 - this.embedding.getEpsilon() * rand.nextDouble();
			while (res[res.length - 1] > 1) {
				res[res.length - 1]--;
			}
			return res;
		}
		return null;
	}

	protected double[][] swap(double callerID, double[] neighborsID, int ttl,
			Random rand) {
		if (ttl <= 1) {
			double[][] res = new double[2][];
			// System.out.println("Index when found " + this.getIndex());
			// System.out.println("ID when found " + this.getID());
			res[0] = new double[] { this.getID(), this.getIndex() };
			res[1] = this.knownIDs;
			// String line = "";
			// for (int k = 0; k < this.knownIDs.length; k++){
			// line = line + " " + this.knownIDs[k];
			// }
			// System.out.println("Neighbors when found " + line);
			return res;
		} else {
			int[] out = this.getOutgoingEdges();
			return ((IdentifierNode) this.getGraph().getNode(
					out[rand.nextInt(out.length)])).swap(callerID, neighborsID,
					ttl - 1, rand);
		}
	}

}
