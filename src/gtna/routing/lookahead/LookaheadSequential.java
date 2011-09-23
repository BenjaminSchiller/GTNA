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
 * Lookahead.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: benni;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.routing.lookahead;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.BIPartition;
import gtna.id.DPartition;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.lookahead.LookaheadElement;
import gtna.id.lookahead.LookaheadList;
import gtna.id.lookahead.LookaheadLists;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * @author benni
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LookaheadSequential extends RoutingAlgorithmImpl implements RoutingAlgorithm {
	private int ttl;

	private IdentifierSpace idSpace;

	private Partition[] p;

	private LookaheadLists lists;

	public LookaheadSequential(int ttl) {
		super("LOOKAHEAD_SEQUENTIAL", new String[] { "TTL" }, new String[] { "" + ttl });
		this.ttl = ttl;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		Identifier target = this.idSpace.randomID(rand);
		while (this.p[start].contains(target)) {
			target = this.idSpace.randomID(rand);
		}
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes(), new HashSet<Integer>());
	}

	private Route route(ArrayList<Integer> route, int current,
			Identifier target, Random rand, Node[] nodes, HashSet<Integer> seen) {
		route.add(current);
		seen.add(current);
		if (this.idSpace.getPartitions()[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		LookaheadList list = this.lists.getList(current);

		int via = -1;

		if (list.getList().length == 0) {
			return new RouteImpl(route, false);
		}
		
		if (list.getList()[0].getPartition() instanceof DPartition) {
			double currentDist = (Double) this.p[current].distance(target);
			double minDist = (Double) this.idSpace.getMaxDistance();
			for (LookaheadElement l : list.getList()) {
				double dist = ((DPartition) l.getPartition()).distance(target);
				if (dist < minDist && dist < currentDist
						&& !seen.contains(l.getVia())) {
					minDist = dist;
					via = l.getVia();
				}
			}
		} else if (list.getList()[0].getPartition() instanceof BIPartition) {
			BigInteger currentDist = (BigInteger) this.p[current]
					.distance(target);
			BigInteger minDist = (BigInteger) this.idSpace.getMaxDistance();
			for (LookaheadElement l : list.getList()) {
				BigInteger dist = ((BIPartition) l.getPartition())
						.distance(target);
				if (dist.compareTo(minDist) == -1
						&& dist.compareTo(currentDist) == -1
						&& !seen.contains(l.getVia())) {
					minDist = dist;
					via = l.getVia();
				}
			}
		} else {
			return null;
		}

		if (via == -1) {
			return new RouteImpl(route, false);
		}
		return this.route(route, via, target, rand, nodes, seen);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("LOOKAHEAD_LIST_0")
				&& graph.getProperty("LOOKAHEAD_LIST_0") instanceof LookaheadLists;
	}

	@Override
	public void preprocess(Graph graph) {
		this.idSpace = (IdentifierSpace) graph.getProperty("ID_SPACE_0");
		this.p = (Partition[]) this.idSpace.getPartitions();
		this.lists = (LookaheadLists) graph.getProperty("LOOKAHEAD_LIST_0");
	}

}
