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
 * LookaheadObfuscated.java
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
import gtna.id.ring.RingID;
import gtna.id.ring.RingIDSpaceSimple;
import gtna.id.ring.RingPartitionSimple;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class LookaheadObfuscated extends RoutingAlgorithmImpl implements
		RoutingAlgorithm {
	private RingIDSpaceSimple idSpace;

	private RingPartitionSimple[] p;

	private int ttl;

	private double maxRelativeDistance;

	private RingID[][] obfuscatedIDs;

	public LookaheadObfuscated(double maxRelativeDistance) {
		super("LOOKAHEAD_OBFUSCATED", new String[] { "MAX_RELATIVE_DISTANCE" },
				new String[] { "" + maxRelativeDistance });
		this.maxRelativeDistance = maxRelativeDistance;
		this.ttl = Integer.MAX_VALUE;
	}

	public LookaheadObfuscated(double maxRelativeDistance, int ttl) {
		super("LOOKAHEAD_OBFUSCATED", new String[] { "MAX_RELATIVE_DISTANCE",
				"TTL" }, new String[] { "" + maxRelativeDistance, "" + ttl });
		this.maxRelativeDistance = maxRelativeDistance;
		this.ttl = ttl;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		RingID target = this.idSpace.randomID(rand);
		while (this.p[start].contains(target)) {
			target = this.idSpace.randomID(rand);
		}
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes(), new HashSet<Integer>());
	}

	private Route route(ArrayList<Integer> route, int current, RingID target,
			Random rand, Node[] nodes, HashSet<Integer> seen) {
		route.add(current);
		seen.add(current);
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
			} else {
				for (RingID obfuscated : this.obfuscatedIDs[out]) {
					dist = obfuscated.distance(target);
					if (dist < minDist && dist < currentDist
							&& !seen.contains(out)) {
						minDist = dist;
						minNode = out;
					}
				}
			}
		}
		if (minNode == -1) {
			return new RouteImpl(route, false);
		}
		return this.route(route, minNode, target, rand, nodes, seen);
	}

	private RingID closeID(RingID original, double maxDistance, Random rand) {
		double sign = rand.nextBoolean() ? 1.0 : -1.0;
		double id = original.getPosition() + sign * rand.nextDouble()
				* maxDistance;
		return new RingID(id, original.getIdSpace());
	}

	@Override
	public boolean applicable(Graph graph) {
		// TODO add support for additional IDSpace implementations
		return graph.hasProperty("ID_SPACE_0")
				&& graph.getProperty("ID_SPACE_0") instanceof RingIDSpaceSimple;
	}

	@Override
	public void preprocess(Graph graph) {
		this.idSpace = (RingIDSpaceSimple) graph.getProperty("ID_SPACE_0");
		this.p = (RingPartitionSimple[]) idSpace.getPartitions();
		double maxDistance = this.maxRelativeDistance
				* this.idSpace.getModulus();
		Random rand = new Random();
		// computed once for every edge (not per neighbor)
		this.obfuscatedIDs = new RingID[graph.getNodes().length][];
		for (Node n : graph.getNodes()) {
			this.obfuscatedIDs[n.getIndex()] = new RingID[n.getOutDegree()];
			int[] out = n.getOutgoingEdges();
			for (int i = 0; i < out.length; i++) {
				RingID original = (RingID) this.idSpace.getPartitions()[out[i]]
						.getRepresentativeID();
				this.obfuscatedIDs[n.getIndex()][i] = this.closeID(original,
						maxDistance, rand);
			}
		}
	}

}
