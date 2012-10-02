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
import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.DoubleIdentifier;
import gtna.id.DoublePartition;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.model.placementmodels.NodeConnectorImpl;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * Connects nodes based on their distance. If the distance is below
 * <code>range</code> the nodes are connect, else they are not.
 * 
 * 
 * @author Philipp Neubrand
 * 
 */
public class UDGConnector extends NodeConnectorImpl {

	private double range;

	/**
	 * The standard constructor for this class.
	 * 
	 * @param range
	 *            The range below which all nodes are connected.
	 */
	public UDGConnector(double range) {
		this.range = range;
		setKey("UDG");
		setAdditionalConfigParameters(new Parameter[] { new DoubleParameter(
				"RANGE", range) });
	}

	/**
	 * Connects nodes based on their distance. If the distance is below
	 * <code>range</code> the nodes are connect, else they are not.
	 */
	@Override
	public Edges connect(Node[] nodes, PlaneIdentifierSpaceSimple ids, Graph g) {

		Edges edges = new Edges(nodes, nodes.length * (nodes.length - 1));

		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i != j
						&& ((DoublePartition) ids.getPartitions()[i])
								.distance((DoubleIdentifier) ids.getPartitions()[j]
										.getRepresentativeIdentifier()) <= range) {
					edges.add(i, j);
				}
			}
		}

		g.addProperty("RANGE_0", new RangeProperty(range, nodes.length));

		edges.fill();

		return edges;
	}
}
