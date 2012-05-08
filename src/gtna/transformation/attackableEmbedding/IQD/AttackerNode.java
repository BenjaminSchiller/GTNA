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
 * AttackerNode.java
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
import gtna.id.ring.RingIdentifier;
import gtna.transformation.attackableEmbedding.swapping.SwappingNode;

import java.util.Random;

/**
 * @author stef 
 *         IQDNode with adversarial behavior implemented three types:
 *         CONTRACTION: collapse ID space into points 
 *         DIVERGENCE: distribut random IDs
 *         REJECTION: make neighbor reject ID
 */
public abstract class AttackerNode extends DecisionNode {
	private boolean isAttacker;
	private int neighborIndex = -1;
	public static double close = 10E-10;

	/**
	 * @param index
	 * @param g
	 * @param embedding
	 */
	public AttackerNode(int index, Graph g, AttackerIQDEmbedding embedding,
			boolean isAttacker) {
		super(index, g, embedding);
		this.isAttacker = isAttacker;
	}

	@Override
	public void turn(Random rand) {
		if (this.isAttacker) {
			AttackerIQDEmbedding attEmbedding = (AttackerIQDEmbedding) this.embedding;
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.CONTRACTION) {
				// ID close to certain ID as in ask
				this.setID(this.ask(rand, this));
			}
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.DIVERGENCE) {
				// random ID
				this.setID(rand.nextDouble());
			}
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.REJECTION) {
				// Id far form neighbors
				this.setID(maxMiddle(this.knownIDs));
			}
			if (attEmbedding.getIdMethod() == IQDEmbedding.IdentifierMethod.SWAPPING) {
				// swapping behavior
				this.swappingTurn(rand);
			}
		} else {
			super.turn(rand);
		}
	}

	private void swappingTurn(Random rand) {
		AttackerIQDEmbedding attEmbedding = (AttackerIQDEmbedding) this.embedding;
		if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.CONTRACTION) {
			// select a random neighbor
			if (this.neighborIndex == -1) {
				this.neighborIndex = rand.nextInt(this.getOutDegree());
			}
			// select ID close to neighbor + neighbors far away
			double id = (this.knownIDs[this.neighborIndex] + rand.nextDouble()
					* close) % 1.0;
			double[] neighbors = new double[this.getOutDegree()];
			for (int i = 0; i < neighbors.length; i++) {
				neighbors[i] = (id + 0.5 + rand.nextDouble() * close) % 1.0;
			}
			// select ttl
			int ttl = rand.nextInt(TTL) + 1;
			// send swap request
			int[] out = this.getOutgoingEdges();
			((IdentifierNode) this.getGraph().getNode(
					out[rand.nextInt(out.length)])).swap(id, neighbors,
					ttl - 1, rand);

		}
		if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.DIVERGENCE) {
			// select ID randomly + neighbors far away
			double id = rand.nextDouble();
			double[] neighbors = new double[this.getOutDegree()];
			for (int i = 0; i < neighbors.length; i++) {
				neighbors[i] = (id + 0.5 + rand.nextDouble() * close) % 1.0;
			}
			// select ttl
			int ttl = rand.nextInt(TTL) + 1;
			// send swap request
			int[] out = this.getOutgoingEdges();
			((IdentifierNode) this.getGraph().getNode(
					out[rand.nextInt(out.length)])).swap(id, neighbors,
					ttl - 1, rand);
		}
		if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.REJECTION) {
			// do nothing
		}
	}

	/**
	 * allows a node to lie about its ID => attacker
	 * 
	 * @param rand
	 * @param node
	 * @return
	 */
	@Override
	public double ask(Random rand, Node node) {
		if (this.isAttacker
				&& (!((AttackerNode) node).isAttacker || node == this)) {
			AttackerIQDEmbedding attEmbedding = (AttackerIQDEmbedding) this.embedding;
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.CONTRACTION) {
				// return ID close to certain neighbor
				if (neighborIndex == -1) {
					neighborIndex = rand.nextInt(this.knownIDs.length);
				}
				return (this.knownIDs[neighborIndex] - close
						* rand.nextDouble()) % 1;
			}
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.DIVERGENCE) {
				// random ID
				return rand.nextDouble();
			}
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.REJECTION) {
				// fix victim by giving very good ID
				return (((IQDNode) node).getID() - close * rand.nextDouble()) % 1;
			}
			return this.getID();
		} else {
			return super.ask(rand, node);
		}
	}

	@Override
	protected double[][] swap(double callerID, double[] neighborsID, int ttl,
			Random rand) {
		if (this.isAttacker) {
			double[][] res = new double[2][];
			AttackerIQDEmbedding attEmbedding = (AttackerIQDEmbedding) this.embedding;
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.CONTRACTION) {
				// return ID close to certain neighbor
				// and neighbors close to victims ID
				res[0] = new double[] { this.ask(rand, this), -1 };
				res[1] = new double[this.knownIDs.length];
				double d = 1;
				for (int i = 0; i < neighborsID.length; i++) {
					d = d
							* attEmbedding.computeDistance(callerID,
									neighborsID[i]);
				}
				for (int i = 0; i < res[1].length; i++) {
					res[1][i] = (callerID + Math.min(close, d)
							* rand.nextDouble()) % 1;
				}
			}
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.DIVERGENCE) {
				// random ID and neighbors close to victim's ID
				res[0] = new double[] { rand.nextDouble(), -1 };
				res[1] = new double[this.knownIDs.length];
				for (int i = 0; i < res[1].length; i++) {
					res[1][i] = (callerID + close * rand.nextDouble()) % 1;
				}
			}
			if (attEmbedding.getAttackertype() == AttackerIQDEmbedding.AttackerType.REJECTION) {
				// very bad ID
				res[0] = new double[] {
						(maxMiddle(neighborsID) + rand.nextDouble() * close) % 1.0,
						-1 };
				res[1] = new double[this.knownIDs.length];
				for (int i = 0; i < res[1].length; i++) {
					res[1][i] = (callerID + close * rand.nextDouble()) % 1;
				}
			}
			return res;
		} else {
			return super.swap(callerID, neighborsID, ttl, rand);
		}
	}

}
