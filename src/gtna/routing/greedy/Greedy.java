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

import gtna.data.Series;
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.id.data.LruDataStore;
import gtna.metrics.Metric;
import gtna.metrics.basic.DegreeDistribution;
import gtna.metrics.routing.DataStorageMetric;
import gtna.metrics.routing.Routing;
import gtna.networks.Network;
import gtna.networks.util.ReadableFile;
import gtna.plot.Plotting;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.routingTable.CcnRouting;
import gtna.routing.selection.source.ConsecutiveSourceSelection;
import gtna.routing.selection.source.SourceSelection;
import gtna.routing.selection.target.DataStorageRandomTargetSelection;
import gtna.routing.selection.target.TargetSelection;
import gtna.transformation.Transformation;
import gtna.transformation.id.node.NodeIds;
import gtna.transformation.id.node.NodeIdsDataStorage;
import gtna.transformation.id.node.NodeIdsRoutingTable;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class Greedy extends RoutingAlgorithm {

	private int ttl;

	public Greedy() {
		super("GREEDY");
		this.ttl = Integer.MAX_VALUE;
	}

	public Greedy(int ttl) {
		super("GREEDY", new Parameter[] { new IntParameter("TTL", ttl) });
		this.ttl = ttl;
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

		if (this.isEndPoint(current, target)) {
			return new Route(route, true);
		}
		if (route.size() > this.ttl) {
			return new Route(route, false);
		}

		int closest = target.getClosestNode(nodes[current].getOutgoingEdges(),
				this.identifierSpace.getPartitions());
		if (!target.isCloser(this.identifierSpace.getPartition(closest),
				this.identifierSpace.getPartition(current))) {
			return new Route(route, false);
		}

		return this.route(route, closest, target, rand, nodes);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE_0", IdentifierSpace.class);
	}

}
