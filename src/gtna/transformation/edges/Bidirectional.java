/*
 * ===========================================================
 * GTNA : Graph-Theoretic Network Analyzer
 * ===========================================================
 * 
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors
 * 
 * Project Info:  http://www.p2p.tu-darmstadt.de/research/gtna/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * ---------------------------------------
 * Bidirectional.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 * 
 * Original Author: Benjamin Schiller;
 * Contributors:    -;
 * 
 * Changes since 2011-05-17
 * ---------------------------------------
*/
package gtna.transformation.edges;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.GraphProperties;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

/**
 * Transforms the given graph into a bidirectional version by adding every
 * reverse edge that is not already contained, i.e., for every edge (a,b) add
 * edge (b,a) if it does not exist.
 * 
 * @author benni
 * 
 */
public class Bidirectional extends TransformationImpl implements Transformation {
	public Bidirectional() {
		super("BIDIRECTIONAL", new String[] {}, new String[] {});
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {
		if (GraphProperties.bidirectional(g)) {
			return g;
		}
		Edges edgeSet = new Edges(g.nodes, g.edges * 2);
		Edge[] edges = g.edges();
		edgeSet.add(edges);
		for (int i = 0; i < edges.length; i++) {
			Edge back = new Edge(edges[i].dst, edges[i].src);
			if (!edgeSet.contains(back)) {
				edgeSet.add(back);
			}
		}
		edgeSet.fill();
		g.computeDegrees();
		g.computeEdges();
		return g;
	}

}
