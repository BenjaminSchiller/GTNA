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
import gtna.id.DID;
import gtna.id.DIDSpace;
import gtna.id.DPartition;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class Greedy extends RoutingAlgorithmImpl implements RoutingAlgorithm {
	private DIDSpace idSpace;

	private DPartition[] p;

	private int ttl;

	public Greedy() {
		super("GREEDY", new String[] {}, new String[] {});
		this.ttl = Integer.MAX_VALUE;
	}

	public Greedy(int ttl) {
		super("GREEDY", new String[] { "TTL" }, new String[] { "" + ttl });
		this.ttl = ttl;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		DID target = (DID) this.idSpace.randomID(rand);
		while (this.p[start].contains(target)) {
			target = (DID) this.idSpace.randomID(rand);
		}
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes());
	}

	private Route route(ArrayList<Integer> route, int current, DID target,
			Random rand, Node[] nodes) {
		route.add(current);
		if (this.idSpace.getPartitions()[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		double currentDist = this.idSpace.getPartitions()[current]
				.distance(target);
		double minDist = this.idSpace.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = this.p[out].distance(target);
			if (dist < minDist && dist < currentDist) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1) {
			return new RouteImpl(route, false);
		}
		return this.route(route, minNode, target, rand, nodes);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0")
				&& graph.getProperty("ID_SPACE_0") instanceof DIDSpace;
	}

	@Override
	public void preprocess(Graph graph) {
		this.idSpace = (DIDSpace) graph.getProperty("ID_SPACE_0");
		this.p = (DPartition[]) idSpace.getPartitions();
	}

}
