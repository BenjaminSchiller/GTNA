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
 * GreedyTemplate.java
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

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.BIIdentifier;
import gtna.id.BIIdentifierSpace;
import gtna.id.BIPartition;
import gtna.id.DIdentifier;
import gtna.id.DIdentifierSpace;
import gtna.id.DPartition;
import gtna.id.Identifier;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Random;

/**
 * templete for greedy-like algorithms
 * 
 * @author stefanie
 * 
 */
public abstract class GreedyTemplate extends RoutingAlgorithm {

	DIdentifierSpace idSpaceD;

	DPartition[] pD;

	BIIdentifierSpace idSpaceBI;

	BIPartition[] pBI;

	private int ttl;

	public GreedyTemplate(String name) {
		super(name);
		this.ttl = Integer.MAX_VALUE;
	}

	public GreedyTemplate(int ttl, String name) {
		super(name, new Parameter[] { new IntParameter("TTL", ttl) });
		this.ttl = ttl;
	}

	public GreedyTemplate(String name, Parameter[] parameters) {
		super(name, parameters);
		this.ttl = Integer.MAX_VALUE;
	}

	public GreedyTemplate(int ttl, String name, Parameter[] parameters) {
		super(name, parameters);
		this.ttl = ttl;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		this.setSets(graph.getNodes().length);
		if (this.idSpaceBI != null) {
			return this.routeBI(new ArrayList<Integer>(), start,
					(BIIdentifier) target, rand, graph.getNodes());
		} else if (this.idSpaceD != null) {
			return this.routeD(new ArrayList<Integer>(), start,
					(DIdentifier) target, rand, graph.getNodes());
		} else {
			return null;
		}
	}

	/**
	 * generic method for the routing procedure: check if target is reached, if
	 * not select the next node or fail
	 * 
	 * @param route
	 * @param current
	 * @param target
	 * @param rand
	 * @param nodes
	 * @return
	 */
	private Route routeBI(ArrayList<Integer> route, int current,
			BIIdentifier target, Random rand, Node[] nodes) {
		route.add(current);
		if (this.isEndPoint(current, target)) {
			return new Route(route, true);
		}
		if (route.size() > this.ttl) {
			return new Route(route, false);
		}
		int minNode = this.getNextBI(current, target, rand, nodes);
		if (minNode == -1) {
			return new Route(route, false);
		}
		return this.routeBI(route, minNode, target, rand, nodes);
	}

	/**
	 * generic method for the routing procedure: check if target is reached, if
	 * not select the next node or fail
	 * 
	 * @param route
	 * @param current
	 * @param target
	 * @param rand
	 * @param nodes
	 * @return
	 */
	private Route routeD(ArrayList<Integer> route, int current,
			DIdentifier target, Random rand, Node[] nodes) {
		route.add(current);
		if (this.isEndPoint(current, target)) {
			return new Route(route, true);
		}
		if (route.size() > this.ttl) {
			return new Route(route, false);
		}
		int minNode = this.getNextD(current, target, rand, nodes);
		if (minNode == -1) {
			return new Route(route, false);
		}
		return this.routeD(route, minNode, target, rand, nodes);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0")
				&& (graph.getProperty("ID_SPACE_0") instanceof DIdentifierSpace || graph
						.getProperty("ID_SPACE_0") instanceof BIIdentifierSpace);
	}

	@Override
	public void preprocess(Graph graph) {
		super.preprocess(graph);
		GraphProperty p = graph.getProperty("ID_SPACE_0");
		if (p instanceof DIdentifierSpace) {
			this.idSpaceD = (DIdentifierSpace) p;
			this.pD = (DPartition[]) this.idSpaceD.getPartitions();
			this.idSpaceBI = null;
			this.pBI = null;
		} else if (p instanceof BIIdentifierSpace) {
			this.idSpaceD = null;
			this.pD = null;
			this.idSpaceBI = (BIIdentifierSpace) p;
			this.pBI = (BIPartition[]) this.idSpaceBI.getPartitions();
		} else {
			this.idSpaceD = null;
			this.pD = null;
			this.idSpaceBI = null;
			this.pBI = null;
		}
	}

	/**
	 * abstract method for getting the next nodes
	 * 
	 * @param current
	 * @param target
	 * @param rand
	 * @param nodes
	 * @return
	 */
	public abstract int getNextD(int current, DIdentifier target, Random rand,
			Node[] nodes);

	public abstract int getNextBI(int current, BIIdentifier target,
			Random rand, Node[] nodes);

	/**
	 * abstract method initiating the necessary objects
	 * 
	 * @param nr
	 */
	public abstract void setSets(int nr);

}
