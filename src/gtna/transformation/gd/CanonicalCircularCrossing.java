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
 * CanonicalCircularCrossing.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: Nico;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.gd;

import gtna.drawing.GraphPlotter;
import gtna.graph.Graph;
import gtna.util.parameter.BooleanParameter;
import gtna.util.parameter.DoubleParameter;
import gtna.util.parameter.IntParameter;
import gtna.util.parameter.Parameter;

/**
 * @author Nico
 * 
 */
public class CanonicalCircularCrossing extends CircularAbstract {
	public CanonicalCircularCrossing(int realities, double modulus,
			boolean wrapAround, GraphPlotter plotter) {
		super("GDA_CANONICALCIRCULARCROSSING", new Parameter[] {
				new IntParameter("REALITIES", realities),
				new DoubleParameter("MODULUS", modulus),
				new BooleanParameter("WRAPAROUND", wrapAround) });
		this.realities = realities;
		this.modulus = modulus;
		this.wrapAround = wrapAround;
		this.graphPlotter = plotter;
	}

	public GraphDrawingAbstract clone() {
		return new CanonicalCircularCrossing(realities, modulus, wrapAround,
				graphPlotter);
	}

	@Override
	public Graph transform(Graph g) {
		// int crossingsStart, crossingsEnd;

		initIDSpace(g);
		if (graphPlotter != null)
			graphPlotter.plotStartGraph(g, idSpace);

		// crossingsStart = edgeCrossings.calculateCrossings(g.generateEdges(),
		// idSpace, true);

		reduceCrossingsBySwapping(g);

		// crossingsEnd = edgeCrossings.calculateCrossings(g.generateEdges(),
		// idSpace, true);
		// System.out.println("Crossings at the beginning: " + crossingsStart +
		// " - and afterwards: " + crossingsEnd);

		if (graphPlotter != null)
			graphPlotter.plotFinalGraph(g, idSpace);
		writeIDSpace(g);
		return g;
	}
}
