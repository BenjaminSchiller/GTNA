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
 * GraphPlotter.java
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
package gtna.drawing;

import gtna.graph.Graph;
import gtna.id.IdentifierSpace;
import gtna.util.Config;

/**
 * @author Nico
 * 
 */
public class GraphPlotter {
	private Gephi gephi;
	private String basename, extension;
	private int iterationModulus = -1;
	private GephiDecorator[] decorators;

	public GraphPlotter(String basename, String extension, GephiDecorator[] decorators, int iterationModulus) {
		this.gephi = new Gephi();
		this.basename = basename;
		this.extension = extension;
		this.decorators = decorators;
		this.iterationModulus = iterationModulus;
	}

	public GraphPlotter(String basename, String extension, int iterationModulus) {
		this(basename, extension, null, iterationModulus);
	}

	public GraphPlotter(String basename, String extension) {
		this(basename, extension, -1);
	}

	public void setIterationModulus(int iterationModulus) {
		this.iterationModulus = iterationModulus;
	}

	public String getBasename() {
		return this.basename;
	}

	public void plot(Graph g, IdentifierSpace idSpace, String filename) {
		gephi.plot(g, this.decorators, idSpace, Config.get("MAIN_PLOT_FOLDER") + filename + "." + extension);
	}

	public void plotIteration(Graph g, IdentifierSpace idSpace, int iteration) {
		if (iterationModulus <= 0) {
			/*
			 * Assume we should not print any intermediate steps
			 */
		} else if (iteration % iterationModulus == 0)
			plot(g, idSpace, basename + "-" + iteration);
	}

	public void plotStartGraph(Graph g, IdentifierSpace idSpace) {
		plot(g, idSpace, basename + "-start");
	}

	public void plotFinalGraph(Graph g, IdentifierSpace idSpace) {
		plot(g, idSpace, basename + "-final");
	}

	public void plotSpanningTree(Graph g, IdentifierSpace idSpace) {
		gephi.useSpanningTreeOnNextPlot();
		plot(g, idSpace, basename + "-spanningTree");
	}
}
