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
package gtna.transformation.attackableEmbedding.IQD;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.transformation.attackableEmbedding.AttackableEmbeddingNode;
import gtna.transformation.attackableEmbedding.lmc.LMCNode;

import java.util.Random;

/**
 * @author stef I(getIdentifier)Q(getQuality)D(getDecision)Node: abstract class
 *         for a Node with a one-dimensional ID, dividing the turn phase into 3
 *         phases
 */
public abstract class IQDNode extends AttackableEmbeddingNode {
	double id;
	protected IQDEmbedding embedding;
	int partnerID = -1;

	/**
	 * @param index
	 * @param g
	 */
	public IQDNode(int index, Graph g, IQDEmbedding embedding) {
		super(index, g);
		this.embedding = embedding;
	}

	public void updateNeighbors(Random rand) {
		int[] out = this.getOutgoingEdges();
		for (int i = 0; i < out.length; i++) {
			this.knownIDs[i] = ((IQDNode) this.getGraph().getNode(out[i])).ask(
					rand, this);
		}
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
		// phase 1: get Identifiers
		double[] ids = this.getIdentifiers(rand);
		// phase 2: compute quality
		double[] q = this.getQuality(rand, ids);
		// phase 3: decide on new Id and set it
		int newID = this.getDecision(rand, q);
		if (this.partnerID != -1 && ids[newID] != this.id) {
			((IQDNode) this.getGraph().getNode(this.partnerID)).setID(this,
					this.getID());
		}
		
		this.setID(ids[newID]);
	}

	public abstract double[] getIdentifiers(Random rand);

	public abstract double[] getQuality(Random rand, double[] ids);

	public abstract int getDecision(Random rand, double[] metrics);

	public void setID(double id) {
		this.id = id;
	}

	public void setID(Node caller, double id) {
		this.id = id;
	}

	public double getID() {
		return this.id;
	}

	/**
	 * allows a node to lie about its ID => attacker
	 * 
	 * @param rand
	 * @param node
	 * @return
	 */
	public double ask(Random rand, Node node) {
		return this.id;
	}

}
