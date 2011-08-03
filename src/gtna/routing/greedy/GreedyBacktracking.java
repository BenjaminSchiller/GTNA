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

	public GreedyBacktracking() {
		super("GREEDY_BACKTRACKING", new String[] {}, new String[] {});
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		ID target = this.idSpace.randomID(rand);
		while (this.p[start].contains(target)) {
			target = this.idSpace.randomID(rand);
		}
		System.out.println("\n\n");
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes(), new HashMap<Integer, Integer>());
	}

	private Route route(ArrayList<Integer> route, int current, ID target,
			Random rand, Node[] nodes, HashMap<Integer, Integer> from) {
		System.out.println("=> " + current);
		route.add(current);
		if (this.idSpace.getPartitions()[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		double currentDist = this.idSpace.getPartitions()[current]
				.distance(target);
		double minDist = Double.MAX_VALUE;
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = this.p[out].distance(target);
			if (dist < minDist && dist < currentDist && !from.containsKey(out)) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1 && from.containsKey(current)) {
			System.out.println("BT");
			return this.route(route, from.get(current), target, rand, nodes,
					from);
		} else if (minNode == -1) {
			System.out.println("failed @ " + route.size() + " / " + from.size());
			System.out.println("    current = " + current);
			for(int i=0; i<route.size(); i++){
				System.out.print("  " + route.get(i) + "/" + from.containsKey(current));
			}
			System.out.println("");
			return new RouteImpl(route, false);
		}
		System.out.println("    " + current + " => " + minNode);
		from.put(minNode, current);
		return this.route(route, minNode, target, rand, nodes, from);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE");
	}

	@Override
	public void preprocess(Graph graph) {
		this.idSpace = (IDSpace) graph.getProperty("ID_SPACE");
		this.p = idSpace.getPartitions();
	}

}
