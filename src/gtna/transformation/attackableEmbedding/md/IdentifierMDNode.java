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
package gtna.transformation.attackableEmbedding.md;

import gtna.graph.Graph;

import java.util.Arrays;
import java.util.Random;

/**
 * @author stef
 * 
 */
public abstract class IdentifierMDNode extends IQDMDNode {

	public static int TTL = 6;
	protected double[][] swapped;
	protected boolean neighbor = false;

	/**
	 * @param index
	 * @param g
	 * @param identifier
	 * @param epsilon
	 *            : random offset for IDs
	 */
	public IdentifierMDNode(int index, Graph g, IQDMDEmbedding embedding) {
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
	public double[][] getIdentifiers(Random rand) {
		if (this.embedding.getIdMethod() == IQDMDEmbedding.IdentifierMethod.ONERANDOM) {
			// return a random alternative ID
			double[] nID = new double[this.getID().length];
			for (int i = 0; i < nID.length; i++) {
				nID[i] = rand.nextDouble();
			}
			return new double[][] { nID, this.getID() };
		}
		if (this.embedding.getIdMethod() == IQDMDEmbedding.IdentifierMethod.TWORANDOM) {
			// return two random alternative IDs
			double[] nID = new double[this.getID().length];
			for (int i = 0; i < nID.length; i++) {
				nID[i] = rand.nextDouble();
			}
			double[] nID2 = new double[this.getID().length];
			for (int i = 0; i < nID.length; i++) {
				nID2[i] = rand.nextDouble();
			}
			return new double[][] { nID, nID2, this.getID() };
		}
		if (this.embedding.getIdMethod() == IQDMDEmbedding.IdentifierMethod.SWAPPING) {
			double[][][] ids = this
					.swap(this.getID(), this.knownIDs, TTL, rand);
			this.partnerID = (int) ids[0][1][0];
			this.swapped = ids[1];
			if (this.hasNeighbor(this.partnerID)){
				this.neighbor = true;
			} else {
				this.neighbor = false;
			}
			return new double[][] { ids[0][0], this.getID() };
		}
		if (this.embedding.getIdMethod() == IQDMDEmbedding.IdentifierMethod.RANDNEIGHBOR) {
			// return an ID close to one neighbor
			int index = rand.nextInt(this.knownIDs.length);
			double[] nextID = new double[this.getID().length];
			for (int i = 0; i < nextID.length; i++) {
				nextID[i] = this.knownIDs[index][i]
						- this.embedding.getEpsilon() / nextID.length
						* rand.nextDouble();
				if (nextID[i] < 0) {
					nextID[i]++;
				}
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
					return new double[][] { nextID };
				}
			}
			return new double[][] { nextID, this.getID() };
		}

		if (this.embedding.getIdMethod() == IQDMDEmbedding.IdentifierMethod.ALLNEIGHBOR) {
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
			double[][] res;
			if (found) {
				res = new double[this.knownIDs.length + 1][this.getID().length];
				res[res.length - 1] = this.getID();
			} else {
				res = new double[this.knownIDs.length][this.getID().length];
			}
			for (int i = 0; i < this.knownIDs.length; i++) {
				double[] nextID = new double[res[0].length];
				for (int j = 0; j < nextID.length; j++) {
					nextID[j] = this.knownIDs[i][j]
							- this.embedding.getEpsilon() / nextID.length
							* rand.nextDouble();
					if (nextID[j] < 0) {
						nextID[j]++;
					}
				}
				res[i] = nextID;
			}
			return res;
		}

		return null;
	}

	protected double[][][] swap(double[] callerID, double[][] neighborsID,
			int ttl, Random rand) {
		if (ttl <= 1) {
			double[][][] res = new double[2][][];
			// System.out.println("Index when found " + this.getIndex());
			// System.out.println("ID when found " + this.getID());
			res[0] = new double[][] { this.getID(),
					new double[] { this.getIndex() } };
			res[1] = this.knownIDs;
			// String line = "";
			// for (int k = 0; k < this.knownIDs.length; k++){
			// line = line + " " + this.knownIDs[k];
			// }
			// System.out.println("Neighbors when found " + line);
			return res;
		} else {
			int[] out = this.getOutgoingEdges();
			return ((IdentifierMDNode) this.getGraph().getNode(
					out[rand.nextInt(out.length)])).swap(callerID, neighborsID,
					ttl - 1, rand);
		}
	}

}
