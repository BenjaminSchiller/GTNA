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
 * SwapAttackKleinberg.java
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

public class SwapAttackKleinberg extends SwapAttackNew {
	boolean[] attacks;
	int numAtt;
	Random rand = new Random();
	private double acceptProb;

	public SwapAttackKleinberg(int it, int a) {
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
			// improvement => try to prevent acceptance
			loc = IDs[0].pos + 0.5;
		} else {
			// ensure acceptance if possible
			loc = 0.5 * before / after + IDs[0].pos;
		}
		while (loc > 1) {
			loc--;
		}

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
				RingID id = getWorse(cur, ids);
				if (id != null) {
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
		RingID id = getWorse(attacker, ids);
		if (id != null) {
			Random rand = new Random();
			if (rand.nextDouble() <= acceptProb) {
				partner.setID(id);
			}
		}
	}

	/**
	 * attempt to supply a worse ID for cur that will be accepted if attacker
	 * pretends to have a 'bad' ID
	 * 
	 * TO DO: find a way to choose cleverly such possible ID!! currently: try
	 * ten IDs and choose
	 * 
	 * @param attacker
	 * @param cur
	 * @return
	 */
	public RingID getWorse(RingNode attacker, RingID[] cur) {
		double before = 1;
		RingNode neighbor;
		for (int i = 1; i < cur.length; i++) {
			before = before * cur[0].dist(cur[i]);
		}

		double best = 0;
		// flag if any suitable ID was found
		boolean found = false;
		double after, loc, before2, after2;
		RingID id = new RingID(rand.nextDouble()), id2, re = null;
		Random rand = new Random();
		for (int t = 0; t < 10; t++) {
			after = 1;
			id = new RingID(rand.nextDouble());

			for (int i = 1; i < cur.length; i++) {
				after = after * id.dist(cur[i]);
			}
			if (after > before) {
				if (!found && best < after) {
					best = after;
					re = id;
					acceptProb = before / after;
				}
				if (id.pos < 0.5) {
					id2 = new RingID(id.pos + 0.5);
				} else {
					id2 = new RingID(id.pos - 0.5);
				}
				before2 = before * Math.pow(0.5, attacker.out().length);
				after2 = after
						* Math.pow(id2.dist(cur[0]), attacker.out().length);
				if (before2 <= after2) {
					if (found == false) {
						found = true;
						best = after;
						re = id;
						acceptProb = 1;
					}
					if (best < after) {
						best = after;
						re = id;
					}
				}
			}
		}
		if (re != null) {
			return re;
		} else {

			return null;
		}
	}

}
