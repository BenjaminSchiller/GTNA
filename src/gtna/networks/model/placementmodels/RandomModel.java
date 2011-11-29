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
 * NodeConnector.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Philipp Neubrand;
 * Contributors:    -;
 *
 * ---------------------------------------
 */
package gtna.networks.model.placementmodels;

import gtna.graph.Graph;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class RandomModel extends AbstractPlacementModel implements Network {
	private boolean inCenter;
	private int width;
	private int height;
	
	public RandomModel(int nodes, double width, double height, boolean inCenter, NodeConnector nc, RoutingAlgorithm ra, Transformation[] t) {
		super("RANDOMMODEL", nodes, new String[] {"inCenter"}, new String[] {Boolean.toString(inCenter)}, width, height, nc, ra, t);

		this.inCenter = inCenter;

		setCoords(new PlaneIdentifierSpaceSimple(null, width, height, false));
	}

	public Graph generate() {
		getCoords().setPartitions(Placement.placeByRandomModel(width, height, nodes(), inCenter, getCoords()));

		return finish();
	}

}
