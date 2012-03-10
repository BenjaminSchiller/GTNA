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
 * HideSmallDegree.java
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

import java.util.ArrayList;

import gtna.graph.Graph;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.graph.spi.LayoutData;

public class HideSmallDegree extends GephiDecorator {
	private int degreeLimit = 0;
	private ArrayList<Integer> hiddenNodes;

	public HideSmallDegree(int limit) {
		this.degreeLimit = limit;
	}

	public void init(Graph g) {
		this.hiddenNodes = new ArrayList<Integer>();
	}
	
	public boolean showNode(gtna.graph.Node gtnaNode) {
		if ( gtnaNode.getDegree() < degreeLimit) {
			hiddenNodes.add(gtnaNode.getIndex());
			System.out.println("hiding " + gtnaNode.getIndex());
			return false;
		} else {
			return true;
		}
	}

	public boolean showEdge(int src, int dest) {
		return (!hiddenNodes.contains(src) && !hiddenNodes.contains(dest));
	}

}
