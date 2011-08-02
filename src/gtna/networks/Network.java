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
 * Network.java
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
package gtna.networks;

import gtna.graph.Graph;
import gtna.routingOld.RoutingAlgorithm;
import gtna.transformation.Transformation;

/**
 * Interface that must be implemented by all network generators.
 * 
 * @author benni
 * 
 */
public interface Network {

	/**
	 * 
	 * @return key of the network, used in the configuration
	 */
	public String key();

	/**
	 * 
	 * @return name of the network, including configuration, routing algorithm,
	 *         and transformations
	 */
	public String name();

	/**
	 * 
	 * @return configuration keys of the network's configuration parameters
	 */
	public String[] configKeys();

	/**
	 * 
	 * @return configuration parameters of the network's instance
	 */
	public String[] configValues();

	/**
	 * 
	 * @return folder prefix for storing the data
	 */
	public String folder();

	/**
	 * 
	 * @return number of nodes in the network
	 */
	public int nodes();

	/**
	 * 
	 * @return number of edges in the network
	 */
	public int edges();

	/**
	 * Generate an instance of the network topology specified by the class and
	 * the individual configuration parameter given to the constructor.
	 * 
	 * @return generated network instance
	 */
	public Graph generate();

	/**
	 * This property is only available if the SPL metric is computed!
	 * 
	 * @return true if the network is fully connected, false otherwise
	 */
	// TODO remove
	public boolean isConnected();

	public double getConnectivity();

	public boolean isRoutingFailure();

	public double getRoutingSuccess();

	/**
	 * used by SPL to set connectivity
	 * 
	 * @param connected
	 */
	// TODO add as graph properties
	public void setConnected(boolean connected);

	public void setConnectivity(double connectivity);

	public void setRoutingFailure(boolean routingFailure);

	public void setRoutingSuccess(double routingSuccess);

	/**
	 * Sets the number of nodes in the network. This method is used by certain
	 * network generators there the network size cannot be predefined due to
	 * their construction principles.
	 * 
	 * @param nodes
	 *            number of nodes in the network
	 */
	public void setNodes(int nodes);

	/**
	 * Sets the number of edges in the network. This method is used by a Series
	 * object after the generation of all instances or single run.
	 * 
	 * @param edges
	 *            number of edges in the generated network instances
	 */
	public void setEdges(int edges);

	public String compareName(Network nw, String key);

	public String compareName(Network nw);

	public String compareNameShort(Network nw);

	public String compareNameLong(Network nw);

	public String compareValue(Network nw);

	public String description();

	public String description(Network compare);

	public String description(Network compare1, Network compare2);

	public String description(String key);

	public String description(String key, Network compare);

	public String description(String key, Network compare1, Network compare2);

	public RoutingAlgorithm routingAlgorithm();

	public Transformation[] transformations();
}
