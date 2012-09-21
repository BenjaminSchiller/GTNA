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
 * RoutingTableRouting.java
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
package gtna.routing.routingTable;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.id.data.DataStoreList;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.table.RoutingTables;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
public class RoutingTableRouting extends RoutingAlgorithm {

	private static final int ttl = 100;

	protected RoutingTables rt;

	public RoutingTableRouting() {
		this("ROUTING_TABLE_ROUTING");
	}

	protected RoutingTableRouting(String key) {
		super(key);
	}

	@Override
	public void preprocess(Graph graph) {
		super.preprocess(graph);
		if (graph.hasProperty("ROUTING_TABLES_0", RoutingTables.class)) {
			this.rt = (RoutingTables) graph.getProperty("ROUTING_TABLES_0");
		}
	}

	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		return this.routeToTarget(new ArrayList<Integer>(), start, target,
				graph.getNodes());
	}

	public Route routeToTarget(ArrayList<Integer> route, int current,
			Identifier target, Node[] nodes) {
		route.add(current);

		if (this.isEndPoint(current, target))
			return new Route(route, true);

		if (route.size() > ttl)
			return new Route(route, false);

		int nextHop = this.rt.getRoutingTable(current).getNextHop(target);
		if (nextHop == current || nextHop == RoutingTables.noRoute)
			return new Route(route, false);

		return this.routeToTarget(route, nextHop, target, nodes);
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ROUTING_TABLES_0", RoutingTables.class)
				&& (graph.hasProperty("ID_SPACE_0", IdentifierSpace.class) || graph
						.hasProperty("DATA_STORAGE_0", DataStoreList.class));
	}
}
