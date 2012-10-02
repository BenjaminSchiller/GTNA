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

import java.util.Random;

/**
 * The <code>LogDistanceConnector</code> connects nodes based on their distance.
 * The probability of two nodes being connected follows a log-normal
 * distribution, depending on the distance. The distance is calculated as
 * <code>(10 * gamma * Math.log10(distance / d0)) + rnd.nextGaussian() * sigma</code>
 * . If the resulting value is smaller or equal to <code>range</code> the nodes
 * are connected, otherwise they are not connected.
 * 
 * 
 * @author Philipp Neubrand
 * 
 */
public class LogDistanceConnector extends NodeConnectorImpl {

	private double range;
	private double gamma;
	private double d0;
	private double sigma;

	/**
	 * Standard constructor for this class, takes all the necessary information.
	 * As mentioned, an edge is determined by calculating
	 * <code>((10 * gamma * Math.log10(distance/d0) + rnd.nextGaussian() * sigma</code>
	 * and checking if the result is smaller than <code>range</code>.
	 * 
	 * @param range
	 *            The range from the formula.
	 * @param gamma
	 *            Gamma from the formula.
	 * @param d0
	 *            D0 from the formula.
	 * @param sigma
	 *            Sigma from the formula.
	 */
	public LogDistanceConnector(double range, double gamma, double d0,
			double sigma) {
		this.range = range;
		this.gamma = gamma;
		this.d0 = d0;
		this.sigma = sigma;
		setKey("LOG");
		setAdditionalConfigParameters(new Parameter[] {
				new DoubleParameter("RANGE", range),
				new DoubleParameter("GAMMA", gamma),
				new DoubleParameter("D0", d0),
				new DoubleParameter("SIGMA", sigma) });
	}

	/**
	 * The probability of two nodes being connected follows a log-normal
	 * distribution, depending on the distance. The distance is calculated as
	 * <code>(10 * gamma * Math.log10(distance / d0)) + rnd.nextGaussian() * sigma</code>
	 * . If the resulting value is smaller or equal to <code>range</code> the
	 * nodes are connected, otherwise they are not connected.
	 * 
	 */
	@Override
	public Edges connect(Node[] nodes, PlaneIdentifierSpaceSimple ids, Graph g) {
		Random rnd = new Random();
		Edges edges = new Edges(nodes, nodes.length * (nodes.length - 1));
		double dist;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i == j)
					continue;
				dist = 10
						* gamma
						* Math.log10(((DoublePartition) ids.getPartitions()[i])
								.distance((DoubleIdentifier) ids.getPartitions()[j]
										.getRepresentativeIdentifier())
								/ d0) + rnd.nextGaussian() * sigma;
				if (dist < range)
					edges.add(i, j);
			}
		}

		// As the distance calculation for this connector involves a random
		// element, establishing a reliable minimum distance is not that easy.
		// Even if the first element of the formula is 0, the random portion can
		// theoretically be greater than the range, thus preventing a connection
		// from being established. Using the fact that 99.6% of all values for a
		// gaussian distribution are within 3*sigma, we use 3*sigma as kind of a
		// maximum value for the random part of the equation. Solving the
		// equation for d then yields the result below.
		g.addProperty(
				"RANGE_0",
				new RangeProperty(Math.pow(10, (range - 3 * sigma)
						/ (10 * gamma))
						* d0, nodes.length));

		edges.fill();

		return edges;
	}
}
