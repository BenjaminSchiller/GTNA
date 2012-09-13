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
import gtna.id.DIdentifier;
import gtna.id.DIdentifierSpace;
import gtna.id.Identifier;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class GreedyBacktracking extends RoutingAlgorithm {

	private int ttl;

	public GreedyBacktracking() {
		super("GREEDY_BACKTRACKING");
		this.ttl = Integer.MAX_VALUE;
	}

	public GreedyBacktracking(int ttl) {
		super("GREEDY_BACKTRACKING", new Parameter[] { new IntParameter("TTL",
				ttl) });
		this.ttl = ttl;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		return this.route(new ArrayList<Integer>(), start,
				(DIdentifier) target, rand, graph.getNodes(),
				new HashMap<Integer, Integer>());
	}

	@SuppressWarnings("unchecked")
	private Route route(ArrayList<Integer> route, int current,
			DIdentifier target, Random rand, Node[] nodes,
			HashMap<Integer, Integer> from) {
		route.add(current);
		if (this.isEndPoint(current, target)) {
			return new Route(route, true);
		}
		if (route.size() > ttl) {
			return new Route(route, false);
		}
		double currentDist = (Double) this.identifierSpace.getPartitions()[current]
				.distance(target);
		double minDist = (Double) this.identifierSpace.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = (Double) this.identifierSpace.getPartitions()[out]
					.distance(target);
			if (dist < minDist && dist < currentDist && !from.containsKey(out)) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1 && from.containsKey(current)) {
			return this.route(route, from.get(current), target, rand, nodes,
					from);
		} else if (minNode == -1) {
			return new Route(route, false);
		}
		from.put(minNode, current);
		return this.route(route, minNode, target, rand, nodes, from);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0", DIdentifierSpace.class);
	}

}
