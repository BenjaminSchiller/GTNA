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
 * IDNode.java
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
package gtna.trash.routing.node;

import gtna.graph.Node;
import gtna.trash.routing.node.identifier.Identifier;

import java.util.Random;

/**
 * Interface for nodes that are placed on any kind of identifier space specified
 * by the respective implementation of the used gtna.routing.identifier.ID. This
 * interface provides the basic functionalities for routing in networks based on
 * a given identifier space. Basic greedy routing algorithms can use such nodes
 * since they provide a deterministically computable distance relation between
 * all nodes as well as between nodes and arbitrary identifiers.
 * 
 * @author benni
 * 
 */
@Deprecated
public interface IDNode {
	/**
	 * Determines if the node is responsible for the given identifier or has
	 * stored a copy of the identifier / its value.
	 * 
	 * @param id
	 *            identifier to check
	 * @return true if the node has a copy or is responsible for the given
	 *         identifier, false otherwise
	 */
	public boolean contains(Identifier id);

	/**
	 * Computes the distance between the node and the given identifier.
	 * 
	 * @param id
	 *            identifier to compute distance to
	 * @return distance between the nodes and the given identifier
	 */
	public double dist(Identifier id);

	/**
	 * Generates an identifier chosen uniformly at random from the identifier
	 * space. Depending on the implementation, this may only be an identifier
	 * from a randomly chosen node in the network.
	 * 
	 * @param rand
	 *            PRNG
	 * @return random identifier
	 */
	public Identifier randomID(Random rand, Node[] nodes);

	/**
	 * Computes the distance between this node an the given one. This might
	 * simply be the distance between their identifier but depends on the
	 * identifier space and the implementation of the nodes.
	 * 
	 * @param node
	 *            node to compute distance to
	 * @return distance between the two nodes
	 */
	public double dist(IDNode node);

	public int getIndex();

	public int[] getIncomingEdges();

	public int[] getOutgoingEdges();
}
