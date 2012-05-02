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
package gtna.transformation.attackableEmbedding;

import gtna.graph.Edge;
import gtna.graph.Edges;
import gtna.graph.Graph;
import gtna.graph.GraphProperty;
import gtna.graph.Node;
import gtna.graph.sorting.NodeSorting;
import gtna.id.DIdentifierSpace;
import gtna.id.Partition;
import gtna.id.ring.RingIdentifier;
import gtna.id.ring.RingPartitionSimple;
import gtna.transformation.Transformation;
import gtna.util.parameter.Parameter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

/**
 * abstract class for an embedding, including possibility of malicious nodes
 * 
 * @author stefanieroos
 * 
 */

public abstract class AttackableEmbedding extends Transformation {

	protected int iterations;
	private DIdentifierSpace idspace;

	/**
	 * @param key
	 * @param configKeys
	 * @param configValues
	 */
	public AttackableEmbedding(int iterations, String key,
			Parameter[] parameters) {
		super(key, parameters);
		this.iterations = iterations;
	}

	public boolean applicable(Graph g) {
		return true;
	}

	public Graph transform(Graph g) {

		Random rand = new Random();
		AttackableEmbeddingNode[] nodes = this.generateNodes(g, rand);
		AttackableEmbeddingNode[] selectionSet = this.generateSelectionSet(
				nodes, rand);
		g.setNodes(nodes);
		GraphProperty[] prop = g.getProperties("ID_SPACE");
		DIdentifierSpace idSpace = (DIdentifierSpace) prop[prop.length - 1];
		// Partition<Double>[] ids = idSpace.getPartitions();
		this.setIdspace(idSpace);
		RingIdentifier[] ids = this.getIds();
		for (int i = 0; i < this.iterations * selectionSet.length; i++) {
			int index = rand.nextInt(selectionSet.length);
			if (selectionSet[index].getOutDegree() > 0) {
				// double old =
				// ids[selectionSet[index].getIndex()].getPosition();
				selectionSet[index].updateNeighbors(rand);
				selectionSet[index].turn(rand);
			}
		}
		Partition<Double>[] parts = new RingPartitionSimple[g.getNodes().length];

		for (int i = 0; i < parts.length; i++) {
			parts[i] = new RingPartitionSimple(ids[i]);
			// System.out.println(parts[i].getRepresentativeID());
		}
		idSpace.setPartitions(parts);
		// g.addProperty(g.getNextKey("ID_SPACE"), idSpace);
		return g;
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
	protected abstract AttackableEmbeddingNode[] generateNodes(Graph g,
			Random rand);

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
	protected abstract AttackableEmbeddingNode[] generateSelectionSet(
			AttackableEmbeddingNode[] nodes, Random rand);

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
	protected HashSet<Integer> selectNodesRandomly(Node[] nodes, int number,
			Random rand) {
		HashSet<Integer> attackers = new HashSet<Integer>();
		while (attackers.size() < number) {
			int index = rand.nextInt(nodes.length);
			attackers.add(index);
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
	protected HashSet<Integer> selectNodesByDegreeDesc(Node[] nodes,
			int number, Random rand) {
		Node[] sorted = NodeSorting.degreeDesc(nodes, rand);
		HashSet<Integer> attackers = new HashSet<Integer>();
		for (int i = 0; i < number; i++) {
			attackers.add(sorted[i].getIndex());
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
	protected HashSet<Integer> selectNodesByDegreeAsc(Node[] nodes, int number,
			Random rand) {
		Node[] sorted = NodeSorting.degreeDesc(nodes, rand);
		HashSet<Integer> attackers = new HashSet<Integer>();
		for (int i = 0; i < number; i++) {
			attackers.add(sorted[sorted.length - i - 1].getIndex());
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
	protected HashSet<Integer> selectNodesByDegree(Node[] nodes, int number,
			Random rand, int min, int max) {
		ArrayList<Node> potential = new ArrayList<Node>();
		for (int i = 0; i < nodes.length; i++) {
			int d = nodes[i].getDegree();
			if (min <= d && d <= max) {
				potential.add(nodes[i]);
			}
		}
		HashSet<Integer> attackers = new HashSet<Integer>();
		number = Math.min(number, potential.size());
		while (attackers.size() < number) {
			int index = rand.nextInt(potential.size());
			attackers.add(potential.get(index).getIndex());
		}
		return attackers;
	}

	protected HashSet<Integer> selectNodesAroundMedian(Node[] nodes,
			int number, Random rand, int setSize) {
		Node[] sorted = NodeSorting.degreeDesc(nodes, rand);
		int median = sorted[sorted.length / 2].getDegree();
		int medianStart = 0;
		int medianEnd = sorted.length;
		for (int i = 0; i < sorted.length; i++) {
			int degree = sorted[i].getDegree();
			if (degree == median) {
				medianStart = i;
				break;
			}
		}
		for (int i = sorted.length - 1; i >= 0; i--) {
			int degree = sorted[i].getDegree();
			if (degree == median) {
				medianEnd = i;
				break;
			}
		}
		int medianIndex = medianStart + (medianEnd - medianStart) / 2;
		int startIndex = medianIndex - (setSize / 2);
		int endIndex = medianIndex + (setSize / 2);
		ArrayList<Node> potential = new ArrayList<Node>(setSize);
		for (int i = startIndex; i < endIndex; i++) {
			potential.add(sorted[i]);
		}
		HashSet<Integer> attackers = new HashSet<Integer>();
		number = Math.min(number, setSize);
		while (attackers.size() < number) {
			int index = rand.nextInt(potential.size());
			attackers.add(potential.get(index).getIndex());
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
	protected void init(Graph g, Node[] nodes) {
		Edges edges = new Edges(nodes, g.computeNumberOfEdges());
		Edge[] oldEdges = g.generateEdges();
		for (int i = 0; i < oldEdges.length; i++) {
			edges.add(oldEdges[i].getSrc(), oldEdges[i].getDst());
		}
		edges.fill();
		for (int i = 0; i < nodes.length; i++) {
			((AttackableEmbeddingNode) nodes[i]).initKnownIDs();
		}
	}

	/**
	 * @return the idspace
	 */
	public DIdentifierSpace getIdspace() {
		return this.idspace;
	}

	/**
	 * @param idspace
	 *            the idspace to set
	 */
	public void setIdspace(DIdentifierSpace idspace) {
		this.idspace = idspace;
	}

	public abstract RingIdentifier[] getIds();

}
