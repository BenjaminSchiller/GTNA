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
 * GephiDecorator.java
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

import org.gephi.graph.api.Edge;

import gtna.graph.Graph;

/**
 * @author Nico
 * 
 */
public abstract class GephiDecorator {
	protected boolean initialized = false;

	public GephiDecorator() {
	}

	public abstract void init(Graph g);

	public boolean showNode(gtna.graph.Node gtnaNode) {
		return true;
	}

	public void decorateNode(org.gephi.graph.api.Node gephiNode, gtna.graph.Node gtnaNode) {
	}

	public boolean showEdge(int src, int dest) {
		return true;
	}

	public void decorateEdge(Edge temp, int src, int dest) {
	}

}
