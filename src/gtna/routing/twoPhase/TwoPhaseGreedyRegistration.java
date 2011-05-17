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
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * TwoPhaseGreedyRegistration.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.routing.twoPhase;

import gtna.graph.NodeImpl;
import gtna.routing.IDRouteImpl;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.node.IDNode;
import gtna.routing.node.RingNode;
import gtna.routing.node.identifier.Identifier;
import gtna.routing.node.identifier.RingID;

import java.util.Random;

public class TwoPhaseGreedyRegistration extends TwoPhaseGreedy implements
		RoutingAlgorithm {
	private int copies;

	private int ttl;

	private int duplicates;

	public TwoPhaseGreedyRegistration(int copies, int ttl, int duplicates) {
		super("TWO_PHASE_GREEDY_REGISTRATION", new String[] { "COPIES", "TTL",
				"DUPLICATES" }, new String[] { "" + copies, "" + ttl,
				"" + duplicates });
		this.copies = copies;
		this.ttl = ttl;
		this.duplicates = duplicates;
	}

	public boolean applicable(NodeImpl[] nodes) {
		return nodes[0] instanceof RingNode;
	}

	public Route randomRoute(NodeImpl[] nodes, NodeImpl src, Random rand) {
		RingNode s = (RingNode) src;
		Identifier dest = s.randomID(rand, nodes);
		Route route = this.phase1(s, s, dest, rand, new IDRouteImpl(dest));
		for (int i = 1; i <= this.duplicates; i++) {
			if (!route.success()) {
				double pos = (((RingID) dest).pos + ((double) i / (double) (this.duplicates + 1))) % 1.0;
				route = this.phase1(s, s, new RingID(pos), rand, route);
			}
		}
		return route;
	}

	public void init(NodeImpl[] nodes) {
		for (int i = 0; i < nodes.length; i++) {
			RingNode n = (RingNode) nodes[i];
			this.registerAll(n, ((RingNode) n).getID(), n.index());
		}
	}

	private void registerAll(RingNode start, RingID id, int nodeID) {
		this.register(start, id, nodeID);
		for (int i = 1; i <= this.duplicates; i++) {
			double pos = (id.pos + ((double) i / (double) (this.duplicates + 1))) % 1.0;
			this.register(start, new RingID(pos), nodeID);
		}
	}

	private void register(RingNode start, RingID id, int nodeID) {
		if (this.copies == 1) {
			this.registerPhase1(start, id, nodeID);
		} else {
			Random rand = new Random(System.currentTimeMillis());
			for (int i = 0; i < this.copies; i++) {
				RingNode start2 = this.randomWalk(start, this.ttl, rand);
				this.registerPhase1(start2, id, nodeID);
			}
		}
	}

	private RingNode randomWalk(RingNode current, int ttl, Random rand) {
		if (ttl == 0) {
			return current;
		}
		RingNode next = (RingNode) current.out()[rand
				.nextInt(current.out().length)];
		return this.randomWalk(next, ttl - 1, rand);
	}

	private void registerPhase1(RingNode current, RingID id, int nodeID) {
		NodeImpl[] out = current.out();
		NodeImpl max = null;
		int maxD = current.out().length + current.in().length;
		for (int i = 0; i < out.length; i++) {
			if (maxD < out[i].out().length + out[i].in().length) {
				max = out[i];
				maxD = out[i].out().length + out[i].in().length;
			}
		}
		if (max == null) {
			this.registerPhase2(current, id, nodeID);
		} else {
			this.registerPhase1((RingNode) max, id, nodeID);
		}
	}

	private void registerPhase2(RingNode n, RingID id, int nodeID) {
		NodeImpl[] out = n.out();
		NodeImpl min = null;
		double minD = n.dist(id);
		for (int i = 0; i < out.length; i++) {
			if (((IDNode) out[i]).dist(id) < minD) {
				min = out[i];
				minD = ((IDNode) out[i]).dist(id);
			}
		}
		if (min == null) {
			n.register(id);
		} else {
			this.registerPhase2((RingNode) min, id, nodeID);
		}
	}
}
