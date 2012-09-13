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
 * LookaheadSimple.java
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
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class LookaheadSimple extends RoutingAlgorithm {
	private int ttl;

	@SuppressWarnings("rawtypes")
	protected IdentifierSpace idSpace;

	@SuppressWarnings("rawtypes")
	protected Partition[] p;

	public LookaheadSimple(int ttl) {
		super("LOOKAHEAD_SIMPLE",
				new Parameter[] { new IntParameter("TTL", ttl) });
		this.ttl = ttl;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		return this.route(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes(), new HashSet<Integer>());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Route route(ArrayList<Integer> route, int current,
			Identifier target, Random rand, Node[] nodes, HashSet<Integer> seen) {
		route.add(current);
		seen.add(current);

		if (this.isEndPoint(current, target)) {
			return new Route(route, true);
		}
		if (route.size() > this.ttl) {
			return new Route(route, false);
		}

		int via = -1;

		if (nodes[current].getOutDegree() == 0) {
			return new Route(route, false);
		}

		if (this.p[current] instanceof DPartition) {
			double currentDist = (Double) this.p[current].distance(target);
			double minDist = (Double) this.idSpace.getMaxDistance();
			for (int neighbor : nodes[current].getOutgoingEdges()) {
				double dist = ((DPartition) this.p[neighbor]).distance(target);
				if (dist < minDist && dist < currentDist
						&& !seen.contains(neighbor)) {
					minDist = dist;
					via = neighbor;
				}
			}
			for (int neighbor : nodes[current].getOutgoingEdges()) {
				for (int lookahead : nodes[neighbor].getOutgoingEdges()) {
					double dist = ((DPartition) this.p[lookahead])
							.distance(target);
					if (dist < minDist && dist < currentDist
							&& !seen.contains(neighbor)) {
						minDist = dist;
						via = neighbor;
					}
				}
			}
		} else if (this.p[current] instanceof BIPartition) {
			BigInteger currentDist = (BigInteger) this.p[current]
					.distance(target);
			BigInteger minDist = (BigInteger) this.idSpace.getMaxDistance();
			for (int neighbor : nodes[current].getOutgoingEdges()) {
				BigInteger dist = ((BIPartition) this.p[neighbor])
						.distance(target);
				if (dist.compareTo(minDist) == -1
						&& dist.compareTo(currentDist) == -1
						&& !seen.contains(neighbor)) {
					minDist = dist;
					via = neighbor;
				}
			}
			for (int neighbor : nodes[current].getOutgoingEdges()) {
				for (int lookahead : nodes[neighbor].getOutgoingEdges()) {
					BigInteger dist = ((BIPartition) this.p[lookahead])
							.distance(target);
					if (dist.compareTo(minDist) == -1
							&& dist.compareTo(currentDist) == -1
							&& !seen.contains(neighbor)) {
						minDist = dist;
						via = neighbor;
					}
				}
			}
		} else {
			return null;
		}

		if (via == -1) {
			return new Route(route, false);
		}
		return this.route(route, via, target, rand, nodes, seen);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0", IdentifierSpace.class);
	}

}
