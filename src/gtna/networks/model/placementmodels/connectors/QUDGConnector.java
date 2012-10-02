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
 * A <code>QUDGConnector</code> connects nodes based on their distance. If the
 * distance is smaller than or equal to <code>range1</code>, they will always
 * get an edge. If the distance is smaller than or equal to <code>range2</code>,
 * the nodes will have a connection with probability <code>p</code>. Else, they
 * will not have an edge.
 * 
 * @author Philipp Neubrand
 * 
 */
public class QUDGConnector extends NodeConnectorImpl {

	private double range1;
	private double range2;
	private double perc;

	/**
	 * Standard constructor for this class.
	 * 
	 * @param range1
	 *            The range below which nodes will always be connected.
	 * @param range2
	 *            The range below which nodes might be connected with
	 *            probability perc.
	 * @param perc
	 *            The probability for which nodes that are between range1 and
	 *            range2 are connected.
	 */
	public QUDGConnector(double range1, double range2, double perc) {
		this.range1 = range1;
		this.range2 = range2;
		this.perc = perc;
		setKey("QUDG");
		setAdditionalConfigParameters(new Parameter[] {
				new DoubleParameter("RANGE1", range1),
				new DoubleParameter("RANGE2", range2),
				new DoubleParameter("PROB", perc) });
	}

	/**
	 * Connects nodes based on their range. If the distance is smaller than or
	 * equal to <code>range1</code>, they will always get an edge. If the
	 * distance is smaller than or equal to <code>range2</code>, the nodes will
	 * have a connection with probability <code>p</code>. Else, they will not
	 * have an edge.
	 */
	@Override
	public Edges connect(Node[] nodes, PlaneIdentifierSpaceSimple ids, Graph g) {

		Edges edges = new Edges(nodes, nodes.length * (nodes.length - 1));
		double dist;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i == j)
					continue;
				dist = ((DoublePartition) ids.getPartitions()[i])
						.distance((DoubleIdentifier) ids.getPartitions()[j]
								.getRepresentativeIdentifier());
				if (dist <= range1)
					edges.add(i, j);
				else if (dist <= range2 && Math.random() < perc)
					edges.add(i, j);
			}
		}

		g.addProperty("RANGE_0", new RangeProperty(range1, nodes.length));

		edges.fill();

		return edges;
	}
}
