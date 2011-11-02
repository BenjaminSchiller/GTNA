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
 * TwoPhaseGreedyRouting.java
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
package gtna.routing.twoPhase;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.BIIdentifierSpace;
import gtna.id.BIPartition;
import gtna.id.DIdentifier;
import gtna.id.DIdentifierSpace;
import gtna.id.DPartition;
import gtna.id.storage.StorageList;
import gtna.routing.Route;
import gtna.routing.RouteImpl;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class TwoPhaseGreedy extends RoutingAlgorithmImpl implements
		RoutingAlgorithm {
	private DIdentifierSpace idSpaceD;

	private DPartition[] pD;

	private BIIdentifierSpace idSpaceBI;

	private BIPartition[] pBI;

	private StorageList storageList;

	private int ttl;

	public TwoPhaseGreedy() {
		super("TWO_PHASE_GREEDY", new String[] {}, new String[] {});
		this.ttl = Integer.MAX_VALUE;
	}

	public TwoPhaseGreedy(int ttl) {
		super("TWO_PHASE_GREEDY", new String[] { "TTL" }, new String[] { ""
				+ ttl });
		this.ttl = ttl;
	}

	@Override
	public Route routeToRandomTarget(Graph graph, int start, Random rand) {
		if (this.idSpaceBI != null) {
			return this.routeToRandomTargetBI(graph, start, rand);
		} else if (this.idSpaceD != null) {
			return this.routeToRandomTargetD(graph, start, rand);
		} else {
			return null;
		}
	}

	private Route routeToRandomTargetBI(Graph graph, int start, Random rand) {
		BIIdentifier target = (BIIdentifier) this.idSpaceBI.randomID(rand);
		while (this.pBI[start].contains(target)) {
			target = (BIIdentifier) this.idSpaceBI.randomID(rand);
		}
		return this.routeBIFirst(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes());
	}

	private Route routeBIFirst(ArrayList<Integer> route, int current,
			BIIdentifier target, Random rand, Node[] nodes) {
		route.add(current);
		if (this.storageList.get(current).contains(target)
				|| this.pBI[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		int outDegree = nodes[current].getOutDegree();
		int next = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			if (nodes[out].getOutDegree() > outDegree) {
				next = out;
			}
		}
		if (next == -1) {
			return this.routeBISecond(route, current, target, rand, nodes);
		} else {
			return this.routeBIFirst(route, next, target, rand, nodes);
		}
	}

	private Route routeBISecond(ArrayList<Integer> route, int current,
			BIIdentifier target, Random rand, Node[] nodes) {
		route.add(current);
		if (this.storageList.get(current).contains(target)
				|| this.pBI[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		BigInteger currentDist = this.idSpaceBI.getPartitions()[current]
				.distance(target);
		BigInteger minDist = this.idSpaceBI.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			BigInteger dist = this.pBI[out].distance(target);
			if (dist.compareTo(minDist) == -1
					&& dist.compareTo(currentDist) == -1) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1) {
			return new RouteImpl(route, false);
		}
		return this.routeBISecond(route, minNode, target, rand, nodes);
	}

	private Route routeToRandomTargetD(Graph graph, int start, Random rand) {
		DIdentifier target = (DIdentifier) this.idSpaceD.randomID(rand);
		while (this.pD[start].contains(target)) {
			target = (DIdentifier) this.idSpaceD.randomID(rand);
		}
		return this.routeDFirst(new ArrayList<Integer>(), start, target, rand,
				graph.getNodes());
	}

	private Route routeDFirst(ArrayList<Integer> route, int current,
			DIdentifier target, Random rand, Node[] nodes) {
		route.add(current);
		if (this.storageList.get(current).contains(target)
				|| this.pD[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		int outDegree = nodes[current].getOutDegree();
		int next = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			if (nodes[out].getOutDegree() > outDegree) {
				next = out;
			}
		}
		if (next == -1) {
			return this.routeDSecond(route, current, target, rand, nodes);
		} else {
			return this.routeDFirst(route, next, target, rand, nodes);
		}
	}

	private Route routeDSecond(ArrayList<Integer> route, int current,
			DIdentifier target, Random rand, Node[] nodes) {
		route.add(current);
		if (this.storageList.get(current).contains(target)
				|| this.pD[current].contains(target)) {
			return new RouteImpl(route, true);
		}
		if (route.size() > this.ttl) {
			return new RouteImpl(route, false);
		}
		double currentDist = this.idSpaceD.getPartitions()[current]
				.distance(target);
		double minDist = this.idSpaceD.getMaxDistance();
		int minNode = -1;
		for (int out : nodes[current].getOutgoingEdges()) {
			double dist = this.pD[out].distance(target);
			if (dist < minDist && dist < currentDist) {
				minDist = dist;
				minNode = out;
			}
		}
		if (minNode == -1) {
			return new RouteImpl(route, false);
		}
		return this.routeDSecond(route, minNode, target, rand, nodes);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0")
				&& graph.hasProperty("STORAGE_LIST_0");
	}

	@Override
	public void preprocess(Graph graph) {
		GraphProperty p = graph.getProperty("ID_SPACE_0");
		if (p instanceof DIdentifierSpace) {
			this.idSpaceD = (DIdentifierSpace) p;
			this.pD = (DPartition[]) this.idSpaceD.getPartitions();
			this.idSpaceBI = null;
			this.pBI = null;
			this.storageList = (StorageList) graph
					.getProperty("STORAGE_LIST_0");
		} else if (p instanceof BIIdentifierSpace) {
			this.idSpaceD = null;
			this.pD = null;
			this.idSpaceBI = (BIIdentifierSpace) p;
			this.pBI = (BIPartition[]) this.idSpaceBI.getPartitions();
			this.storageList = (StorageList) graph
					.getProperty("STORAGE_LIST_0");
		} else {
			this.idSpaceD = null;
			this.pD = null;
			this.idSpaceBI = null;
			this.pBI = null;
		}
	}

}
