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

public class GridHotspotModel extends AbstractHotspotModel implements Network {
	private int cols;
	private int rows;

	public GridHotspotModel(int spots, int nodesperspot, double overallWidth,
			double overallHeight, int cols, int rows, int spotWidth,
			int spotHeight, double sigma, boolean inCenter, NodeConnector nc,
			RoutingAlgorithm ra, Transformation[] t) {
		super("GRID_", spots, nodesperspot, overallWidth, overallHeight,
				spotWidth, spotHeight, sigma, inCenter, new String[] { "COLS",
						"ROWS" }, new String[] { Integer.toString(cols),
						Integer.toString(rows) }, nc, ra, t);
		
		this.cols = cols;
		this.rows = rows;
	}

	@Override
	protected PlanePartitionSimple[] getHotspots(
			PlaneIdentifierSpaceSimple idspace) {
		return Placement.placeByGridModel(getWidth(), getHeight(), cols, rows,
				idspace);
	}

}
