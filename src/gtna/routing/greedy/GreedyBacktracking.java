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
 * Greedy.java
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
package gtna.routing.greedy;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.ID;
import gtna.id.IDSpace;
import gtna.id.Partition;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class GreedyBacktracking extends RoutingAlgorithmImpl implements
		RoutingAlgorithm {
	private IDSpace idSpace;

	private Partition[] p;

	private int ttl;

	public GreedyBacktracking() {
		super("GREEDY_BACKTRACKING", new String[] {}, new String[] {});
		this.ttl = Integer.MAX_VALUE;
	}

	public GreedyBacktracking(int ttl) {
		super("GREEDY_BACKTRACKING", new String[] { "TTL" }, new String[] { ""
				+ ttl });
		this.ttl = ttl;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		ID target = this.idSpace.randomID(rand);
		while (this.p[start].contains(target)) {
			target = this.idSpace.randomID(rand);
		}
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes(), new HashMap<Integer, Integer>());
	}

	private Route route(ArrayList<Integer> route, int current, ID target,
			Random rand, Node[] nodes, HashMap<Integer, Integer> from) {
		route.add(current);
		if (this.idSpace.getPartitions()[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > ttl) {
			return new RouteImpl(route, false);
		}
		double currentDist = this.idSpace.getPartitions()[current]
				.distance(target);
		double minDist = this.idSpace.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = this.p[out].distance(target);
			if (dist < minDist && dist < currentDist && !from.containsKey(out)) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1 && from.containsKey(current)) {
			return this.route(route, from.get(current), target, rand, nodes,
					from);
		} else if (minNode == -1) {
			return new RouteImpl(route, false);
		}
		from.put(minNode, current);
		return this.route(route, minNode, target, rand, nodes, from);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE")
				&& graph.getProperty("ID_SPACE") instanceof IDSpace;
	}

	@Override
	public void preprocess(Graph graph) {
		this.idSpace = (IDSpace) graph.getProperty("ID_SPACE");
		this.p = idSpace.getPartitions();
	}

}
