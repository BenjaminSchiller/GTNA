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
 * SwappingAttack.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.identifier.attackOld2;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.routing.node.RingNode;
import gtna.routing.node.identifier.RingID;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

public class SwappingAttack extends TransformationImpl implements
		Transformation {
//	public NodeImpl[] curNodes;
	int iterations;
	int attackers;
	int m = 50;
	double ratio = 0.05;
	Random rand;
	private boolean[] attacks;
	private HashMap<Integer, Integer> indexAttackers;
	private RingNode[] neighbors;
	int[] level;
	Vector<LinkedList<Boolean>> attempts;
	double[] pos;
	double[][] imaginePos;

	public SwappingAttack(int it, int att) {
		super("SWAPPING_ATTACK", new String[] { "ITERATIONS", "ATTACKERS" },
				new String[] { "" + it, "" + att });
		this.attackers = att;
		this.iterations = it;
	}

	public boolean applicable(Graph g) {
		return g.nodes[0] instanceof RingNode;
	}

	@Override
	public Graph transform(Graph g) {
		NodeImpl[] curNodes = g.nodes;
		attacks = new boolean[curNodes.length];
		indexAttackers = new HashMap<Integer, Integer>();
		neighbors = new RingNode[this.attackers];
		pos = new double[this.attackers];
		imaginePos = new double[this.attackers][10];
		level = new int[attackers];
		attempts = new Vector<LinkedList<Boolean>>(this.attackers);
		for (int i = 0; i < this.attackers; i++) {
			attempts.add(new LinkedList<Boolean>());
		}

		int found = 0;
		int place;
		RingNode cur, partner;
		rand = new Random();
		// get attackers and their "victim"(e.g. node whose position they
		// want to spread)
		while (found < attackers) {
			place = rand.nextInt(attacks.length);
			if (!attacks[place]) {
				attacks[place] = true;
				cur = (RingNode) curNodes[place];
				indexAttackers.put(cur.index(), found);
				neighbors[found] = (RingNode) cur.out()[rand
						.nextInt(cur.out().length)];
				while (neighbors[found].out().length == 1) {
					neighbors[found] = (RingNode) cur.out()[rand.nextInt(cur
							.out().length)];
				}
				pos[found] = neighbors[found].getID().pos;
				cur.getID().pos = neighbors[found].getID().pos
						+ rand.nextGaussian() * 0.0000001;
				;
				level[found] = 2;
				for (int i = 0; i < imaginePos[found].length; i++) {
					imaginePos[found][i] = cur.getID().pos + 0.5
							+ rand.nextGaussian() * 0.0001;
					if (imaginePos[found][i] > 1) {
						imaginePos[found][i]--;
					}
				}
				int corrupted = 0;
				int steps = 0;
				double oldId;
				while (corrupted < 10) {
					steps++;
					partner = randomWalk(neighbors[found], rand
							.nextInt(level[found] - 1) + 1);
					while (partner.equals(cur)) {
						partner = randomWalk(neighbors[found], rand
								.nextInt(level[found] - 1) + 1);
					}
					if (shouldSwap(cur, partner)) {
						if (!(neighbors[found].dist(cur.getID()) < 0.0000001)) {
							corrupted++;
						}
						cur.setID(new RingID(neighbors[found].getID().pos
								+ rand.nextDouble() * 0.0000001));
						;

					}
					if (steps > Math.pow(10, level[found]) && level[found] < 6) {
						level[found]++;
					}
				}
				found++;
			}
		}
		// System.out.println("All attackers found");

		this.continueSort(curNodes);
		return g;
	}

	public void continueSort(NodeImpl[] curNodes) {
		RingNode cur, partner;
		LinkedList<Boolean> rep;
		int[][] totalSt = new int[this.attackers][2];
		int count = 0;
		int atIndex;
		while (count < iterations * curNodes.length) {
			count++;
			cur = (RingNode) curNodes[rand.nextInt(curNodes.length)];
			if (attacks[cur.index()]) {
				atIndex = indexAttackers.get(cur.index());
				partner = randomWalk(neighbors[atIndex], rand
						.nextInt(level[atIndex] - 1) + 1);
				while (partner.equals(cur)) {
					partner = randomWalk(neighbors[atIndex], rand
							.nextInt(level[atIndex] - 1) + 1);
				}
				totalSt[atIndex][0]++;
				rep = attempts.get(atIndex);
				if (shouldSwapCor(cur, imaginePos[atIndex], partner)) {
					cur.getID().pos = pos[atIndex] + rand.nextDouble()
							* 0.0000001;
					// neighbors[atIndex].myId.location +
					// rand.nextGaussian()*0.0000001;
					if (!(cur.dist(partner.getID()) < 0.0000001)) {
						rep.add(true);
						totalSt[atIndex][1]++;
					} else {
						rep.add(false);
					}
				} else {
					rep.add(false);
				}
				boolean b;
				if (rep.size() > Math.pow(10, level[atIndex] - 1)
						&& level[atIndex] < 6) {
					rep.removeFirst();
					int succ = 0;
					for (int i = 0; i < m; i++) {
						b = rep.removeFirst();
						if (b)
							succ++;
						rep.add(b);
					}
					if ((double) succ / m < ratio) {
						level[atIndex]++;
						// System.out.println("Increased level at "
						// +totalSt[atIndex][0] +" "+ totalSt[atIndex][1]);
						rep = new LinkedList<Boolean>();
						attempts.remove(atIndex);
						attempts.add(rep);
					}
				}
			} else {
				partner = randomWalk(cur, 6);
				boolean b;
				if (attacks[partner.index()]) {
					atIndex = indexAttackers.get(partner.index());
					b = shouldSwapCor(partner, imaginePos[atIndex], cur);
					if (b) {
						partner.getID().pos = pos[atIndex]
								+ +rand.nextGaussian() * 0.0000001;
					}
				} else {
					shouldSwap(cur, partner);
				}

			}
		}
		System.out.println("Statistics for attackers");
		for (int i = 0; i < totalSt.length; i++) {
			System.out.println("Attacker " + i + " made " + totalSt[i][0]
					+ " attempts with succesful: " + totalSt[i][1]);
		}
	}

	private RingNode randomWalk(RingNode cur, int ttl) {
		RingNode next = (RingNode) cur.out()[rand.nextInt(cur.out().length)];
		if (attacks[next.index()]) {
			return next;
		}
		if (ttl <= 1) {
			return next;
		} else {
			return randomWalk(next, ttl - 1);
		}
	}

	public boolean shouldSwap(RingNode initiator, RingNode partner) {
		NodeImpl[] friends = initiator.out();
		double before = 1;
		double after = 1;
		for (int j = 0; j < friends.length; j++) {
			before = before * initiator.dist(((RingNode) friends[j]).getID());
			if (!friends[j].equals(partner)) {
				after = after * partner.dist(((RingNode) friends[j]).getID());
			} else {
				after = after * initiator.dist(partner.getID());
			}
		}
		friends = partner.out();
		for (int j = 0; j < friends.length; j++) {
			before = before * partner.dist(((RingNode) friends[j]).getID());
			if (!friends[j].equals(partner)) {
				after = after * initiator.dist(((RingNode) friends[j]).getID());
			} else {
				after = after * initiator.dist(partner.getID());
			}
		}

		// decide if a switch is performed
		RingID placeHolder;
		if (rand.nextDouble() < before / after) {
			placeHolder = initiator.getID();
			initiator.setID(partner.getID());
			partner.setID(placeHolder);
			return true;
		}
		return false;
	}

	public boolean shouldSwapCor(RingNode attacker, double[] pos,
			RingNode partner) {

		double before = 1;
		double after = 1;
		for (int j = 0; j < pos.length; j++) {
			before = before * attacker.dist(new RingID(pos[j]));
			after = after * partner.dist(new RingID(pos[j]));

		}
		NodeImpl[] friends = partner.out();
		for (int j = 0; j < friends.length; j++) {
			before = before * partner.dist(((RingNode) friends[j]).getID());
			if (!friends[j].equals(partner)) {
				after = after * attacker.dist(((RingNode) friends[j]).getID());
			} else {
				after = after * attacker.dist(partner.getID());
			}
		}

		// decide if a switch is performed
		RingID placeHolder;
		if (rand.nextDouble() < before / after) {
			placeHolder = attacker.getID();
			attacker.setID(partner.getID());
			partner.setID(placeHolder);
			return true;
		}
		return false;
	}
}
