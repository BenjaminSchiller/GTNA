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
package gtna.networks.model.placementmodels.connectors;

import gtna.graph.Edges;
import gtna.graph.Node;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.model.placementmodels.NodeConnectorImpl;

/**
 * @author Flipp
 * 
 */
public class QUDGConnector extends NodeConnectorImpl {

	private double range1;
	private double range2;
	private double perc;

	/**
	 * @param i
	 */
	public QUDGConnector(double range1, double range2, double perc) {
		this.range1 = range1;
		this.range2 = range2;
		this.perc = perc;
		setKey("UDG");
		setAdditionalConfigKeys(new String[] { "RANGE1", "RANGE2", "PERC" });
		setAdditionalConfigValues(new String[] { Double.toString(range1),
				Double.toString(range2), Double.toString(perc) });
	}

	@Override
	public Edges connect(Node[] nodes, PlaneIdentifierSpaceSimple ids) {

		Edges edges = new Edges(nodes, nodes.length * (nodes.length - 1));
		double dist;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i == j)
					continue;
				dist = ids.getPartitions()[i].distance((ids.getPartitions()[j]
						.getRepresentativeID()));
				if (dist < range1)
					edges.add(i, j);
				else if (dist < range2 && Math.random() < perc)
					edges.add(i, j);
			}
		}

		edges.fill();

		return edges;
	}
}
