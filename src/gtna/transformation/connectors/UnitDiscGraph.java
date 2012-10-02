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
 * UDG.java
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
package gtna.transformation.connectors;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.id.DoubleIdentifier;
import gtna.id.DoublePartition;
import gtna.id.plane.PlaneIdentifierSpaceSimple;
import gtna.transformation.Transformation;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.Parameter;

/**
 * @author benni
 * 
 */
public class UnitDiscGraph extends Transformation {
	private double radius;

	public UnitDiscGraph(double radius) {
		super("UNIT_DISC_GRAPH", new Parameter[] { new DoubleParameter(
				"RADIUS", radius) });
		this.radius = radius;
	}

	@Override
	public Graph transform(Graph g) {
		Edges edges = new Edges(g.getNodes(), g.getNodes().length
				* (g.getNodes().length - 1));
		PlaneIdentifierSpaceSimple idSpace = (PlaneIdentifierSpaceSimple) g
				.getProperty("ID_SPACE_0");
		for (int i = 0; i < g.getNodes().length; i++) {
			for (int j = 0; j < g.getNodes().length; j++) {
				if (i != j
						&& ((DoublePartition) idSpace.getPartition(i))
								.distance(((DoubleIdentifier) idSpace
										.getPartition(j)
										.getRepresentativeIdentifier())) <= radius) {
					edges.add(i, j);
				}
			}
		}
		edges.fill();
		return g;
	}

	@Override
	public boolean applicable(Graph g) {
		return g.getProperty("ID_SPACE_0") instanceof PlaneIdentifierSpaceSimple;
	}

}
