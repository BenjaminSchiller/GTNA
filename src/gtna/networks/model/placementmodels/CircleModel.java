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
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class CircleModel extends AbstractPlacementModel implements Network {
	private double radius;
	private DistributionType oalpha;
	private DistributionType od;
	private double centerx;
	private double centery;

	public CircleModel(int nodes, double centerx, double centery, double width,
			double height, double radius, DistributionType oalpha,
			DistributionType od, NodeConnector nc, RoutingAlgorithm ra,
			Transformation[] t) {
		super("CIRCLEMODEL", nodes, new String[] { "CENTER_X", "CENTER_Y",
				"RADIUS" }, new String[] { Double.toString(centerx),
				Double.toString(centery), Double.toString(radius) }, width,
				height, nc, ra, t);

		this.oalpha = oalpha;
		this.od = od;
		this.radius = radius;

		this.centerx = centerx;

		this.centery = centery;
	}

	public Graph generate() {
		getCoords().setPartitions(
				Placement.placeByCircleModel(centerx, centery, radius, this
						.getWidth(), this.getHeight(), nodes(), oalpha, od,
						getCoords()));

		return finish();
	}

}
