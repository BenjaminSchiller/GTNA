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
 * SwapAttackRandom.java
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

public class SwapAttackRandom extends SwapAttackNew {

	boolean[] attacks;
	int numAtt;
	Random rand = new Random();
	private double acceptProb;

	public SwapAttackRandom(int it, int a) {
		super("SWA_Kleinberg", it, a);
		this.numAtt = a;
	}

	@Override
	public RingID asked(RingNode attacker, RingNode cur, RingID[] IDs,
			RingID oldID) {
		RingID current = cur.getID();
		cur.setID(oldID);
		double before = 1;
		RingNode neighbor;
		for (int i = 0; i < cur.out().length; i++) {
			neighbor = (RingNode) cur.out()[i];
			if (!(neighbor.equals(attacker))) {
				if (isAttacker(neighbor)) {
					before = before
							* cur.dist(last.get(neighbor.index()).get(
									cur.index()));
				} else {
					before = before * cur.dist(neighbor);
				}
			}
		}
		for (int i = 1; i < IDs.length; i++) {
			before = before * IDs[0].dist(IDs[i]);
		}

		cur.setID(IDs[0]);
		double after = 1;
		for (int i = 0; i < cur.out().length; i++) {
			neighbor = (RingNode) cur.out()[i];
			if (!(neighbor.equals(attacker))) {
				if (isAttacker(neighbor)) {
					after = after
							* cur.dist(last.get(neighbor.index()).get(
									cur.index()));
				} else {
					after = after * cur.dist(neighbor);
				}
			}
		}
		for (int i = 1; i < IDs.length; i++) {
			after = after * oldID.dist(IDs[i]);
		}
		cur.setID(current);

		double loc;
		if (after <= before) {
			// improvement => make sure ID is accepted
			loc = (IDs[0].pos + oldID.pos) / 2;
		} else {
			// acceptance
			loc = 0.5 * before / after + IDs[0].pos;
		}
		while (loc > 1) {
			loc--;
		}
		cur.setID(current);

		RingID id = new RingID(loc);
		// ensure ID is allowed in mode 2

		last.get(attacker.index()).put(cur.index(), id);
		return id;
	}

	@Override
	public void determineAttacker(NodeImpl[] curNodes, Random rand) {
		attacks = new boolean[curNodes.length];
		int count = 0;

		last = new HashMap<Integer, HashMap<Integer, RingID>>();
		while (count < numAtt) {
			int next = rand.nextInt(curNodes.length);
			if (!attacks[next]) {
				attacks[next] = true;
				RingNode a = (RingNode) curNodes[next];
				HashMap<Integer, RingID> curHash = new HashMap<Integer, RingID>();
				for (int k = 0; k < a.out().length; k++) {
					curHash.put(a.out()[k].index(), ((RingNode) a).getID());
				}
				last.put(next, curHash);
				// create array of RingIDs last given to neighbors

				count++;
			}
		}
	}

	@Override
	public RingID[] getPartnerIDs(RingNode cur) {
		Random rand = new Random();

		partner = null;
		RingNode start = cur;
		for (int i = 0; i < 6; i++) {
			cur = (RingNode) cur.out()[rand.nextInt(cur.out().length)];
			if (isAttacker(cur)) {
				RingID[] ids = this.curKnownIDs(start);
				RingID id = new RingID(rand.nextDouble());
				RingID[] newIds = new RingID[cur.out().length + 1];
				newIds[0] = id;
				for (int k = 0; k < cur.out().length; k++) {
					if (id.pos > 0.5) {
						newIds[k + 1] = new RingID(id.pos - 0.5);
					} else {
						newIds[k + 1] = new RingID(id.pos + 0.5);
					}
				}
				return newIds;
			}
		}
		partner = cur;
		return this.curKnownIDs(cur);
	}

	@Override
	public boolean isAttacker(RingNode node) {
		return attacks[node.index()];
	}

	/**
	 * random walk of length of length 6 + try to make partner take 'bad' ID
	 */
	@Override
	public void turn(RingNode attacker) {
		RingID[] ids = getPartnerIDs(attacker);
		RingID id = new RingID(rand.nextDouble());
		RingID[] newIds = new RingID[attacker.out().length + 1];
		newIds[0] = id;
		for (int k = 0; k < attacker.out().length; k++) {
			if (id.pos > 0.5) {
				newIds[k] = new RingID(id.pos - 0.5);
			} else {
				newIds[k] = new RingID(id.pos + 0.5);
			}
		}
		if (partner != null) {
			double before = Math.pow(0.5, attacker.out().length);
			double after = Math.pow(newIds[1].dist(ids[0]), ids.length - 1);
			for (int i = 1; i < ids.length; i++) {
				before = before * ids[i].dist(ids[0]);
				after = after * ids[i].dist(id);
			}
			Random rand = new Random();
			if (rand.nextDouble() < before / after) {
				partner.setID(id);
			}
		}
	}

}
