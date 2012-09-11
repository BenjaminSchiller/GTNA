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
 * RestrictedDFE.java
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
package gtna.routing.greedyVariations;

import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.DIdentifier;

import java.math.BigInteger;
import java.util.Random;
import java.util.Vector;

/**
 * @author stef
 * 
 */
public class RestrictedDFE extends EdgeGreedy {
	boolean[] done;

	public RestrictedDFE() {
		super("RESTRICTED_DFE");
	}

	public RestrictedDFE(int ttl) {
		super(ttl, "RESTRICTED_DFE");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.routing.greddyStef.GreedyTemplate#getNextD(int,
	 * gtna.id.DIdentifier, java.util.Random, gtna.graph.Node[])
	 */
	@Override
	public int getNextD(int current, DIdentifier target, Random rand,
			Node[] nodes) {

		// System.out.println("Currently at " + current);
		Vector<Integer> pre = from.get(current);
		Vector<Integer> res = new Vector<Integer>();
		if (pre == null) {
			pre = new Vector<Integer>();
			from.put(current, pre);
		}

		Vector<Integer> minList = null;
		double minDist = this.idSpaceD.getMaxDistance();
		double minDec = this.idSpaceD.getMaxDistance();
		double curDist = this.pD[current].distance(target);
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = this.pD[out].distance(target);
			if (dist >= curDist && dist < minDec && !done[out]) {
				minDec = dist;
			}
			Vector<Integer> f = from.get(out);
			if (f != null && f.contains(current)) {
				res.add(out);
				continue;
			}
			if (pre.contains(out)) {
				continue;
			}
			if (dist < minDist) {
				minDist = dist;
				minNode = out;
				minList = f;
				// System.out.println("mindsit " + current);
			}

		}
		if (minDist >= curDist) {
			done[current] = true;
		}
		// System.out.println("minNode" + minNode + "at " + current);
		// if (minDist > minDec && pre.size() > 0){
		//
		// //System.out.println("reset" + current);
		// minNode = -1;
		// minList = null;
		// }

		if (minNode == -1 && pre.size() > 0) {
			done[current] = true;
			for (int i = pre.size() - 1; i > -1; i--) {
				if (!res.contains(pre.get(i))) {
					minNode = pre.get(i);
					minList = from.get(minNode);
				}
				// return pre.remove(pre.size()-1);
			}
		}
		// System.out.println("At " + current + " contact " + minNode + " " +
		// pre.size());
		if (minList != null) {
			minList.add(current);

		} else {
			minList = new Vector<Integer>();
			minList.add(current);
			if (!from.containsKey(minNode)) {
				from.put(minNode, minList);
			}
		}

		return minNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.routing.greddyStef.GreedyTemplate#getNextBI(int,
	 * gtna.id.BIIdentifier, java.util.Random, gtna.graph.Node[])
	 */
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

	@Override
	public void setSets(int nr) {
		super.setSets(nr);
		done = new boolean[nr];
	}
}