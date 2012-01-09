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
 * Main.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Flipp;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna;

import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.networks.Network;
import gtna.networks.model.partitioners.SimplePartitioner;
import gtna.networks.model.placementmodels.PlacementModelContainer;
import gtna.networks.model.placementmodels.connectors.LogDistanceConnector;
import gtna.networks.model.placementmodels.models.GridPlacementModel;
import gtna.plot.Gephi;
import gtna.routing.greedy.Greedy;

/**
 * @author Flipp
 * 
 */
public class Main {
	public static void main(String[] args) {
		Network nw = new PlacementModelContainer(500, 25,
				new GridPlacementModel(1000, 1000, 5, 5),
				new GridPlacementModel(50, 50, 4, 5), new SimplePartitioner(),
				new LogDistanceConnector(0.025, 5, 15, 1), new Greedy(), null);
		// public PlacementModelContainer(int nodes, int hotspots,
		// AbstractPlacementModel hotspotPlacer,
		// AbstractPlacementModel nodePlacer, AbstractPartitioner partitioner,
		// AbstractNodeConnector node
		// Connector, RoutingAlgorithm r, Transformation[] t){
		Graph g = nw.generate();
		Gephi ge = new Gephi();
		ge.Plot(g, (IdentifierSpace) g.getProperty("id_space_0"), "test.pdf");

	}
}
