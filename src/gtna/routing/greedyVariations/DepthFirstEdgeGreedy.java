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
 * DepthFirstEdgeGreedy.java
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
import java.util.Random;
import java.util.Vector;

/**
 * a weighted depth first search that marks the edges
 * 
 * @author stefanie
 * 
 */
public class DepthFirstEdgeGreedy extends EdgeGreedy {

	public DepthFirstEdgeGreedy() {
		super("DEPTH_FIRST_EDGE_GREEDY");
	}

	public DepthFirstEdgeGreedy(int ttl) {
		super(ttl, "DEPTH_FIRST_EDGE_GREEDY");
	}

	@Override
	public int getNextD(int current, DIdentifier target, Random rand,
			Node[] nodes) {
		// System.out.println("Currently at " + current);
		Vector<Integer> pre = from.get(current);
		Vector<Integer> res = new Vector<Integer>();
		if (pre == null) {
			pre = new Vector<Integer>();
			// System.out.println("Null pre " + current);
		}
		Vector<Integer> minList = null;
		double minDist = this.idSpaceD.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			Vector<Integer> list = from.get(out);
			if (list == null) {
				list = new Vector<Integer>();
			}
			if (list.contains(current)) {
				res.add(out);
			}
			if (pre.contains(out)) {
				continue;
			}
			double dist = this.pD[out].distance(target);

			if (dist < minDist && !list.contains(current)) {
				minDist = dist;
				minNode = out;
				minList = list;
			}
		}

		if (minNode == -1 && pre.size() > 0) {
			for (int i = pre.size() - 1; i > -1; i--) {
				if (!res.contains(pre.get(i))) {
					return pre.get(i);
				}
				// return pre.remove(pre.size()-1);
			}
		}
		if (minList != null) {
			minList.add(current);
			if (!from.containsKey(minNode)) {
				from.put(minNode, minList);
			}
		}
		return minNode;
	}

	@Override
	public int getNextBI(int current, BIIdentifier target, Random rand,
			Node[] nodes) {

		Vector<Integer> pre = from.get(current);
		if (pre == null) {
			pre = new Vector<Integer>();
		}
		Vector<Integer> minList = null;
		BigInteger minDist = this.idSpaceBI.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			if (pre.contains(out)) {
				continue;
			}
			BigInteger dist = this.pBI[out].distance(target);
			Vector<Integer> list = from.get(out);
			if (list == null) {
				list = new Vector<Integer>();
			}
			if (dist.compareTo(minDist) == -1 && !list.contains(current)) {
				minDist = dist;
				minNode = out;
				minList = list;
			}
		}

		if (minNode == -1 && pre.size() > 0) {
			return pre.remove(pre.size() - 1);
		}
		if (minList != null) {
			minList.add(current);
			if (!from.containsKey(minNode)) {
				from.put(minNode, minList);
			}
		}
		return minNode;
	}

}
