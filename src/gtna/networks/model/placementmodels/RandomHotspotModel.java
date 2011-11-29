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

import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.networks.Network;
import gtna.routing.RoutingAlgorithm;
import gtna.transformation.Transformation;

public class RandomHotspotModel extends AbstractHotspotModel implements Network {
	private boolean overallInCenter;

	public RandomHotspotModel(int spots, int nodesperspot, double overallWidth,
			double overallHeight, boolean overallInCenter, int hotspotWidth,
			int hotspotHeight, double hotspotSigma, boolean hotspotInCenter,
			NodeConnector nc, RoutingAlgorithm ra, Transformation[] t) {
		
		super("RANDOM_", spots, nodesperspot, overallWidth, overallHeight,
				hotspotWidth, hotspotHeight, hotspotSigma, hotspotInCenter,
				new String[] { "OVERALL_IN_CENTER" }, new String[] { Boolean
						.toString(overallInCenter) }, nc, ra, t);

		this.overallInCenter = overallInCenter;

	}

	@Override
	protected PlanePartitionSimple[] getHotspots(
			PlaneIdentifierSpaceSimple idspace) {
		return Placement.placeByRandomModel(getWidth(), getHeight(), spots,
				overallInCenter, idspace);
	}

}
