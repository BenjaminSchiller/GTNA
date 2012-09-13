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
 * MultiPhaseRouting.java
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
package gtna.routing.multiPhase;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.Identifier;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.StringParameter;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author benni
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class MultiPhaseRouting extends RoutingAlgorithm {

	private int retries;

	private RoutingAlgorithm[] phases;

	public MultiPhaseRouting(RoutingAlgorithm[] phases) {
		super("MULTI_PHASE_ROUTING", new Parameter[] { new StringParameter(
				"PHASES", RoutingAlgorithm.toString(phases)) });
		this.retries = 1;
		this.phases = phases;
	}

	public MultiPhaseRouting(int retries, RoutingAlgorithm[] phases) {
		super("MULTI_PHASE_ROUTING",
				new Parameter[] {
						new IntParameter("RETRIES", retries),
						new StringParameter("PHASES",
								RoutingAlgorithm.toString(phases)) });
		this.retries = retries;
		this.phases = phases;
	}

	@Override
	public Route routeToTarget(Graph graph, int start, Identifier target,
			Random rand) {
		return this.route(graph, start, target, rand, graph.getNodes());
	}

	private Route route(Graph g, int start, Identifier target, Random rand,
			Node[] nodes) {
		ArrayList<Integer> route = new ArrayList<Integer>();
		route.add(start);

		if (this.isEndPoint(start, target)) {
			return new Route(route, true);
		}

		route.add(start);

		for (int run = 0; run < this.retries; run++) {

			int current = start;
			for (RoutingAlgorithm phase : this.phases) {
				Route r = phase.routeToTarget(g, current, target, rand);

				for (int i = 1; i < r.getRoute().length; i++) {
					route.add(r.getRoute()[i]);
				}

				if (r.isSuccessful()) {
					return new Route(route, true);
				}
			}

		}

		return new Route(route, false);
	}

	@Override
	public boolean applicable(Graph graph) {
		if (!graph.hasProperty("ID_SPACE_0")) {
			return false;
		}
		for (RoutingAlgorithm phase : this.phases) {
			if (!phase.applicable(graph)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void preprocess(Graph graph) {
		super.preprocess(graph);

		for (RoutingAlgorithm phase : this.phases) {
			phase.preprocess(graph);
		}
	}

}
