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
import gtna.routing.Route;
import gtna.routing.RoutingAlgorithm;
import gtna.routing.RoutingAlgorithmImpl;

import java.util.Random;

/**
 * @author benni
 * 
 */
public class Greedy extends RoutingAlgorithmImpl implements RoutingAlgorithm {
	private IDSpace idSpace;

	public Greedy() {
		super("GREEDY", new String[] {}, new String[] {});
	}

	@Override
	public Route route(Graph graph, Node start, ID target, Random rand) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean applicable(Graph graph) {
		return graph.hasProperty("ID_SPACE");
	}

	@Override
	public void preprocess(Graph graph) {
		this.idSpace = (IDSpace) graph.getProperty("ID_SPACE");
	}

}
