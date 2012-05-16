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
 * TwoPhaseDataStorage.java
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
package gtna.transformation.id.data;

import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.id.Identifier;
import gtna.id.IdentifierSpace;
import gtna.id.data.DataStorageList;
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;
import gtna.util.parameter.ParameterListArrayParameter;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class MultiPhaseDataStorage extends Transformation {

	private int retries;

	private RoutingAlgorithm[] phases;

	public MultiPhaseDataStorage(int retries, RoutingAlgorithm[] phases) {
		super("MULTI_PHASE_DATA_STORAGE", new Parameter[] {
				new IntParameter("RETRIES", retries),
				new ParameterListArrayParameter("PHASES", phases) });
		this.retries = retries;
		this.phases = phases;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Graph transform(Graph g) {
		for (RoutingAlgorithm phase : this.phases) {
			phase.preprocess(g);
		}

		Random rand = new Random();

		for (GraphProperty gp : g.getProperties("ID_SPACE")) {
			IdentifierSpace ids = (IdentifierSpace) gp;
			DataStorageList dsl = BasicDataStorage.getBasicDataStorage(ids);

			for (Node node : g.getNodes()) {
				this.registerDataItems(g, node, dsl, ids, rand);
			}

			g.addProperty(g.getNextKey("DATA_STORAGE"), dsl);
		}
		return g;
	}

	@SuppressWarnings("rawtypes")
	private void registerDataItems(Graph g, Node node, DataStorageList dsl,
			IdentifierSpace ids, Random rand) {
		Identifier id = ids.getPartitions()[node.getIndex()]
				.getRepresentativeID();
		for (int i = 0; i < this.retries; i++) {
			int current = node.getIndex();

			for (RoutingAlgorithm phase : this.phases) {
				Route r = phase.routeToTarget(g, current, id, rand);
				current = r.getLastNode();
			}

			dsl.getStorageForNode(current).add(id);
		}
	}

	@Override
	public boolean applicable(Graph g) {
		if (!g.hasProperty("ID_SPACE_0")) {
			return false;
		}
		for (RoutingAlgorithm phase : this.phases) {
			if (!phase.applicable(g)) {
				return false;
			}
		}
		return true;
	}

}
