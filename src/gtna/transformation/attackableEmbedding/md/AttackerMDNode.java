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
package gtna.transformation.attackableEmbedding.md;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.ring.RingIdentifier;
import gtna.transformation.attackableEmbedding.swapping.SwappingNode;

import java.util.Random;

/**
 * @author stef IQDNode with adversarial behavior implemented
 */
public abstract class AttackerMDNode extends DecisionMDNode {
	private boolean isAttacker;
	private int neighborIndex = -1;
	public static double close = 10E-8;

	/**
	 * @param index
	 * @param g
	 * @param embedding
	 */
	public AttackerMDNode(int index, Graph g, AttackerIQDMDEmbedding embedding,
			boolean isAttacker) {
		super(index, g, embedding);
		this.isAttacker = isAttacker;
	}

	@Override
	public void turn(Random rand) {
		if (this.isAttacker) {
			AttackerIQDMDEmbedding attEmbedding = (AttackerIQDMDEmbedding) this.embedding;
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.CONTRACTION) {
				// ID close to certain ID as in ask
				this.setID(this.ask(rand, this));
			}
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.DIVERGENCE) {
				// random ID
				this.setID(this.ask(rand, this));
			}
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.REJECTION) {
				// Id far form neighbors
				double[] newID = new double[this.getID().length];
				double[] temp = new double[this.knownIDs.length];
				for (int i = 0; i < newID.length; i++) {
					for (int j = 0; j < temp.length; j++) {
						temp[j] = this.knownIDs[j][i];
					}
					newID[i] = maxMiddle(temp);
				}
				this.setID(newID);
			}
			if (attEmbedding.getIdMethod() == IQDMDEmbedding.IdentifierMethod.SWAPPING) {
				// swapping behavior
				this.swappingTurn(rand);
			}
		} else {
			super.turn(rand);
		}
	}

	private void swappingTurn(Random rand) {
		AttackerIQDMDEmbedding attEmbedding = (AttackerIQDMDEmbedding) this.embedding;
		if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.CONTRACTION) {
			// System.out.println("AttackTurn ");
			// select a random neighbor
			if (this.neighborIndex == -1) {
				this.neighborIndex = rand.nextInt(this.getOutDegree());
			}
			// select ID close to neighbor + neighbors far away
			double[] id = new double[this.getID().length];
			for (int i = 0; i < id.length; i++) {
				id[i] = (this.knownIDs[this.neighborIndex][i] + rand
						.nextDouble() * close) % 1.0;
			}
			double[][] neighbors = new double[this.getOutDegree()][id.length];
			for (int i = 0; i < neighbors.length; i++) {
				for (int j = 0; j < id.length; j++) {
					neighbors[i][j] = (id[j] + 0.5 + rand.nextDouble() * close) % 1.0;
				}
			}
			// select ttl
			int ttl = rand.nextInt(TTL) + 1;
			// send swap request
			int[] out = this.getOutgoingEdges();
			((IdentifierMDNode) this.getGraph().getNode(
					out[rand.nextInt(out.length)])).swap(id, neighbors,
					ttl - 1, rand);

		}
		if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.DIVERGENCE) {
			// select ID randomly + neighbors far away
			double[] id = new double[this.getID().length];
			for (int i = 0; i < id.length; i++) {
				id[i] = rand.nextDouble();
			}
			double[][] neighbors = new double[this.getOutDegree()][id.length];
			for (int i = 0; i < neighbors.length; i++) {
				for (int j = 0; j < id.length; j++) {
					neighbors[i][j] = (id[j] + 0.5 + rand.nextDouble() * close) % 1.0;
				}
			}
			// select ttl
			int ttl = rand.nextInt(TTL) + 1;
			// send swap request
			int[] out = this.getOutgoingEdges();
			((IdentifierMDNode) this.getGraph().getNode(
					out[rand.nextInt(out.length)])).swap(id, neighbors,
					ttl - 1, rand);
		}
		if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.REJECTION) {
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
	public double[] ask(Random rand, Node node) {
		if (this.isAttacker
				&& (!((AttackerMDNode) node).isAttacker || node == this)) {
			AttackerIQDMDEmbedding attEmbedding = (AttackerIQDMDEmbedding) this.embedding;
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.CONTRACTION) {
				// return ID close to certain neighbor
				if (neighborIndex == -1) {
					neighborIndex = rand.nextInt(this.knownIDs.length);
				}
				double[] id = new double[this.getID().length];
				for (int i = 0; i < id.length; i++) {
					id[i] = (this.knownIDs[this.neighborIndex][i] + rand
							.nextDouble() * close) % 1.0;
				}
				return id;
			}
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.DIVERGENCE) {
				// random ID
				double[] id = new double[this.getID().length];
				for (int i = 0; i < id.length; i++) {
					id[i] = rand.nextDouble();
				}
				return id;
			}
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.REJECTION) {
				// fix victim by giving very good ID
				double[] id = new double[this.getID().length];
				double[] pID = ((IQDMDNode) node).getID();
				for (int i = 0; i < id.length; i++) {
					id[i] = (pID[i] - rand.nextDouble() * close) % 1.0;
				}
				return id;
			}
			return this.getID();
		} else {
			return super.ask(rand, node);
		}
	}

	@Override
	protected double[][][] swap(double[] callerID, double[][] neighborsID,
			int ttl, Random rand) {
		if (this.isAttacker) {
			double[][][] res = new double[2][][];
			AttackerIQDMDEmbedding attEmbedding = (AttackerIQDMDEmbedding) this.embedding;
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.CONTRACTION) {
				// System.out.println("Attack ");
				// return ID close to certain neighbor
				// and neighbors close to victims ID
				res[0] = new double[][] { this.ask(rand, this),
						new double[] { -1 } };
				res[1] = new double[this.knownIDs.length][this.getID().length];
				double d = 1;
				for (int i = 0; i < neighborsID.length; i++) {
					d = d
							* attEmbedding.computeDistance(callerID,
									neighborsID[i]);
				}
				// System.out.println(d);
				for (int i = 0; i < res[1].length; i++) {
					for (int j = 0; j < res[1][i].length; j++) {
						res[1][i][j] = (callerID[j] + Math.min(close, d)
								/ (double) callerID.length * rand.nextDouble()) % 1;
					}
					// System.out.println("distance caller pretend neighbor " +
					// attEmbedding.computeDistance(callerID, res[1][i]));
				}
			}
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.DIVERGENCE) {
				// random ID and neighbors close to victim's ID
				res[0] = new double[][] { this.ask(rand, this),
						new double[] { -1 } };
				res[1] = new double[this.knownIDs.length][this.getID().length];
				for (int i = 0; i < res[1].length; i++) {
					for (int j = 0; j < res[1][i].length; j++) {
						res[1][i][j] = (callerID[j] + close
								/ (double) callerID.length * rand.nextDouble()) % 1;
					}
				}

			}
			if (attEmbedding.getAttackertype() == AttackerIQDMDEmbedding.AttackerType.REJECTION) {
				// very bad ID

				// res[0] = new double[][] {this.getID(),
				// new double[]{-1} };
				res[0] = new double[2][];
				res[0][0] = new double[this.getID().length];
				for (int j = 0; j < res[0][0].length; j++) {
					double[] vals = new double[neighborsID.length];
					for (int i = 0; i < vals.length; i++) {
						vals[i] = neighborsID[i][j];
					}
					res[0][0][j] = (maxMiddle(vals) + close
							/ (double) callerID.length * rand.nextDouble()) % 1;
				}
				res[0][1] = new double[] { -1 };
				res[1] = new double[this.knownIDs.length][this.getID().length];
				for (int i = 0; i < res[1].length; i++) {
					for (int j = 0; j < res[1][i].length; j++) {
						res[1][i][j] = (callerID[j] + close * rand.nextDouble()) % 1;
					}
				}
			}
			return res;
		} else {
			return super.swap(callerID, neighborsID, ttl, rand);
		}
	}

}
