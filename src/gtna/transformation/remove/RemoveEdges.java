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
 * RemoveEdges.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: stef;
 * Contributors:    -;
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 *
 */
package gtna.transformation.remove;

import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;

import java.util.HashMap;
import java.util.Vector;

/**
 * @author stef abstract class for removing edges
 */
public abstract class RemoveEdges extends Transformation {

	/**
	 * @param key
	 * @param parameters
	 */
	public RemoveEdges(String key, Parameter[] parameters) {
		super(key, parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		HashMap<Integer, Vector<Integer>> map = this.getEdgeSet(g);
		Edges edges = new Edges(g.getNodes(), g.getEdges().size() - map.size());
		for (int i = 0; i < g.getNodes().length; i++) {
			int[] out = g.getNodes()[i].getOutgoingEdges();
			Vector<Integer> deleted = map.get(i);
			if (deleted == null) {
				deleted = new Vector<Integer>();
			}
			for (int j = 0; j < out.length; j++) {
				if (!deleted.contains(out[j])) {
					edges.add(i, out[j]);
				}
			}
		}
		edges.fill();
		return g;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#applicable(gtna.graph.Graph)
	 */
	@Override
	public boolean applicable(Graph g) {
		return true;
	}

	public abstract HashMap<Integer, Vector<Integer>> getEdgeSet(Graph g);

}
