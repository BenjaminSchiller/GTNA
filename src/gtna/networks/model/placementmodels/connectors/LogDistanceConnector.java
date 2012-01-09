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

import java.util.Random;

import gtna.graph.Edges;
import gtna.graph.Node;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.networks.model.placementmodels.NodeConnectorImpl;

/**
 * @author Flipp
 * 
 */
public class LogDistanceConnector extends NodeConnectorImpl {

	private double range;
	private double gamma;
	private double d0;
	private double sigma;

	/**
	 * @param i
	 */
	public LogDistanceConnector(double range, double gamma, double d0, double sigma) {
		this.range = range;
		this.gamma = gamma;
		this.d0 = d0;
		this.sigma = sigma;
		setKey("LOG");
		setAdditionalConfigKeys(new String[] { "RANGE", "GAMMA", "D0", "SIGMA" });
		setAdditionalConfigValues(new String[] { Double.toString(range),
				Double.toString(gamma), Double.toString(d0), Double.toString(sigma) });
	}

	@Override
	public Edges connect(Node[] nodes, PlaneIdentifierSpaceSimple ids) {
		Random rnd = new Random();
		Edges edges = new Edges(nodes, nodes.length * (nodes.length - 1));
		double dist;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes.length; j++) {
				if (i == j)
					continue;
				dist = 10
						* gamma
						* Math.log10(ids.getPartitions()[i].distance((ids
								.getPartitions()[j].getRepresentativeID()))
								/ d0) + rnd.nextGaussian() * sigma;
				if (dist < range)
					edges.add(i, j);
			}
		}

		edges.fill();

		return edges;
	}
}
