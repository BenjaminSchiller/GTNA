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
 * LMCAttackContract.java
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

import gtna.graph.NodeImpl;
import gtna.routing.node.RingNode;
import gtna.routing.node.identifier.RingID;

import java.util.HashMap;
import java.util.Random;

public class LMCAttackContract extends LMCAttackNew {

	boolean[] attacks;
	int numAtt;
	Random rand = new Random();
	HashMap<Integer, Double> cmap;

	public LMCAttackContract(int maxIter, int mode, double pRand, double delta,
			int C, boolean deg1, String file, int number) {
		super("LMC_ATTACK_CONTRACT", maxIter, mode, pRand, delta, C, deg1,
				file, number);
		numAtt = number;

	}

	@Override
	public void determineAttacker(NodeImpl[] curNodes, Random rand) {
		attacks = new boolean[curNodes.length];
		int count = 0;
		cmap = new HashMap<Integer, Double>();
		last = new HashMap<Integer, HashMap<Integer, RingID>>();
		while (count < numAtt) {
			int next = rand.nextInt(curNodes.length);
			if (!attacks[next]) {
				attacks[next] = true;
				// decide on node which ID is 'distributed'
				// currently: node of largest degree
				// maybe better: node with most neighbors in common??
				int i = 0;
				NodeImpl a = curNodes[next];
				int max = 0;
				for (int j = 0; j < a.out().length; j++) {
					if (a.out()[j].out().length > max) {
						max = a.out()[j].out().length;
						i = j;
					}
				}
				cmap.put(next, ((RingNode) a.out()[i]).getID().pos);

				HashMap<Integer, RingID> curHash = new HashMap<Integer, RingID>();
				for (int k = 0; k < a.out().length; k++) {
					curHash.put(a.out()[k].index(), ((RingNode) a).getID());
				}
				last.put(next, curHash);

				count++;
			}
		}
	}

	@Override
	public boolean isAttacker(RingNode node) {
		return attacks[node.index()];
	}

	@Override
	public RingID asked(RingNode node, RingNode cur, double min, Random rand,
			RingID oldID, RingID newID) {

		RingID current = cur.getID();
		cur.setID(oldID);
		double before = 1;
		RingNode neighbor;
		for (int i = 0; i < cur.out().length; i++) {
			neighbor = (RingNode) cur.out()[i];
			if (!(neighbor.equals(node))) {
				if (isAttacker(neighbor)) {
					before = before
							* cur.dist(last.get(neighbor.index()).get(
									cur.index()));
				} else {
					before = before * cur.dist(neighbor);
				}
			}
		}

		cur.setID(newID);
		double after = 1;
		for (int i = 0; i < cur.out().length; i++) {
			neighbor = (RingNode) cur.out()[i];
			if (!(neighbor.equals(node))) {
				if (isAttacker(neighbor)) {
					after = after
							* cur.dist(last.get(neighbor.index()).get(
									cur.index()));
				} else {
					after = after * cur.dist(neighbor);
				}
			}
		}
		cur.setID(current);

		// determine which ID is closer to desired ID
		RingID target = new RingID(cmap.get(node.index()));

		double distOld = target.dist(oldID);
		double distNew = target.dist(newID);

		double loc;
		if (distOld < distNew) {
			// new should be rejected
			loc = newID.pos + 0.5;
		} else {
			// accept new ID
			if (before <= after) {
				loc = 0.5 * before / after + newID.pos;
			} else {
				loc = (newID.pos + oldID.pos) / 2;
			}
			while (loc > 1) {
				loc--;
			}
		}

		RingID id = new RingID(loc);
		// ensure ID is allowed in mode 2
		if (this.modus == 2 && id.dist(newID) < min && distOld >= distNew) {
			loc = newID.pos + min;
			while (loc > 1) {
				loc--;
			}
			id = new RingID(loc);
		}

		last.get(node.index()).put(cur.index(), id);
		return id;
	}

}
