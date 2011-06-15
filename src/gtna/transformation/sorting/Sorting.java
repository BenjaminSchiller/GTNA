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
 * Sorting.java
 * ---------------------------------------
 * (C) Copyright 2009-2011, by Benjamin Schiller (P2P, TU Darmstadt)
 * and Contributors 
 *
 * Original Author: "Benjamin Schiller";
 * Contributors:    "Stefanie Roos";
 *
 * Changes since 2011-05-17
 * ---------------------------------------
 * 2011-06-14 : v1 (BS)
 *
 */
package gtna.transformation.sorting;

import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.routing.node.RingNode;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.HashSet;
import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public abstract class Sorting extends TransformationImpl implements
		Transformation {
	
	private int iterations;

	/**
	 * @param key
	 * @param configKeys
	 * @param configValues
	 */
	public Sorting(int iterations, String key, String[] configKeys, String[] configValues) {
		super(key, configKeys, configValues);
		this.iterations = iterations;
	}

	public boolean applicable(Graph g) {
		return g.nodes[0] instanceof RingNode;
	}

	public Graph transform(Graph g) {
		HashSet<NodeImpl> attackers = this.selectAttackers(g);
		SortingNode[] nodes = this.generateNodes(g, attackers);
		Random rand = new Random();
		for (int i = 0; i < this.iterations * g.nodes.length; i++) {
			int index = rand.nextInt(nodes.length);
			nodes[index].updateNeighbors();
			nodes[index].turn();
		}
		return null;
	}

	protected abstract HashSet<NodeImpl> selectAttackers(Graph g);

	protected abstract SortingNode[] generateNodes(Graph g,
			HashSet<NodeImpl> attackers);
	
	protected abstract SortingNode[] generateSelectionSet(SortingNode[] nodes);

}
