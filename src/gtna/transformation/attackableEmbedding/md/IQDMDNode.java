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
 * IQDNode.java
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
import gtna.graph.Node;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.lmc.LMCNode;
import gtna.transformation.attackableEmbedding.md.AttackerIQDMDEmbedding.AttackerType;

import java.util.HashMap;
import java.util.Random;

/**
 * @author stef I(getIdentifier)Q(getQuality)D(getDecision)Node: abstract class
 *         for a Node with a one-dimensional ID, dividing the turn phase into 3
 *         phases
 */
public abstract class IQDMDNode extends AttackableEmbeddingNode {
	double[] id;
	protected IQDMDEmbedding embedding;
	int partnerID = -1;
	protected double[][] knownIDs;

	/**
	 * @param index
	 * @param g
	 */
	public IQDMDNode(int index, Graph g, IQDMDEmbedding embedding) {
		super(index, g);
		this.embedding = embedding;
	}

	public void updateNeighbors(Random rand) {
		int[] out = this.getOutgoingEdges();
		for (int i = 0; i < out.length; i++) {
			this.knownIDs[i] = ((IQDMDNode) this.getGraph().getNode(out[i]))
					.ask(rand, this);
		}
	}

	public void initKnownIDs() {
		// this.position = new HashMap<Integer, Integer>(this.getOutDegree());
		this.knownIDs = new double[this.getOutDegree()][this.embedding.dimension];
		// for (int i = 0; i < this.getOutDegree(); i++) {
		// this.position.put(this.getOutgoingEdges()[i], i);
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gtna.transformation.attackableEmbedding.AttackableEmbeddingNode#turn(
	 * java.util.Random)
	 */
	@Override
	public void turn(Random rand) {
		double[][] ids = this.getIdentifiers(rand);
		double[] q = this.getQuality(rand, ids);
		int newID = this.getDecision(rand, q);
		//if (((AttackerIQDMDEmbedding)this.embedding).getAttackertype() != AttackerType.NONE)
		  // System.out.println(q[0] + " " + q[1]);
		if (this.partnerID != -1 && ids[newID] != this.id) {
			((IQDMDNode) this.getGraph().getNode(this.partnerID)).setID(this,
					this.getID());
		}
//		if (newID == 0 && this.partnerID == -1){
//			System.out.println("Accept");
//		}
		this.setID(ids[newID]);
	}

	public abstract double[][] getIdentifiers(Random rand);

	public abstract double[] getQuality(Random rand, double[][] ids);

	public abstract int getDecision(Random rand, double[] metrics);

	public void setID(double[] id) {
		this.id = id;
	}

	public void setID(Node caller, double[] id) {
		this.id = id;
	}

	public double[] getID() {
		return this.id;
	}

	/**
	 * allows a node to lie about its ID => attacker
	 * 
	 * @param rand
	 * @param node
	 * @return
	 */
	public double[] ask(Random rand, Node node) {
		return this.id;
	}

	protected boolean equalArrays(double[] a1, double[] a2) {
		if (a1.length != a2.length) {
			return false;
		}
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] != a2[i]) {
				return false;
			}
		}
		return true;
	}

}
