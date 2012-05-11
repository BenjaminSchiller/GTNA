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
 * PlaneIdentifierSpaceVisualization.java
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
package gtna.metrics.id;

import gtna.data.Single;
import gtna.graph.Graph;
import gtna.id.DPartition;
import gtna.id.IdentifierSpace;
import gtna.id.plane.PlaneIdentifier;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.id.plane.PlanePartitionSimple;
import gtna.io.DataWriter;
import gtna.metrics.Metric;
import gtna.networks.Network;

import java.util.HashMap;

/**
 * @author benni
 * 
 */
public class PlaneIdentifierSpaceVisualization extends Metric {

	private double[][] points;

	public PlaneIdentifierSpaceVisualization() {
		super("PLANE_IDENTIFIER_SPACE_VISUALIZATION");
		this.points = new double[][] { new double[] { -1, -1 } };
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void computeData(Graph g, Network n, HashMap<String, Metric> m) {
		IdentifierSpace ids = (IdentifierSpace) g.getProperty("ID_SPACE_0");
		PlaneIdentifierSpaceSimple plane = (PlaneIdentifierSpaceSimple) ids;

		this.points = new double[plane.getPartitions().length][2];
		int index = 0;
		for (DPartition p : plane.getPartitions()) {
			PlaneIdentifier id = ((PlanePartitionSimple) p).getId();
			double x = id.getX() / plane.getModulusX();
			double y = id.getY() / plane.getModulusY();
			this.points[index++] = new double[] { x, y };
		}
	}

	@Override
	public boolean writeData(String folder) {
		boolean success = true;
		success &= DataWriter.writeWithoutIndex(this.points,
				"PLANE_IDENTIFIER_SPACE_VISUALIZATION_POINTS", folder);
		return success;
	}

	@Override
	public Single[] getSingles() {
		return new Single[0];
	}

	@Override
	public boolean applicable(Graph g, Network n, HashMap<String, Metric> m) {
		return g.hasProperty("ID_SPACE_0")
				&& g.getProperty("ID_SPACE_0") instanceof PlaneIdentifierSpaceSimple;
	}

}
