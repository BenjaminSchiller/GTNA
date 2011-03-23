package gtna.routing.node;

import gtna.graph.Node;
import gtna.graph.NodeImpl;
import gtna.routing.node.identifier.Identifier;

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
public interface IDNode extends Node {
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
	public Identifier randomID(Random rand, NodeImpl[] nodes);

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
}
