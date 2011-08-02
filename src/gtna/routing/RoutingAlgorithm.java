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
 * RoutingAlgorithm.java
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
package gtna.routing;

import gtna.graph.Graph;
import gtna.graph.Node;
import gtna.id.ID;

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
	public Route route(Graph graph, Node start, ID target, Random rand);

	/**
	 * Checks if this routing algorithm can be applied to the given network
	 * represented by the set of nodes.
	 * 
	 * @param nodes
	 *            list of all nodes in the network
	 * @return true if this routing algorithm can be applied to the network
	 *         represented by the given set of nodes, false otherwise
	 */
	public boolean applicable(Graph graph);

	/**
	 * Induces a pre-processing of the routing algorithm if required to, e.g.,
	 * distribute replicas or init routing tables.
	 * 
	 * @param nodes
	 *            list of all nodes contained in the network
	 */
	public void init(Graph graph);

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
