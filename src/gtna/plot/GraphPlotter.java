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
package gtna.plot;

import gtna.graph.Graph;

/**
 * @author Nico
 *
 */
public class GraphPlotter {
	private Gephi gephi;
	private String basename, extension;
	private int iterationModulus = -1;
	private boolean disabled = false;
	
	public GraphPlotter( String basename, String extension) {
		this.gephi = new Gephi();
		this.basename = basename;
		this.extension = extension;
	}
	
	public GraphPlotter ( boolean disabled ) {
		this.disabled = disabled;
	}
		
	public void setIterationModulus(int iterationModulus) {
		this.iterationModulus = iterationModulus;
	}

	public String getBasename() {
		return this.basename;
	}

	public void plot(Graph g, String filename) {
		if ( !disabled ) gephi.Plot(g, filename + "." + extension);
	}
	
	public void plotIteration(Graph g, int iteration) {
		if ( disabled ) return;
		if ( iterationModulus < 0 ) {
			throw new RuntimeException("iterationModulus for GraphPlotter not set yet or smaller than zero");
		}
		if ( iteration % iterationModulus == 0) plot(g, basename + "-" + iteration);
	}
	
	public void plotFinalGraph(Graph g) {
		plot(g, basename + "-final");
	}
}
