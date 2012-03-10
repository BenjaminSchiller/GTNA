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
 * DegreeColorizer.java
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
public class DegreeColorizer extends GephiDecorator {
	int maxDegree = 0;

	public void init(Graph g) {
		for (gtna.graph.Node n : g.getNodes()) {
			maxDegree = Math.max(maxDegree, n.getDegree());
		}
		this.initialized = true;
	}

	@Override
	public void decorateNode(org.gephi.graph.api.Node gephiNode, gtna.graph.Node gtnaNode) {
		if (!initialized) {
			throw new RuntimeException("GephiDecorator not initialized yet");
		}
		int degree = gtnaNode.getDegree();
		float color = (float) degree / maxDegree;
		gephiNode.getNodeData().setColor(color, color, color);
	}
}
