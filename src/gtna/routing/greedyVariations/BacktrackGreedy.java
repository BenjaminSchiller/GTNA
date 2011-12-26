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
 * BacktrackGreedy.java
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
 * backtracking
 * @author stefanie
 *
 */
public class BacktrackGreedy extends  NodeGreedy{
	
	
	public BacktrackGreedy(int ttl){
		super(ttl, "BACKTRACK_GREEDY");
	}
	
	public BacktrackGreedy(){
		super("BACKTRACK_GREEDY");
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
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = this.pD[out].distance(target);
			if (dist < minDist && dist < currentDist && !from.containsKey(out)) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1 && from.containsKey(current)) {
	       return from.get(current);
		}
		from.put(minNode, current);
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
		for (int out : nodes[current].getOutgoingEdges()) {
			BigInteger dist = this.pBI[out].distance(target);
			if (dist.compareTo(minDist) == -1 && dist.compareTo(currentDist) == -1 && !from.containsKey(out)) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1 && from.containsKey(current)) {
	       return from.get(current);
		}
		from.put(minNode, current);
		return minNode;
	}

	

	
}
