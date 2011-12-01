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

public class GridModel extends AbstractPlacementModel implements Network {
	private int cols;
	private int rows;
	
	public GridModel(double width, double height, int cols, int rows, NodeConnector nc, RoutingAlgorithm ra, Transformation[] t) {
		super("GRIDMODEL", (cols*rows), width, height, new String[] {"COLS", "ROWS"}, new String[] {Integer.toString(cols), Integer.toString(rows)}, nc, ra, t);
		this.cols = cols;
		this.rows = rows;
		
		this.setNodes(cols * rows);

		setCoords(new PlaneIdentifierSpaceSimple(null, width, height, false));
	}

	public Graph generate() {
		getCoords().setPartitions(Placement.placeByGridModel(getWidth(), getHeight(), cols, rows, getCoords()));

		return finish();
	}

}
