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
import gtna.networks.model.placementmodels.AbstractNodeConnector;

/**
 * @author Flipp
 * 
 */
public class UDGConnector extends AbstractNodeConnector {

	private double range;

	/**
	 * @param i
	 */
	public UDGConnector(double range) {
		this.range = range;
		setKey("UDG");
		setAdditionalConfigKeys(new String[] { "RANGE" });
		setAdditionalConfigValues(new String[] { Double.toString(range) });
	}

	@Override
	public Edges connect(Node[] nodes, PlaneIdentifierSpaceSimple ids) {

		Edges edges = new Edges(nodes, nodes.length * (nodes.length - 1));

		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i != j
						&& ids.getPartitions()[i]
								.distance((ids.getPartitions()[j]
										.getRepresentativeID())) < range) {
					edges.add(i, j);
				}
			}
		}

		edges.fill();

		return edges;
	}
}
