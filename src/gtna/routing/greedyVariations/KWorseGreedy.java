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
import java.util.Vector;

/**
 * a backtracking algorithm allowing one decline
 * @author stefanie
 *
 */
public class KWorseGreedy extends GreedyTemplate{
	HashMap<Integer, Integer> from;
	boolean[] done;
	int k;
	HashMap<Integer, Vector<Integer>> to;
	
	public KWorseGreedy(int k){
		super("KWORSE_GREEDY", new String[]{"K"}, new String[]{""+k});
		this.k = k;
	}
	
	public KWorseGreedy(int k, int ttl){
		super(ttl, "KWORSE_GREEDY", new String[]{"K"}, new String[]{""+k});
		this.k = k;
	}

	/* (non-Javadoc)
	 * @see gtna.routing.greddyStef.GreedyTemplate#getNextD(int, gtna.id.DIdentifier, java.util.Random, gtna.graph.Node[])
	 */
	@Override
	public int getNextD(int current, DIdentifier target, Random rand,
			Node[] nodes) {
		double currentDist = this.idSpaceD.getPartitions()[current]
				.distance(target);
		double minDist = this.idSpaceD.getMaxDistance();
		int minNode = -1;
		Vector<Integer> succ = to.get(current);
		if (succ == null){
			succ = new Vector<Integer>();
			to.put(current, succ);
		}
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = this.pD[out].distance(target);
			if (dist < minDist &&  !done[out]) {
				minDist = dist;
				minNode = out;
			}
		}
		if (succ.size() > this.k){
			minNode = -1;
		}
		if (minNode == -1 && from.containsKey(current)) {
	       return from.get(current);
		}
		if (minNode == -1){
			return minNode;
		}
		if (!from.containsKey(minNode)){
			from.put(minNode, current);
		}
		if (currentDist <= minDist){
			succ.add(minNode);
		}
		if (succ.size() == this.k){
			done[current] = true;
		}
		return minNode;
	}

	/* (non-Javadoc)
	 * @see gtna.routing.greddyStef.GreedyTemplate#getNextBI(int, gtna.id.BIIdentifier, java.util.Random, gtna.graph.Node[])
	 */
	@Override
	public int getNextBI(int current, BIIdentifier target, Random rand,
			Node[] nodes) {
		BigInteger currentDist = this.idSpaceBI.getPartitions()[current]
				.distance(target);
		BigInteger minDist = this.idSpaceBI.getMaxDistance();
		int minNode = -1;
		Vector<Integer> succ = to.get(current);
		if (succ == null){
			succ = new Vector<Integer>();
			to.put(current, succ);
		}
		for (int out : nodes[current].getOutgoingEdges()) {
			BigInteger dist = this.pBI[out].distance(target);
			if (dist.compareTo(minDist)==-1 &&  !done[out] && !succ.contains(out)) {
				minDist = dist;
				minNode = out;
			}
		}
		if (succ.size() > this.k){
			minNode = -1;
		}
		if (minNode == -1 && from.containsKey(current)) {
	       return from.get(current);
		}
		if (minNode == -1){
			return minNode;
		}
		if (!from.containsKey(minNode)){
			from.put(minNode, current);
		}
		if (currentDist.compareTo(minDist) == 1){
			succ.add(minNode);
		}
		if (succ.size() == this.k){
			done[current] = true;
		}
		
		return minNode;
	}

	/* (non-Javadoc)
	 * @see gtna.routing.greddyStef.GreedyTemplate#setSets(int)
	 */
	@Override
	public void setSets(int nr) {
		from = new HashMap<Integer,Integer>();
		to = new HashMap<Integer,Vector<Integer>>();
		done = new boolean[nr];
	}
	
	

}
