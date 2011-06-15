/*
 * ===========================================================
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
 * SwapAttackNew.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Stefanie Roos;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-13 : v1 (SR)
 */

package gtna.transformation.identifier.attackOld3;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.routing.node.RingNode;
import gtna.routing.node.identifier.RingID;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.HashMap;
import java.util.Random;

public abstract class SwapAttackNew extends TransformationImpl implements
		Transformation {

	// nr of iterations (per node)
	private int iterations;
	private int num_Attack;
	HashMap<Integer, HashMap<Integer, RingID>> last;
	RingNode partner;

	public SwapAttackNew(String name, int iterations, int attacker) {
		super("SWAPPING_SORTING", new String[] { "ITERATIONS", "NUM" },
				new String[] { "" + iterations, "" + attacker });
		this.num_Attack = attacker;
		this.iterations = iterations;
	}

	public boolean applicable(Graph g) {
		return g.nodes[0] instanceof RingNode;
	}

	public Graph transform(Graph g) {
		Random rand = new Random(System.currentTimeMillis());
		RingNode initiator;
		RingID placeHolder;

		// int steps = (int) Math
		// .floor(Math.log(g.nodes.length) / Math.log(2) / 2);
		this.determineAttacker(g.nodes, rand);
		for (int i = 0; i < iterations * g.nodes.length; i++) {
			initiator = (RingNode) g.nodes[rand.nextInt(g.nodes.length)];
			if (isAttacker(initiator)) {
				turn(initiator);
				continue;
			}

			// retrieve IDs of neighbor
			RingID[] ids = getPartnerIDs(initiator);
			double before = 1;
			RingNode neighbor;
			for (int j = 0; j < initiator.out().length; j++) {
				neighbor = (RingNode) initiator.out()[j];
				if (isAttacker(neighbor)) {
					before = before
							* initiator.dist(this.asked(neighbor, initiator,
									ids, initiator.getID()));
				} else {
					before = before * initiator.dist(neighbor.getID());
				}
			}

			for (int j = 1; j < ids.length; j++) {
				before = before * ids[0].dist(ids[j]);
			}

			double after = 1;
			for (int j = 0; j < initiator.out().length; j++) {
				neighbor = (RingNode) initiator.out()[j];
				if (isAttacker(neighbor)) {
					after = after
							* ids[0].dist(last.get(neighbor.index()).get(
									initiator.index()));
				} else {
					after = after * ids[0].dist(neighbor.getID());
				}
			}
			for (int j = 1; j < ids.length; j++) {
				after = after * initiator.dist(ids[j]);
			}

			// decide if a switch is performed
			if (rand.nextDouble() < before / after) {
				placeHolder = initiator.getID();
				initiator.setID(ids[0]);
				if (partner != null) {
					partner.setID(placeHolder);
				}
			}

		}
		return g;
	}

	public int getCount() {
		return iterations;
	}

	/**
	 * return ID of cur +IDs of neighbors of node as node BELIEVES they are
	 * 
	 * @param node
	 * @return
	 */
	public RingID[] curKnownIDs(RingNode node) {
		RingID[] ids = new RingID[node.out().length + 1];
		ids[0] = node.getID();
		for (int i = 0; i < node.out().length; i++) {
			RingNode neighbor = (RingNode) node.out()[i];
			if (isAttacker(neighbor)) {
				ids[i + 1] = last.get(neighbor.index()).get(node.index());
			} else {
				ids[i + 1] = neighbor.getID();
			}
		}
		return ids;
	}

	public abstract void determineAttacker(NodeImpl[] curNodes, Random rand);

	public abstract boolean isAttacker(RingNode node);

	/**
	 * Ids retrieved by a random walk of length 6, might be changed in case of
	 * an attacking node
	 * 
	 * @return array with first element ID of partner, remaining neighbors
	 */
	public abstract RingID[] getPartnerIDs(RingNode cur);

	/**
	 * 
	 * @param attacker
	 * @param cur
	 *            : node in turn
	 * @param min
	 * @param IDs
	 *            : ID of partner + neighbor
	 * @param oldID
	 *            : old ID of cur
	 * @return
	 */
	public abstract RingID asked(RingNode attacker, RingNode cur, RingID[] IDs,
			RingID oldID);

	/**
	 * behavior of attacker in turn
	 * 
	 * @param attacker
	 */
	public abstract void turn(RingNode attacker);
}
