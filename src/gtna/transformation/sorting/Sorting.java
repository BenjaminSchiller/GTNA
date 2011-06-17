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

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.NodeImpl;
import gtna.graph.sorting.NodeSorting;
import gtna.routing.node.RingNode;
import gtna.transformation.Transformation;
import gtna.transformation.TransformationImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * @author "Benjamin Schiller"
 * 
 */
public abstract class Sorting extends TransformationImpl implements
		Transformation {

	protected int iterations;

	/**
	 * @param key
	 * @param configKeys
	 * @param configValues
	 */
	public Sorting(int iterations, String key, String[] configKeys,
			String[] configValues) {
		super(key, configKeys, configValues);
		this.iterations = iterations;
	}

	public boolean applicable(Graph g) {
		return g.nodes[0] instanceof RingNode;
	}

	public Graph transform(Graph g) {
		Random rand = new Random();
		SortingNode[] nodes = this.generateNodes(g, rand);
		SortingNode[] selectionSet = this.generateSelectionSet(nodes, rand);
		for (int i = 0; i < this.iterations * selectionSet.length; i++) {
			int index = rand.nextInt(selectionSet.length);
			if (selectionSet[index].out().length > 0){
			  selectionSet[index].updateNeighbors(rand);
			  selectionSet[index].turn(rand);
			}
		}
		return new Graph(g.name, nodes, g.timer);
	}

	/**
	 * Generates a set of SortingNodes from the given graph (nodes need to be
	 * RingNodes).
	 * 
	 * @param g
	 *            input graph containing instances of RingNode
	 * @param rand
	 *            PRNG
	 * @return set of SortingNodes generated from the input graph
	 */
	protected abstract SortingNode[] generateNodes(Graph g, Random rand);

	/**
	 * Generates a selection set of all nodes that contains every node that
	 * should perform turn actions. It can be used to easily increase a node's
	 * turn frequency by adding it multiple times to the list.
	 * 
	 * @param nodes
	 *            list of nodes to create the selection from
	 * @param rand
	 *            PRNG
	 * @return selection set of all nodes containing every node that should
	 *         perform turn actions
	 */
	protected abstract SortingNode[] generateSelectionSet(SortingNode[] nodes,
			Random rand);

	/**
	 * generates a set of unique nodes at uniformly random
	 * 
	 * @param nodes
	 *            nodes to select from
	 * @param number
	 *            number of nodes to select
	 * @param rand
	 *            PRNG
	 * @return set of unique nodes selected uniformly at random
	 */
	protected HashSet<NodeImpl> selectNodesRandomly(NodeImpl[] nodes,
			int number, Random rand) {
		HashSet<NodeImpl> attackers = new HashSet<NodeImpl>();
		while (attackers.size() < number) {
			int index = rand.nextInt(nodes.length);
			attackers.add(nodes[index]);
		}
		return attackers;
	}

	/**
	 * generates a set of the highest degree nodes
	 * 
	 * @param nodes
	 *            nodes to select from
	 * @param number
	 *            number of nodes to select
	 * @param rand
	 *            PRNG
	 * @return set of the highest degree nodes from the given list
	 */
	protected HashSet<NodeImpl> selectNodesByDegreeDesc(NodeImpl[] nodes,
			int number, Random rand) {
		NodeImpl[] sorted = NodeSorting.degreeDesc(nodes, rand);
		HashSet<NodeImpl> attackers = new HashSet<NodeImpl>();
		for (int i = 0; i < number; i++) {
			attackers.add(sorted[i]);
		}
		return attackers;
	}

	/**
	 * generates a set of the lowest degree nodes
	 * 
	 * @param nodes
	 *            nodes to select from
	 * @param number
	 *            number of nodes to select
	 * @param rand
	 *            PRNG
	 * @return set of lowest degree nodes from the given list
	 */
	protected HashSet<NodeImpl> selectNodesByDegreeAsc(NodeImpl[] nodes,
			int number, Random rand) {
		NodeImpl[] sorted = NodeSorting.degreeDesc(nodes, rand);
		HashSet<NodeImpl> attackers = new HashSet<NodeImpl>();
		for (int i = 0; i < number; i++) {
			attackers.add(sorted[sorted.length - i - 1]);
		}
		return attackers;
	}

	/**
	 * generates a set of nodes within the bounds of the given minimum and
	 * maximum degree
	 * 
	 * @param nodes
	 *            nodes to select from
	 * @param number
	 *            number of nodes to select
	 * @param rand
	 *            PRNG
	 * @param min
	 *            minimum degree of a node to select
	 * @param max
	 *            maximum degree of a node to select
	 * @return set of nodes within the bounds of the given minimum and maximum
	 *         degree
	 */
	protected HashSet<NodeImpl> selectNodesByDegree(NodeImpl[] nodes,
			int number, Random rand, int min, int max) {
		ArrayList<NodeImpl> potential = new ArrayList<NodeImpl>();
		for (int i = 0; i < nodes.length; i++) {
			int d = nodes[i].out().length + nodes[i].in().length;
			if (min <= d && d <= max) {
				potential.add(nodes[i]);
			}
		}
		HashSet<NodeImpl> attackers = new HashSet<NodeImpl>();
		number = Math.min(number, potential.size());
		while (attackers.size() < number) {
			int index = rand.nextInt(potential.size());
			attackers.add(potential.get(index));
		}
		return attackers;
	}

	/**
	 * initializes the edges for the newly created SortingNodes using the edges
	 * from the given graph (assuming the indices of both node arrays as
	 * connection); also, the initKnownIDs() method is called for every
	 * SortingNode afterwards
	 * 
	 * @param g
	 *            graph to copy the edges from
	 * @param nodes
	 *            nodes to add edges for
	 */
	protected void init(Graph g, SortingNode[] nodes) {
		Edges edges = new Edges(nodes, g.edges);
		Edge[] oldEdges = g.edges();
		for (int i = 0; i < oldEdges.length; i++) {
			SortingNode src = nodes[oldEdges[i].src.index()];
			SortingNode dst = nodes[oldEdges[i].dst.index()];
			edges.add(src, dst);
		}
		edges.fill();
		for (int i = 0; i < nodes.length; i++) {
			nodes[i].initKnownIDs();
		}
	}

}
