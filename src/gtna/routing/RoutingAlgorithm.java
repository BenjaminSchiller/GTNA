package gtna.routing;

import gtna.graph.Node;
import gtna.graph.NodeImpl;

import java.util.Random;

/**
 * Interface that must be implemented by all routing algorithms. All methods are
 * implemented by RoutingAlgorithmImpl except for the actual routing, check for
 * applicability, and the initialization.
 * 
 * @author benni
 * 
 */
public interface RoutingAlgorithm {
	/**
	 * Implements the actual routing. The given source node attempts to route
	 * towards a random destination which can be a random identifier in the
	 * identifier space of a specific node (or its identifier). Choosing the
	 * destination of such a random routing attempt is also part of the routing
	 * algorithm's implementation.
	 * 
	 * @param nodes
	 *            set of all nodes in the network
	 * @param src
	 *            node from which the routing should start
	 * @param rand
	 *            PRNG
	 * @return Path object containing information about the routing attempt
	 */
	public Route randomRoute(NodeImpl[] nodes, NodeImpl src, Random rand);

	/**
	 * Checks if this routing algorithm can be applied to the given network
	 * represented by the set of nodes.
	 * 
	 * @param nodes
	 *            list of all nodes in the network
	 * @return true if this routing algorithm can be applied to the network
	 *         represented by the given set of nodes, false otherwise
	 */
	public boolean applicable(NodeImpl[] nodes);

	/**
	 * Induces a pre-processing of the routing algorithm if required to, e.g.,
	 * distribute replicas or init routing tables.
	 * 
	 * @param nodes
	 *            list of all nodes contained in the network
	 */
	public void init(NodeImpl[] nodes);

	/**
	 * 
	 * @return key of the routing algorithm,, used in the configuration
	 */
	public String key();

	/**
	 * 
	 * @return name of the routing algorithm including its configuration
	 */
	public String name();

	/**
	 * 
	 * @return long version of the name
	 */
	public String nameLong();

	/**
	 * 
	 * @return short version of the name
	 */
	public String nameShort();

	/**
	 * 
	 * @return part of the folder name representing the routing algorithm
	 */
	public String folder();

	/**
	 * 
	 * @return configuration keys of the routing algorithm's configuration
	 *         parameters
	 */
	public String[] configKeys();

	/**
	 * 
	 * @return configuration parameters of the routing algorithm's instance
	 */
	public String[] configValues();

	/**
	 * 
	 * @param ra
	 *            routing algorithm to compare to
	 * @return first configuration parameter (actual value) that differs between
	 *         the two compared routing algorithms
	 */
	public String compareValue(RoutingAlgorithm ra);

	/**
	 * 
	 * @param ra
	 *            routing algorithm to compare to
	 * @param key
	 *            representation of the name
	 * @return name / type of the first configuration parameter that differs
	 *         between the two compared routing algorithm
	 */
	public String compareName(RoutingAlgorithm ra, String key);
}
