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
 * BubbleTree_BoundingBox.java
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

import gtna.graph.spanningTree.SpanningTree;
import gtna.plot.GraphPlotter;

/**
 * @author Nico
 *
 */
public class BubbleTree_BoundingBox extends BubbleTree {
	public BubbleTree_BoundingBox(double modulusX, double modulusY, GraphPlotter plotter) {
		super("GDA_BUBBLETREE_BOUNDINGBOX", modulusX, modulusY, plotter);
		throw new RuntimeException("Bubble tree with bounding box is not ready yet");
	}
	
	private double calculateSmallestEnclosingCircle(SpanningTree tree, int node) {
		//TODO implement Bennis bounding box
		return -1;
	}
}
