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
 * RandomWalk.java
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
package gtna.routing.random;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.id.Partition;
import gtna.id.data.DataStorageList;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RandomWalk extends RoutingAlgorithm {

	private int ttl;

	private IdentifierSpace ids;

	private Partition[] p;

	private DataStorageList dsl;

	public RandomWalk(int ttl) {
		super("RANDOM_WALK", new Parameter[] { new IntParameter("TTL", ttl) });
		this.ttl = ttl;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		Identifier target = (Identifier) this.ids.randomID(rand);
		while (this.p[start].contains(target)) {
			target = (Identifier) this.ids.randomID(rand);
		}
		return this.routeToTarget(graph, start, target, rand);
	}

	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes());
	}

	private Route route(ArrayList<Integer> route, int current,
			Identifier target, Random rand, Node[] nodes) {
		route.add(current);
		if (route.size() > 1 && this.p[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > 1 && this.dsl != null
				&& this.dsl.getStorageForNode(current).containsId(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}

		if (nodes[current].getOutDegree() == 0) {
			return new RouteImpl(route, false);
		}

		int nextIndex = rand.nextInt(nodes[current].getOutDegree());
		int nextHop = nodes[current].getOutgoingEdges()[nextIndex];

		return this.route(route, nextHop, target, rand, nodes);

	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0")
				&& graph.getProperty("ID_SPACE_0") instanceof IdentifierSpace;
	}

	@Override
	public void preprocess(Graph graph) {
		this.ids = (IdentifierSpace) graph.getProperty("ID_SPACE_0");
		this.p = this.ids.getPartitions();
		if (graph.hasProperty("DATA_STORAGE_0")) {
			this.dsl = (DataStorageList) graph.getProperty("DATA_STORAGE_0");
		}
	}

}
