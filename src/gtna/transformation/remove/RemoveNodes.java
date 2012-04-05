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
 * RemoveNodes.java
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
import gtna.graph.Node;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;

import java.util.HashMap;

/**
 * @author stef abstract class for removing nodes
 */
public abstract class RemoveNodes extends Transformation {

	/**
	 * @param key
	 * @param parameters
	 */
	public RemoveNodes(String key, Parameter[] parameters) {
		super(key, parameters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gtna.transformation.Transformation#transform(gtna.graph.Graph)
	 */
	@Override
	public Graph transform(Graph g) {
		boolean[] remove = this.getNodeSet(g);
		Node[] oldNodes = g.getNodes();
		// Node[] newNodes = new Node[oldNodes.length-remove.size()];
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		int count = 0;
		for (int i = 0; i < oldNodes.length; i++) {
			if (!remove[i]) {
				map.put(i, count);
				count++;
			}
		}
		Node[] newNodes = new Node[count];
		int edges = 0;
		count = 0;
		for (int i = 0; i < oldNodes.length; i++) {
			if (!remove[i]) {
				newNodes[count] = oldNodes[i];
				newNodes[count].setIndex(count);
				count++;
				edges = edges + oldNodes[i].getOutDegree();
			}
		}
		Edges edgeSet = new Edges(newNodes, edges);
		for (int j = 0; j < newNodes.length; j++) {
			int[] out = newNodes[j].getOutgoingEdges();
			for (int i = 0; i < out.length; i++) {
				if (!remove[out[i]]) {
					edgeSet.add(j, map.get(out[i]));
				}
			}
		}
		edgeSet.fill();
		g.setNodes(newNodes);
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

	public abstract boolean[] getNodeSet(Graph g);

}
