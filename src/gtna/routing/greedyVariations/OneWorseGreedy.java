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
 * RestrictedBacktrack.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stefanie;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.routing.greedyVariations;

import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.DIdentifier;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

/**
 * a backtracking algorithm allowing one decline
 * 
 * @author stefanie
 * 
 */
public class OneWorseGreedy extends GreedyTemplate {
	HashMap<Integer, Integer> from;
	boolean[] done;

	public OneWorseGreedy() {
		super("ONE_WORSE_GREEDY");
	}

	public OneWorseGreedy(int ttl) {
		super(ttl, "ONE_WORSE_GREEDY");
	}

	@Override
	public int getNextD(int current, DIdentifier target, Random rand,
			Node[] nodes) {
		// System.out.println(current);
		if (!from.containsKey(current)) {
			from.put(current, -1);
		}
		double currentDist = this.idSpaceD.getPartitions()[current]
				.distance(target);
		double minDist = this.idSpaceD.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = this.pD[out].distance(target);
			if (dist < minDist && !done[out]) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1 && from.containsKey(current)) {
			done[current] = true;
			return from.get(current);
		}
		if (minNode == -1) {
			return minNode;
		}
		if (!from.containsKey(minNode)) {
			from.put(minNode, current);
		}
		if (minDist >= currentDist) {
			done[current] = true;
		}
		return minNode;
	}

	@Override
	public int getNextBI(int current, BIIdentifier target, Random rand,
			Node[] nodes) {
		BigInteger currentDist = this.idSpaceBI.getPartitions()[current]
				.distance(target);
		BigInteger minDist = this.idSpaceBI.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			BigInteger dist = this.pBI[out].distance(target);
			if (dist.compareTo(minDist) == -1 && !done[out]) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1 && from.containsKey(current)) {
			return from.get(current);
		}
		if (minNode == -1) {
			return minNode;
		}
		if (!from.containsKey(minNode)) {
			from.put(minNode, current);
		}
		if (currentDist.compareTo(minDist) == 1) {
			done[current] = true;
		}
		return minNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.routing.greddyStef.GreedyTemplate#setSets(int)
	 */
	@Override
	public void setSets(int nr) {
		from = new HashMap<Integer, Integer>();
		done = new boolean[nr];
	}

}
